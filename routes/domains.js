exports.domains = function(req, res){
  var json = {
  "aaData": [
    'lf.io', 'master', '3', 'michael'
            ]
  };
  res.send(JSON.stringify(json));
};