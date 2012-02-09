exports.domains = function(req, res) {
  //db.domain.Domain.find(function(domains) {
  //  console.log(domains.length);
  //});
  
  var json = {
  'aaData': [ [
    'lf.io', 'master', '3', 'michael'
  ] ]
  };
  res.send(JSON.stringify(json));
};