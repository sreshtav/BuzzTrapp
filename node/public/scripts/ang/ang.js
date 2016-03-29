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
        .state('help', {
          url: '/help',
          templateUrl: '/partials/help.html',
		})
	}]);


controllers.historyCtrl = function (infoFact, $scope, $http) {
  infoFact.getTrips().success(function (data){
    for (d in data) {
      var date = new Date(data[d].startDate);
      data[d].strStartDate = monthNum[date.getMonth()] + ' ' + date.getDate();      
      date = new Date(data[d].endDate);
      data[d].strEndDate = monthNum[date.getMonth()] + ' ' + date.getDate();
    }
    $scope.myTrips = data;
  });

}

controllers.homeCtrl = function ($scope) {
}

controllers.UserCtrl = function ($scope, $http, $window) {
  $scope.isAuthenticated = false;
  $scope.welcome = '';
  $scope.message = '';

  if ($window.localStorage.token){
    $scope.isAuthenticated = true;
  }

  $scope.submit = function () {
    $http
      .post('/auth/login', $scope.user)
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
  };

  $scope.logout = function () {
    if (confirm("Are you sure you want to log out?")) {
      $scope.welcome = '';
      $scope.message = '';
      $scope.isAuthenticated = false;
      delete $window.localStorage.token;
    } else {
    }
  };

  // $scope.callRestricted = function () {
  //   $http({url: '/api/myTrips', method: 'GET'})
  //   .success(function (data, status, headers, config) {
  //     $scope.message = data; // Should log 'foo'
  //   })
  //   .error(function (data, status, headers, config) {
  //     alert(data);
  //   });
  // };
}

factories.infoFact = function ($http){
  var services = {};

  services.getTrips = function () {
    return $http.get('/api/myTrips');
  };

  services.temp = function () {
    return "yo";
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
