'use strict';

angular.module('jpmorganApp')
    .factory('StockTrade', function ($resource, DateUtils) {
        return $resource('api/stockTrades/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.tradeDate = DateUtils.convertDateTimeFromServer(data.tradeDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
