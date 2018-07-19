/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/*部署模块ts模版代码*/
var api;
(function (api) {
    var Deploy = /** @class */ (function () {
        function Deploy() {
            this.add = "add";
            this.view = "view";
            this.edit = "edit";
        }
        /**
         * 环境集-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeploySetContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            return "\n           <p>\u73AF\u5883\u96C6</p>\n            \n            ";
        };
        /**
         * 服务-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployServiceContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            return "\n           <p>\u670D\u52A1</p>\n            \n            ";
        };
        /**
         * 节点-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployHostContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            return "\n           <p>\u8282\u70B9</p>\n            \n            ";
        };
        /**
         * 部署单元-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        Deploy.prototype.exportAddDeployUnitContext = function (type, biz, data) {
            if (type === void 0) { type = this.add || this.edit || this.view; }
            return "\n           <p>\u90E8\u7F72\u5355\u5143</p>\n            \n            ";
        };
        /**
         * 环境集操作栏
         * @param value
         * @param row
         */
        Deploy.prototype.exportDeploySetActionContext = function (value, row) {
            return "<span class=\"link-button-table\">\n            <a href=\"javascript:void(0)\" title=\"\u8BE6\u60C5\"  onclick=\"viewDeploySetEditByID(" + value + ",'view')\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u4FEE\u6539\"  onclick=\"viewDeploySetEditByID(" + value + ",'edit')\"><span class=\"glyphicon glyphicon-edit\"></span></a>\n            <a href=\"javascript:void(0)\" title=\"\u5220\u9664\"  onclick=\"delDeploySet(" + value + ")\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n            </span>";
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
        return Deploy;
    }());
    api.Deploy = Deploy;
})(api || (api = {}));
