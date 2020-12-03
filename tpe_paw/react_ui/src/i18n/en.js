export default {
  test: "My test in ENGLISH",
  app: "Snippit",
  snippets: "Snippets",
  errors: {
    serverError: "Error in server, please try again later",
    unknownError: "Uknown error, please try again later",
    noConnection: "Connection to server lost, please try again later",
  },
  nav: {
    home: "Home",
    homeSearch: "Home Search",
    login: "Login",
    signup: "Sign Up",
    snippets: "Snippets",
    snippetsDetail: "Snippets Detail",
    searchHint: "Search...",
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
        invalidPass: "Invalid password",
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
  explore: {},
  snippet: {
    flagged: {
      title: "Beware!",
      message: "The following snippet was marked as incorrect by the administrator. It may not work or be unsafe to use."
    }
  }
};
