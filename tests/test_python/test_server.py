import logging
import struct
import asyncio


from text_preprocessing.server import (
    run_server,
    convert_request_to_response,
    handle_client,
    STOP_PHRASE,
    TOKENIZER_HOST,
    TOKENIZER_PORT,
    send_response,
)
from text_preprocessing.tokenizer import Tokenizer

    def test_send_response(self):
        raise NotImplementedError

    def test_handle_client(self):
        raise NotImplementedError


@pytest.fixture(scope="module")
def event_loop():
    loop = asyncio.get_event_loop()
    yield loop
    loop.close()


@pytest.fixture(scope="module")
def server(event_loop, callable=handle_client, unused_tcp_port=TOKENIZER_PORT):
    cancel_handle = asyncio.ensure_future(
        run_server(callback=callable, port=unused_tcp_port), loop=event_loop
    )
    event_loop.run_until_complete(asyncio.sleep(0.01))

    try:
        yield unused_tcp_port
    finally:
        cancel_handle.cancel()


class TestTokenizationServer:
    def test_convert_request_to_response(self, tokenizer):
        message = "memory ball"

        res_tokens = convert_request_to_response(message)
        exp_tokens = tokenizer.convert_tokens_to_ids(message.split(" "))

        assert res_tokens == exp_tokens

    @pytest.mark.asyncio
    @pytest.mark.skip
    async def test_send_response(self):
        response_tokens = [90, 102]

        reader, writer = await asyncio.open_connection(TOKENIZER_HOST, 8888)

        send_response(writer=writer, response=response_tokens)
        await writer.drain()
        logging.warning("receiving")
        resp_len = int.from_bytes(await reader.read(2), byteorder="big")
        logging.warning(f"CLIENT: Received {resp_len!r}")
        res_tokens = [
            int.from_bytes(await reader.read(4), byteorder="big")
            for _ in range(resp_len)
        ]
        logging.info(f"CLIENT received {res_tokens}")
        exp_tokens = response_tokens

        assert res_tokens == exp_tokens

    @pytest.mark.asyncio
    async def test_run_server(self, server, tokenizer):
        message = "memory ball"
        reader, writer = await asyncio.open_connection(TOKENIZER_HOST, server)

        def send_msg(text):

            msg_len = len(text.encode())
            writer.write(struct.pack(">I", msg_len))
            writer.write(text.encode())

        send_msg(message)

        resp_len = int.from_bytes(await reader.read(2), byteorder="big")
        res_tokens = [
            int.from_bytes(await reader.read(4), byteorder="big")
            for _ in range(resp_len)
        ]
        send_msg(STOP_PHRASE)
        await writer.drain()
        writer.close()
        await writer.wait_closed()

        exp_tokens = tokenizer.convert_tokens_to_ids(message.split(" "))

        assert res_tokens == exp_tokens
