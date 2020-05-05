/*
    TO manually populate database in terminal: psql -U postgres -d paw -a -f populate.sql
 */

delete from roles where true;
delete from user_roles where true;

insert into roles(role) values('ADMIN');
insert into roles(role) values('USER');

insert into user_roles(user_id,role_id) values((select id from users where username='JohnDoe'),(select id from roles where role='USER'));
insert into user_roles(user_id,role_id) values((select id from users where username='JaneRoe'),(select id from roles where role='USER'));
insert into user_roles(user_id,role_id) values((select id from users where username='admin'),(select id from roles where role='USER'));
insert into user_roles(user_id,role_id) values((select id from users where username='admin'),(select id from roles where role='ADMIN'));
