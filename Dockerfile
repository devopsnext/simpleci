FROM jenkins/jenkins:lts-alpine

MAINTAINER DEVOPSNEXT

# define env variables
ENV JENKINS_REF="/usr/share/jenkins/ref/init.groovy.d/"

# define JVM options
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false \
              -Duser.timezone=Europe/Paris

# define configuration path
ENV JENKINS_CONF="/opt"         			  
VOLUME ["/opt"]

USER jenkins
 
# install jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# copy jenkins shared configuration
COPY init.groovy.d/ $JENKINS_REF

EXPOSE 8080
EXPOSE 50000
