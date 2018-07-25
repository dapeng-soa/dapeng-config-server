/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
module api {
    export class Api {

        public get(url: string, data: any, success?: (data: any) => any) {
            const settings: JQueryAjaxSettings = {type: "get", data: data, url: url, dataType: "json"};
            $.ajax(settings)
                .done(success);
        }

        public $get(url: string,success?: (data: any) => any) {
            const settings: JQueryAjaxSettings = {type: "get", url: url, dataType: "json"};
            $.ajax(settings)
                .done(success);
        }

        public post(url: string, data: any, success?: (data: any) => any) {
            const settings: JQueryAjaxSettings = {type: "post", data: data, url: url, dataType: "json"};
            $.ajax(settings)
                .done(success);
        }
    }
}