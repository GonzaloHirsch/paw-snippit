/*
    TO manually populate database in terminal: psql -U username -d myDataBase -a -f myInsertFile
 */

insert into users(username, password, email) values('defaultUser','defaultPassword','defaultUser@default.com');

insert into languages(name) values('java');
insert into tags(name) values('tag');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUsername'),'snippet1','description','javajavjajavjajvajvjajvjavjajvja');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUsername'),'snippet2','description','');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUsername'),'snippet3','description','javajavjajavjajvajvjajvjavjajvja');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUsername'),'snippet4','description','javajavjajavjajvajvjajvjavjajvja');
