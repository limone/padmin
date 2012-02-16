var pg = require('pg');

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

PgClient.prototype.getClient = function() {
  return this._client;
}