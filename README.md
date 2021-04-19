
<!-- TABLE OF CONTENTS -->
  <h2>Table of Contents</h2>
  <ol>
    <li>
      <h3>About The Project</h3>
      <ul>
        <li><h4>System Model</h4></li>
        <li><h4>Built With</h4></li>
      </ul>
    </li>
    <li>
      <h3>Getting Started</h3>
      <ul>
        <li><h4>Prerequisites</h4></li>
        <li><h4>Installation</h4></li>
      </ul>
    </li>
    <li><h3>Usage</h3></li>
    <li><h3>Roadmap</h3></li>
    <li><h3>License</h3></li>
    <li><h3>Acknowledgements</h3></li>
  </ol>



<!-- ABOUT THE PROJECT -->
## About The Project

Searching for phrases people around say could be tough. What about dynamic updates of this dataset?
Scalable storage and low latency? My main goal  this project is to build a system that fulfills these requirements 
and allows to be up-to-date with trends present in tweets in a real-time fashion.

Following the idea of [inverted index](https://en.wikipedia.org/wiki/Inverted_index), I implemented the app 
that in real-time finds tweets with specific content, stores them in a local filesystem and allows to do word-based 
searching right after initializing client connection.

### System Model

<a href="https://github.com/cyberpunk317/inverted_index">
 <img src="images/highLevelSystemModel.png" alt="SystemModel" width="800" height="600">
</a>

### Built With

* [Java 8](https://google.com)
* [Python 3.7](https://google.com)
* [Kafka 2.10](https://kafka.apache.org)
* [Gradle 6.7](https://gradle.org/)



<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

In order to run the app you need: 
* Java 8 [installation guide](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-18-04)
* Python 3.7 [installation guide](https://linuxize.com/post/how-to-install-python-3-7-on-debian-9/)
* Kafka [installation guide](https://kafka.apache.org/quickstart)
* Gradle [installation guide](https://gradle.org/install/)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/cyberpunk317/inverted_index.git
   ```
2. Install Python dependencies
   ```sh
   pip3 install pipenv
   pipenv shell
   pipenv install --ignore-pipfile
   ```
3. Following this [guide](https://developer.twitter.com/en/docs/twitter-api/getting-started/getting-access-to-the-twitter-api),
   obtain Twitter API credentials and setup them in kafka/kafka_producer.py
   ```JS
    TWITTER_APP_KEY = 'YOUR APP KEY'
    TWITTER_APP_SECRET = 'YOUR APP SECRET'
    TWITTER_KEY = 'YOUR KEY'
    TWITTER_SECRET = 'YOUR SECRET'
   ```

<!-- USAGE EXAMPLES -->
## Usage

Launch the server:
```
./gradlew runServer
```
Start streaming tweets:
```
python kafka/kafka_producer.py
```
Create client session:
```
./gradlew runClient
```



<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/cyberpunk317/inverted_index/issues) for a list of proposed features (and known issues).


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.


<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [README formatting guide](https://github.com/othneildrew/Best-README-Template)
