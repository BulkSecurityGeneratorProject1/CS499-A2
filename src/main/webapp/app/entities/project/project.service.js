(function() {
    'use strict';
    angular
        .module('a2App')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.deadline = DateUtils.convertDateTimeFromServer(data.deadline);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
