angular.module('tradingApp').factory('openInterestService', ['$q', '$http', function($q, $http) {
	
	return  {
		getOpenInterest: getOpenInterest
	};
	
	function getOpenInterest(from, to, success, failure) {
		$http({
			  method: 'POST',
			  params:{"from":from.getTime(), "to":to.getTime()},
			  data: '',
			  headers: {
			      "Content-Type": "application/x-www-form-urlencoded"
			  },
			  url: 'https://technicaltrader.herokuapp.com/oidata/interval'
			  //url:'/oidata/interval'
			}).then(success, failure);
		
	}
}]);