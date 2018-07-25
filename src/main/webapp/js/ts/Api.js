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
        Api.prototype.post = function (url, data, success) {
            var settings = { type: "post", data: data, url: url, dataType: "json" };
            $.ajax(settings)
                .done(success);
        };
        Api.prototype.change = function () {
        };
        // 下拉并默认选中第一个
        Api.prototype.selectInitOption = function (url, el, id) {
            this.get(url, {}, function (res) {
                if (res.code === window.SUCCESS_CODE) {
                    var html = "";
                    for (var i = 0; i < res.context.length; i++) {
                        var seled = "";
                        if (i === 0) {
                            seled = "selected";
                        }
                        if (id !== undefined && id !== "") {
                            seled = res.context[i].id === id ? "selected" : "";
                        }
                        html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
                    }
                    $(el).html(html);
                }
            });
        };
        return Api;
    }());
    api.Api = Api;
})(api || (api = {}));
