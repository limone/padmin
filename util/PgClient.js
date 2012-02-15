var pg = require('pg');

var defaults = {
  host: '127.0.0.1',
  port: 5433,
  db: 'postgres'
};

module.exports = PgClient = function(options, callback) {
  var self = this;
  this._client = new pg.Client(options);
  this._client.connect(function(err, client) {
    if (err) {
      console.log(err);
      throw "Could not connect to PG. " + err;
    }
    console.log('Connected to PG');
    self._client = client;
  });
}