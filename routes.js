var index = require('./routes/index.js');
var domains = require('./routes/domains.js');

module.exports = function(app) {
  app.all('/', index.index);
  app.get('/domains.json', domains.domains);
}