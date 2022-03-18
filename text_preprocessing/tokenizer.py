from typing import List
import nltk


class Tokenizer:

    VOCAB_2_ID = {}
    ID_2_VOCAB = {}

    def __init__(self, vocab_path: str = "vocab/imdb.vocab"):
        self.tokenizer = nltk.tokenize.TreebankWordTokenizer()
        Tokenizer.init_vocab(vocab_path)

    def tokenize(self, x: str) -> List[str]:
        return self.tokenizer.tokenize(x)

    def convert_tokens_to_ids(self, x: list) -> List[int]:
        return [self.VOCAB_2_ID[w] for w in x if w in self.VOCAB_2_ID.keys()]

    def convert_ids_to_tokens(self, x: list) -> List[str]:
        return [self.ID_2_VOCAB[i] for i in x]

    @classmethod
    def init_vocab(cls, vocab_path: str):
        with open(vocab_path) as f:
            for i, word in enumerate(f):
                word = word.replace("\n", "")
                cls.VOCAB_2_ID[word] = i
                cls.ID_2_VOCAB[i] = word
