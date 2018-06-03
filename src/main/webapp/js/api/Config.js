/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
var api;
(function (api) {
    var Config = /** @class */ (function () {
        function Config() {
            //
            this.servicesUrl = window.basePath + "/api/services";
            this.add = "add";
            this.view = "view";
            this.edit = "edit";
        }
        // 导出添加配置页面内容
        Config.prototype.exportAddConfigContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            // language=HTML
            return "\n                <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加配置" : (type == c.edit ? "修改配置:" + biz : (type == c.view ? "配置详情:" + biz : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                " + (type != c.add ? "\n                <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u66F4\u65B0\u65F6\u95F4:</label>\n                        <div class=\"col-sm-9\">\n                            <input type=\"text\" " + (type != c.add ? "disabled" : "") + " class=\"form-control\" value=\"" + data.updatedAt + "\"/>\n                        </div>\n                    </div>\n                " : "") + " \n                       " + (type != c.add ? "\n                <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u7248\u672C\u53F7:</label>\n                        <div class=\"col-sm-9\">\n                            <input type=\"text\" " + (type != c.add ? "disabled" : "") + " class=\"form-control\" value=\"" + data.version + "\"/>\n                        </div>\n                    </div>\n                " : "") + "\n                    <div class=\"form-group\">" + ("\n                    <label class=\"col-sm-2 control-label\">" + (type == c.add ? "选择服务:" : "服务名:") + "</label>\n                        <div class=\"col-sm-9\">\n                            " + (type == c.add ?
                "<select class=\"form-control\" id=\"services-sel\">\n                            </select>\n                            " : "\n                            <input type=\"text\" " + (type != c.add ? "disabled" : "") + " class=\"form-control\" value=\"" + data.serviceName + "\"/>\n                    ") + "\n                        </div>") + "\n                    </div>\n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u8D85\u65F6\u914D\u7F6E:</label>\n                        <!--\u8D85\u65F6\u914D\u7F6E-->\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type != c.add ? "disabled" : "") + " id=\"timeout-config-area\" class=\"col-sm-2 form-control\" rows=\"5\">" + (type != c.add ? data.timeoutConfig : "") + "</textarea>\n                        </div>\n                    </div>\n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u8D1F\u8F7D\u5747\u8861:</label>\n                        <!--\u8D1F\u8F7D\u5747\u8861\u914D\u7F6E-->\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type != c.add ? "disabled" : "") + " id=\"loadbalance-config-area\" class=\"col-sm-2 form-control\" rows=\"5\">" + (type != c.add ? data.loadbalanceConfig : "") + "</textarea>\n                        </div>\n                    </div>\n                    <!--\u8DEF\u7531\u914D\u7F6E-->\n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u8DEF\u7531\u914D\u7F6E:</label>\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type != c.add ? "disabled" : "") + " id=\"router-config-area\" class=\"form-control\" rows=\"5\">" + (type != c.add ? data.routerConfig : "") + "</textarea>\n                        </div>\n                    </div>\n                    <!--\u9650\u6D41\u914D\u7F6E-->\n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u9650\u6D41\u914D\u7F6E:</label>\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type != c.add ? "disabled" : "") + " id=\"freq-config-area\" class=\"form-control\" rows=\"7\">" + (type != c.add ? data.freqConfig : "") + "</textarea>\n                        </div>\n                    </div>\n                    " + (type != c.add ? "" : "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"saveconfig()\">\u4FDD\u5B58\u914D\u7F6E</button>\n                    <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearConfigInput()\">\u6E05\u7A7A\u914D\u7F6E</button>\n                    </span>\n                    ") + "\n                </div>\n            ";
        };
        // 初始化服务信息
        Config.prototype.initServices = function () {
            var config = this;
            var optionsEl = "";
            $.get(config.servicesUrl, function (res) {
                var services = res.context;
                for (var _i = 0, services_1 = services; _i < services_1.length; _i++) {
                    var s = services_1[_i];
                    optionsEl += "<option value=" + s + ">" + s + "</option>";
                }
                $("#services-sel").html(optionsEl);
            }, "json");
        };
        //表格操作模版
        Config.prototype.exportTableActionContext = function (id, status) {
            return "<span class=\"link-button-table\">\n            " + (status != 3 ? "<a href=\"javascript:void(0)\" onclick=\"publishConfig(" + id + ")\">\u53D1\u5E03</a>" : "") + "\n            " + (status == 3 ? "<a href=\"javascript:void(0)\"  onclick=\"rollback(" + id + ")\">\u56DE\u6EDA</a>" : "") + "\n            <a href=\"javascript:void(0)\"  onclick=\"viewOrEditByID(" + id + ",'view')\">\u8BE6\u60C5</a>\n            </span>";
        };
        return Config;
    }());
    api.Config = Config;
})(api || (api = {}));
