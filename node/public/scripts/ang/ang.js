var app = angular.module('MainApp', ['ui.router']);
var controllers = {};
var factories = {};
app.controller(controllers);
app.factory(factories);
app.config([
	'$urlRouterProvider', 
	'$stateProvider', 
	function ($urlRouterProvider, $stateProvider) {
		$urlRouterProvider.otherwise('/');
		$stateProvider.state('home', {
			url: '/',
		  template: "<h1>My Contacts</h1><a href='#/about'>click here</a>"
		})
		.state('account', {
		  url: '/account',
		  templateUrl: '/partials/about.html',
		  controller: 'oneCtrl'
		})
	}]);



controllers.oneCtrl = function ($scope) {
    $scope.cookies = "balooga whales";
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
    $scope.welcome = '';
    $scope.message = '';
    $scope.isAuthenticated = false;
    delete $window.localStorage.token;
  };

  $scope.callRestricted = function () {
    $http({url: '/api/myTrips', method: 'GET'})
    .success(function (data, status, headers, config) {
      $scope.message = data; // Should log 'foo'
    })
    .error(function (data, status, headers, config) {
      alert(data);
    });
  };
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
