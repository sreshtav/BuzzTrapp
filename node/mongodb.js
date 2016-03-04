var mongoose = require('mongoose');
var bcrypt = require('bcrypt');
var Schema = mongoose.Schema;

var userSchema = new Schema ({
  email: {type: String, required: true},
  name: {type: String, required: true},
  password: {type: String, required: true},
  interests: [{type: String, required : false}],
  reminderPreference: {type: Number, required: false},
  profilePicture: {data: Buffer, contentType: String, required: false}
});
var tripSchema = new Schema ({
  startDate: {type: Date, required: true},
  endDate: {type: Date, required: true},
  userId: {type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true},
  location: {type: String, required: true}
});
var tripItemSchema = new Schema ({
  tripId: {type: mongoose.Schema.Types.ObjectId, ref: 'Trip', required: true},
  startTime: {type: Date, required: true},
  interestPointId: {type: mongoose.Schema.Types.ObjectId, ref: 'InterestPoint', required: true}
});
var interestPointSchema = new Schema ({
  name: {type: String, required: true},
  address: {type: String, required: true},
  city: {type: String, required: true},
  description: {type: String, required: true},
  photos: [{data: Buffer, contentType: String, required: false}],
  averageTime: {type: Number, required: false}, //minutes
  interest: {type: String, required: true},
});

userSchema.pre('save', function (next) {
    var user = this;
    if (this.isModified('password') || this.isNew) {
        bcrypt.genSalt(10, function (err, salt) {
            if (err) return next(err);
            bcrypt.hash(user.password, salt, function (err, hash) {
                if (err) return next(err);
                user.password = hash;
                next();
            });
        });
    } else {
        return next();
    }
});

userSchema.methods.checkPassword = function (passw, cb) {
    bcrypt.compare(passw, this.password, function (err, isMatch) {
        if (err) return cb(err);
        cb(null, isMatch);
    });
};

mongoose.model('User', userSchema);
mongoose.model('Trip', tripSchema);
mongoose.model('TripItem', tripItemSchema);
mongoose.model('InterestPoint', interestPointSchema);
mongoose.connect('mongodb://test:123@ds011168.mlab.com:11168/buzztrapp');

