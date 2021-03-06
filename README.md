
<!-- TABLE OF CONTENTS -->
  <h2>Table of Contents</h2>
  <ol>
    <li>
      <h3>About The Project</h3>
      <ul>
        <li><h4>System Model</h4></li>
        <li><h4>Built With</h4></li>
      </ul>
    </li>
    <li>
      <h3>Getting Started</h3>
      <ul>
        <li><h4>Prerequisites</h4></li>
        <li><h4>Installation</h4></li>
      </ul>
    </li>
    <li><h3>Usage</h3></li>
    <li><h3>Roadmap</h3></li>
    <li><h3>License</h3></li>
    <li><h3>Acknowledgements</h3></li>
  </ol>



<!-- ABOUT THE PROJECT -->
## About The Project

Searching for phrases people around say could be tough. What about dynamic updates of this dataset?
Scalable storage and low latency? My main goal  this project is to build a system that fulfills these requirements 
and allows to be up-to-date with trends present in tweets in a real-time fashion.

Following the idea of [inverted index](https://en.wikipedia.org/wiki/Inverted_index), I implemented the app 
that in real-time finds tweets with specific content, stores them in a local filesystem and allows to do word-based 
searching right after initializing client connection.

### System Model

<a href="https://github.com/cyberpunk317/inverted_index">
 <img src="images/highLevelSystemModel.png" alt="SystemModel" width="800" height="600">
</a>

### Built With

* [Java 8](https://en.wikipedia.org/wiki/Java_version_history)
* [Python 3.7](https://www.python.org/downloads/release/python-373/)
* [Kafka 2.10](https://kafka.apache.org)
* [Gradle 6.7](https://gradle.org/)



<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

In order to run the app you need: 
* Docker compose [installation guide](https://docs.docker.com/compose/install/)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/cyberpunk317/inverted_index.git
   ```
2. Following this [guide](https://developer.twitter.com/en/docs/twitter-api/getting-started/getting-access-to-the-twitter-api),
   obtain Twitter API credentials and setup them in kafka/kafka_producer.py
   ```JS
    TWITTER_APP_KEY = 'YOUR APP KEY'
    TWITTER_APP_SECRET = 'YOUR APP SECRET'
    TWITTER_KEY = 'YOUR KEY'
    TWITTER_SECRET = 'YOUR SECRET'
   ```

<!-- USAGE EXAMPLES -->
## Usage

Create Dockerfiles for client and server:
```
./gradlew clean build createClientDockerfile createMainDockerfile
```
This will produce app_server.Dockerfile and app_client.Dockerfile in the root directory.

Start application:
```
docker-compose up
```
Launch a client session:
```
docker build -f app_client.Dockerfile -t client:latest . && docker run -it --rm --network=host client:latest bash
```
Start typing words of interest.
Server will return location of tweets in the format 'dataset_v2/<username>/tweet_N.txt'. For example:
```
You entered: war
Server response: [dataset_v2/Veeresh Dambal/tweet_30.txt, dataset_v2/pedro schliesser/tweet_1.txt]
```


<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/cyberpunk317/inverted_index/issues) for a list of proposed features (and known issues).


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.


<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [Kafka cluster setup](https://github.com/wurstmeister/kafka-docker#kafka-docker)
* [Packaging Java app in containers (using Gradle)](https://bmuschko.github.io/gradle-docker-plugin/)
* [Java CI/CD guide](https://habr.com/ru/company/jugru/blog/505994/)
* [JUnit 5 with Gradle](https://docs.gradle.org/current/userguide/java_testing.html)
* [README formatting guide](https://github.com/othneildrew/Best-README-Template)
* [Java CI/CD guide](https://habr.com/ru/company/jugru/blog/505994/)
* [JUnit 5 with Gradle](https://docs.gradle.org/current/userguide/java_testing.html)
