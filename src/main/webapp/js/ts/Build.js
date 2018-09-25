/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>
var api;
(function (api) {
    var Build = /** @class */ (function () {
        function Build() {
            this.add = "add";
            this.view = "view";
            this.edit = "edit";
        }
        Build.prototype.exportAddBuildHostContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            var c = this;
            return "\n           <div class=\"panel-header window-header\">\n                    <div class=\"input-group\">\n                        <p class=\"left-panel-title\">" + (type == c.add ? "添加节点" : (type == c.edit ? "修改节点" : (type == c.view ? "节点详情" : ""))) + "</p>\n                    </div>\n                </div>\n                <div class=\"form-horizontal\" style=\"margin-top: 81px;\">\n                    <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u8282\u70B9\u540D\u79F0:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"name\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.name : "") + "\">\n                                <div class=\"advance-format-item\">\n                                <p class=\"advance-format-title\" onclick=\"toggleBlock(this)\" ><span class=\"glyphicon glyphicon-question-sign\"></span></p>\n                                <div class=\"advance-format-content\">\n                                  <pre>\n\u5907\u6CE8:\n\u8282\u70B9\u540D\u53EA\u80FD\u4F7F\u7528\u82F1\u6587,\u5C3D\u91CF\u8BED\u610F\u6E05\u6670\u3002\n                                 </pre>\n                                </div>\n                              </div>\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">IP\u5730\u5740:</label>\n                            <div class=\"col-sm-9\">\n                                <input type=\"text\" " + (type == c.view ? "disabled" : "") + " id=\"ip\" class=\"col-sm-2 form-control\" value=\"" + (type != c.add ? data.host : "") + "\">\n                            </div>\n                        </div>\n                        <div class=\"form-group\">\n                            <label class=\"col-sm-2 control-label\">\u5907\u6CE8:</label>\n                            <div class=\"col-sm-9\">\n                                <textarea " + (type == c.view ? "disabled" : "") + " id=\"remark-area\" class=\"form-control\" rows=\"10\">" + (type != c.add ? data.remark : "") + "</textarea>\n                            </div>\n                        </div>\n                        " + (type == c.add ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                        <button type=\"button\" class=\"btn btn-success\" onclick=\"saveBuildHost()\">\u4FDD\u5B58</button>\n                        <button type=\"button\" class=\"btn btn-danger\" onclick=\"clearBuildHostInput()\">\u6E05\u7A7A</button>\n                        </span>\n                        " : type == c.edit ? "\n                        <span class=\"input-group-btn panel-button-group text-center\">\n                    <button type=\"button\" class=\"btn btn-success\" onclick=\"editedBuildHost(" + data.id + ")\">\u4FDD\u5B58\u4FEE\u6539</button>\n                    </span>\n                        " : "") + "\n                       \n                </div>\n            \n            ";
        };
        Build.prototype.buildHostActionContext = function (value) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewBuildHostOrEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewBuildHostOrEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delBuildHost(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
        };
        /**
         * 构建依赖
         * @param depends
         */
        Build.prototype.buildTaskDependsContext = function (depends) {
            var options = "";
            for (var i in depends) {
                options += "<tr>\n                    <th>" + i + "</th>\n                    <td><span class=\"buildDependServiceName\">" + depends[i].serviceName + "</span></td>\n                    <td><input class=\"form-control buildDependBranch\" type=\"text\" value=\"" + depends[i].branchName + "\"/></td>\n                 </tr>";
            }
            return options;
        };
        return Build;
    }());
    api.Build = Build;
})(api || (api = {}));
