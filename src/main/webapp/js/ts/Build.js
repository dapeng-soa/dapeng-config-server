/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>
var api;
(function (api) {
    var Build = /** @class */ (function () {
        function Build() {
        }
        Build.prototype.buildTasksContext = function (tasks) {
            var html = "";
            for (var i in tasks) {
                var task = tasks[i];
                var dependsHtml = "";
                for (var n in task.depends) {
                    var depend = task.depends[n];
                    dependsHtml += "\n                    <li>\n                        <span>" + depend.serviceName + "</span> =>\n                        <span>" + depend.branchName + "</span>\n                    </li>\n                    ";
                }
                html += "\n                <tr>\n                            <th scope=\"row\">" + ++i + "</th>\n                            <td>" + task.setName + "</td>\n                            <td>" + task.hostName + "</td>\n                            <td ><a href=\"javascript:void(0)\" onclick=\"getTaskBuildList(" + task.id + ")\">" + task.serviceName + "</a></td>\n                            <td>" + task.deployHostName + "</td>\n                            <td>\n                                <div class=\"advance-format-item\">\n                                    <p class=\"advance-format-title\"\n                                       onclick=\"toggleBlock(this)\">" + task.branch + "</p>\n                                    <div class=\"advance-format-content\">\n                                        <ul>\n                                               " + dependsHtml + "\n                                        </ul>\n                                    </div>\n                                </div>\n                            </td>\n                            <td>" + task.updatedAt + "</td>\n                            <td><a href=\"javascript:void(0)\" title=\"\u5F00\u59CB\u6784\u5EFA\"\n                                   onclick=\"execBuildService(" + task.id + ")\"><i\n                                    class=\"fa fa-play-circle\" aria-hidden=\"true\"></i></a>\n                            </td>\n                        </tr>\n                ";
            }
            return html;
        };
        Build.prototype.buildingListContext = function (records) {
            var items = "";
            var size = 0;
            if (records !== null && records !== undefined) {
                size = records.length;
            }
            for (var i in records) {
                items += "\n                <a href=\"" + window.basePath + "/build/console/" + records[i].id + "\" class=\"list-group-item\">\n                    <p>\n                    <i class=\"fa " + ((records[i].status == 0 || records[i].status == 1) ? "text-primary fa-meh-o" : records[i].status == 3 ? "text-danger fa-frown-o" : "text-primary fa-smile-o") + " \" aria-hidden=\"true\"></i>\n                    <span class=\"build-number\">#" + size-- + "-" + records[i].buildService + "</span> <span class=\"build-date\">" + records[i].createdAt + "</span>\n                    </p>\n                    " + ((records[i].status == 0 || records[i].status == 1) ? "\n                    <div class=\"progress\">\n                        <div class=\"progress-bar progress-bar-striped active\" role=\"progressbar\"\n                             aria-valuenow=\"40\"\n                             aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 40%;\">\n                            <span class=\"sr-only\">40%</span>\n                        </div>\n                    </div>\n                    " : "") + "\n                </a>    \n                ";
            }
            return items;
        };
        return Build;
    }());
    api.Build = Build;
})(api || (api = {}));
