var app = angular.module('MainApp', ['ui.router']);
var controllers = {};
var factories = {};
var monthNum = ['January', 'Feburary', 'March','April', 'May', 'June','July', 'August', 'September','October', 'November', 'December',];
app.controller(controllers);
app.factory(factories);
app.config([
	'$urlRouterProvider', 
	'$stateProvider', 
	function ($urlRouterProvider, $stateProvider) {
		$urlRouterProvider.otherwise('/');
		$stateProvider.state('home', {
		  url: '/',
		  templateUrl: "/partials/home.html",
      controller: 'homeCtrl'
        })
        .state('history', {
          url: '/history',
          templateUrl: '/partials/history.html',
          controller: 'historyCtrl'
        })
        .state('setting', {
          url: '/setting',
          templateUrl: '/partials/setting.html',
        })
        .state('newTrip', {
          url: '/newTrip',
          templateUrl: '/partials/newTrip.html',
          controller: 'newTripCtrl'
        })
        .state('help', {
          url: '/help',
          templateUrl: '/partials/help.html',

        })
        .state("suggestionMap",{
          url:"/suggestionMap",
          templateUrl:"/partials/suggestionMap.html",
    		  controller: 'suggestionMapCtrl'
        })
	}]);

controllers.suggestionMapCtrl = function ($scope, $http, infoFact, $state, $compile) {
  if(jQuery.isEmptyObject(infoFact.currentEditingTrip)) $state.go('home');
  $scope.selectMonth = function (month) {
    console.log("balls");
  }

  $scope.select = function (day) {
    console.log(day);
  }

  var  cities = {
    "Miami" : "plni8h4k",
    "New York" : "pm1iajik"
  }
  
  var cityCoordinates = {
    "Miami" :  [25.774387672608608, -80.19444465637207],
    "San Francisco" :  [37.77493, -122.419416],
    "Hawaii" :  [21.306944, -157.858333],
    "London" :  [51.507351, -0.127758],
    "Washington DC" :  [38.907192, -77.036871],
    "New York" :  [40.73034831215804, -73.99085998535155],
    "Hong Kong" :  [22.396428, 114.109497]
  }

  $scope.tripInfo = infoFact.currentEditingTrip;
  var mapCoord = cityCoordinates[$scope.tripInfo.location] || [25.774387672608608, -80.19444465637207];
  L.mapbox.accessToken = 'pk.eyJ1IjoiZGhhbmFuaTk0IiwiYSI6ImNpbHh2eDdiYTA4NDl2Z2tzZ3Rsemw5bGgifQ.73Vj_xaGomgnX0LRDzqM8w';

  var map = L.mapbox.map('map2', 'mapbox.streets', { zoomControl: false })
      .setView(mapCoord, 14);

  map.dragging.disable();
  map.touchZoom.disable();
  map.doubleClickZoom.disable();
  map.scrollWheelZoom.disable();
  map.keyboard.disable();

  var CLIENT_ID = 'Z1YL1IRWJINRSIYFKX2UPN42HG0KBKZ5ZX3QWZWLPNW1LGDE';
  var CLIENT_SECRET = 'UB3QP3YNFAXSFV0STRV1YOPHSX1MKCTB5TEKDM3J2THCJOIH';

  // https://developer.foursquare.com/start/search
  var API_ENDPOINT = 'https://api.foursquare.com/v2/venues/search' +
    '?client_id=CLIENT_ID' +
    '&client_secret=CLIENT_SECRET' +
    '&v=20130815' +
    '&ll=LATLON' +
    '&query=TYPE' +
    '&callback=?';

  // Keep our place markers organized in a nice group.
  var foursquarePlaces = L.layerGroup().addTo(map);

  // Use jQuery to make an AJAX request to Foursquare to load markers data.
  $.getJSON(API_ENDPOINT
      .replace('CLIENT_ID', CLIENT_ID)
      .replace('CLIENT_SECRET', CLIENT_SECRET)
      .replace('TYPE', 'coffee')
      .replace('LATLON', map.getCenter().lat +
          ',' + map.getCenter().lng), function(result, status) {
      if (status !== 'success') return alert('Request to Foursquare failed');
      for (var i = 0; i < result.response.venues.length; i++) {
        var venue = result.response.venues[i];
        var latlng = L.latLng(venue.location.lat, venue.location.lng);
        var html = '<span><b>'+venue.name+'</b><p>'+venue.location.formattedAddress[0]+'</p><button type="button" ng-click="addItem(venue, \'Food\')" style="background-color:#2185C5;color:#ffffff;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Add to List</button></span>',
        linkFunction = $compile(angular.element(html)),
        newScope = $scope.$new();
        newScope.venue = venue;
        var marker = L.marker(latlng, {
            icon: L.mapbox.marker.icon({
              'marker-color': '#BE9A6B',
              'marker-symbol': 'cafe',
              'marker-size': 'large'
            })
          })
        .bindPopup(linkFunction(newScope)[0])
          .addTo(foursquarePlaces);
      }
  });

  $.getJSON(API_ENDPOINT
      .replace('CLIENT_ID', CLIENT_ID)
      .replace('CLIENT_SECRET', CLIENT_SECRET)
      .replace('TYPE', 'food')
      .replace('LATLON', map.getCenter().lat +
          ',' + map.getCenter().lng), function(result, status) {
      if (status !== 'success') return alert('Request to Foursquare failed');
      for (var i = 0; i < result.response.venues.length; i++) {
        var venue = result.response.venues[i];
        var latlng = L.latLng(venue.location.lat, venue.location.lng);
        var html = '<span><b>'+venue.name+'</b><p>'+venue.location.formattedAddress[0]+'</p><button type="button" ng-click="addItem(venue, \'Food\')" style="background-color:#2185C5;color:#ffffff;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Add to List</button></span>',
        linkFunction = $compile(angular.element(html)),
        newScope = $scope.$new();
        newScope.venue = venue;
        var marker = L.marker(latlng, {
            icon: L.mapbox.marker.icon({
              'marker-color': '#BE4730',
              'marker-symbol': 'restaurant',
              'marker-size': 'large'
            })
          })
        .bindPopup(linkFunction(newScope)[0])
          .addTo(foursquarePlaces);
      }
  });

  $.getJSON(API_ENDPOINT
      .replace('CLIENT_ID', CLIENT_ID)
      .replace('CLIENT_SECRET', CLIENT_SECRET)
      .replace('TYPE', 'arts')
      .replace('LATLON', map.getCenter().lat +
          ',' + map.getCenter().lng), function(result, status) {
      if (status !== 'success') return alert('Request to Foursquare failed');
      for (var i = 0; i < result.response.venues.length; i++) {
        var venue = result.response.venues[i];
        var latlng = L.latLng(venue.location.lat, venue.location.lng);
        var html = '<span><b>'+venue.name+'</b><p>'+venue.location.formattedAddress[0]+'</p><button type="button" ng-click="addItem(venue, \'Sightseeing\')" style="background-color:#2185C5;color:#ffffff;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Add to List</button></span>',
        linkFunction = $compile(angular.element(html)),
        newScope = $scope.$new();
        newScope.venue = venue;
        var marker = L.marker(latlng, {
            icon: L.mapbox.marker.icon({
              'marker-color': '#49BE62',
              'marker-symbol': 'camera',
              'marker-size': 'large'
            })
          })
        .bindPopup(linkFunction(newScope)[0])
          .addTo(foursquarePlaces);
      }
  });

  $.getJSON(API_ENDPOINT
      .replace('CLIENT_ID', CLIENT_ID)
      .replace('CLIENT_SECRET', CLIENT_SECRET)
      .replace('TYPE', 'drinks')
      .replace('LATLON', map.getCenter().lat +
          ',' + map.getCenter().lng), function(result, status) {
      if (status !== 'success') return alert('Request to Foursquare failed');
      for (var i = 0; i < result.response.venues.length; i++) {
        var venue = result.response.venues[i];
        var latlng = L.latLng(venue.location.lat, venue.location.lng);
        var html = '<span><b>'+venue.name+'</b><p>'+venue.location.formattedAddress[0]+'</p><button type="button" ng-click="addItem(venue, \'Food\')" style="background-color:#2185C5;color:#ffffff;" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Add to List</button></span>',
        linkFunction = $compile(angular.element(html)),
        newScope = $scope.$new();
        newScope.venue = venue;
        var marker = L.marker(latlng, {
            icon: L.mapbox.marker.icon({
              'marker-color': '#3070BE',
              'marker-symbol': 'bar',
              'marker-size': 'large'
            })
          })
        .bindPopup(linkFunction(newScope)[0])
          .addTo(foursquarePlaces);
      }
  });

  $scope.addItem = function (fsqObj, interest) {
    console.log(interest);
    var rtnObj = {};
    var interstPObj = {};
    interstPObj.name = fsqObj.name || "undefined";
    interstPObj.address = fsqObj.location.address || "undefined";
    interstPObj.city = $scope.tripInfo.location;
    interstPObj.description = fsqObj.name;
    interstPObj.foursqID = fsqObj.id;
    interstPObj.interest = interest || "undefined";
    rtnObj.tripId = $scope.tripInfo._id;
    rtnObj.startTime = $scope.newStartTime || new Date();
    rtnObj.endTime = $scope.newEndTime || new Date();
    rtnObj.obj = interstPObj;

    $http
      .post('/api/addFourSQPoint', rtnObj)
      .success (function (data, status, headers, config) {
        console.log(data);
      })
      .error (function (data, status, headers, config) {
        console.log(data);
      })

  }

  $scope.deleteTrip = function () {
    console.log("deleting" + "/removeTrip?tripid=" + $scope.tripInfo._id);
    $http
      .delete("/api/removeTrip?tripid=" + $scope.tripInfo._id)
      .success(function (data, status, headers, config) {
        infoFact.currentEditingTrip = {};
        $scope.tripInfo = {};
        $state.go("home");
      })
      .error(function (data, status, headers, config) {
        console.log(data);
      });
  }
}

