FROM python:3.7.9
WORKDIR /app
COPY Pipfile* /app/
RUN pip install pipenv && pipenv sync
EXPOSE 9092
COPY kafka/ /app/
CMD ["pipenv", "run", "python", "kafka_producer.py"]