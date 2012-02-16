module.exports = function(app){
  require('./controllers/index.js')(app);
  require('./controllers/acs.js')(app);
  require('./controllers/domains.js')(app);
};