controllers.newTripCtrl = function ($scope, $location) {
  $scope.cancel = function () {
    $location.path('/');
  }

}
controllers.historyCtrl = function (infoFact, $scope, $http) {
  infoFact.getTrips().then(function (data){
    $scope.myTrips = data;
  });

}

controllers.homeCtrl = function ($scope, infoFact, $http, $state) {
  infoFact.getTrips().then(function (data){
    $scope.myTrips = data;
  });

  $scope.addNewTrip = function (){

    console.log("ADDING NEW TRIP");
    $http
      .post('/api/addTrip', $scope.newTrip)
      .success(function (data, status, headers, config) {
        if (data.succes) {
          console.log("ADDED NEW TRIP");
          infoFact.getTrips().then(function (data){
            $scope.myTrips = data;
            $scope.dismiss()
          });
        } else {
        }
      })
      .error(function (data, status, headers, config) {
      });
  }

  $scope.editTripAction = function (trip){
    infoFact.currentEditingTrip = trip;
    $state.go("suggestionMap");

  }

}

controllers.UserCtrl = function ($scope, $http, $window) {
  $scope.isAuthenticated = false;
  $scope.welcome = '';
  $scope.message = '';
  if ($window.localStorage.token){
    $scope.isAuthenticated = true;
    $scope.userEmail = $window.localStorage.userEmail;
  }

  $scope.login = function () {
    $http
      .post('/auth/login', $scope.user)
      .success(function (data, status, headers, config) {
        if (data.success) {
            $window.localStorage.token = data.msg;
            $window.localStorage.userEmail = data.userEmail;
            $scope.userEmail = data.userEmail;
            $scope.isAuthenticated = true;
        } else {
            delete $window.localStorage.token;
            $scope.isAuthenticated = false;
            $scope.error = 'Error: Invalid user or password';
        }
      })
      .error(function (data, status, headers, config) {
        delete $window.localStorage.token;
        $scope.isAuthenticated = false;
        $scope.error = 'Error: Invalid user or password';
      });
  };

  $scope.submitNewUser = function () {
    var userObj = {};
    userObj.name = $scope.newUser.name;
    userObj.email = $scope.newUser.email;
    if ($scope.newUser.password1 === $scope.newUser.password1)
      userObj.password = $scope.newUser.password1;
    $http
      .post('/auth/signup', userObj)
      .success(function (data, status, headers, config) {
        if (data.success) {
            $window.localStorage.token = data.msg;
            $scope.isAuthenticated = true;
        } else {
            delete $window.localStorage.token;
            $scope.isAuthenticated = false;
            $scope.error = 'Error: Invalid user or password';
        }
      })
      .error(function (data, status, headers, config) {
        delete $window.localStorage.token;
        $scope.isAuthenticated = false;
        $scope.error = 'Error: Invalid user or password';
      });

  }

  $scope.logout = function () {
    if (confirm("Are you sure you want to log out?")) {
      $scope.welcome = '';
      $scope.message = '';
      $scope.isAuthenticated = false;
      delete $window.localStorage.token;
      delete $window.localStorage.userEmail;
    } else {
    }
  };
}

