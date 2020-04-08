-- drop table if exists votes_for;
-- drop table if exists favorites;
-- drop table if exists follows;
-- drop table if exists snippet_tags;
-- drop table if exists tags;
-- drop table if exists snippets;
-- drop table if exists languages;
-- drop table if exists users;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) UNIQUE,
    password VARCHAR(30) NOT NULL,
    email VARCHAR(60) UNIQUE,
    reputation INT,
    date_joined DATE,
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
    id SERIAL PRIMARY KEY ,
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL,
    title VARCHAR(50),
    description VARCHAR(300),
    code VARCHAR(500),
    date_created DATE,
    language_id INT REFERENCES languages(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS votes_for (
    user_id INT REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL,
    snippet_id INT REFERENCES snippets(id) ON UPDATE CASCADE ON DELETE CASCADE,
    type INT,
    PRIMARY KEY(user_id, snippet_id)
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