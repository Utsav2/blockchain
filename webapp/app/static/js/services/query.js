'use strict';

angular.module('bitcoins')
.service('query', function($http, $q) {

  var JAVA_PORT = '3000';
  var PYTHON_PORT = '5000';

  var getUrl = function(port) {
    return window.location.protocol + '//' + window.location.hostname + ':' + port + '/';
  };

  var JAVA_URL = getUrl(JAVA_PORT);
  var PYTHON_URL = window.location.protocol + '//' + '127.0.0.1:' + PYTHON_PORT + '/';

  this.getClosure = function(address) {

    var deferred = $q.defer();
    $http.get(PYTHON_URL + 'getHash', { params : { address : address } })
    .then(function(response) {
      $http.get(JAVA_URL + 'getClosure', { params : { hash : response.data } })
      .then(function(response) {
        if (response.data.length == 0) {
          deferred.reject('No such data');
        } else {
          $http.get(PYTHON_URL + 'getAddress', { params : { hashes : JSON.stringify(response.data) } })
          .then(function(response) {
            deferred.resolve(response.data.data);
          });
        }
      });
    });

    return deferred.promise;
  }
});
