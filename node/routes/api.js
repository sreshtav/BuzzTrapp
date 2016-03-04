var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var InterestPoint = mongoose.model('InterestPoint');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});


router.get('/allInterestPoints', function(req, res, next) {
  InterestPoint.find({}, function (err, data){
  	if (err) res.send(err);
  	else res.send(data);
  });
});

router.post('/createInterestPoint', function (req, res, next) {
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

module.exports = router;
