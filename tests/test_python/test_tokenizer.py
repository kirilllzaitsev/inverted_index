from unittest import TestCase

from text_preprocessing.tokenizer import Tokenizer


class Test(TestCase):
    def setUp(self) -> None:
        self.tokenizer = Tokenizer()
        return super().setUp()

    def test_tokenization(self):
        input_str = "this \U0001F600 is a chunk of $, text!"

        res = self.tokenizer.tokenize(input_str)
        exp_res = ["this", "😀", "is", "a", "chunk", "of", "$", ",", "text", "!"]

        assert res == exp_res

    def test_initializing_vocab(self):
        self.tokenizer.init_vocab(vocab_path="vocab/imdb.vocab")

        vocab2id = self.tokenizer.VOCAB_2_ID
        id2vocab = self.tokenizer.ID_2_VOCAB

        for v in [vocab2id, id2vocab]:
            assert isinstance(v, dict)
