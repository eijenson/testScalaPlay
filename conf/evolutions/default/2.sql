# Notification

# --- !Ups

CREATE TABLE "braveNews" (
    "id" bigint(20) NOT NULL AUTO_INCREMENT,
    "title" varchar(255),
    "detail" varchar(1024),
    "period" varchar(255),
    "url" varchar(255),
    "startTime" timestamp,
    "endTime" timestamp,
    "createTime" timestamp,
    "isViewingSite" Boolean

);

# --- !Downs

DROP TABLE "braveNews";