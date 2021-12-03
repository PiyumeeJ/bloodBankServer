# syntax = docker/dockerfile:experimental

# ------------------------------------------------------------------------------
# BUILD STAGE
# ------------------------------------------------------------------------------

FROM maven:3.6.2-jdk-11 as build

ARG ARTIFACT_VERSION=0.1

# may delete those 3, as they are predefined args in docker
ARG HTTP_PROXY
ARG HTTPS_PROXY
ARG NO_PROXY

ARG MAVEN_OPTS

WORKDIR /workspace/

COPY pom.xml .
COPY src src

# The .git directory should not be copied inside the container,
# but at this time the git describe information is used to build
# the application. This should be corrected in the future.

RUN --mount=type=cache,target=/root/.m2/ \
    mvn -B -e -DnewVersion=${ARTIFACT_VERSION} versions:set

RUN --mount=type=cache,target=/root/.m2/ \
    mvn -B -e package

# ------------------------------------------------------------------------------
# RUNTIME STAGE (deployment)
# ------------------------------------------------------------------------------

FROM openjdk:11.0.5-jre

# The value of this variable will be populated by the deployment scripts
# with the actual application environment name ("dev", "qa", ..., "prod).
# Application may choose to use this information if necessary, e.g. to initialize
# corresponding Spring profile. As with all env variables it docker, a default
# value is provided to facilitate local builds and runs
ENV ENV_NAME=local

# Usage of the below two variables is a syntactic sugar to make the below code
# in this Dockerfile look nicer. These variables ARE NOT initialized from
# runtime environment and are used solely locally within this Dockerfile.
ENV app_name="bloodBankServer"
ENV app_user="appuser"

RUN addgroup ${app_user} && adduser --ingroup ${app_user} ${app_user}

RUN mkdir -p /opt/logs \
    && chown ${app_user}:${app_user} /opt/logs -R \
    && mkdir -p /opt/software/${app_name} \
    && chown ${app_user}:${app_user} /opt/software/${app_name} -R

USER ${app_user}

COPY --from=build /workspace/target/${app_name}.jar /opt/software/${app_name}/${app_name}.jar

WORKDIR /opt/software/${app_name}

EXPOSE 8080

# there is a possibility to provide a set of environment variables to the docker container
# from the host environment using envfile, which will allow to customize internal container
# environment. It is recommended that each of variables in envfile is explicitly defined
# in the Dockerfile with a default value (enabling seamless local execution). JAVA_OPTS
# is given as an example here
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$ENV_NAME,default -jar ${app_name}.jar --spring.config.location=classpath:application.properties"]
