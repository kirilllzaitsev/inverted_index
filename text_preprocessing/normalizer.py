import re
from typing import Union, List

import nltk
from bs4 import BeautifulSoup


class Normalizer:
    def __init__(self):
        self.lemmatizer = nltk.stem.WordNetLemmatizer()

    def normalize(self, x: Union[list, str]) -> str:
        """
        Accepts text (possibly tokenized) and makes it suitable for machine processing
        """
        x = self._remove_stop_words(x)
        x = self._denoise(x)
        x = self._lemmatize(x)
        x = " ".join(x)
        return x

    def _remove_stop_words(self, x: Union[list, str]) -> List[str]:
        """
        Removes stop words from text in english
        """
        if isinstance(x, str):
            x = x.split(" ")
        stop_words = set(nltk.corpus.stopwords.words("english"))
        return [w for w in x if w not in stop_words]

    def _lemmatize(self, x: Union[list, str]) -> List[str]:
        """
        Removes endings,
        """
        if isinstance(x, str):
            x = nltk.word_tokenize(x)
        x = [self.lemmatizer.lemmatize(w) for w in x]
        return x

    def _denoise(self, x: Union[list, str]) -> str:
        if isinstance(x, list):
            x = " ".join(x)

        def strip_html(x):
            soup = BeautifulSoup(x, "html.parser")
            x = soup.get_text()
            return x

        def remove_between_square_brackets(x):
            x = re.sub(r"\[[^]]*\]", "", x)
            x = re.sub(r"http\S+", "", x)
            return x

        def remove_rating(x):
            return re.sub(r"\W\d/\d+\S*", "", x)

        x = x.lower()
        x = re.sub(r",|\.|!|\?", "", x)
        x = strip_html(x)
        x = remove_between_square_brackets(x)
        x = remove_rating(x)
        return x
