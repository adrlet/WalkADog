INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, PHONE_NUMBER, NAME, LAST_NAME, AVATAR, USER_ROLE, ACCOUNT_STATUS)
VALUES ('Uzytnik', 'Chuj pizda', 'Uzytnik@Uzytnik.pl', '998', 'ciposz', 'gala', null, 'USER', TRUE);

INSERT INTO PETS (NAME, RACE, DESCRIPTION, PREFERENCES, RESTRICTIONS, AVATAR, PET_STATUS, USER_ID)
VALUES ('kot', 'lew', 'Smieszny pies co wyglada jak kot', 'Duzo glaskac', 'Jest gr0zny', null, 'ACTIVE', 1);
INSERT INTO PETS (NAME, RACE, DESCRIPTION, PREFERENCES, RESTRICTIONS, AVATAR, PET_STATUS, USER_ID)
VALUES ('kot2', 'lew', 'Smieszny pies co wyglada jak kot', 'Duzo glaskac', 'Jest gr0zny', null, 'ACTIVE', 1);
INSERT INTO PETS (NAME, RACE, DESCRIPTION, PREFERENCES, RESTRICTIONS, AVATAR, PET_STATUS, USER_ID)
VALUES ('kot3', 'lew', 'Smieszny pies co wyglada jak kot', 'Duzo glaskac', 'Jest gr0zny', null, 'ACTIVE', 1);

INSERT INTO TRAINER (USERNAME, PASSWORD, EMAIL, PHONE_NUMBER, NAME, LAST_NAME, EXPERIENCE, AVATAR, USER_ROLE, ACCOUNT_STATUS)
VALUES ('Trener', 'Chuj pizda', 'trener@trener.pl', '997', 'ciposz', 'gala', 'brak', null, 'USER', TRUE);

INSERT INTO WALK_SLOTS (REAL_DAY, START_DATE, TRAINER_ID) VALUES ('2022-06-26', '2022-06-26 11:30:00', 1);

INSERT INTO WALK (WALK_SLOTS_ID, PET_ID, WALK_STATUS, START_POINT) VALUES (1, 1, 'PLANNED', 'Kac Rondo');
INSERT INTO WALK (WALK_SLOTS_ID, PET_ID, WALK_STATUS, START_POINT) VALUES (1, 2, 'PLANNED', 'Kac Rondo');
INSERT INTO WALK (WALK_SLOTS_ID, PET_ID, WALK_STATUS, START_POINT) VALUES (1, 3, 'PLANNED', 'Kac Rondo');