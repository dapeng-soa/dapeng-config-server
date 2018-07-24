/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
module api {
    export class Api {
        // ajax
        public Ajax(type: String, url: String, dataType: String) {

        }

        public post (url: String, dataType: String) {
            const settings: JQueryAjaxSettings = {type: "post", url: url, dataType: "json"}
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
        }

        // 初始化
        public selectInitOption() {

        }
    }
}