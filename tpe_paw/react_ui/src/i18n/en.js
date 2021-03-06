const i18n_en = {
  app: "Snippit",
  snippet: "Snippet",
  snippetsEmpty: "There are no snippets here",
  itemsEmpty: "There are no {{items}} here",
  snippet_plural: "Snippets",
  snippetWithNumber: "{{count}} Snippet",
  snippetWithNumber_plural: "{{count}} Snippets",
  tagsWithNumber: "{{count}} Tag",
  tagsWithNumber_plural: "{{count}} Tags",
  languagesWithNumber: "{{count}} Language",
  languagesWithNumber_plural: "{{count}} Languages",
  tags: "Tag",
  tags_plural: "Tags",
  languages: "Language",
  languages_plural: "Languages",
  tagsLower: "tag",
  tagsLower_plural: "tags",
  languagesLower: "language",
  languagesLower_plural: "languages",
  create: "Create",
  nav: {
    home: "Home",
    homeSearch: "Home Search",
    login: "Login",
    signup: "Sign Up",
    snippets: "Snippets",
    snippetsDetail: "Snippets Detail",
    favorites: "Favorites",
    tags: "Tags",
    languages: "Languages",
    explore: "Explore",
    profile: "Profile",
    following: "Following",
    upvoted: "Upvoted",
    logout: "Log Out",
    flagged: "Flagged",
    greeting: "Hello, {{user}}!",
    recover: "Recover",
    resetPassword: "Reset Password",
    verify: "Verify Email",
    goodbye: "Goodbye",
    users: "Users",
    error_404: "Error 404",
    search: {
      searchHint: "Search...",
      showEmpty: "Hiding Empty",
      hideEmpty: "Showing Empty",
      showOnlyFollowing: "Showing All",
      hideOnlyFollowing: "Showing Followed",
      filter: {
        hint: "Search By",
        all: "All",
        tag: "Tag",
        title: "Title",
        content: "Content",
        username: "Username",
        language: "Language",
      },
      order: {
        hint: "Sort By",
        ascending: "Ascending",
        descending: "Descending",
        no: "No Order",
      },
    },
  },
  login: {
    title: "Welcome back to ",
    message: "Please Log In",
    signup: "Don't have an account? ",
    signupCall: "Sign Up Here!",
    recover: "Forgot your password? ",
    recoverCall: "Recover it Here!",
    form: {
      remember: "Remember Me",
      user: "Username",
      pass: "Password",
      action: "Log In",
      errors: {
        invalidGeneral: "Invalid username or password",
        emptyUser: "User is required",
        smallUser: "Username must be at least 6 characters long",
        bigUser: "Username must be at less than 50 characters long",
        invalidUser: "Invalid username",
        emptyPass: "Password is required",
        smallPass: "Password must be at least 8 characters long",
        invalidPass: "Password can not contain spaces",
      },
    },
  },
  signup: {
    title: "Welcome to ",
    message: "Please Sign Up",
    login: "Already have an account? ",
    loginCall: "Log In Here!",
    form: {
      user: "Username",
      email: "Email",
      pass: "Password",
      repeatPass: "Repeat Password",
      action: "Sign Up!",
    },
  },
  recover: {
    title: "Recover Password",
    message:
      "Enter your account's email so that we can send you a link to reset your password",
    afterMessage:
      "A link has been sent to your email to complete the password recovery process",
    form: {
      email: "Insert your email here",
      action: "Send Recovery Email",
      afterAction: "Go Home",
      errors: {
        emptyEmail: "Email is required",
        invalidEmail: "Invalid email",
      },
    },
  },
  snippetCreate: {
    name: "Snippet Create",
    message: "Go ahead and create your first Snippet",
    button: "Create Snippet",
    header: "Create a Snippet",
    fields: {
      title: "Title",
      description: "Description",
      code: "Code",
      language: "Language",
      tags: "Tags",
    },
    placeholders: {
      title: "Enter your snippet's title...",
      description: "Enter your snippet's description...",
      code: "Enter your snippet's code...",
      language: "Select a language...",
      tags: "Select tags related to your snippet...",
    },
    errors: {
      empty: "{{field}} can not be empty",
      maxLimit: "{{field}} must be less than {{num}} charaters",
      minLimit: "{{field}} must include more than {{num}} charaters",
      language: "Must select a language",
      tags: "",
    },
  },
  explore: {
    form: {
      orderBy: {
        header: "Order",
        placeholder: "Order By",
        reputation: "User Reputation",
        votes: "Snippet Votes",
        title: "Snippet Title",
        date: "Date Uploaded",
      },
      sort: {
        placeholder: "Sort",
        asc: "Ascending",
        desc: "Descending",
        no: "No Order",
      },
      flagged: {
        header: "Flagged Snippets",
        placeholder: "Include Flagged Snippets",
      },
      title: {
        header: "Title",
        placeholder: "Snippet Title",
      },
      language: {
        header: "Language",
        placeholder: "Select a language",
      },
      tags: {
        header: "Tag",
        placeholder: "Select a tag",
      },
      user: {
        username: "Username",
        reputation: "User Reputation",
      },
      votes: "Snippet Votes",
      date: "Date Uploaded",
      placeholder: {
        from: "From",
        to: "To",
      },
      submit: "Search",
      reset: "Reset",
      errors: {
        range: "From value must be smaller than To value",
        min: "Values must be larger than {{min}}",
        max: "Values must be smaller than {{max}}",
        title: "Title must be less than {{num}} characters",
        username: "Username must be less than {{num}} characters",
        date: "From date must be an earlier date than To date",
      },
    },
  },
  changePassword: {
    invalidLink: {
      message: "The link has expired",
      action: "Go to Recover Password",
    },
    changeForm: {
      title: "Change Password",
      password: "Password",
      repeatPassword: "Repeat password",
      action: "Change Password",
      errors: {
        differentPasswords: "Passwords don't match",
      },
    },
    changeSuccess: {
      message: "Password has been successfully reset",
      actionGoLogin: "Go Login",
      actionGoHome: "Go Home",
    },
  },
  snippetDetail: {
    copied: "Copied to clipboard!",
    uploaded: "Uploaded on {{date}}",
    flagged: {
      title: "Beware!",
      message:
        "The following snippet was marked as incorrect by the administrator. It may not work or be unsafe to use.",
    },
    deleted: {
      title: "This snippet has been deleted by the owner!",
      message:
        "Deleted snippets will remain saved in your Favorites if liked before deletion. If you own the deleted snippet, you can recover it.",
    },
    reported: {
      title: "This snippet was reported by a user!",
      message:
        "Someone wants to let you know of an error they found or a suggestion they may have. Check your email to read the message sent by the user.",
      messageSuggestion:
        "If there is an error in your snippet, we recommend you delete it before the administrator flags it as unsafe and harms your reputation.",
    },
    reporting: {
      title: "Report Snippet",
      message:
        "Write a message to the owner of this snippet with any errors you found or suggestions you may have.",
      actionConfirm: "Confirm",
      actionCancel: "Cancel",
      form: {
        hint: "Write your message",
        errors: {
          emptyMessage: "Message cannot be empty",
          lengthMessage:
            "Message too long, must have less than {{limit}} characters",
        },
      },
    },
  },
  profile: {
    activeSnippets: "Active Snippets",
    deletedSnippets: "Deleted Snippets",
    dateJoined: "Date joined {{date}}",
    verify: {
      title: "Verify your account",
      message:
        "Your email has not been verified, meaning you will not receive any emails from us. To verify your email click ",
      action: "here.",
    },
    joinedOn: "Joined on {{date}}",
    edit: {
      begin: "Edit Description",
      save: "Save Description",
      discard: "Discard",
    },
    form: {
      descriptionPlaceholder: "Enter your description here...",
      errors: {
        description: "Description must not be longer than {{limit}} characters",
      },
    },
    errors: {
      imageTooBig: "Uploaded image is too big, maximum size is {{size}}",
      changeImageServerError: "Error changing image, please try again later",
    },
  },
  verifyEmail: {
    docTitle: "Verify Email",
    title: "Verify your account",
    message: "Enter the code sent to your email account",
    form: {
      action: "Verify",
      actionResend: "Resend code to email",
      code: "Code",
      validResend: "Email sent successfully",
      errors: {
        emptyCode: "Code is required",
        invalidCodeLength: "Code must be 6 digits long",
        invalidFormat: "Code must contain only digits",
        invalidCode: "Code is invalid",
        invalidResend: "Error sending email with code",
      },
    },
  },
  loading: {
    general: "Loading...",
    page: "Snippit is loading...",
  },
  goodbye: {
    title: "See you soon!",
    login: "Back to Login",
    home: "Back Home",
    create: "Create a New Account",
  },
  errors: {
    e400: "The entered search is invalid.",
    e404: {
      header: "Error 404",
      number: "404",
      title: "Content Not Found",
      message:
        "Looks like the page you are looking for doesn't exist, why not go back Home?",
      action: "Take me Home!",
    },
    e500: {
      header: "Error 500",
      number: "500",
      title: "Server Error",
      message:
        "Looks like there was an error on our side, please try again later or go back to Home",
      action: "Take me Home!",
    },
    serverError: "Error in server, please try again later",
    unknownError: "Uknown error, please try again later",
    noConnection: "Connection to server lost, please try again later",
    snippetCreate:
      "An error occurred while creating your snippet. Please try again later.",
  },
  items: {
    empty: "No Snippets",
    follow: "Follow",
    unfollow: "Unfollow",
    snippetsFor: "Snippets for {{item}}",
    delete: {
      actionName: "Delete",
      title: "Confirm Deletion",
      tagsMessage:
        'Are you sure you want to delete "{{name}}" from Tags? Deleted tags will be removed from all the snippets containing it and from all users following list. This action can\'t be undone.',
      languagesMessage:
        'Are you sure you want to delete "{{name}}" from Languages? Deleted languages will no longer be available for new snippets and will not appear in the language list unless added again by an administrator.',
      cancel: "Cancel",
      confirm: "Confirm",
    },
    deleted: {
      title: "This language has been deleted by the administrator",
      message:
        "Deleted languages are not available for use in new snippets and do not appear in the languages list.",
    },
  },
  itemCreate: {
    header: "Add Tags and Languages",
    name: "Item Creation",
    add: "Add",
    indications: "List of {{items}} to add will appear below",
    existsMsg:
      "List items with a gray background already exists in Snippit and will not be created again",
    tag: {
      name: "tag",
      name_plural: "tags",
      action: "Add Tags",
    },
    language: {
      name: "language",
      name_plural: "languages",
      action: "Add Languages",
    },
    errors: {
      length: "Value must be between 1 and 30 characters",
      spaces: "Value must not contain spaces",
    },
    placeholders: {
      item: "Enter the {{item}} to add...",
    },
    alert: {
      success: "Successfully added the entered {{items}}.",
      danger: "Something went wrong, some of the {{items}} were not added.",
      addError: "An error occurred when adding {{name}} to the list.",
    },
  },
  following: {
    errorMsg: "Unable to unfollow tag {{name}}. Try again later.",
    showMore: "Show more",
  },
};

export default i18n_en;
