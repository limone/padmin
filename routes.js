module.exports = function(app){
  require('./controllers/acs.js')(app);
  require('./controllers/domains.js')(app);
};