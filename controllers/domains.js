/*var _ = require('underscore');
 var models = require('../models.js');
 var forms = require('../forms.js');
 var utils = require('../utils.js');
 var User = models.User;*/
var Domain = require('../model/domain');

module.exports = function (app) {
  app.get('/index.html', function (req, res) {
    res.render('index', {title:"Home"});
  });

  app.get('/domains.json', function(req, res) {
    Domain.getAll(function(domains) {
      var aaData = {};
      aaData['aaData'] = domains;
      res.json(aaData);
    });
  });

  app.get('/domain.html', function(req, res) {
    var domainId = req.query.id;
    console.log('Looking for domain ID ' + domainId);
    Domain.getById(domainId, function(domain) {
      console.log(domain);
      res.render('domain', {title:"Edit Domain"});
    });
  })
};