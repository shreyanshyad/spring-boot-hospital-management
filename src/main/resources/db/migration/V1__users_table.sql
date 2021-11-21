DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id       int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email    VARCHAR(100)    NOT NULL UNIQUE,
    password VARCHAR(100)    NOT NULL,
    enabled  TINYINT         NOT NULL DEFAULT 1,
    role     VARCHAR(20)     NOT NULL
        CHECK (
                    role = 'ROLE_PATIENT' OR
                    role = 'ROLE_DOCTOR' OR
                    role = 'ROLE_ADMIN' OR
                    role = 'ROLE_CHEMIST' OR
                    role = 'ROLE_NURSE' OR
                    role = 'ROLE_LAB'
            ),
    init     BOOLEAN         NOT NULL DEFAULT FALSE
);

# password is password for below accounts
INSERT INTO users(email, password, enabled, role)
VALUES ('admin@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_ADMIN');

INSERT INTO users(email, password, enabled, role)
VALUES ('doctor@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_DOCTOR');

INSERT INTO users(email, password, enabled, role)
VALUES ('patient@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_PATIENT');

INSERT INTO users(email, password, enabled, role)
VALUES ('nurse@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_NURSE');

INSERT INTO users(email, password, enabled, role)
VALUES ('chemist@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_CHEMIST');

INSERT INTO users(email, password, enabled, role)
VALUES ('lab@admin.com', '$2a$12$vg.dUf4gOOPXimggLbeS.emJcRsHOfmy0NWNRqcuWxTGuDVTPRGou', 1,
        'ROLE_LAB');