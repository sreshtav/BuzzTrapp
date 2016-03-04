var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var InterestPoint = mongoose.model('InterestPoint');
var Trip = mongoose.model('Trip');
var passport	  = require('passport');

var basicAuth = passport.authenticate('jwt', { session: false});

router.get('/allInterestPoints', basicAuth, function(req, res, next) {
  InterestPoint.find({}, function (err, data){
  	if (err) res.send(err);
  	else res.send(data);
  });
});

router.post('/createInterestPoint', basicAuth, function (req, res, next) {
	var object = new InterestPoint();
	console.log(req.body);
	object.name = req.body.name;
	object.location = req.body.location;
	object.description = req.body.description;
	// object.photos = req.body.photos;
	object.averageTime = req.body.averageTime;
	object.interest = req.body.interest;
	object.save(function (err) {
		if (err) {
			res.send(err);
		} else {
			res.send(200);
		}
	});
});

router.get('/myTrips', basicAuth, function (req, res){
	Trip.find({'userId' : req.user._id}, function (err, data){
		if (err) res.send("Sorry, error"); //TODO: Make a real error catch
		res.send(data);
	});
});

router.post('/addTrip', basicAuth, function (req, res){
	var newTrip = new Trip({
		'startDate' : req.body.startDate,
		'endDate' : req.body.endDate,
		'userId' : req.user._id,
		'location' : req.body.location 
	});
	newTrip.save(function(err) {
	  if (err) {
	    res.json({succes: false, msg: err});
	  } else {
	    res.json({succes: true, msg: 'Successful created trip!'});
	  }
	});
});

router.get('/testAuth', basicAuth, function(req, res) {
        res.send(req.user);
    }
);

module.exports = router;
