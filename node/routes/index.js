var express = require('express');
var router = express.Router();

var passport	  = require('passport');

// var basicAuth = passport.authenticate('jwt', {session: true, failureRedirect: '/login' });


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('temp', {title: 'Express' });
});

router.get('/temp', function(req, res, next) {
  res.render('temp');
});

module.exports = router;
