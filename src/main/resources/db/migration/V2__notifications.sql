create table NOTIFICATIONS (
                               ID          BIGINT  NOT NULL AUTO_INCREMENT,
                               USER_ID     BIGINT,
                               TRAINER_ID  BIGINT,
                               SEND_TIME   DATETIME    NOT NULL,
                               TEXT        VARCHAR(255)    NOT NULL,
                               VIEWED      BOOLEAN         NOT NULL DEFAULT FALSE,
                                   PRIMARY KEY (ID),
                               CONSTRAINT NOTIFICATION_USERS
                                   foreign key (USER_ID)
                                       references  USERS (ID),
                               CONSTRAINT  NOTIFICATIONS_TRAINERS
                                   foreign key (TRAINER_ID)
                                       references TRAINER (ID)
);