var router 			= require('express').Router();
var User 				= require('mongoose').model('User');
var jwt         = require ('jwt-simple');
var JwtStrategy = require('passport-jwt').Strategy;
var passport	  = require('passport');

var ExtractJwt = require('passport-jwt').ExtractJwt;

var opts = {}
opts.secretOrKey = "taufiqsAwesome";
opts.jwtFromRequest = ExtractJwt.fromAuthHeader();
passport.use(new JwtStrategy(opts, function(jwt_payload, done) {
  User.findOne({_id: jwt_payload}, function(err, user) {
    if (err) {
      return done(err, false);
    }
    if (user) {
      done(null, user);
    } else {
      done(null, false);
    }
  });
}));

router.post('/signup', function(req, res) {
  if (!req.body.email || !req.body.password || !req.body.name) {
    res.json({success: false, msg: 'Please pass name, email, and password.'});
  } else {
    var newUser = new User({
      name: req.body.name,
      email: req.body.email,
      password: req.body.password
    });
    newUser.save(function(err) {
      if (err) {
        res.json({success: false, msg: 'Could not create user.'});
      } else {
        res.json({success: true, msg: 'Successfully created user!'});
      }
    });
  }
});

router.post('/login', function(req, res) {
  console.log(req.body);
  User.findOne({
    email: req.body.email
  }, function(err, user) {
    if (err) res.send(500);
    else if (!user) {
      res.send({success: false, msg: 'userNotFound'});
    } else {
      user.checkPassword(req.body.password, function(err, isMatch) {
        if (isMatch && !err) {
          var token = jwt.encode(user._id, "taufiqsAwesome");
          res.json({success: true, msg: 'JWT ' + token, userEmail : user.email});
        } else {
          res.send({success: false, msg: 'passwordError'});
        }
      });
    }
  });
});

module.exports = router;