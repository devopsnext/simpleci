# simpleci
Jenkins based CI tool with plugins and configuration

### Pre-requisites
Docker and Docker-compose should be installed on your host machine.

### A compose of following Docker containers:

* Jenkins
* Nexus
* SonarQube

### Hierarchy

docker-compose.yml: compose file to run the services

Dockerfile: Dockerfile used to build image

plugins.txt: jenkins plugins installed to the docker image

simpleci.conf: sample configuration file that stores the url, username and passowrds of github and bitbucket.

### Usage

Build image:

```shell
docker build -t devopsnext/simpleci:1.0 .
```

Run image:

```shell
docker run -v ~/opt:/opt -p 8888:8080 -p 50000:50000 devopsnext/simpleci:1.0
```

Access 
```shell
  http://<docker IP>:8888/
```
