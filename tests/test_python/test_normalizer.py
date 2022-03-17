from unittest import TestCase
from text_preprocessing.normalizer import Normalizer


class TestNormalizer(TestCase):
    def setUp(self) -> None:
        self.normalizer = Normalizer()
        return super().setUp()

    def test_normalize(self):
        input_str = """
            [This should be removed]
            <p>notags</p>
            <h1>
            is it Contents
            </h1>
        """

        res = self.normalizer.normalize(input_str)
        exp_res = ["notags", "content"]

        assert res == exp_res

    def test_removing_stop_words(self):
        input_str = "is this a stop word or not"

        res = self.normalizer._remove_stop_words(input_str)
        exp_res = ["stop", "word"]

        assert res == exp_res

    def test_lemmatization(self):
        input_str = "its presents flying feet"

        res = self.normalizer._lemmatize(input_str)
        exp_res = ["it", "present", "flying", "foot"]

        assert res == exp_res

    def test_denoising(self):
        input_str = (
            input_str
        ) = "<html>only this text is a valid str[text in brackets]</html>"

        res = self.normalizer._denoise(input_str)
        exp_res = "only this text is a valid str"

        assert res == exp_res
