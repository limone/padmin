var forms = require('../model/forms'),
  _ = require('underscore');

module.exports = function (app) {
  app.get('/login.html', function(req, res){
    res.render('login', {title:"sign in"});
  });

  app.post(
    '/login.html', forms.LoginForm,
    function(req, res) {
      var data = {};
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
        data['status'] = "success";
        res.json(data);
      } else {
        console.log('form was not valid');

        var errors = {};
        for (var field in req.form) {
          var fieldErrors = req.form.getErrors(field);
          errors[field] = fieldErrors;
        }
        data['status'] = 'failed';
        data['errors'] = errors;
        res.json(data);
      }
    });

  app.get('/logout/', function(req, res){
    req.session.destroy(function(){
      res.redirect('/');
    });
  });

  app.post(
    '/login/', /*forms.LoginForm,*/
    function(req, res) {
      /*if (req.form.isValid) {
       User.authenticate(
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
       });
       } else {
       req.session.messages = _.union(
       req.session.messages||[],
       req.form.errors);

       res.redirect('back');
       }*/
    });

  app.post(
    '/signup/'/*, forms.SignupForm*/,
    function(req, res) {
      /*if (req.form.isValid) {
       User.newUser(
       req.form.email, req.form.password,
       function (err, user) {
       if ((user)&&(!err)) {
       req.session.regenerate(function(){
       req.session.user = user;
       res.redirect('/home/');
       });
       } else {
       if (!req.session.messages)
       req.session.messages = [];

       if (err.errors.email) {
       req.session.messages.push(
       err.errors.email.type
       );
       }
       res.redirect('back');
       }
       });

       } else {
       req.session.messages = _.union(
       req.session.messages||[],
       req.form.errors);

       res.redirect('back');
       }*/
    });


  app.get('/forgot-password', function(req, res) {
    res.render('forgot-password');
  });

  app.post(
    '/forgot-password', /*forms.ResetPasswdForm,*/
    function(req, res) {
      /*if (req.form.isValid) {
       User.findOne({email: req.form.email}, function (error, user) {
       if (user && user.password) {
       var oldPasswordHash = encodeURIComponent(user.password);
       var userId = user._id;
       var resetLink = conf.site_url+"/reset-password/?userId="+userId+"&verify="+oldPasswordHash;
       var resetMessage = "Olá, <br/>Clique no link para resetar sua senha no PopBroker:<br/><a href=\""+resetLink+"\">"+resetLink+"</a>";
       mail.message({
       'MIME-Version': '1.0',
       'Content-type': 'text/html;charset=UTF-8',
       from: 'PopBroker Suporte <'+ conf.site_email + '>',
       to: user.email,
       subject: conf.site_name + ': resetar senha'
       }).body(resetMessage)
       .send(function(err) {
       if (err) {
       req.session.messages = [err.toString()];
       res.redirect('back');
       }
       });
       }
       else {
       req.session.messages = ["E-mail não encontrado na lista de usuários!"];
       res.redirect('back');
       }
       });
       }
       else {
       req.session.messages = _.union(
       req.session.messages||[],
       req.form.errors);
       res.redirect('back');
       }*/
    });
};