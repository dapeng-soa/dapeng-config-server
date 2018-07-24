/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
var api;
(function (api) {
    var Api = /** @class */ (function () {
        function Api() {
        }
        // ajax
        Api.prototype.Ajax = function (type, url, dataType) {
        };
        Api.prototype.post = function (url, dataType) {
            var settings = { type: "post", url: url, dataType: "json" };
            $.ajax(settings)
                .done(function (result) {
                if (isModel) {
                    self.resultToHTML(result);
                    window.$previousModle.push($("#struct-model .struct-model-context").html());
                    if (window.$previousModle.length > 1) {
                        $(".back-to-previous").show();
                    }
                }
            });
        };
        // 初始化
        Api.prototype.selectInitOption = function () {
        };
        return Api;
    }());
    api.Api = Api;
})(api || (api = {}));
