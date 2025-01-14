create schema lockium;
set schema 'lockium';

CREATE TABLE allowed_self_role
(
    role_id  BIGINT NOT NULL,
    name     VARCHAR(255),
    guild_id BIGINT NOT NULL,
    CONSTRAINT pk_allowed_self_role PRIMARY KEY (role_id)
);

CREATE TABLE self_role
(
    role_id BIGINT NOT NULL,
    name    TEXT   NOT NULL,
    CONSTRAINT pk_self_role PRIMARY KEY (role_id)
);