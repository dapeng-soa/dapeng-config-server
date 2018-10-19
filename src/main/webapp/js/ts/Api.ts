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

        /**
         * 控制台打印
         * @param {string} row
         * @param {string} lv
         */
        public consoleView(row: string, lv?: string) {
            let c = this;
            let rowStr = `
            <p><span style="color:#00bb00">[${new Date().toLocaleTimeString()}]#</span> ${row}</p>
            `;
            let ob = $("#consoleView");
            ob.append(rowStr);
            document.getElementById("consoleView").scrollTop = document.getElementById("consoleView").scrollHeight
        }
    }
}