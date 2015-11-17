angular.module('bitcoins')
  .controller('HomeController', function (query, $scope) {
    $scope.$watch('address', function() {
      $scope.error = null;
    });
    $scope.submit = function() {
      query.getClosure($scope.address)
      .then(function(data) {
        $scope.resultSet = data;
        $scope.error = null;
      })
      .catch(function(error) {
        $scope.error = error;
        $scope.resultSet = null;
      });
    };
  });
