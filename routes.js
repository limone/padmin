var index = require('./routes/index.js');
var domains = require('./routes/domains.js');

module.exports = function(app, db) {
  app.all('/', index.index);
  app.get('/domains.json', db, domains.domains);
}