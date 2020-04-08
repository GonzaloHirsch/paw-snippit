/*TODO: Borrar esto en el futuro*/
drop table snippets;
drop table users;
drop table tags;

create table if not exists users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    dateJoined VARCHAR(255)
);

create table if not exists snippets(
    id SERIAL PRIMARY KEY,
    owner INTEGER NULL, /*Lo dejo en Null por ahora para testeo. TODO: Cambiar a NULL*/
    code VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY(owner) REFERENCES users(id)
);

create table if not exists tags(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

/*Agregar la tabla de snippet_has_tag (para R NxN)*/

/*Testeo*/
insert into users(username,password,email) values('user1','password1','email');
insert into snippets(code,title) values('java','title1');

insert into tags(name) values('tag1');