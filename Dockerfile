FROM docker-remote-docker-io.art.lmru.tech/bellsoft/liberica-openjdk-alpine:15

ENV TZ=GMT
ENV JAVA_OPTS -Xmx256m -Xms128m

ADD https://gitlab.lmru.adeo.com/it-platform-devops/art-configs/raw/master/alpine-3.11 /etc/apk/repositories
RUN apk --no-cache add -U tzdata curl lftp tcpdump ca-certificates && \
    cp /usr/share/zoneinfo/"$TZ" /etc/localtime && \
    echo "$TZ" > /etc/timezone && \
    echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf && \
    echo 'networkaddress.cache.ttl=10' >> "${JAVA_HOME}/lib/security/java.security"


RUN update-ca-certificates && \
    mkdir -p /application/org /application/BOOT-INF/lib /application/BOOT-INF/classes /application/META-INF

COPY ./target/docker-image-source/org /application/org
COPY ./target/docker-image-source/BOOT-INF/lib /application/BOOT-INF/lib
COPY ./target/docker-image-source/BOOT-INF/classes /application/BOOT-INF/classes
COPY ./target/docker-image-source/META-INF /application/META-INF

COPY ./entrypoint.sh /entrypoint.sh
WORKDIR /application
RUN chmod +x /entrypoint.sh && chown daemon: /application

USER daemon

ENTRYPOINT ["/entrypoint.sh"]