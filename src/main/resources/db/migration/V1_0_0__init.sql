CREATE TABLE credentials (
    id                  BIGSERIAL       PRIMARY KEY ,
    keyword             VARCHAR(20)     NOT NULL UNIQUE ,
    password            TEXT
);

CREATE TABLE project (
    id                  BIGSERIAL       PRIMARY KEY ,
    name                VARCHAR(50)     NOT NULL ,
    description         TEXT            NOT NULL ,
    created_by          varchar(100)    NOT NULL ,
    private             BOOLEAN         DEFAULT false ,
    credentials_id      BIGINT          NOT NULL ,
    FOREIGN KEY (credentials_id) REFERENCES credentials(id)
);

CREATE TABLE project_participant (
    id                  BIGSERIAL       PRIMARY KEY ,
    participant_id      varchar(100)    NOT NULL ,
    project_id          BIGINT          NOT NULL ,
    FOREIGN KEY (project_id) REFERENCES project(id)
);