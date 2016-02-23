'use strict';

angular.module('jpmorganApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


