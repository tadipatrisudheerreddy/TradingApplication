angular.module('tradingApp').factory('ohlcService', ['$q', '$http', function($q, $http){
	return {
		getOhlcData: getOhlcData
	};
	
	function getOhlcData() {
		$http({
			method:'GET',
			url:'http://localhost:8080/ohlcdata',
		}).then( function successCallBack(response) {
			console.log(response);
		}, function errorCallBack(response) {
			
		});
	}
}]);