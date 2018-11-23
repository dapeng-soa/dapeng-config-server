/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>

module api {
    export class Build {
        add: string = "add";
        view: string = "view";
        edit: string = "edit";

        public exportAddBuildHostContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加节点" : (type == c.edit ? "修改节点" : (type == c.view ? "节点详情" : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">节点名称:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="name" class="col-sm-2 form-control" value="${type != c.add ? data.name : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
节点名只能使用英文,尽量语意清晰。
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">IP地址:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="ip" class="col-sm-2 form-control" value="${type != c.add ? data.host : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="10">${type != c.add ? data.remark : ""}</textarea>
                            </div>
                        </div>
                        ${type == c.add ? `
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveBuildHost()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearBuildHostInput()">清空</button>
                        </span>
                        ` : type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedBuildHost(${data.id})">保存修改</button>
                    </span>
                        ` : ""}
                       
                </div>
            
            `;
        }

        public buildHostActionContext(value: string) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewBuildHostOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewBuildHostOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delBuildHost(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 构建依赖
         * @param depends
         */
        public buildTaskDependsContext(depends: any) {
            let options = "";
            for (let i in depends) {
                options += `<tr>
                    <th>${i}</th>
                    <td><span class="form-control-static">${depends[i].serviceName}</span></td>
                    <input type="hidden" class="build-depend-name" value="${depends[i].serviceName}">
                    <td><input class="form-control build-depend-branch" type="text" value="${depends[i].branchName}"/></td>
                    <input type="hidden" class="build-depend-gitname" value="${depends[i].gitName}">
                    <input type="hidden" class="build-depend-giturl" value="${depends[i].gitURL}" />
                    <input type="hidden" class="build-depend-operation" value="${depends[i].buildOperation}" />
                 </tr>`;
            }
            return options;
        }

        public buildingListContext(records: any) {
            let items = "";

            let size = 0;
            if (records !== null && records !== undefined){
                size = records.length
            }
            for (let i in records) {
                items += `
                <a href="${window.basePath}/build/console/${records[i].id}" class="list-group-item">
                    <p>
                    <i class="fa ${(records[i].status == 0 || records[i].status == 1 || records[i].status == 3) ? "text-danger fa-heart-o" : "text-primary fa-heart"} " aria-hidden="true"></i>
                    <span class="build-number">#${size--}</span> <span class="build-date">${records[i].createdAt}</span>
                    </p>
                    ${(records[i].status == 0 || records[i].status == 1) ? `
                    <div class="progress">
                        <div class="progress-bar progress-bar-striped active" role="progressbar"
                             aria-valuenow="40"
                             aria-valuemin="0" aria-valuemax="100" style="width: 40%;">
                            <span class="sr-only">40%</span>
                        </div>
                    </div>
                    ` : ""}
                </a>    
                `;
            }
            return items;
        }
    }
}