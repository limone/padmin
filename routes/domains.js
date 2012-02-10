exports.domains = function (req, res) {
  var json = '{ "aaData": [';

  var sqlQuery = 'SELECT domains.id, domains.name, domains.type, Record_Count.count_records FROM domains LEFT JOIN zones ON domains.id=zones.domain_id LEFT JOIN (SELECT COUNT(domain_id) AS count_records, domain_id FROM records GROUP BY domain_id ) Record_Count ON Record_Count.domain_id=domains.id GROUP BY domains.name, domains.id, domains.type, Record_Count.count_records';

  var query = req.app.pg.query(sqlQuery);
  query.on('row', function (row) {
    json += '{';
    for (column in row) {
      json += '"' + column + '" : "' + row[column] + '", '
    }
    json = json.substring(0, json.lastIndexOf(','));
    json += '},';
  });

  query.on('end', function () {
    json = json.substring(0, json.lastIndexOf(','));
    json += ']}';
    res.send(json);
  });
};