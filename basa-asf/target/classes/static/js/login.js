const login = angular.module("BasaApp", ['ui.router']);

login.controller('LoginController', function ($scope, $rootScope) {
    $scope.$on('$viewContentLoaded', function(event){
        // alert("Hi");
    });
});