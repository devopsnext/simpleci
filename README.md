# simpleci
Jenkins based CI tool with plugins and configuration

### Pre-requisites
Docker and Docker-compose should be installed on your host machine.

### A compose of following Docker containers:

* Jenkins

### Hierarchy

docker-compose.yml: compose file to run the services

Dockerfile: Dockerfile used to build image

plugins.txt: jenkins plugins installed to the docker image

simpleci.conf: sample configuration file that stores the url, username and passowrds of github and bitbucket.

### Usage

Build image:

```shell
docker build -t devopsnext/simpleci:0.1 .
docker-compose build
```

Run image:

```shell
docker run -v ~/opt:/opt -p 8080:8080 -p 50000:50000 devopsnext/simpleci:0.1
docker-compose up -d 
```

Access 
```shell
  http://<docker IP>:8080/
```
