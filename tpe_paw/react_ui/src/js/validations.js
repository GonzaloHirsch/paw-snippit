import i18n from "../i18n";

// Handles the change in one field, validates that field
export function handleChange(e, name, validations, fields, errors) {
  fields[name] = e.target.value;
  errors[name] = validations[name](fields[name]);
  return { fields: fields, errors: errors };
}

// Validates the whole form
export function validateAll(validations, fields) {
  let errors = {};
  for (let key in fields) {
    errors[key] = validations[key](fields[key]);
  }
  return errors;
}

export function hasErrors(errors) {
  let hasErrors = false;
  for (let key in errors) {
    if (errors[key] !== null) {
      hasErrors = true;
    }
  }
  return hasErrors;
}

export const LOGIN_VALIDATIONS = {
  user: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyUser")) ||
      (val.length < 6 && i18n.t("login.form.errors.smallUser")) ||
      (val.length > 50 && i18n.t("login.form.errors.bigUser")) ||
      (!RegExp("^[a-zA-Z0-9-_.]+$").test(val) &&
        i18n.t("login.form.errors.invalidUser")) ||
      null
    );
  },
  pass: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyPass")) ||
      (val.length < 8 && i18n.t("login.form.errors.smallPass")) ||
      (RegExp("\\s").test(val) && i18n.t("login.form.errors.invalidPass")) ||
      null
    );
  },
  remember: (val) => {
    return null;
  },
};

export const RECOVER_SEND_VALIDATIONS = {
  email: (val) => {
    return (
      (!val && i18n.t("recover.form.errors.emptyEmail")) ||
      (!RegExp(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$"
      ).test(val) &&
        i18n.t("recover.form.errors.invalidEmail")) ||
      null
    );
  },
};

export const CHANGE_PASS_VALIDATIONS = {
  newPassword: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyPass")) ||
      (val.length < 8 && i18n.t("login.form.errors.smallPass")) ||
      (RegExp("\\s").test(val) && i18n.t("login.form.errors.invalidPass")) ||
      null
    );
  },
  repeatNewPassword: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyPass")) ||
      (val.length < 8 && i18n.t("login.form.errors.smallPass")) ||
      (RegExp("\\s").test(val) && i18n.t("login.form.errors.invalidPass")) ||
      null
    );
  },
};

export const VERIFY_EMAIL_VALIDATIONS = {
  code: (val) => {
    return (
      (!val && i18n.t("verifyEmail.form.errors.emptyCode")) ||
      (val.length !== 6 &&
        i18n.t("verifyEmail.form.errors.invalidCodeLength")) ||
      (!RegExp("^\\d{6}$").test(val) &&
        i18n.t("verifyEmail.form.errors.invalidFormat")) ||
      null
    );
  },
};

export const EXPLORE_FORM_VALIDATIONS = {
  title: (val) => {
    return (
      (val.length > 50 && i18n.t("explore.form.errors.title", { num: 50 })) ||
      null
    );
  },
  username: (val) => {
    return (
      (val.length > 50 &&
        i18n.t("explore.form.errors.username", { num: 50 })) ||
      null
    );
  },
};

export const REPORT_VALIDATIONS = {
  message: (val) => {
    return (
      (!val && i18n.t("snippetDetail.reporting.form.errors.emptyMessage")) ||
      (val.length > 300 &&
        i18n.t("snippetDetail.reporting.form.errors.lengthMessage", {
          limit: 300,
        })) ||
      null
    );
  },
};

export const REGISTER_VALIDATIONS = {
  username: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyUser")) ||
      (val.length < 6 && i18n.t("login.form.errors.smallUser")) ||
      (val.length > 50 && i18n.t("login.form.errors.bigUser")) ||
      (!RegExp("^[a-zA-Z0-9-_.]+$").test(val) &&
        i18n.t("login.form.errors.invalidUser")) ||
      null
    );
  },
  email: (val) => {
    return (
      (!val && i18n.t("recover.form.errors.emptyEmail")) ||
      (!RegExp(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$"
      ).test(val) &&
        i18n.t("recover.form.errors.invalidEmail")) ||
      null
    );
  },
  password: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyPass")) ||
      (val.length < 8 && i18n.t("login.form.errors.smallPass")) ||
      (RegExp("\\s").test(val) && i18n.t("login.form.errors.invalidPass")) ||
      null
    );
  },
  repeatPassword: (val) => {
    return (
      (!val && i18n.t("login.form.errors.emptyPass")) ||
      (val.length < 8 && i18n.t("login.form.errors.smallPass")) ||
      (RegExp("\\s").test(val) && i18n.t("login.form.errors.invalidPass")) ||
      null
    );
  },
};

export const PROFILE_VALIDATION = {
  description: (val) => {
    return (
      (val.length > 500 &&
        i18n.t("profile.form.errors.description", {
          limit: 500,
        })) ||
      null
    );
  },
};

export const SNIPPET_CREATE_VALIDATIONS = {
  title: (val) => {
    return (
      (val.length === 0 &&
        i18n.t("snippetCreate.errors.empty", {
          field: "Title",
        })) ||
      (val.length > 50 &&
        i18n.t("snippetCreate.errors.maxLimit", { field: "Title", num: 50 })) ||
      (val.length < 5 &&
        i18n.t("snippetCreate.errors.minLimit", { field: "Title", num: 5 })) ||
      null
    );
  },
  description: (val) => {
    return (
      (val.length > 500 &&
        i18n.t("snippetCreate.errors.minLimit", {
          field: "Description",
          num: 500,
        })) ||
      null
    );
  },
  code: (val) => {
    return (
      (val.length === 0 &&
        i18n.t("snippetCreate.errors.empty", {
          field: "Code",
        })) ||
      (val.length > 30000 &&
        i18n.t("snippetCreate.errors.maxLimit", {
          field: "Code",
          num: 30000,
        })) ||
      (val.length < 5 &&
        i18n.t("snippetCreate.errors.minLimit", { field: "Code", num: 5 })) ||
      null
    );
  },
  language: (val) => {
    return (
      (val.length === 0 && i18n.t("snippetCreate.errors.language")) || null
    );
  },
  tags: (val) => {
    return null;
  },
};
