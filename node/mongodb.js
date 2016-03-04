var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema ({
  userName: {type: String, required: true},
  password: {type: String, required: true},
  interests: [{type: String, required : false}],
  reminderPreference: {type: Number, required: false},
  profilePicture: {data: Buffer, contentType: String, required: false}
});
var tripSchema = new Schema ({
  startDate: {type: Date, required: true},
  endDate: {type: Date, required: true},
  userId: {type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true},
  locations: [{type: String, required: true}]
});
var tripItemSchema = new Schema ({
  tripId: {type: mongoose.Schema.Types.ObjectId, ref: 'Trip', required: true},
  startTime: {type: Date, required: true},
  interestPointId: {type: mongoose.Schema.Types.ObjectId, ref: 'InterestPoint', required: true}
});
var interestPointSchema = new Schema ({
  name: {type: String, required: true},
  location: {type: String, required: true},
  description: {type: String, required: true},
  photos: [{data: Buffer, contentType: String, required: false}],
  averageTime: {type: Number, required: false}, //minutes
  interest: {type: String, required: true},
});

mongoose.model('User', userSchema);
mongoose.model('Trip', tripSchema);
mongoose.model('TripItem', tripItemSchema);
mongoose.model('InterestPoint', interestPointSchema);
mongoose.connect('mongodb://test:123@ds011168.mlab.com:11168/buzztrapp');

