 'use strict';

angular.module('jpmorganApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jpmorganApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jpmorganApp-params')});
                }
                return response;
            }
        };
    });
