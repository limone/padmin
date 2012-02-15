var express = require('express');
var pg = require('pg');
var PgClient = require('./util/PgClient.js');
var auth = require('./util/auth.js');

var log = require('log4js').getLogger();

var path = require('path');
if (path.existsSync('./configLocal.js')) {
  var configLocal = require('./configLocal.js');
  conf = configLocal.getSiteConfig();
} else {
  console.log('No local configuration found, using defaults.');
  var configDefault = require('./config.js');
  conf = configDefault.getSiteConfig();
}

var app = module.exports = express.createServer(
  express.bodyParser(),
  express.cookieParser(),
  express.session({
    secret:conf.secret,
    maxAge:new Date(Date.now() + 3600000),
    db:new PgClient(conf.db)
  })
);

// Configuration
app.configure(function () {
  app.set('views', __dirname + '/views');
  app.set('view engine', 'ejs');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});

app.dynamicHelpers({
  messages:function (req) {
    var msgs = req.session.messages;
    req.session.messages = null;
    return msgs
  },
  user:function (req) {
    if (req.session)
      return req.session.user;
  }
});

app.configure('development', function () {
  app.use(express.errorHandler({ dumpExceptions:true, showStack:true }));
});

app.configure('production', function () {
  app.use(express.errorHandler());
});

/**
 * We will inject the environment type to each request, so that we can inject debugging JS files when needed.
 */
app.all('/*.html', auth.restrict, function (req, res, next) {
  var envType = req.app.settings.env;
  res.local('envType', envType);
  // console.log('Setting env type to ' + envType);
  next();
});
require('./routes.js')(app);

if (!module.parent) {
  app.listen(3000);
  console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);
}