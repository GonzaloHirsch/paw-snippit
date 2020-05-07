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
            ALTER TABLE users ADD COLUMN lang VAR(5) DEFAULT ''en'';
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE ''column lang already exists in users.'';
        END;
    END;
' language plpgsql;

DO '
    BEGIN
        BEGIN
            ALTER TABLE users ADD COLUMN region VAR(5) DEFAULT ''US'';
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

CREATE OR REPLACE VIEW complete_snippets AS
SELECT aux.sn_id   AS id,
       aux.code    AS code,
       aux.title   AS title,
       aux.descr   AS description,
       aux.dc      AS date_created,
       aux.user_id AS user_id,
       aux.u_name  AS username,
       aux.rep     AS reputation,
       aux.lang    AS lang,
       aux.reg     AS region
       aux.ver     AS verified,
       aux.lang_id AS language_id,
       l.name      AS language,
       aux.icon    AS icon,
       aux.votes   AS votes,
       aux.flag    AS flagged
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
                u.locale        AS locale,
                u.region        AS reg
                u.verified      AS ver,
                sn.votes        AS votes,
                sn.flag         AS flag
         FROM (SELECT sni.id                    AS id,
                      sni.code                  AS code,
                      sni.title                 AS title,
                      sni.description           AS description,
                      sni.language_id           AS language_id,
                      sni.date_created          AS date_created,
                      sni.user_id               AS user_id,
                      sni.flagged               AS flag,
                      SUM(COALESCE(vf.type, 0)) AS votes
               FROM snippets AS sni
                        LEFT OUTER JOIN votes_for AS vf ON vf.snippet_id = sni.id
               GROUP BY sni.id) AS sn
                  JOIN users AS u ON sn.user_id = u.id
     ) AS aux
         JOIN languages AS l ON aux.lang_id = l.id;

-- Notation of trigger uses single quotes instead of $$ due to parser errors while parsing
-- The use of TG_OP is needed to avoid the error of not assigned variables NEW or OLD
CREATE OR REPLACE FUNCTION update_reputation() RETURNS TRIGGER AS
'
    DECLARE
        newReputation int;
        uid           bigint;
        snippetId     bigint;
    BEGIN
        IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
            snippetId = NEW.snippet_id;
        ELSIF TG_OP = ''DELETE'' THEN
            snippetId = OLD.snippet_id;
        END IF;
        uid = (SELECT user_id
               FROM snippets AS sn
               WHERE sn.id = snippetId);
        newReputation = (SELECT reputation
                         FROM users AS u
                         WHERE u.id = uid);
        IF TG_OP = ''INSERT'' THEN
            IF NEW IS NOT null THEN
                newReputation = newReputation + NEW.type;
            END IF;
        ELSIF TG_OP = ''UPDATE'' THEN
            IF NEW IS NOT null THEN
                newReputation = newReputation + NEW.type;
            END IF;
            IF OLD IS NOT null THEN
                newReputation = newReputation - OLD.type;
            END IF;
        ELSIF TG_OP = ''DELETE'' THEN
            IF OLD IS NOT null THEN
                newReputation = newReputation - OLD.type;
            END IF;
        END IF;
        UPDATE users AS u
        SET reputation = newReputation
        WHERE u.id = uid;
        IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
            RETURN NEW;
        ELSIF TG_OP = ''DELETE'' THEN
            RETURN OLD;
        END IF;
    END;
' language plpgsql;

DROP TRIGGER IF EXISTS update_reputation_trigger ON votes_for;

-- Notation uses PROCEDURE instead of FUNCTION to make it backwards compatible
CREATE TRIGGER update_reputation_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON votes_for
    FOR EACH ROW
EXECUTE PROCEDURE update_reputation();

