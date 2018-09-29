/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>
/*部署模块ts模版代码*/
var api;
(function (api) {
    var Deploy = /** @class */ (function () {
        function Deploy() {
            this.add = "add";
            this.view = "view";
            this.edit = "edit";
            this.api = new api.Api();
            this.serviceView = 1;
            this.hostView = 2;
        }
        /**
         * 环境集-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeploySetContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n          <div class=\"panel-header window-header\">\n                <div class=\"input-group\">\n                    <p class=\"left-panel-title\">" + (type == c.add ? "添加环境集" : (type == c.edit ? "修改环境集" : (type == c.view ? "环境集详情" : ""))) + "</p>\n                </div>\n            </div>\n            <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                 " + (type == c.add ? "\n                 <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\"></label>\n                        <div class=\"col-sm-9\">\n                            <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><i class=\"fa fa-files-o\" aria-hidden=\"true\"></i>\u70B9\u6B64\u53EF\u590D\u5236\u4E00\u4E2A\u5DF2\u5B58\u5728\u7684\u73AF\u5883\u96C6</p>\n                                <div class=\"advance-format-content\">\n                                <div class=\"row\">\n                                <div class=\"col-sm-10\"><select class=\"form-control\" id=\"addCopySetSelect\" style=\"width:100%\"></select></div>\n                                <div class=\"col-sm-2\"><button style=\"width: 100%\" type=\"button\" class=\"btn btn-info\" onclick=\"copySetChange()\">\u590D\u5236</button></div>\n                                </div>\n                                <code>\u9009\u62E9\u4E00\u4E2A\u5DF2\u6709\u7684\u73AF\u5883\u96C6,\u5E76\u70B9\u51FB\u590D\u5236,\u4E0D\u8981\u5FD8\u8BB0\u4FEE\u6539\u73AF\u5883\u96C6\u7684\u540D\u79F0\u548C\u5BF9\u5E94\u7684ENV\uFF01</code>\n                                </div>\n                              </div>\n                        </div>\n                    </div>\n                 " : "") + "\n                  <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u73AF\u5883\u96C6\u540D\u79F0:</label>\n                        <div class=\"col-sm-9\">\n                            <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"name\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.name : "") + "\">\n                        </div>\n                    </div>\n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">ENV:</label>\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type == c.view ? "disabled" : "") + " id=\"env-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.env : "") + "</textarea>\n                            <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nENV(\u73AF\u5883\u53D8\u91CF):\n\u4F5C\u7528:\u63D0\u4F9B\u5E94\u7528\u6240\u9700\u7684\u73AF\u5883\u53D8\u91CF\n\u4E66\u5199\u683C\u5F0F:\nappName=goodsService(\u5EFA\u8BAE) \nappName:goodsService\n\u5907\u6CE8:\n\u5C3D\u91CF\u5C06\u516C\u5171\u73AF\u5883\u53D8\u91CF\u914D\u7F6E\u5728\u6B64\u5904\n                                  </pre>\n                                </div>\n                              </div>\n                        </div>\n                    </div>\n                   \n                    <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\">\u5907\u6CE8:</label>\n                        <div class=\"col-sm-9\">\n                            <textarea " + (type == c.view ? "disabled" : "") + " id=\"remark-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.remark : "") + "</textarea>\n                        </div>\n                    </div>\n                   \n                    " + (type == c.add ? "\n                    <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"saveDeploySet()\">\u4FDD\u5B58</button>\n                    <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearDeploySetInput()\">\u6E05\u7A7A</button>\n                    </span>\n                    " : type == c.edit ? "\n                    <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedDeploySet(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                    " : "") + "\n                </div>\n";
        };
        Deploy.prototype.exportAddSubEnvBySetIdContext = function (type, setId, subEnv) {
            var c = this;
            return "\n            <div class=\"panel-header window-header\">\n                <div class=\"input-group\">\n                    <p class=\"left-panel-title\">\u73AF\u5883\u96C6\u670D\u52A1ENV\u914D\u7F6E(SetSubEnv)</p>\n                </div>\n            </div>\n            <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n            \n                 <div class=\"form-group\">\n                        <label class=\"col-sm-1 control-label\"></label>\n                        <div class=\"col-sm-10\">\n                            <div id=\"sub-from-container\">\n                            " + c.viewSubEnv("view", subEnv) + "\n                            </div>\n                            " + (type !== c.view ? "\n                        <div style=\"margin-top: 10px\" class=\"icon-add\"><a href=\"#\" onclick=\"addSubFromBySet()\"><span class=\"glyphicon glyphicon-plus\"></span></a>\u70B9\u51FB\u65B0\u589E\u914D\u7F6E</div>\n                    <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"saveSubEnvs(" + setId + ")\">\u4FDD\u5B58</button>\n                    </span>\n                    " : "") + "\n           </div>\n           </div>\n           </div>               \n";
        };
        /**
         * 环境集配置文件管理
         */
        Deploy.prototype.exportAddConfigBySetIdContext = function (setId, services, files) {
            var c = this;
            return "\n            <div class=\"panel-header window-header\">\n                <div class=\"input-group\">\n                    <p class=\"left-panel-title\">\u73AF\u5883\u96C6\u914D\u7F6E\u6587\u4EF6\u7BA1\u7406</p>\n                </div>\n            </div>\n            <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                 <div class=\"form-group\">\n                    <label class=\"col-sm-1 control-label\"></label>\n                    <div class=\"col-sm-10\">\n                        <div id=\"configFiles-from-container\">\n                           " + c.getConfigFiles(services, files) + "\n                         </div>\n                         <div style=\"margin-top: 10px\" class=\"icon-add\"><a href=\"#\" onclick=\"addConfigFilesEm()\"><span class=\"glyphicon glyphicon-plus\"></span></a>\u70B9\u51FB\u65B0\u589E\u914D\u7F6E</div>\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveConfigFile(" + setId + ")\">\u4FDD\u5B58</button>\n                        </span>\n                    </div>\n                </div>\n            </div> \n            ";
        };
        Deploy.prototype.getConfigFiles = function (services, files) {
            var html = "";
            var c = this;
            if (files !== undefined && files.length > 0) {
                for (var f in files) {
                    html += c.exportConfigFilesContext(services, files[f]);
                }
            }
            return html;
        };
        /**
         * 导出
         * @returns {string}
         */
        Deploy.prototype.exportConfigFilesContext = function (services, file) {
            var options = "";
            for (var index in services) {
                var s = services[index];
                options += "<option  value=\"" + s.id + "\">" + s.name + "</option>";
            }
            return "\n            <div class=\"form-horizontal from-group-item\" style=\"margin-top: 20px;\">\n                " + (file !== undefined ? "" : "<a class=\"from-group-item-rm\" href=\"javascript:void(0)\"><span class=\"glyphicon glyphicon-remove\"></span></a>") + "\n                <input type=\"hidden\" class=\"data-ops-id\" value=\"" + (file === undefined ? 0 : file.id) + "\">\n                <div class=\"form-group\">\n                    <div class=\"col-sm-12\">\n                        <input class=\"form-control data-name-input\" placeholder=\"\u6587\u4EF6\u540D(\u542B\u6587\u4EF6\u7C7B\u578B\u5982\uFF1Aserver.xml)\" value=\"" + (file === undefined ? "" : file.fileName) + "\" >\n                    </div>\n                </div>\n                <div class=\"form-group\">\n                        <div class=\"col-sm-12\">\n                            <select class=\"form-control selectpicker data-service-select\" multiple data-live-search=\"true\" data-actions-box=\"true\" title=\"\u7ED1\u5B9A\u670D\u52A1\">\n                                " + options + "\n                            </select>\n                        </div>\n                </div>   \n                <div class=\"form-group\">\n                    <div class=\"col-sm-12\">\n                        <textarea class=\"form-control data-context-textarea\" placeholder=\"\u6587\u4EF6\u5185\u5BB9\">" + (file === undefined ? "" : file.fileContext) + "</textarea>\n                            </div>\n                        </div> \n                    </div>\n            ";
        };
        Deploy.prototype.viewSubEnv = function (type, subEnvs) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            var url = basePath + "/api/deploy-services";
            $.get(url, function (res) {
                var ops = res.context.content;
                var view = "";
                for (var index in subEnvs) {
                    var s = subEnvs[index];
                    view += c.exportAddSubEnvContext(type, ops, s);
                }
                $("#sub-from-container").append(view);
            }, "json");
            return "";
        };
        Deploy.prototype.exportAddSubEnvContext = function (type, services, subEnv) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var ops = "<option value=\"0\">\u8BF7\u9009\u62E9\u670D\u52A1</option>";
            var sId = 0;
            var env = {};
            if (type !== this.add) {
                sId = subEnv.serviceId;
                env = subEnv.env;
            }
            for (var index in services) {
                var s = services[index];
                var sed = "";
                if (s.id === sId) {
                    sed = "selected";
                }
                ops += "<option  " + sed + " value=\"" + s.id + "\">" + s.name + "</option>";
            }
            return "\n                <div class=\"form-horizontal from-group-item\" style=\"margin-top: 20px;\">\n                    " + (type !== this.add ? "" : "<a class=\"from-group-item-rm\" href=\"javascript:void(0)\"><span class=\"glyphicon glyphicon-remove\"></span></a>") + "\n                    <input type=\"hidden\" class=\"data-ops-id\" value=\"" + (subEnv === undefined ? 0 : subEnv.id) + "\">\n                    <div class=\"form-group\">\n                        <div class=\"col-sm-12\">\n                            <select  class=\"form-control data-service-select\" " + (type !== this.add ? "disabled" : "") + ">" + ops + "</select>\n                        </div>\n                    </div>   \n                    <div class=\"form-group\">\n                        <div class=\"col-sm-12\">\n                            <textarea class=\"form-control data-env-textarea\"  >" + (type === this.add ? "" : env) + "</textarea>\n                        </div>\n                    </div> \n                </div>\n            ";
        };
        /**
         * 服务-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployServiceContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n           <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加服务" : (type == c.edit ? "修改服务" : (type == c.view ? "服务详情" : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                 " + (type == c.add ? "\n                  <div class=\"form-group\">\n                        <label class=\"col-sm-2 control-label\"></label>\n                        <div class=\"col-sm-9\">\n                            <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><i class=\"fa fa-files-o\" aria-hidden=\"true\"></i>\u70B9\u6B64\u53EF\u590D\u5236\u4E00\u4E2A\u5DF2\u5B58\u5728\u7684\u670D\u52A1</p>\n                                <div class=\"advance-format-content\">\n                                <div class=\"row\">\n                                <div class=\"col-sm-10\"><select class=\"form-control\" id=\"addCopyServiceSelect\" style=\"width:100%\"></select></div>\n                                <div class=\"col-sm-2\"><button style=\"width: 100%\" type=\"button\" class=\"btn btn-info\" onclick=\"copyServiceChange()\">\u590D\u5236</button></div>\n                                </div>\n                                <code>\u9009\u62E9\u4E00\u4E2A\u5DF2\u6709\u7684\u670D\u52A1,\u5E76\u70B9\u51FB\u590D\u5236,\u4E0D\u8981\u5FD8\u8BB0\u4FEE\u6539\u65B0\u52A0\u7684\u670D\u52A1\u4FE1\u606F\uFF01</code>\n                                </div>\n                              </div>\n                        </div>\n                    </div> \n                 " : "") + "\n                   <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u670D\u52A1\u540D\u5B57:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"name\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.name : "") + "\">\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u670D\u52A1\u955C\u50CF:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"image\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.image : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u670D\u52A1\u542F\u52A8\u7684\u955C\u50CF\u540D\n\u5343\u4E07\u6CE8\u610F,\u4E0D\u8981\u586B\u5199\u955C\u50CFTAG,\u955C\u50CFTAG\u5E94\u5F53\u662F\u5728\u90E8\u7F72\u5355\u5143\u4E2D\u65B0\u589E\u90E8\u7F72\u65F6\u586B\u5199\n\u5199\u6CD5\u793A\u4F8B:\ndapengsoa/redis-wzx(\u6B63\u786E)\ndapengsoa/redis-wzx:3.2.3.1(\u9519\u8BEF,\u65E0\u9700\u586B\u5199\u955C\u50CFTAG)\n                                  </pre>\n                                </div>\n                                </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6807\u7B7E:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"labels\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.labels : "") + "\">\n                            </div>\n                        </div>\n                        \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">ENV:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"env-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.env : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nENV(\u73AF\u5883\u53D8\u91CF):\n\u4F5C\u7528:\u63D0\u4F9B\u5E94\u7528\u6240\u9700\u7684\u73AF\u5883\u53D8\u91CF\n\u4E66\u5199\u683C\u5F0F:\nappName=goodsService(\u5EFA\u8BAE) \nappName:goodsService\n\u5907\u6CE8:\n\u670D\u52A1\u7684\u73AF\u5883\u53D8\u91CF\u6700\u597D\u662F\u53EA\u9488\u5BF9\u5F53\u524D\u670D\u52A1\u6240\u9700\u8981\u7684\u914D\u7F6E\u5373\u53EF\uFF0C\u516C\u5171\u914D\u7F6E\u53EF\u914D\u7F6E\u5230\u73AF\u5883\u96C6\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">VOLUMES:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"volumes-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.volumes : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nVOLUMES(\u6302\u8F7D\u5377):\n\u4F5C\u7528:\u5E94\u7528\u5BB9\u5668\u4E0E\u5BBF\u4E3B\u673A\u7684\u76EE\u5F55\u6620\u5C04\n\u4E66\u5199\u683C\u5F0F:\n\u5BBF\u4E3B\u673A\u76EE\u5F55:\u5BB9\u5668\u76EE\u5F55\n/data/log:/data/log\n\u5907\u6CE8:\n\u670D\u52A1\u7684\u5377\u6302\u8F7D\u6700\u597D\u662F\u53EA\u9488\u5BF9\u5F53\u524D\u670D\u52A1\u6240\u9700\u8981\u7684\u914D\u7F6E\u5373\u53EF\uFF0C\u516C\u5171\u914D\u7F6E\u53EF\u914D\u7F6E\u5230\u73AF\u5883\u96C6\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">PORTS:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"ports-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.ports : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nPORTS(\u6620\u5C04\u7AEF\u53E3):\n\u4F5C\u7528:\u5E94\u7528\u5BB9\u5668\u4E0E\u5BBF\u4E3B\u673A\u7684\u7AEF\u53E3\u6620\u5C04\n\u4E66\u5199\u683C\u5F0F:\n\u5BBF\u4E3B\u673A\u7AEF\u53E3:\u5BB9\u5668\u7AEF\u53E3\n8080:8080\n9999:9999\n\u5907\u6CE8:\n\u5C3D\u91CF\u5728\u670D\u52A1\u4E2D\u660E\u786E\u7AEF\u53E3\u7684\u6620\u5C04\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">composeLabels:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"composeLabels-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.composeLabels : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\ndocker-compsoe\u7684labels\u914D\u7F6E:\n\u4E66\u5199\u683C\u5F0F:\nproject.owner=struy\n\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">dockerExtras:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"dockerExtras-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.dockerExtras : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u9644\u52A0\u7684docker-compose\u914D\u7F6E,\u5728service\u4E0B\u7EA7,\u4E0Eenvironment\u540C\u7EA7:\n\u4E66\u5199\u683C\u5F0F(\u6682\u4E0D\u652F\u6301\u591A\u884C\u914D\u7F6E):\nrestart: on-failure:3\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5907\u6CE8:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"remark-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.remark : "") + "</textarea>\n                            </div>\n                        </div>\n                         " + (type == c.add ? "\n                         <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveDeployService()\">\u4FDD\u5B58</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearDeployServiceInput()\">\u6E05\u7A7A</button>\n                        </span>\n                         " : type == c.edit ? "\n                         <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedDeployService(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                         " : "") + "\n                </div>\n            \n            ";
        };
        /**
         * 节点-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployHostContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n           <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加节点" : (type == c.edit ? "修改节点" : (type == c.view ? "节点详情" : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                    <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u8282\u70B9\u540D\u79F0:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"name\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.name : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u8282\u70B9\u540D\u53EA\u80FD\u4F7F\u7528\u82F1\u6587,\u5C3D\u91CF\u8BED\u610F\u6E05\u6670\u3002\n\u5F53\u670D\u52A1\u8FD0\u884C\u65F6,\u6240\u6709\u7684\u8282\u70B9ip\u90FD\u4F1A\u5199\u5165\u5BB9\u5668hosts\n\u5982:\nhost1 192.168.0.666\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">IP\u5730\u5740:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"ip\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.ip : "") + "\">\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6807\u7B7E:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"labels\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.labels : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u53EF\u7528\u4E8E\u7B5B\u9009\u7684\u6807\u7B7E\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6240\u5C5E\u73AF\u5883\u96C6:</label>\n                            <div class=\"col-sm-9\">\n                               <select " + (type == c.view ? "disabled" : "") + " id=\"setSelect\" class=\"col-sm-2 form-control\">\n                                 \n                                </select>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u5982\u679C\u9009\u62E9\u4E86\u67D0\u4E2A\u73AF\u5883\u96C6\uFF0C\u5219\u5F53\u524D\u6DFB\u52A0\u4E3B\u673A\u96B6\u5C5E\u4E8E\u6240\u9009\u73AF\u5883\u96C6\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u662F\u5426\u5916\u90E8\u673A\u5668:</label>\n                            <div class=\"col-sm-9\">\n                               <select " + (type == c.view ? "disabled" : "") + " id=\"extraSelect\" class=\"col-sm-2 form-control\">\n                                  <option value=\"0\" " + (type != c.add ? (data.extra == 0 ? "selected" : "") : "") + ">\u662F</option>\n                                  <option value=\"1\" " + (type != c.add ? (data.extra == 1 ? "selected" : "") : "selected") + ">\u5426</option>\n                                </select>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u5185\u90E8\u673A\u5668\u7528\u4E8E\u90E8\u7F72\u4E1A\u52A1\u670D\u52A1\n\u5916\u90E8\u673A\u5668\u7528\u4E8E\u90E8\u7F72\u7C7B\u4F3C\u6570\u636E\u5E93,\u4E2D\u95F4\u4EF6\u7B49\u5916\u90E8\u670D\u52A1\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">ENV:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"env-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.env : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nENV(\u73AF\u5883\u53D8\u91CF):\n\u4F5C\u7528:\u63D0\u4F9B\u5E94\u7528\u6240\u9700\u7684\u73AF\u5883\u53D8\u91CF\n\u4E66\u5199\u683C\u5F0F:\nappName=goodsService(\u5EFA\u8BAE) \nappName:goodsService\n\u5907\u6CE8:\n1.\u8282\u70B9\u73AF\u5883\u53D8\u91CF\u5E94\u5F53\u5C3D\u91CF\u4E0D\u586B\u5199\uFF0C\u9664\u975E\u957F\u671F\u7684\u5728\u67D0\u4E2A\u8282\u70B9\u5B58\u5728\u7279\u6709\u914D\u7F6E\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5907\u6CE8:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"remark-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.remark : "") + "</textarea>\n                            </div>\n                        </div>\n                        " + (type == c.add ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveDeployHost()\">\u4FDD\u5B58</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearDeployHostInput()\">\u6E05\u7A7A</button>\n                        </span>\n                        " : type == c.edit ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedDeployHost(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                        " : "") + "\n                       \n                </div>\n            \n            ";
        };
        /**
         * 部署单元-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployUnitContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n           <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加部署单元" : (type == c.edit ? "修改部署单元" : (type == c.view ? "部署单元详情" : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                    <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u53D1\u5E03TAG:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"gitTag\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.gitTag : "") + "\">\n                            </div>\n                        </div>\n                        \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6240\u5C5E\u73AF\u5883\u96C6:</label>\n                            <div class=\"col-sm-9\">\n                               <select " + (type == c.view || type == c.edit ? "disabled" : "") + " id=\"setSelect\" onchange=\"addUnitSetChanged(this)\" class=\"col-sm-2 form-control \">\n          \n                                </select>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre id=\"setEnvView\">\n\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6240\u5C5E\u4E3B\u673A:</label>\n                            <div class=\"col-sm-9\">\n                               <select " + (type == c.view || type == c.edit ? "disabled" : "") + " onchange=\"addUnitHostChanged(this)\" id=\"hostSelect\" class=\"col-sm-2 form-control\">\n                                </select>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre id=\"hostEnvView\">\n\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6240\u5C5E\u670D\u52A1:</label>\n                            <div class=\"col-sm-9\">\n                               <select " + (type == c.view || type == c.edit ? "disabled" : "") + " onchange=\"addUnitServiceChanged(this)\" id=\"serviceSelect\" class=\"col-sm-2 form-control\">\n                                </select>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre id=\"serviceEnvView\">\n\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u955C\u50CFTAG:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"imageTag\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.imageTag : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u672C\u6B21\u90E8\u7F72\u7684\u670D\u52A1\u955C\u50CFtag,\u65E0\u9700\u586B\u5199\u955C\u50CF\u540D\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                       \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">ENV:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"env-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.env : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nENV(\u73AF\u5883\u53D8\u91CF):\n\u4F5C\u7528:\u63D0\u4F9B\u5E94\u7528\u6240\u9700\u7684\u73AF\u5883\u53D8\u91CF\n\u4E66\u5199\u683C\u5F0F:\nappName=goodsService(\u5EFA\u8BAE) \nappName:goodsService\n\u5907\u6CE8:\n\u90E8\u7F72\u5355\u5143\u7684env\u53D8\u91CF\u5E94\u5F53\u53EA\u6709\u5728\u5C11\u6570\u60C5\u51B5\u4E0B\u9700\u8981\u6DFB\u52A0,\u5982\u67D0\u53F0\u8282\u70B9\u9700\u8981\u7279\u6B8A\u7684\u914D\u7F6E\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                      \n                  \n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">VOLUMES:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"volumes-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.volumes : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nVOLUMES(\u6302\u8F7D\u5377):\n\u4F5C\u7528:\u5E94\u7528\u5BB9\u5668\u4E0E\u5BBF\u4E3B\u673A\u7684\u76EE\u5F55\u6620\u5C04\n\u4E66\u5199\u683C\u5F0F:\n\u5BBF\u4E3B\u673A\u76EE\u5F55:\u5BB9\u5668\u5185\u76EE\u5F55\n/data/log:/data/log\n\u5907\u6CE8:\n\u90E8\u7F72\u5355\u5143\u7684VOLUMES\u53EA\u6709\u5728\u6781\u5C11\u6570\u60C5\u51B5\u4E0B\u9700\u8981\u6DFB\u52A0,\u5982\u67D0\u53F0\u8282\u70B9\u9700\u8981\u7279\u6B8A\u7684\u914D\u7F6E\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">PORTS:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"ports-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.ports : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\nPORTS(\u6620\u5C04\u7AEF\u53E3):\n\u4F5C\u7528:\u5E94\u7528\u5BB9\u5668\u4E0E\u5BBF\u4E3B\u673A\u7684\u7AEF\u53E3\u6620\u5C04\n\u4E66\u5199\u683C\u5F0F:\n\u5BBF\u4E3B\u673A\u7AEF\u53E3:\u5BB9\u5668\u7AEF\u53E3\n8080:8080\n\u5907\u6CE8:\n\u90E8\u7F72\u5355\u5143\u7684PORTS\u53EA\u6709\u5728\u6781\u5C11\u6570\u60C5\u51B5\u4E0B\u9700\u8981\u6DFB\u52A0,\u5982\u67D0\u53F0\u8282\u70B9\u9700\u8981\u7279\u6B8A\u7684\u914D\u7F6E\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">dockerExtras:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"dockerExtras-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.dockerExtras : "") + "</textarea>\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u9644\u52A0\u7684docker-compose\u914D\u7F6E,\u5728service\u4E0B\u7EA7,\u4E0Eenv\u540C\u7EA7:\n\u4E66\u5199\u683C\u5F0F(\u6682\u4E0D\u652F\u6301\u591A\u884C\u914D\u7F6E):\nrestart: on-failure:3\n\u5907\u6CE8:\n\u5EFA\u8BAE\u4E0D\u8981\u5728\u6B64\u5904\u6DFB\u52A0\u6B64\u9879\u914D\u7F6E\n                                  </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        " + (type == c.add ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveDeployUnit()\">\u4FDD\u5B58</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearDeployUnitInput()\">\u6E05\u7A7A</button>\n                        </span>\n                        " : type == c.edit ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedDeployUnit(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                        " : "") + "\n                </div>\n            \n            ";
        };
        /**
         * 环境集操作栏
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeploySetActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewDeploySetEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewDeploySetEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <!--<a href=\"javascript:void(0)\" title=\"\u7BA1\u7406\u914D\u7F6E\u6587\u4EF6\"  onclick=\"openAddConfigBySetId(" + value + ")\"><i class=\"fa fa-file-text\" aria-hidden=\"true\"></i></a> -->\n            <a href=\"javascript:void(0)\" title=\"\u7BA1\u7406\u670D\u52A1\u73AF\u5883\u53D8\u91CF\"  onclick=\"openAddSubEnvBySetId(" + value + ",'add')\"><span class=\"glyphicon glyphicon-folder-close\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delDeploySet(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 节点操作栏
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeployHostActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewDeployHostOrEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewDeployHostOrEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delDeployHost(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 服务操作栏
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeployServiceActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewDeployServiceOrEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewDeployServiceOrEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delDeployService(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 部署单元操作栏
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeployUnitActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewDeployUnitOrEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewDeployUnitOrEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delDeployUnit(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 流水操作状态
         * @param value
         * @param row
         * @returns {string}
         */
        Deploy.prototype.exportDeployJournalFlagContext = function (value, row) {
            //1:升级(update);2:重启(restart);3:停止(stop);4:回滚(rollback)
            //0:danger,1:default,2:primary,3:success
            switch (value) {
                case 1:
                    return '<span class="label label-success">升级</span>';
                case 2:
                    return '<span class="label label-info">重启</span>';
                case 3:
                    return '<span class="label label-danger">停止</span>';
                case 4:
                    return '<span class="label label-primary">回滚</span>';
                default:
                    return '<span class="label label-warning">未知</span>';
            }
        };
        Deploy.prototype.exportModifyBatchTagContent = function (eid) {
            return "\n                <div class=\"form-group\" style=\"margin-top: 20px\">\n                        <div class=\"col-sm-12\">\n                            <input type=\"text\"  id=\"" + eid + "\" class=\"form-control\" />\n                        </div>\n                 </div>\n            ";
        };
        /**
         * 查看yml
         * @param value
         * @param row
         * @returns {string}
         */
        Deploy.prototype.exportDeployJournalYmlContext = function (value, row) {
            return "<span class=\"link-button-table\">\n " + (row.opFlag === 1 ? "<a href=\"javascript:void(0)\" title=\"yml\"  onclick=\"viewDeployJournalYml(" + row.id + "," + row.hostId + "," + row.serviceId + ")\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>" : "-") + "\n</span>\n";
        };
        /**
         * 流水操作
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeployJournalActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            " + (row.opFlag === 1 ? "<a href=\"javascript:void(0)\" title=\"\u56DE\u6EDA\"  onclick=\"rollbackDeploy(" + value + ",'" + row.hostName + "','" + row.serviceName + "')\"><i class=\"fa fa-reply-all\" aria-hidden=\"true\"></i></a>" : "-") + "\n            </span>";
        };
        Deploy.prototype.exportViewDeployJournalContext = function () {
            var c = this;
            return "\n                <div class=\"diff-tit\" >\n                <span>\u8FD0\u884C\u914D\u7F6E(\u53EA\u8BFB)</span>\n                <span>\u5F53\u524D\u7248\u672C(\u53EA\u8BFB)</span>\n                </div>\n                <div id=\"mergely\" style=\"margin:20px 0;\">\n                </div>\n                <div class=\"fixed-footer-btn\" >\n                </div>\n            ";
        };
        /**
         * 服务/主机视图
         */
        Deploy.prototype.deployViewChange = function (viewType, data) {
            var dep = this;
            var view = "";
            for (var _i = 0, data_1 = data; _i < data_1.length; _i++) {
                var em = data_1[_i];
                view += "\n            <div class=\"col-sm-6 col-xs-12\">\n                <div class=\"panel panel-default panel-box\">\n                    <div class=\"panel-heading\" style=\"background: #fff\"><p style=\"text-align: center;font-size: 18px\">" + (viewType == dep.serviceView ? em.serviceName : em.hostName + ':[' + em.hostIp + ']') + "</p>\n                    </div>\n                    <div class=\"panel-body\" style=\"overflow-y: auto;height:320px\">\n                         " + dep.serviceViewSubHost(viewType, viewType == dep.serviceView ? em.deploySubHostVos : em.deploySubServiceVos, em) + "\n                    </div>\n                </div>\n            </div>\n            ";
            }
            return view;
        };
        Deploy.prototype.serviceViewSubHost = function (viewType, sub, obj) {
            var dep = this;
            var subView = "";
            for (var _i = 0, sub_1 = sub; _i < sub_1.length; _i++) {
                var em = sub_1[_i];
                var IdPrefix = viewType == dep.serviceView ? em.hostIp + obj.serviceName : obj.hostIp + em.serviceName;
                subView += "<div class=\"row\" style=\"border-bottom: 1px solid gainsboro;padding: 10px 0;\">\n                            <div class=\"col-sm-3 col-xs-12\">\n                                <p style=\"font-size: 20px\">" + (viewType == dep.serviceView ? em.hostName : em.serviceName) + "</p>\n                                " + (viewType == dep.serviceView ? "<p >" + em.hostIp + "</p>" : "") + "\n                                <p>Tag\uFF1A<span id=\"" + IdPrefix + "-ImageTag\">none</span></p>\n                            </div>\n                            <div class=\"col-sm-6 col-xs-12\">\n                                <p>\u914D\u7F6E\u66F4\u65B0\u65F6\u95F4\uFF1A<span id=\"" + IdPrefix + "-configUpdateTime\" data-real-configUpdateBy=\"" + em.configUpdateBy + "\">" + dep.unix2Time(em.configUpdateBy) + "</span></p>\n                                <p>\u4E3B\u673A\u670D\u52A1\u65F6\u95F4\uFF1A<span id=\"" + IdPrefix + "-deployTime\">" + em.deployTime + "</span></p>\n                                <p>\u670D\u52A1\u72B6\u6001\uFF1A<span id=\"" + IdPrefix + "-serviceStatus\">" + dep.realStatus(em.serviceStatus) + "</span></p>\n                                <p>\u9700\u8981\u66F4\u65B0\uFF1A<span id=\"" + IdPrefix + "-needUpdate\">" + dep.updateStatus(em.needUpdate) + "</span></p>\n                                </div>\n                                <div class=\"col-sm-3 col-xs-12\">\n                                    <p ><a href=\"javascript:void(0)\" style=\"color: #1E9FFF\" onclick=\"serviceYamlPreview('" + (IdPrefix + "-deployTime") + "','" + (IdPrefix + "-configUpdateTime") + "'," + em.unitId + ")\">\u5347\u7EA7</a></p>\n                                <p ><a href=\"javascript:void(0)\" style=\"color: #1E9FFF\" onclick=\"stopService(" + em.unitId + ")\">\u505C\u6B62</a></p>\n                                <p ><a href=\"javascript:void(0)\" style=\"color: #1E9FFF\" onclick=\"restartService(" + em.unitId + ")\">\u91CD\u542F</a></p>\n                                <p ><a href=\"javascript:void(0)\" style=\"color: #1E9FFF\" onclick=\"serviceYamlPreview('" + (IdPrefix + "-deployTime") + "','" + (IdPrefix + "-configUpdateTime") + "'," + em.unitId + ",'view')\">\u9884\u89C8</a></p>\n                            </div>\n                        </div>\n            ";
            }
            return subView;
        };
        /**
         * 根据事件返回处理
         * @param realInfo
         */
        Deploy.prototype.processServiceStatus = function (realInfo) {
            var t = this;
            // 构造domID
            var configUpdateId = t.el(realInfo.ip + realInfo.serviceName + "-configUpdateTime");
            var deployTimeId = t.el(realInfo.ip + realInfo.serviceName + "-deployTime");
            var serviceStatusId = t.el(realInfo.ip + realInfo.serviceName + "-serviceStatus");
            var needUpdateId = t.el(realInfo.ip + realInfo.serviceName + "-needUpdate");
            var imageTag = t.el(realInfo.ip + realInfo.serviceName + "-ImageTag");
            if (configUpdateId != null && deployTimeId != null && serviceStatusId != null && needUpdateId != null) {
                var realConfigupdateby = Number(configUpdateId.dataset.realConfigupdateby);
                var updateStatus = realInfo.time < realConfigupdateby;
                deployTimeId.innerHTML = t.unix2Time(realInfo.time);
                serviceStatusId.innerHTML = t.realStatus(realInfo.status ? 1 : 2);
                needUpdateId.innerHTML = t.updateStatus(updateStatus);
                imageTag.innerHTML = realInfo.tag;
            }
        };
        /**
         * 服务状态
         * @param {Number} status
         * @returns {string}
         */
        Deploy.prototype.realStatus = function (status) {
            switch (status) {
                case 1:
                    return "<span style=\"color: #00AA00\"><i class=\"fa fa-cog icon-spin\" aria-hidden=\"true\"></i>\u8FD0\u884C</span>";
                case 2:
                    return "<span style=\"color:#ff4d4d\"><i class=\"fa fa-pause-circle\" aria-hidden=\"true\"></i>\u505C\u6B62</span>";
                default:
                    return "<span style=\"color:#ffd248\"><i class=\"fa fa-pause-circle\" aria-hidden=\"true\"></i>\u672A\u77E5</span>";
            }
        };
        /**
         * 更新状态
         * @param {boolean} b
         */
        Deploy.prototype.updateStatus = function (b) {
            if (b) {
                return "<span style=\"color: #ff4d4d\">\u662F</span>";
            }
            else {
                return "<span style=\"color: #00AA00\">\u5426</span>";
            }
        };
        Deploy.prototype.el = function (ementId) {
            return document.getElementById(ementId);
        };
        /**
         * 预览yaml
         * @returns {string}
         */
        Deploy.prototype.viewDeployYamlContext = function (deployTime, updateTime, unitId, type) {
            var c = this;
            var realDeployTime = c.el("" + deployTime).innerHTML;
            var realUpdateTime = c.el("" + updateTime).innerHTML;
            return "\n                <div class=\"diff-tit\" >\n                <span>\u8FD0\u884C\u914D\u7F6E(\u53EA\u8BFB)[" + realDeployTime + "]</span>\n                <span>\u5F53\u524D\u7248\u672C(\u53EA\u8BFB)[" + realUpdateTime + "]</span>\n                </div>\n                <div id=\"mergely\" style=\"margin:20px 0;\">\n                </div>\n                <div class=\"fixed-footer-btn\" >\n                <span class=\"input-group-btn panel-button-group text-center\">\n                " + (type == undefined || type != c.view ? "\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"execServiceUpdate(" + unitId + ")\">\u786E\u8BA4\u5347\u7EA7</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"cancelServiceUpdate()\">\u53D6\u6D88\u5347\u7EA7</button>\n                        " : "") + "\n                <button type=\"button\" class=\"btn btn-info\" onclick=\"downloadYaml(" + unitId + ")\">\u4E0B\u8F7D\u5F53\u524D\u914D\u7F6E</button>\n                 </span>\n                </div>\n            ";
        };
        /*
        服务配置预览
         */
        Deploy.prototype.serviceCnfigView = function (data) {
            return "\n<span style=\"color: #aa0e0e\">[" + data.name + "\u914D\u7F6E:]</span>\n\n<span style=\"color: #aa0e0e\">[env]</span>\n" + data.env + "\n<span style=\"color: #aa0e0e\">[image]</span>\n" + data.image + "\n<span style=\"color: #aa0e0e\">[ports]</span>\n" + data.ports + "\n<span style=\"color: #aa0e0e\">[volumes]</span>\n" + data.volumes + "\n<span style=\"color: #aa0e0e\">[dockerExtras]</span>\n" + data.dockerExtras + "\n<span style=\"color: #aa0e0e\">[composeLabels]</span>\n" + data.composeLabels + "\n<span style=\"color: #aa0e0e\">[labels]</span>\n" + data.labels + "\n<span style=\"color: #aa0e0e\">[remark]</span>\n" + data.remark + "\n            ";
        };
        /*
       环境集配置预览
        */
        Deploy.prototype.setCnfigView = function (data) {
            return "\n<span style=\"color: #aa0e0e\">[" + data.name + "\u914D\u7F6E:]</span>\n\n<span style=\"color: #aa0e0e\">[env]</span>\n" + data.env + "\n<span style=\"color: #aa0e0e\">[remark]</span>\n" + data.remark + "\n            ";
        };
        /*
主机配置预览
*/
        Deploy.prototype.hostCnfigView = function (data) {
            return "\n<span style=\"color: #aa0e0e\">[" + data.name + "\u914D\u7F6E:]</span>\n\n<span style=\"color: #aa0e0e\">[ip]</span>\n" + data.ip + "\n<span style=\"color: #aa0e0e\">[env]</span>\n" + data.env + "\n<span style=\"color: #aa0e0e\">[remark]</span>\n" + data.remark + "\n<span style=\"color: #aa0e0e\">[\u662F\u5426\u5916\u90E8\u673A\u5668]</span>\n" + (data.extra == 1 ? '否' : '是') + "\n            ";
        };
        /**
         * 控制台打印
         * @param {string} row
         * @param {string} lv
         */
        Deploy.prototype.consoleView = function (row, lv) {
            var c = this;
            var rowStr = "\n            <p><span style=\"color:#00bb00\">[" + new Date().toLocaleTimeString() + "]#</span> " + row + "</p>\n            ";
            var ob = $("#consoleView");
            ob.append(rowStr);
            document.getElementById("consoleView").scrollTop = document.getElementById("consoleView").scrollHeight;
        };
        Deploy.prototype.unix2Time = function (unix) {
            var unixTimestamp = new Date(unix * 1000);
            return unixTimestamp.pattern("yyyy-MM-dd hh:mm:ss");
        };
        return Deploy;
    }());
    api.Deploy = Deploy;
    var ServiceFiles = /** @class */ (function () {
        function ServiceFiles() {
            this.add = "add";
            this.view = "view";
            this.edit = "edit";
            this.api = new api.Api();
        }
        ServiceFiles.prototype.exportAddServiceFilesContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n           <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加节点" : (type == c.edit ? "修改节点" : (type == c.view ? "节点详情" : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                    <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5BB9\u5668\u5185\u6587\u4EF6\u540D:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"fileName\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.fileName : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u5BB9\u5668\u5185\u7684\u6620\u5C04\u6587\u4EF6/\u6587\u4EF6\u5939\u540D(\u5305\u542B\u8DEF\u5F84[\u5982\uFF1A/data/config/config.ini | /data/config/])\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5916\u90E8\u6620\u5C04\u6587\u4EF6\u540D:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"fileExtName\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.fileExtName : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u5BBF\u4E3B\u673A\u7684\u6620\u5C04\u6587\u4EF6/\u6587\u4EF6\u5939\u540D(\u6587\u4EF6\u5939\u5E94\u5F53\u5199\u5168\u8DEF\u5F84/\u6587\u4EF6\u53EA\u5199\u6587\u4EF6\u540D)\n\u6587\u4EF6\u5939:/data/logs\n\u6587\u4EF6:/data/var/config.ini\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u6587\u4EF6\u5185\u5BB9:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"fileContext\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.fileContext : "") + "</textarea>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5907\u6CE8:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"remark-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.remark : "") + "</textarea>\n                            </div>\n                        </div>\n                        " + (type == c.add ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveServiceFile()\">\u4FDD\u5B58</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearServiceFileInput()\">\u6E05\u7A7A</button>\n                        </span>\n                        " : type == c.edit ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedServiceFile(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                        " : "") + "\n                       \n                </div>\n            \n            ";
        };
        /**
         * 文件操作
         * @param value
         */
        ServiceFiles.prototype.exportServiceFileAction = function (value, name) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewServiceFilesOrEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewServiceFilesOrEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5173\u8054\u90E8\u7F72\u5355\u5143\"  onclick=\"openLinkDeployUnits(" + value + ",'" + name + "')\"><i class=\"fa fa-link\" aria-hidden=\"true\"></i></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delServiceFiles(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 文件关联部署单元
         * @param vaue
         */
        ServiceFiles.prototype.exportFileLinkUnitContext = function (value, name) {
            return "\n             <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">\u5173\u8054\u90E8\u7F72\u5355\u5143<small>[" + name + "]</small></p>\n                        <span class=\"input-group-btn panel-button-group\">\n                            <button type=\"button\" class=\"btn btn-info\"  id=\"linkButton\" onclick=\"linkDeployUnits(" + value + ")\">\u4E00\u952E\u5173\u8054</button>\n                            <button type=\"button\" class=\"btn btn-danger\" id=\"unLinkButton\" onclick=\"unLinkDeployUnits(" + value + ")\">\u4E00\u952E\u89E3\u7ED1</button>\n                        </span>\n                    </div>\n                </div>\n                <div style=\"margin-top: 81px;\">\n                <input type=\"hidden\" value=\"" + value + "\" id=\"currFileId\">\n                 <div id=\"deploy-unit-tableToolbar\">\n            <span>\u73AF\u5883\u96C6\uFF1A <span style=\"display: inline-block;width: 120px\">\n                <select data-live-search=\"true\" class=\"selectpicker form-control\" onchange=\"viewUnitSetChanged()\"\n                        id=\"setSelectView\">\n\n                </select>\n            </span></span>\n\n            <span>\u8282\u70B9\uFF1A <span style=\"display: inline-block;width: 120px\">\n                <select data-live-search=\"true\" class=\"selectpicker form-control\" onchange=\"viewUnitHostChanged()\"\n                        id=\"hostSelectView\">\n\n                </select>\n            </span></span>\n\n            <span>\u670D\u52A1\uFF1A <span style=\"display: inline-block;width: 120px\">\n                <select data-live-search=\"true\" class=\"selectpicker form-control\" onchange=\"viewUnitServiceChanged()\"\n                        id=\"serviceSelectView\">\n\n                </select>\n            </span></span>\n            <span>\u7ED1\u5B9A\u7C7B\u578B\uFF1A <span style=\"display: inline-block;width: 120px\">\n                <select data-live-search=\"true\" class=\"selectpicker form-control\" onchange=\"linkTypeChanged(" + value + ")\"\n                        id=\"linkTypeSelect\">\n                        <option value=\"1\" selected>\u672A\u7ED1\u5B9A</option>\n                        <option value=\"2\">\u5DF2\u7ED1\u5B9A</option>\n                </select>\n            </span></span>\n        </div>\n                \n             <table id=\"deploy-unit-table\"></table>\n                </div>\n               ";
        };
        return ServiceFiles;
    }());
    api.ServiceFiles = ServiceFiles;
})(api || (api = {}));
