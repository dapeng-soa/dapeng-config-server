/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
var api;
(function (api) {
    var Api = /** @class */ (function () {
        function Api() {
        }
        Api.prototype.get = function (url, data, success) {
            var settings = { type: "get", data: data, url: url, dataType: "json" };
            $.ajax(settings)
                .done(success);
        };
        Api.prototype.$get = function (url, success) {
            var settings = { type: "get", url: url, dataType: "json" };
            $.ajax(settings)
                .done(success);
        };
        /**
         *
         * @param {string} url
         * @param data
         * @param {(data: any) => any} success
         * @param {string} contentType eg:"application/json"
         */
        Api.prototype.post = function (url, data, success, contentType) {
            var settings = { type: "post", data: data, url: url, dataType: "json" };
            if (contentType != undefined)
                settings.contentType = contentType;
            $.ajax(settings)
                .done(success);
        };
        return Api;
    }());
    api.Api = Api;
})(api || (api = {}));
