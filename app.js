var express = require('express'),
  routes = require('./routes'),
  io = require('socket.io'),
  orm = require('orm');

var app = module.exports = express.createServer();
var sio = io.listen(app);
var db = orm.connect("postgresql://powerdns:powerdns2k10^^@plop:5433/powerdns", function (success, db) {
    if (!success) {
        console.log("Could not connect to database!" + db.message);
        return;
    } else {
      console.log('Connected to PG just fine.');
    }

    // you can now use db variable to define models
    var Domain = db.define('domain', {
      'id' : {'type':'int'},
      'name' : {'type': 'string'},
      'master' : {'type' : 'string'},
      'last_check' : {'type' : 'int'},
      'type' : {'type' : 'string'},
      'notified_serial' : {'type' : 'integer'},
      'account' : {'type' : 'string'}
    });
});

// Configuration

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'ejs');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(express.cookieParser());
  app.use(express.session({ secret: 'fmfm is a really cool thing' }));
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true })); 
});

app.configure('production', function(){
  app.use(express.errorHandler()); 
});

// Routes
app.get('/', routes.index);
app.get('/domains.json', function(req, res){
  var json = {
  'aaData': [ [
    'lf.io', 'master', '3', 'michael'
  ] ]
  };
  res.send(JSON.stringify(json));
});

app.listen(3000);

sio.sockets.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});

console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);