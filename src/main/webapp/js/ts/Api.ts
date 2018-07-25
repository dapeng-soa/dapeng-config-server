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

        public change(){

        }

        // 下拉并默认选中第一个
        public selectInitOption(url: string, el: string,id?:string) {
            this.get(url, {}, (res) => {
                if (res.code === window.SUCCESS_CODE) {
                    let html = "";
                    for (let i = 0; i < res.context.length; i++) {
                        let seled = "";
                        if (i === 0) {
                            seled = "selected"
                        }
                        if (id !== undefined && id !== "") {
                            seled = res.context[i].id === id ? "selected" : "";
                        }
                        html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
                    }
                    $(el).html(html);
                }
            })
        }
    }
}