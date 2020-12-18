const i18n_es = {
  app: "Snippit",
  snippet: "Snippet",
  snippetsEmpty: "No hay snippets aquí",
  itemsEmpty: "No hay {{items}} aquí",
  snippet_plural: "Snippets",
  snippetWithNumber: "{{count}} Snippet",
  snippetWithNumber_plural: "{{count}} Snippets",
  tagsWithNumber: "{{count}} Etiqueta",
  tagsWithNumber_plural: "{{count}} Etiquetas",
  languagesWithNumber: "{{count}} Lenguaje",
  languagesWithNumber_plural: "{{count}} Lenguajes",
  tags: "Etiqueta",
  tags_plural: "Etiquetas",
  languages: "Lenguaje",
  languages_plural: "Lenguajes",
  tagsLower: "etiqueta",
  tagsLower_plural: "etiquetas",
  languagesLower: "lenguaje",
  languagesLower_plural: "lenguajes",
  create: "Crear",
  nav: {
    home: "Página Principal",
    login: "Iniciar Sesión",
    signup: "Registrarse",
    snippets: "Snippets",
    snippetsDetail: "Detalle de Snippets",
    homeSearch: "Búsqueda Principal",
    favorites: "Favoritos",
    tags: "Etiquetas",
    languages: "Lenguajes",
    explore: "Explorar",
    profile: "Perfil",
    following: "Siguiendo",
    upvoted: "Votados",
    logout: "Cerrar Sesión",
    flagged: "Marcados",
    greeting: "¡Hola, {{user}}!",
    recover: "Recuperar",
    resetPassword: "Restablecer Contraseña",
    verify: "Verificar Email",
    goodbye: "Adios",
    users: "Usuarios",
    error_404: "Error 404",
    search: {
      searchHint: "Búsqueda...",
      showEmpty: "Escondiendo vacíos",
      hideEmpty: "Mostrando vacíos",
      showOnlyFollowing: "Mostrando todos",
      hideOnlyFollowing: "Mostrando seguidos",
      filter: {
        hint: "Buscar por",
        all: "Todo",
        tag: "Etiqueta",
        title: "Título",
        content: "Contenido",
        username: "Usuario",
        language: "Lenguaje",
      },
      order: {
        hint: "Ordenar por",
        ascending: "Ascendente",
        descending: "Descendente",
        no: "Sin orden",
      },
    },
  },
  login: {
    title: "Bienvenido devuelta a ",
    message: "Por favor inicie sesión",
    signup: "¿No tenés una cuenta? ",
    signupCall: "¡Regístrate aquí!",
    recover: "¿Olvidaste tu contraseña? ",
    recoverCall: "¡Recupérala aquí!",
    form: {
      remember: "Recuérdame",
      user: "Nombre de usuario",
      pass: "Contraseña",
      action: "Iniciar Sesión",
      errors: {
        invalidGeneral: "Nombre de usuario o contraseña inválida",
        emptyUser: "Nombre de usuario es requerido",
        smallUser: "Nombre de usuario debe tener al menos 6 carácteres",
        bigUser: "Nombre de usuario no debe exceder los 50 carácteres",
        invalidUser: "Nombre de usuario inválido",
        emptyPass: "Contraseña es requerida",
        smallPass: "Contraseña debe tener al menos 8 carácteres",
        invalidPass: "Contraseña inválida",
      },
    },
  },
  signup: {
    title: "Bienvenido a ",
    message: "Por favor regístrese",
    login: "¿Ya tenés una cuenta? ",
    loginCall: "¡Inicia sesión aquí!",
    form: {
      user: "Nombre de usuario",
      email: "Email",
      pass: "Contraseña",
      repeatPass: "Repetir la contraseña",
      action: "¡Iniciar Sesión!",
    },
  },
  recover: {
    title: "Recuperar contraseña",
    message:
      "Ingresa el email asociado a tu cuenta y te mandaremos un enlace para recuperar tu contraseña",
    afterMessage:
      "Un enlace se ha mandado al email indicado para completar el proceso de recupero de contraseña",
    form: {
      email: "Insertar tu email aquí",
      action: "Mandar mail",
      afterAction: "Ir a Pag. Principal",
      errors: {
        emptyEmail: "Email es requerido",
        invalidEmail: "Email inválido",
      },
    },
  },
  snippetCreate: {
    name: "Creación de Snippet",
    message: "Comienza creando tu primer Snippet",
    button: "Crear Snippet",
    header: "Crear un Snippet",
    fields: {
      title: "Título",
      description: "Descripción",
      code: "Código",
      language: "Lenguaje",
      tags: "Etiquetas",
    },
    placeholders: {
      title: "Ingresa el título de tu snippet...",
      description: "Ingresa la descripción de tu snippet...",
      code: "Ingresa el código de tu snippet...",
      language: "Seleciona el lenguaje...",
      tags: "Seleciona etiquetas características...",
    },
    errors: {
      empty: "{{field}} no puede estar vacío",
      maxLimit: "{{field}} debe tener menos que {{num}} carácteres",
      minLimit: "{{field}} debe incluir mas de {{num}} carácteres",
      language: "Se debe selecionar un lenguaje",
      tags: "",
    },
  },
  explore: {
    form: {
      orderBy: {
        header: "Orden",
        placeholder: "Ordenar por",
        reputation: "Reputación",
        votes: "Votos",
        title: "Título",
        date: "Fecha subido",
      },
      sort: {
        placeholder: "Orden",
        asc: "Ascendente",
        desc: "Descendente",
        no: "Sin orden",
      },
      flagged: {
        header: "Snippets Marcados",
        placeholder: "Incluir snippets marcados",
      },
      title: {
        header: "Título",
        placeholder: "Título del snippet",
      },
      language: {
        header: "Lenguaje",
        placeholder: "Selecciona un lenguaje",
      },
      tags: {
        header: "Etiqueta",
        placeholder: "Selecciona una etiqueta",
      },
      user: {
        username: "Nombre de usuario",
        reputation: "Reputación del usuario",
      },
      votes: "Votos del snippet",
      date: "Fecha subido",
      placeholder: {
        from: "Desde",
        to: "Hasta",
      },
      submit: "Buscar",
      reset: "Reiniciar",
      errors: {
        range: "Límite inferior debe ser menor al superior",
        min: "Valores deben ser mayores que {{min}}",
        max: "Valores deben ser menores que {{max}}",
        title: "Título debe tener menos que {{num}} carácteres",
        username: "Usuario debe tener menos que {{num}} carácteres",
        date: "Fecha inferior debe ser antes que la superior",
      },
    },
  },
  changePassword: {
    invalidLink: {
      message: "El enlace a expirado",
      action: "Ir a Recuperar Contraseña",
    },
    changeForm: {
      title: "Cambiar contraseña",
      password: "Contraseña",
      repeatPassword: "Repetir contraseña",
      action: "Cambiar contraseña",
      errors: {
        differentPasswords: "Contraseñas no son iguales",
      },
    },
    changeSuccess: {
      message: "La contraseña ha sido restablecida",
      actionGoLogin: "Ir a Iniciar Sesión",
      actionGoHome: "Ir a Página Principal",
    },
  },
  snippetDetail: {
    copied: "¡Copiado!",
    uploaded: "Subido el {{date}}",
    flagged: {
      title: "¡Cuidado!",
      message:
        "Este snippet fue marcado como incorrecto por el administrador. Puede no funcionar o ser inseguro.",
    },
    deleted: {
      title: "¡Este snippet fue borrado por su dueño!",
      message:
        "Snippets borrados permaneceran guardados en Favoritos si fueron likeados antes de ser borrados. Si eres el dueño, lo podras restaurar.",
    },
    reported: {
      title: "Este snippet fue reportado por un usuario.",
      message:
        "Alguien te quiere avisar de algún error que encontró o de alguna sugerencia. Revisa tu email para ver el mensaje que te envío el usuario.",
      messageSuggestion:
        "Si encuentra un error, recomendamos que borre su snippet antes que el administrador lo marque como inseguro.",
    },
    reporting: {
      title: "Reportar un Snippet",
      message:
        "Escribe un mensaje al dueño de este snippet para informarle sobre errores o sugerencias que tengas.",
      actionConfirm: "Confirmar",
      actionCancel: "Cancelar",
      form: {
        hint: "Escribe tu mensaje aquí",
        errors: {
          emptyMessage: "El mensaje no puede estar vacío",
          lengthMessage:
            "Mensaje demansiado largo, no debe exceder los {{limit}} carácteres",
        },
      },
    },
  },
  profile: {
    activeSnippets: "Snippets activos",
    deletedSnippets: "Snippets borrados",
    dateJoined: "Fecha registrado {{date}}",
    verify: {
      title: "Verifica tu cuenta",
      message:
        "Tu email no fue verificado, esto significa que no podras recibir mails nuestros. Para verificar tu cuenta, presiona ",
      action: "aquí.",
    },
    joinedOn: "Registrado el {{date}}",
    edit: {
      begin: "Editar descripción",
      save: "Guardar descripción",
      discard: "Descartar",
    },
    form: {
      descriptionPlaceholder: "Ingresa tu descripción aquí...",
      errors: {
        description: "La aquí no puede exceder los {{num}} carácteres",
      },
    },
    errors: {
      imageTooBig:
        "Imagen subida es demasiado grande, tamaño maximo es {{size}}",
      changeImageServerError:
        "Error cambiando la imagen, por favor inténtelo más tarde.",
    },
  },
  verifyEmail: {
    docTitle: "Verificar Email",
    title: "Verifica tu cuenta",
    message: "Ingresa el código mandado a tu cuenta de email",
    form: {
      action: "Verificar",
      actionResend: "Mandar código denuevo",
      code: "Código",
      validResend: "Email mandado exitosamente",
      errors: {
        emptyCode: "Código es requerido",
        invalidCodeLength: "Código debe contener 6 dígitos",
        invalidFormat: "Código debe contener solamente dígitos",
        invalidCode: "Código es inválido",
        invalidResend: "Error mandando el mail con el código",
      },
    },
  },
  loading: {
    general: "Cargando...",
    page: "Snippit se está cargando...",
  },
  goodbye: {
    title: "¡Vuelve pronto!",
    login: "Devuelta a Iniciar Sesión",
    home: "Devuelta a Página Principal",
    create: "Crear una cuenta nueva",
  },
  errors: {
    e400: "La busqueda ingresada es inválida",
    e404: {
      header: "Error 404",
      number: "404",
      title: "Contenido no hallado",
      message:
        "Parece que la página que estas buscando no existe, ¿por qué no vuelves a la Página Principal?",
      action: "¡Volver a la Página Principal!",
    },
    e500: {
      header: "Error 500",
      number: "500",
      title: "Error de servidor",
      message:
        "Parece que hubo un error en nuestro lado, por favor inténtelo denuevo más tarde o vuelve a la Página Principal",
      action: "¡Volver a la Página Principal!",
    },
    serverError: "Error en el servidor, por favor inténtelo denuevo más tarde",
    unknownError: "Error desconocido, por favor inténtelo denuevo más tarde",
    noConnection:
      "Conexión al servidor fue perdida, por favor inténtelo denuevo más tarde",
    snippetCreate:
      "Ocurrio un error al crear tu snippet. Por favor inténtelo denuevo más tarde.",
  },
  items: {
    empty: "Ningún Snippets",
    follow: "Seguir",
    unfollow: "No seguir",
    snippetsFor: "Snippets para {{item}}",
    delete: {
      actionName: "Borrar",
      title: "Confirmar borrado",
      tagsMessage:
        '¿Está seguro de que desea eliminar "{0}" de la lista de etiquetas? Las etiquetas borradas seran eliminadas de todos los snippets que la contengan y de la lista Siguiendo de los usuarios. Está acción no se puede deshacer.',
      languagesMessage:
        '¿Está seguro de que desea eliminar "{0}" de la lista de lenguajes? Lenguajes borrados ya no estaran disponibles en la creación de nuevos snippets ni estaran en la lista de lenguajes al menos que sean agregados nuevamente por un administrador.',
      cancel: "Cancelar",
      confirm: "Confirmar",
    },
    deleted: {
      title: "Este lenguaje ha sido borrado por el administrador",
      message:
        "Lenguajes borrados ya no estaran disponibles en la creación de nuevos snippets ni estaran en la list de lenguajes",
    },
  },
  itemCreate: {
    header: "Creat Etiquetas y Lenguajes",
    name: "Creación de ítems",
    add: "Agregar",
    indications: "La lista de {{items}} a agregar aparecerá aquí abajo",
    existsMsg:
      "Elementos de la lista con fondo gris ya existen y no serán creados nuevamente",
    tag: {
      name: "etiqueta",
      name_plural: "etiquetas",
      action: "Agregar Etiquetas",
    },
    language: {
      name: "lenguaje",
      name_plural: "lenguajes",
      action: "Agregar Lenguajes",
    },
    errors: {
      length: "Longitud debe estar entre 1 y 30 carácteres",
      spaces: "Valór no puede contener espacios",
    },
    placeholders: {
      item: "Ingresa {{item}} que quieras agregar...",
    },
    alert: {
      success: "Exitosamente se agrego la liste de {{items}}.",
      danger: "Algo salio mal, algunos {{items}} no fueron agregados.",
      addError: "Un error ocurrio agregando {{name}} a la lista.",
    },
  },
  following: {
    errorMsg:
      "Un error se produjo al dejar de seguir {{name}}. por favor inténtelo denuevo más tarde.",
    showMore: "Mostrar más",
  },
};

export default i18n_es;
