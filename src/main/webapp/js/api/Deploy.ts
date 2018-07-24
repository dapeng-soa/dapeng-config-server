/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/*部署模块ts模版代码*/
module api {
    export class Deploy  {

        add: string = "add";
        view: string = "view";
        edit: string = "edit";

        /**
         * 环境集-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeploySetContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
            let c = this;
           return`
          <div class="panel-header window-header">
                <div class="input-group">
                    <p class="left-panel-title">${type == c.add ? "添加环境集" : (type == c.edit ? "修改环境集" : (type == c.view ? "环境集详情":"" ))}</p>
                </div>
            </div>
            <div class="form-horizontal" style="margin-top: 81px;">
                  <div class="form-group">
                        <label class="col-sm-2 control-label">环境集名称:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type == c.view ? "disabled" : ""} id="name" class="col-sm-2 form-control" value="${type != c.add ? data.name : ""}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ENV:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="10">${type != c.add ? data.remark : ""}</textarea>
                        </div>
                    </div>
                    
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveDeploySet()">保存</button>
                    <button type="button" class="btn btn-danger" onclick="clearDeploySetInput()">清空</button>
                    </span>
                </div>
`;
        }

        /**
         * 服务-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployServiceContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
            let c = this;
            return`
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加服务" : (type == c.edit ? "修改服务" : (type == c.view ? "服务详情":"" ))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                   <div class="form-group">
                            <label class="col-sm-2 control-label">服务名字:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="name" class="col-sm-2 form-control" value="${type != c.add ? data.name : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">服务镜像:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="image" class="col-sm-2 form-control" value="${type != c.add ? data.image : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">标签:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="labels" class="col-sm-2 form-control" value="${type != c.add ? data.labels : ""}">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">VOLUMES:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="volumes-area" class="form-control" rows="10">${type != c.add ? data.volumes : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">PORTS:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="ports-area" class="form-control" rows="10">${type != c.add ? data.ports : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">composeLabels:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="composeLabels-area" class="form-control" rows="10">${type != c.add ? data.composeLabels : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">dockerExtras:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="dockerExtras-area" class="form-control" rows="10">${type != c.add ? data.dockerExtras : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="10">${type != c.add ? data.remark : ""}</textarea>
                            </div>
                        </div>
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveDeployService()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployServiceInput()">清空</button>
                        </span>
                </div>
            
            `;
        }

        /**
         * 节点-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployHostContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
            let c = this;
            return`
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加节点" : (type == c.edit ? "修改节点" : (type == c.view ? "节点详情":"" ))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">节点名称:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="name" class="col-sm-2 form-control" value="${type != c.add ? data.name : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">IP地址:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="ip" class="col-sm-2 form-control" value="${type != c.add ? data.ip : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">标签:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="labels" class="col-sm-2 form-control" value="${type != c.add ? data.labels : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属环境集:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} id="setSelect" class="col-sm-2 form-control">
                                 
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">是否外部机器:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} id="extraSelect" class="col-sm-2 form-control">
                                  <option value="0" ${type != c.add ? (data.extra == 0 ? "selected" : "") : ""}>是</option>
                                  <option value="1" ${type != c.add ? (data.extra == 1 ? "selected" : "") : ""}>否</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="10">${type != c.add ? data.remark : ""}</textarea>
                            </div>
                        </div>
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveDeployHost()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployHostInput()">清空</button>
                        </span>
                </div>
            
            `;
        }

        /**
         * 部署单元-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployUnitContext(type: string = this.add || this.edit || this.view , biz?: string, data?: any){
            let c = this;
            return`
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加部署单元" : (type == c.edit ? "修改部署单元" : (type == c.view ? "部署单元详情":"" ))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">发布TAG:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="gitTag" class="col-sm-2 form-control" value="${type != c.add ? data.gitTag : ""}">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属环境集:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} id="setSelect" onchange="addUnitSetChanged(this)" class="col-sm-2 form-control">
          
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属主机:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} onchange="addUnitHostChanged(this)" id="hostSelect" class="col-sm-2 form-control">
                                </select>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属服务:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} onchange="addUnitServiceChanged(this)" id="serviceSelect" class="col-sm-2 form-control">
                                </select>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">镜像TAG:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="imageTag" class="col-sm-2 form-control" value="${type != c.add ? data.imageTag : ""}">
                            </div>
                        </div>
                       
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea disabled id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                            </div>
                        </div>
                      
                  
                        <div class="form-group">
                            <label class="col-sm-2 control-label">VOLUMES:</label>
                            <div class="col-sm-9">
                                <textarea disabled id="volumes-area" class="form-control" rows="10">${type != c.add ? data.volumes : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">PORTS:</label>
                            <div class="col-sm-9">
                                <textarea disabled id="ports-area" class="form-control" rows="10">${type != c.add ? data.ports : ""}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">dockerExtras:</label>
                            <div class="col-sm-9">
                                <textarea disabled id="dockerExtras-area" class="form-control" rows="10">${type != c.add ? data.dockerExtras : ""}</textarea>
                            </div>
                        </div>
                        
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveDeployUnit()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployUnitInput()">清空</button>
                        </span>
                </div>
            
            `;
        }


        /**
         * 环境集操作栏
         * @param value
         * @param row
         */
        public exportDeploySetActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeploySetEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeploySetEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeploySet(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 节点操作栏
         * @param value
         * @param row
         */
        public exportDeployHostActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployHostOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployHostOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployHost(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 服务操作栏
         * @param value
         * @param row
         */
        public exportDeployServiceActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployServiceOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployServiceOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployService(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 部署单元操作栏
         * @param value
         * @param row
         */
        public exportDeployUnitActionContext(value, row){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployUnitOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployUnitOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployUnit(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }
    }
}