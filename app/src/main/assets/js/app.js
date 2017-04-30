    var scotchApp = angular.module('scotchApp', ['ng-fusioncharts']);


    scotchApp.controller('mainController', function($scope) {
    	$scope.activeTab = 1;
    	
    	$scope.setActiveTab = function(index){
    		$scope.activeTab = index;
    	}
    	$scope.isTabActive = function(index){
    		if($scope.activeTab == index)
    			return true;
    		return false;
    	}
    });