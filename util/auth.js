function restrict(req, res, next) {
  // turning off auth
  next();
  return;

  // console.log('Checking if there is a user.');
  if (req.session.user) {
    next();
  } else {
    if (req.url.indexOf('/login.html') != 0) {
      // console.log('User was not logged in, redirecting to login page.');
      req.session.error = 'Access denied!';
      res.redirect('/login.html');
    } else {
      // console.log('Not checking auth for login screen');
      next();
    }
  }
}

exports.restrict = restrict;