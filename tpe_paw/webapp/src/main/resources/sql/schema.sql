--drop table if exists votes_for;
--drop table if exists favorites;
--drop table if exists follows;
--drop table if exists snippet_tags;
--drop table if exists tags;
--drop table if exists snippets;
--drop table if exists languages;
--drop table if exists users;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) UNIQUE,
    password VARCHAR(30) NOT NULL,
    email VARCHAR(60) UNIQUE,
    description VARCHAR(300),
    reputation INT,
    date_joined TIMESTAMP ,
    icon bytea
);

CREATE TABLE IF NOT EXISTS languages (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS snippets (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL,
    title VARCHAR(50),
    description VARCHAR(500),
    code VARCHAR(6000),
    date_created TIMESTAMP ,
    language_id INT REFERENCES languages(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS votes_for (
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL,
    snippet_id INT REFERENCES snippets(id) ON UPDATE CASCADE ON DELETE CASCADE,
    type INT CHECK(type IN (-1, 1)),
    CONSTRAINT one_snippet_one_vote PRIMARY KEY(user_id, snippet_id)
);

CREATE TABLE IF NOT EXISTS favorites (
    snippet_id INT REFERENCES snippets(id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(snippet_id, user_id)
);

CREATE TABLE IF NOT EXISTS follows (
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id INT REFERENCES tags(id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(user_id, tag_id)
);

CREATE TABLE IF NOT EXISTS snippet_tags (
    snippet_id INT REFERENCES snippets(id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id INT REFERENCES tags(id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(snippet_id, tag_id)
);

CREATE OR REPLACE FUNCTION update_reputation() RETURNS TRIGGER AS
    '
    DECLARE
        newReputation int;
        uid bigint;
        snippetId bigint;
    BEGIN
        IF TG_OP = ''INSERT'' OR TG_OP =  ''UPDATE'' THEN
            snippetId = NEW.snippet_id;
        ELSIF TG_OP = ''DELETE'' THEN
            snippetId = OLD.snippet_id;
        END IF;
        uid = (SELECT user_id FROM snippets AS sn WHERE sn.id = snippetId);
        newReputation = (SELECT reputation FROM users AS u WHERE u.id = uid);
        IF NEW IS NOT null THEN
            newReputation = newReputation + NEW.type;
        END IF;
        IF OLD IS NOT null THEN
            newReputation = newReputation - OLD.type;
        END IF;
        UPDATE users AS u SET reputation = newReputation WHERE u.id = uid;
        return new;
    END;
    ' language plpgsql;

DROP TRIGGER IF EXISTS update_reputation_trigger ON votes_for;

CREATE TRIGGER update_reputation_trigger AFTER INSERT OR UPDATE OR DELETE ON votes_for
    FOR EACH ROW
    EXECUTE FUNCTION update_reputation();