factories.infoFact = function ($http, $q){
  var cityClassName = {
    "Miami" : "miami",
    "San Francisco" : "san-francisco",
    "Hawaii" : "hawaii",
    "London" : "london",
    "Washington DC" : "washington-dc",
    "New York" : "new-york",
    "Hong Kong" : "hong-kong"
  }
  var services = {};
  services.currentEditingTrip = {};

  services.getTrips = function () {
    return $q(function(resolve, reject) {
      $http.get('/api/myTrips').then(function (data) {
        data = data['data'];
        for (d in data) {
          var date = new Date(data[d].startDate);
          data[d].strStartDate = monthNum[date.getMonth()] + ' ' + date.getDate();      
          date = new Date(data[d].endDate);
          data[d].strEndDate = monthNum[date.getMonth()] + ' ' + date.getDate();
          data[d].cityClass = cityClassName[data[d].location];
        }
        resolve(data);
      }, function (d, data) {reject(data)});
    });
  }

  return services;

}

app.factory('authInterceptor', function ($rootScope, $q, $window) {
  return {
    request: function (config) {
      config.headers = config.headers || {};
      if ($window.localStorage.token) {
        config.headers.Authorization = $window.localStorage.token;
      }
      return config;
    },
    responseError: function (rejection) {
      if (rejection.status === 401) {
        // handle the case where the user is not authenticated
      }
      return $q.reject(rejection);
    }
  };
});

