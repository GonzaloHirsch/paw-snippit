# PAW 2020 - Final - Grupo 2

## Credenciales Útiles

Credenciales para el Admin:
```
User: pawadmin
Pass: AdM1n2o2oP4w
```

Credenciales para un usuario de prueba (se pueden crear usuarios con libertad):
```
User: JaneRoe
Pass: password
```

Credenciales para un usuario con suficiente reputación para reportar snippets:
```
User: GonzaloHirsch
Pass: AdminPaw123!
```

## Deployment

NOTE: To complete deployment from other profile, change the username in connection to Pampero.

Building the project:

```
mvn clean package
```

Copy the webapp.war to app.war to Desktop:

```
cp webapp/target/webapp.war $HOME/Desktop/app.war && cd $HOME/Desktop/
```

Upload to Pampero:

```
sftp ghirsch@pampero.it.itba.edu.ar
put app.war
exit
```

Connect to Pampero:

```
ssh pampero.it.itba.edu.ar -l ghirsch
```

Connect to Remote:

```
sftp paw-2020a-2@10.16.1.110
```

Copy file from Pampero to Remote:

```
cd web/
put app.war
exit
```

## API Endpoints

### Auth

- /auth/login --> POST

### Languages

- /languages --> GET, POST
- /languages/search --> GET
- /languages/all --> GET
- /languages/{id} --> GET, DELETE
- /languages/exists --> GET
- /languages/{id}/snippets --> GET
- /languages/{id}/snippets/search --> GET

### Tags

- /tags --> GET, POST
- /tags/search --> GET
- /tags/all --> GET
- /tags/{id} --> GET, DELETE
- /tags/exists --> GET
- /tags/{id}/snippets --> GET
- /tags/{id}/snippets/search --> GET
- /tags/{id}/follow --> PUT, DELETE
- /tags/{id}/users/{userId}/follows --> GET

### Users

- /users --> POST
- /users/{id} --> GET, POST
- /users/active_snippets --> GET
- /users/active_snippets/search --> GET
- /users/deleted_snippets --> GET
- /users/deleted_snippets/search --> GET
- /users/{id}/profile_photo --> GET, PUT
- /users/{id}/description --> PUT
- /users/{id}/send_verify_email --> POST
- /users/{id}/verify_email --> POST
- /users/recover_password --> POST
- /users/{id}/valid_token --> POST
- /users/{id}/change_password --> POST
- /users/{id}/favorite_snippets --> GET
- /users/{id}/favorite_snippets/search --> GET
- /users/{id}/following_snippets --> GET
- /users/{id}/following_snippets/search --> GET
- /users/{id}/following_tags --> GET
- /users/{id}/upvoted_snippets --> GET
- /users/{id}/upvoted_snippets/search --> GET

### Snippets

- /snippets --> GET, POST
- /snippets/{id} --> GET, DELETE
- /snippets/search --> GET
- /snippets/explore/search --> GET
- /snippets/flagged --> GET
- /snippets/flagged/search --> GET
- /snippets/{id}/tags --> GET
- /snippets/{id}/vote_count --> GET
- /snippets/{id}/restore --> PUT
- /snippets/{id}/vote_positive --> PUT, DELETE
- /snippets/{id}/vote_negative --> PUT, DELETE
- /snippets/{id}/flag --> PUT, DELETE
- /snippets/{id}/fav --> PUT, DELETE
- /snippets/{id}/report --> PUT
- /snippets/{id}/report/dismiss --> PUT

### Integrantes del Grupo

Florencia Petrikovich - 58637

Gonzalo Hirsch - 59089

**Proyecto de Aplicaciones Web**

Cursada -> 1er Cuatrimestre - 2020

Final -> 2do Cuatrimestre - 2020
