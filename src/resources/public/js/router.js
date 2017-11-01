tradingApp.config(function($routeProvider) {
	
	$routeProvider.when('/ohlc', {
		templateUrl:'partials/ohlc.html',
		controller: 'ohlcController'
	})
	
	$routeProvider.otherwise( {
		templateUrl:'partials/openinterest.html',
		controller: 'openInterestController'
	})
	
});