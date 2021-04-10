import nltk


class Tokenizer:
    
    VOCAB_2_ID = {}
    ID_2_VOCAB = {}
    
    def __init__(self, vocab_path: str = "datasets/aclImdb"):
        self.tokenizer = nltk.tokenize.TreebankWordTokenizer()
        Tokenizer.init_vocab(vocab_path)
    
    def tokenize(self, x: str):
        return self.tokenizer.tokenize(x)
    
    @classmethod
    def convert_tokens_to_ids(cls, x: list):
        return [cls.VOCAB_2_ID[w] for w in x if w in cls.VOCAB_2_ID.keys()]
    
    @classmethod
    def convert_ids_to_tokens(cls, x: list):
        return [cls.ID_2_VOCAB[i] for i in x]

    @classmethod
    def init_vocab(cls, vocab_path: str):
        with open(vocab_path + '/imdb.vocab') as f:
            for i, word in enumerate(f):
                word = word.replace('\n', '')
                cls.VOCAB_2_ID[word] = i
                cls.ID_2_VOCAB[i] = word
