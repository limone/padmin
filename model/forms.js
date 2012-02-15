var form = require("express-form"),
  filter = form.filter,
  validate = form.validate;

var LoginForm = form(
  filter("username").trim(),
  validate("username").required(null, "Please provide your username").isAlphanumeric("Please provide a valid username."),

  filter("password").trim(),
  validate("password")
    .required(null, "Please type your password.")
);

exports.LoginForm = LoginForm;