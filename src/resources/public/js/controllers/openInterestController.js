'use strict';

tradingApp.controller('openInterestController', [
		'$http',
		"$scope",
		"openInterestService",
		function($http, $scope, openInterestService) {

			$scope.hideChart = true;
			$scope.myChartObject = {};

			$scope.myChartObject.type = "ColumnChart";

			var successCallBack = function(response) {
				$scope.hideChart = false;
				var data = response.data;
				console.log(data);

				var rows = [ {
					c : [ {
						v : "F-I-L"
					}, {
						v : data.FII.futureIndexLong
					}, {
						v : data.Client.futureIndexLong
					} ]
				},
				{
					c : [ {
						v : "F-I-S"
					}, {
						v : data.FII.futureIndexShort
					}, {
						v : data.Client.futureIndexShort
					} ]
				},
				{
					c : [ {
						v : "F-S-L"
					}, {
						v : data.FII.futureStockLong
					}, {
						v : data.Client.futureStockLong
					} ]
				},
				{
					c : [ {
						v : "F-S-S"
					}, {
						v : data.FII.futureStockShort
					}, {
						v : data.Client.futureStockShort
					} ]
				},
				{
					c : [ {
						v : "O-I-CL"
					}, {
						v : data.FII.optionIndexCallLong
					}, {
						v : data.Client.optionIndexCallLong
					} ]
				},
				{
					c : [ {
						v : "O-I-PL"
					}, {
						v : data.FII.optionIndexPutLong
					}, {
						v : data.Client.optionIndexPutLong
					} ]
				}, {
					c : [ {
						v : "O-I-CS"
					}, {
						v : data.FII.optionIndexCallShort
					}, {
						v : data.Client.optionIndexCallShort
					} ]
				}, {
					c : [ {
						v : "O-I-PS"
					}, {
						v : data.FII.optionIndexPutShort
					}, {
						v : data.Client.optionIndexPutShort
					} ]
				},
				{
					c : [ {
						v : "O-S-CL"
					}, {
						v : data.FII.optionStockCallLong
					}, {
						v : data.Client.optionStockCallLong
					} ]
				},
				{
					c : [ {
						v : "O-S-PL"
					}, {
						v : data.FII.optionStockPutLong
					}, {
						v : data.Client.optionStockPutLong
					} ]
				},
				{
					c : [ {
						v : "O-S-CS"
					}, {
						v : data.FII.optionStockCallShort
					}, {
						v : data.Client.optionStockCallShort
					} ]
				},
				{
					c : [ {
						v : "O-S-PS"
					}, {
						v : data.FII.optionStockPutShort
					}, {
						v : data.Client.optionStockPutShort
					} ]
				}];

				$scope.myChartObject.data.rows = rows;

			};
			var failCallBack = function(response) {
			};

			$scope.getOpenInterestByDateInterval = function() {
				openInterestService.getOpenInterest($scope.from, $scope.to, successCallBack,
						failCallBack);
			}

			$scope.myChartObject.data = {
				"cols" : [ {
					id : "t",
					label : "option index",
					type : "string"
				}, {
					id : "s",
					label : "FII",
					type : "number"
				}, {
					id : "s",
					label : "client",
					type : "number"
				} ],
				"rows" : [

				]
			};

			/*$scope.myChartObject.options = {
				'title' : 'Last trading day open interest report'
			};*/
		} ]);