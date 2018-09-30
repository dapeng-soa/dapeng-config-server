/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
module api {
    export class Api {

        public get(url: string, data: any, success?: (data: any) => any) {
            const settings: JQueryAjaxSettings = {type: "get", data: data, url: url, dataType: "json"};
            $.ajax(settings)
                .done(success);
        }

        public $get(url: string, success?: (data: any) => any) {
            const settings: JQueryAjaxSettings = {type: "get", url: url, dataType: "json"};
            $.ajax(settings)
                .done(success);
        }

        /**
         *
         * @param {string} url
         * @param data
         * @param {(data: any) => any} success
         * @param {string} contentType eg:"application/json"
         */
        public post(url: string, data: any, success?: (data: any) => any, contentType?: string) {
            const settings: JQueryAjaxSettings = {type: "post", data: data, url: url, dataType: "json"};
            if (contentType != undefined) settings.contentType = contentType;
            $.ajax(settings)
                .done(success);
        }
    }
}