# PAW 2020 - Final - Grupo 2

## Credenciales Útiles

Credenciales para el Admin:

User: admin
Pass: AdM1n2o2oP4w

Credenciales para un usuario de prueba:
(Se pueden crear usuarios con libertad)

User: JaneRoe
Pass: password

Credenciales para un usuario con suficiente reputación para reportar snippets:

User: GonzaloHirsch
Pass: AdminPaw123!

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

Proyecto de Aplicaciones Web
Cursada -> 1er Cuatrimestre - 2020
Final -> 2do Cuatrimestre - 2020