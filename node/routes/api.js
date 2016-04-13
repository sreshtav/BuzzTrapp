var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var InterestPoint = mongoose.model('InterestPoint');
var Trip = mongoose.model('Trip');
var TripItem = mongoose.model('TripItem');
var passport	  = require('passport');

var basicAuth = passport.authenticate('jwt', { session: false});
function printAuth (req, res, next) {
	console.log(req.headers);
	next();
}


router.get('/allInterestPoints', basicAuth, function (req, res, next) {
  InterestPoint.find({}, function (err, data){
  	if (err) res.send(err);
  	else res.send(data);
  });
});


router.post('/createInterestPoint', basicAuth, function (req, res, next) {
	var object = new InterestPoint();
	console.log(req.body);
	object.name = req.body.name;
	object.address = req.body.address;
	object.description = req.body.description;
	// object.photos = req.body.photos;
	object.averageTime = req.body.averageTime;
	object.interest = req.body.interest;
	object.city = req.body.city;
	object.save(function (err) {
		if (err) {
			res.send(err);
		} else {
			res.send(200);
		}
	});
});

router.get('/myTrips',printAuth, basicAuth, function (req, res){
	Trip.find({'userId' : req.user._id}, function (err, data){
		if (err) res.send("Sorry, error"); //TODO: Make a real error catch
		res.send(data);
	});
});

router.delete('/removeTrip', basicAuth, function (req, res){
	var conditions = {
		'_id' : req.query.tripid,
		'userId' : req.user._id
	};
	Trip.remove(conditions, function (err, data){
		if (err) res.send(500); //TODO: Make a real error catch
		console.log(data);
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

router.get('/myTripItems',printAuth, basicAuth, function (req, res){
	var tripId = mongoose.Types.ObjectId(req.query.tripId);
	TripItem.find({'tripId' : tripId}, function (err, data){
		if (err) res.send("Sorry, error"); //TODO: Make a real error catch
		res.send(data);
	});
});

router.post('/addTripItem', basicAuth, function (req, res){
	var newTripItem = new TripItem({
		'tripId' : mongoose.Types.ObjectId(req.body.tripId),
		'startTime' : req.body.startTime,
		'endTime' : req.body.endTime,
		'interestPointId' : mongoose.Types.ObjectId(req.body.interestPointId)
	});
	newTripItem.save(function(err) {
	  if (err) {
	    res.json({succes: false, msg: err});
	  } else {
	    res.json({succes: true, msg: 'Successful created trip!'});
	  }
	});
});

module.exports = router;
