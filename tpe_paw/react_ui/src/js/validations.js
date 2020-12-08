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
