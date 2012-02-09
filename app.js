var express = require('express'),
  routes = require('./routes'),
  io = require('socket.io'),
  pg = require('pg');
  
var index = require('./routes/index');
var domains = require('./routes/domains');

var app = module.exports = express.createServer();
var sio = io.listen(app);

var client = app.pg = new pg.Client("tcp://powerdns:powerdns2k10^^@plop:5433/powerdns");
client.connect();

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

app.all('/', index.index);
app.get('/domains.json', domains.domains);

app.listen(3000);

sio.sockets.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});

console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);