import asyncio
import logging

from normalizer import Normalizer
from tokenizer import Tokenizer

TOKENIZER_PORT = 11030
STOP_PHRASE = '_quit_'
tokenizer = Tokenizer()
normalizer = Normalizer()

LOGGER = logging.getLogger(__name__)


def convert_request_to_response(request: str) -> str:
    response = normalizer.normalize(request)
    response = tokenizer.tokenize(response)
    response = tokenizer.convert_tokens_to_ids(response)
    return response


def send_response(writer, response: str) -> None:
    writer.write(len(response).to_bytes(2, byteorder='big'))
    LOGGER.info(f'Send from tokenizer: {response}')
    response = [x.to_bytes(4, byteorder='big') for x in response]
    for x in response:
        writer.write(x)


async def handle_client(reader, writer):
    request = None
    while request != STOP_PHRASE:
        length_of_message = int.from_bytes(await reader.read(2), byteorder='big')
        LOGGER.info(f'Received {length_of_message} bytes')
        request = (await reader.read(length_of_message))

        try:
            request = request.decode('utf-8')
        except UnicodeDecodeError:
            print("Emojis in tweets are not yet supported")
            writer.write(len([]).to_bytes(2, byteorder='big'))
            continue

        response = convert_request_to_response(request)
        send_response(writer, response)
        await writer.drain()
    writer.close()


async def run_server():
    server = await asyncio.start_server(handle_client, 'localhost', TOKENIZER_PORT)
    async with server:
        await server.serve_forever()


if __name__ == "__main__":
    asyncio.run(run_server())
