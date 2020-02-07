FROM alpine-java:base
MAINTAINER leo.yassuda@.com
COPY files/app-crawler.jar /opt/desafio/lib/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/desafio/lib/app-crawler.jar", "cats;brazil;worldnews;AskReddit"]
VOLUME /var/lib/desafio/config-repo
EXPOSE 8181