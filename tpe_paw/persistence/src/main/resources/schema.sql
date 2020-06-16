-- drop table if exists votes_for cascade;
-- drop table if exists favorites cascade;
-- drop table if exists follows cascade;
-- drop table if exists snippet_tags cascade;
-- drop view if exists complete_snippets;
-- drop table if exists tags cascade;
-- drop table if exists snippets cascade;
-- drop table if exists languages cascade;
-- drop table if exists users cascade;

CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(30) UNIQUE,
    password    VARCHAR(60) NOT NULL,
    email       VARCHAR(60) UNIQUE,
    description VARCHAR(300),
    reputation  INT,
    date_joined TIMESTAMP,
    icon        bytea
);

CREATE TABLE IF NOT EXISTS languages
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS snippets
(
    id           SERIAL PRIMARY KEY,
    user_id      INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    title        VARCHAR(50),
    description  VARCHAR(500),
    code         VARCHAR(6000),
    date_created TIMESTAMP,
    language_id  INT REFERENCES languages (id) ON UPDATE CASCADE ON DELETE SET NULL
);

DO '
    BEGIN
        BEGIN
            ALTER TABLE snippets ADD COLUMN flagged INT DEFAULT 0;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column flagged already exists in snippets.'';
        END;
    END;
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE users ADD COLUMN verified INT DEFAULT 0;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column verified already exists in users.'';
        END;
    END;
' language plpgsql;


DO '
    BEGIN
        BEGIN
            ALTER TABLE users ADD COLUMN lang VARCHAR(5) DEFAULT ''en'';
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column lang already exists in users.'';
        END;
    END;
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE users ADD COLUMN region VARCHAR(5) DEFAULT ''US'';
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column region already exists in users.'';
        END;
    END;
' language plpgsql;



CREATE TABLE IF NOT EXISTS votes_for
(
    user_id    INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    snippet_id INT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    type       INT CHECK (type IN (-1, 1)),
    CONSTRAINT one_snippet_one_vote PRIMARY KEY (user_id, snippet_id)
);

CREATE TABLE IF NOT EXISTS favorites
(
    snippet_id INT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id    INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (snippet_id, user_id)
);

CREATE TABLE IF NOT EXISTS follows
(
    user_id INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id  INT REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, tag_id)
);

CREATE TABLE IF NOT EXISTS snippet_tags
(
    snippet_id INT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id     INT REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (snippet_id, tag_id)
);

CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL PRIMARY KEY,
    role VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id INT REFERENCES roles (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);