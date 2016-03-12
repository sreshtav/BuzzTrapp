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
		  template: '<h1>My Contacts</h1>'
		})
		.state('about', {
			url: '/about',
		  templateUrl: '/partials/about.html',
		  controller: 'oneCtrl'
		})
	}]);

controllers.oneCtrl = function ($scope) {
	$scope.cookies = "balooga whales";
}

