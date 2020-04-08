/*
    TO manually populate database in terminal: psql -U username -d myDataBase -a -f myInsertFile
 */
delete from users where true;
delete from languages where true;
delete from tags where true;
delete from snippets where true;
delete from votes_for where true;
delete from favorites where true;
delete from follows where true;
delete from snippet_tags where true;



insert into users(username, password, email) values('defaultUser','defaultPassword','defaultUser@default.com');

insert into languages(name) values('java');

insert into tags(name) values('tag');

insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUser'),'snippet1','description','javajavjajavjajvajvjajvjavjajvja');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUser'),'snippet2','description','');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUser'),'snippet3','description','javajavjajavjajvajvjajvjavjajvja');
insert into snippets(user_id,title, description,code) values((select id from users where username='defaultUser'),'snippet4','description','javajavjajavjajvajvjajvjavjajvja');

insert into votes_for(user_id, snippet_id) values((select id from users where username='defaultUser'),(select id from snippets where title='snippet1'));

insert into favorites(snippet_id,user_id) values((select id from snippets where title='snippet1'),(select id from users where username='defaultUser'));

insert into follows(user_id,tag_id) values((select id from users where username='defaultUser'),(select id from tags where name='tag'));

insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='snippet1'),(select id from tags where name='tag'));

