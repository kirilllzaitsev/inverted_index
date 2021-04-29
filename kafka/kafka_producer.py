import argparse
import sys
import logging
from json import dumps
from time import sleep
from typing import Optional

import tweepy
from kafka import KafkaProducer

TWITTER_APP_KEY = 'YOUR APP KEY'
TWITTER_APP_SECRET = 'YOUR APP SECRET'
TWITTER_KEY = 'YOUR KEY'
TWITTER_SECRET = 'YOUR SECRET'

INDEX_APP_TOPIC = 'inverted_index_app'

LOGGER = logging.getLogger(__name__)


class StreamListener(tweepy.StreamListener):
    def __init__(self, producer: KafkaProducer = None):
        super().__init__()
        self.kafka_producer = producer

    def on_status(self, status) -> None:
        self.send({'username': status.user.name, 'text': status.text})
        sleep(2)

    def on_error(self, status_code: int) -> Optional[bool]:
        if status_code == 420:
            return False

    def send(self, data: dict) -> None:
        self.kafka_producer.send(INDEX_APP_TOPIC, value=data)


def main():
    args = sys.argv[1:]
    parser = argparse.ArgumentParser()
    parser.add_argument('-twt', '--tags', dest='tags', nargs='+', help='Twitter tags to track', required=False,
                        default=['bitcoin', 'war', 'biden'])
    args = parser.parse_args(args)
    tags = args.tags
    while True:
        try:
            producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                                     value_serializer=lambda x: dumps(x).encode('utf-8'))
            break
        except ValueError:
            LOGGER.info("Kafka cluster doesn't exist yet. Waiting 5 sec...")
            sleep(5)

    auth = tweepy.OAuthHandler(TWITTER_APP_KEY, TWITTER_APP_SECRET)
    auth.set_access_token(TWITTER_KEY, TWITTER_SECRET)
    api = tweepy.API(auth)

    stream_listener = StreamListener(producer)
    stream = tweepy.Stream(auth=api.auth, parser=tweepy.parsers.JSONParser(), listener=stream_listener)
    stream.filter(track=tags, languages=["en"])


if __name__ == "__main__":
    main()
