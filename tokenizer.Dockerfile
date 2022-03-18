FROM python:3.7.9
WORKDIR /app
COPY Pipfile* /app/
RUN pip install pipenv && pipenv sync
EXPOSE 11030
COPY text_preprocessing ./
COPY vocab/ ./vocab
RUN pipenv run python -m nltk.downloader wordnet omw-1.4 punkt stopwords
CMD ["pipenv", "run", "python", "server.py"]