app.config(function ($httpProvider) {
  $httpProvider.interceptors.push('authInterceptor');
});



app.directive("calendar", function() {
    return {
        restrict: "E",
        templateUrl: "partials/calendar.html",
        scope: {
            selected: "="
        },
        link: function(scope) {
            scope.selected = _removeTime(scope.selected || moment());
            scope.month = scope.selected.clone();

            var start = scope.selected.clone();
            start.date(1);
            _removeTime(start.day(0));

            _buildMonth(scope, start, scope.month);

            scope.select = function(day) {
                scope.selected = day.date;  
            };

            scope.next = function() {
                var next = scope.month.clone();
                _removeTime(next.month(next.month()+1).date(1));
                scope.month.month(scope.month.month()+1);
                _buildMonth(scope, next, scope.month);
            };

            scope.previous = function() {
                var previous = scope.month.clone();
                _removeTime(previous.month(previous.month()-1).date(1));
                scope.month.month(scope.month.month()-1);
                _buildMonth(scope, previous, scope.month);
            };
        }
    };

    function _removeTime(date) {
        return date.day(0).hour(0).minute(0).second(0).millisecond(0);
    }

    function _buildMonth(scope, start, month) {
        scope.weeks = [];
        var done = false, date = start.clone(), monthIndex = date.month(), count = 0;
        while (!done) {
            scope.weeks.push({ days: _buildWeek(date.clone(), month) });
            date.add(1, "w");
            done = count++ > 2 && monthIndex !== date.month();
            monthIndex = date.month();
        }
    }

    function _buildWeek(date, month) {
        var days = [];
        var today = new Date();
        for (var i = 0; i < 7; i++) {
            days.push({
                name: date.format("dd").substring(0, 1),
                number: date.date(),
                isCurrentMonth: date.month() === month.month(),
                isToday: date.isSame(today, "day"),
                isPast : date < today,
                date: date
            });
            date = date.clone();
            date.add(1, "d");
        }
        return days;
    }
});








app.directive('myModal', function() {
   return {
     restrict: 'A',
     link: function(scope, element, attr) {
       scope.dismiss = function() {
           element.modal('hide');
       };
     }
   } 
});




app.directive("agenda", function() {
    return {
        restrict: "E",
        templateUrl: "partials/agenda.html",
        scope: {
            selected: "="
        },
        link: function(scope) {
            _buildDay(scope);
        }
    };

    function _createTime(hour, minute) {
        return moment().year(0).month(0).day(0).second(0).millisecond(0).hour(hour).minute(minute);
    }

    function _buildDay(scope) {
        scope.hours = [];
        var date = moment();
        var done = false, hour = 0, count = 0;
        while (!done) {
            scope.hours.push({'hour' : _createTime(hour++, 0), 'visible' : false});
            done = count++ > 23;
        }
        scope.hours[8].visible = true;
        scope.hours[12].visible = true;
        scope.hours[21].visible = true;

    }
});



app.directive("dayButtons", function (infoFact) {
    return {
        restrict: "E",
        templateUrl: "partials/dayButtons.html",
        scope: {
            selected: "="
        },
        link: function(scope) {
            _buildWeek(scope);
        }
    };

    function _createTime(hour, minute) {
        return moment().year(0).month(0).day(0).second(0).millisecond(0).hour(hour).minute(minute);
    }

    function daysInMonth(month) {
      if (month == "Feburary") return 29;
      var day30 = ['April', 'June', 'September', 'November'];
      if (day30.includes(month)) return 30;
      else return 31;
    }

    function _buildWeek(scope) {
        var startDate = moment(infoFact.currentEditingTrip.startDate);
        var endDate = moment(infoFact.currentEditingTrip.endDate);
        scope.months = [];  // months holds month objs: {name : july, number: 6, days : [4,5,6,7,8]}
        for (var i = startDate.month(); i <= endDate.month(); i++) {
          var month = {};
          month.name = monthNum[i];
          month.num = i+1;
          month.days = [];
          var startNum = (startDate.month() == i) ? startDate.date():1; 
          var endNum = (endDate.month() == i) ? endDate.date():daysInMonth(month.name); 
          while (startNum <= endNum)
            month.days.push(startNum++);
          scope.months.push(month);
        };
    }
});