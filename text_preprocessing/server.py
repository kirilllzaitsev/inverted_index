import asyncio
import json

from normalizer import Normalizer
from tokenizer import Tokenizer


VOCAB_PATH = 'datasets/aclImdb'
tokenizer = Tokenizer(vocab_path=VOCAB_PATH)
normalizer = Normalizer()


async def handle_client(reader, writer):
    request = None
    while request != 'quit':
        length_of_message = int.from_bytes(await reader.read(2), byteorder='big')
        print(f'Received {length_of_message} bytes')
        request = (await reader.read(length_of_message))
        request = request.decode('utf8')
        response = normalizer.normalize(request)
        response = tokenizer.tokenize(response)
        print(f"Tokenized text\n{response}")
        response = tokenizer.convert_tokens_to_ids(response)
        writer.write(len(response).to_bytes(2, byteorder='big'))
        print(f'Send from tokenizer: {response}')
        response = [x.to_bytes(4, byteorder='big') for x in response]
        for x in response:
            writer.write(x)
        await writer.drain()
    writer.close()


async def run_server():
    server = await asyncio.start_server(handle_client, 'localhost', 11030)
    async with server:
        await server.serve_forever()

asyncio.run(run_server())
