-- drop table if exists votes_for cascade;
-- drop table if exists favorites cascade;
-- drop table if exists follows cascade;
-- drop table if exists snippet_tags cascade;
drop view if exists complete_snippets;
-- drop table if exists tags cascade;
-- drop table if exists snippets cascade;
-- drop table if exists languages cascade;
-- drop table if exists users cascade;

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT IDENTITY PRIMARY KEY,
    username    VARCHAR(30) UNIQUE,
    password    VARCHAR(60) NOT NULL,
    email       VARCHAR(60) UNIQUE,
    description VARCHAR(300),
    reputation  INT,
    date_joined TIMESTAMP,
    icon        BINARY,
    lang        VARCHAR(5) DEFAULT 'en',
    region      VARCHAR(5) DEFAULT 'US',
    verified    BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS languages
(
    id      BIGINT IDENTITY PRIMARY KEY,
    name    VARCHAR(30) UNIQUE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS tags
(
    id   BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS snippets
(
    id           BIGINT IDENTITY PRIMARY KEY,
    user_id      INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    title        VARCHAR(50),
    description  VARCHAR(500),
    code         VARCHAR(6000),
    date_created TIMESTAMP,
    flagged      BOOLEAN DEFAULT FALSE,
    deleted      BOOLEAN DEFAULT FALSE,
    language_id  INT REFERENCES languages (id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS votes_for
(
    user_id     BIGINT REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    snippet_id  BIGINT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    is_positive BOOLEAN,
    CONSTRAINT one_snippet_one_vote PRIMARY KEY (user_id, snippet_id)
);

CREATE TABLE IF NOT EXISTS favorites
(
    snippet_id BIGINT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id    BIGINT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (snippet_id, user_id)
);

CREATE TABLE IF NOT EXISTS follows
(
    user_id BIGINT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id  BIGINT REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, tag_id)
);

CREATE TABLE IF NOT EXISTS snippet_tags
(
    snippet_id BIGINT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id     BIGINT REFERENCES tags (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (snippet_id, tag_id)
);

CREATE TABLE IF NOT EXISTS roles
(
    id  BIGINT IDENTITY PRIMARY KEY,
    role VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS reported
(
    snippet_id      INT REFERENCES snippets (id) ON UPDATE CASCADE ON DELETE CASCADE,
    reporter_id     INT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    detail          VARCHAR(310),
    owner_dismissed BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (snippet_id, reporter_id)
);


CREATE VIEW complete_snippets AS
SELECT aux.sn_id   AS id,
       aux.code    AS code,
       aux.title   AS title,
       aux.descr   AS description,
       aux.dc      AS date_created,
       aux.user_id AS user_id,
       aux.u_name  AS username,
       aux.rep     AS reputation,
       aux.lang    AS lang,
       aux.reg     AS region,
       aux.ver     AS verified,
       aux.lang_id AS language_id,
       l.name      AS language,
       aux.icon    AS icon,
       aux.votes   AS votes,
       aux.flag    AS flagged,
       aux.deleted AS deleted
FROM (
         SELECT sn.id           AS sn_id,
                sn.code         AS code,
                sn.title        AS title,
                sn.description  AS descr,
                sn.language_id  AS lang_id,
                sn.date_created AS dc,
                u.id            AS user_id,
                u.username      AS u_name,
                u.reputation    AS rep,
                u.icon          AS icon,
                u.lang          AS lang,
                u.region        AS reg,
                u.verified      AS ver,
                sn.votes        AS votes,
                sn.flag         AS flag,
                sn.deleted      AS deleted
         FROM (SELECT sni.id                    AS id,
                      sni.code                  AS code,
                      sni.title                 AS title,
                      sni.description           AS description,
                      sni.language_id           AS language_id,
                      sni.date_created          AS date_created,
                      sni.user_id               AS user_id,
                      sni.flagged               AS flag,
                      sni.deleted               AS deleted,
                      SUM(CASE WHEN vf.is_positive THEN 1 ELSE -1 END) AS votes
               FROM snippets AS sni
                        LEFT OUTER JOIN votes_for AS vf ON vf.snippet_id = sni.id
               GROUP BY sni.id) AS sn
                  JOIN users AS u ON sn.user_id = u.id
     ) AS aux
         JOIN languages AS l ON aux.lang_id = l.id;
