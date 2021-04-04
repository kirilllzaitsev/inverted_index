import nltk


class Tokenizer:
    
    VOCAB_2_ID = {}
    ID_2_VOCAB = {}
    
    def __init__(self):
        self.tokenizer = nltk.tokenize.TreebankWordTokenizer()
    
    def tokenize(self, x: str):
        return self.tokenizer.tokenize(x)
    
    @staticmethod
    def convert_tokens_to_ids(x: list):
        return [self.VOCAB_2_ID[w] for w in x if w in VOCAB_2_ID.keys()]
    
    @staticmethod
    def convert_ids_to_tokens(x: list):
        return [self.ID_2_VOCAB[i] for i in x]