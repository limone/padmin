var Domain = module.exports = function Domain(id, name, numRecords) {
  this.id = id;
  this.name = name;
  this.numRecords = numRecords;
  this.records = [];
};

Domain.getAll = function(callback) {
  var db = global.db;
  var result = db.getClient().query('SELECT domains.id, domains.name, Record_Count.count_records FROM domains LEFT JOIN zones ON domains.id=zones.domain_id LEFT JOIN ( SELECT COUNT(domain_id) AS count_records, domain_id FROM records GROUP BY domain_id ) Record_Count ON Record_Count.domain_id=domains.id GROUP BY domains.name, domains.id, Record_Count.count_records', function(err, result) {
    var domains = [];
    var rows = result.rows;
    if (rows.length > 0) {
      for (var row in rows) {
        row = rows[row];
        domains.push(new Domain(row.id, row.name, row.count_records));
      }
    }
    callback(domains);
  });
}

Domain.getById = function(id, callback) {
  var db = global.db;
  var result = db.getClient().query('SELECT domains.name as domain_name, records.* FROM domains LEFT JOIN records ON domains.id=records.domain_id WHERE domains.id=$1 GROUP BY domains.name, domains.id, records.id', [id], function(err, result) {
    if (err) {
      console.log(err);
      return;
    }
    var domain = null;
    var rows = result.rows;
    if (rows.length > 0) {
      domain = new Domain(id);
      for (var i=0; i<rows.length; i++) {
        row = rows[i];
        if (i == 0) {
          domain = new Domain(id, row.domain_name, rows.length);
        }
        domain.records.push({'id':row.id, 'name':row.name, 'type':row.type, 'content':row.content, 'ttl':row.ttl, 'prio':row.prio, 'changeDate':row.change_date});
      }
    }
    callback(domain);
  });
}