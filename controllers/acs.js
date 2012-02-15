var forms = require('../model/forms'),
  _ = require('underscore');

module.exports = function (app) {
  app.get('/login.html', function(req, res){
    res.render('login', {title:"sign in"});
  });

  app.post(
    '/login.html', forms.LoginForm,
    function(req, res) {
      if (req.form.isValid) {
        console.log('form was valid');
        /*User.authenticate(
          req.form.email, req.form.password,
          function (err, user) {
            if (user) {
              req.session.regenerate(function(){
                req.session.user = user;
                res.redirect('/home/');
              });
            } else {
              if (!req.session.messages)
                req.session.messages = [];

              req.session.messages.push(
                'Autenticação falhou, verifique seu usuário e senha');
              res.redirect('back');
            }
          });*/
        res.render();
      } else {
        console.log('form was not valid');

        var errors = {};
        for (field in req.form) {
          var fieldErrors = req.form.getErrors(field);
          errors[field] = fieldErrors;
        }

        res.json(errors);
      }
    });
};