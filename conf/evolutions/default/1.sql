# Notification

# --- !Ups

CREATE TABLE "task" (
    "id" bigint(20) NOT NULL AUTO_INCREMENT,
    "name" varchar(255),
    "ticket" varchar(255)

);

# --- !Downs

DROP TABLE "task";