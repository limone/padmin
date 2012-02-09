exports.domains = function(req, res) {
  req.app.findDomains();
  
  var json = {
  'aaData': [ [
    'lf.io', 'master', '3', 'michael'
  ] ]
  };
  res.send(JSON.stringify(json));
};