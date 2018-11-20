/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>
/// <reference path="./Mapper.ts"/>
/// <reference path="../../plugins/init.js"/>
/*部署模块ts模版代码*/
module api {

    export class Deploy {

        add: string = "add";
        view: string = "view";
        edit: string = "edit";
        api = new api.Api();
        serviceView: Number = 1;
        hostView: Number = 2;

        /**
         * 环境集-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeploySetContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
          <div class="panel-header window-header">
                <div class="input-group">
                    <p class="left-panel-title">${type == c.add ? "添加环境集" : (type == c.edit ? "修改环境集" : (type == c.view ? "环境集详情" : ""))}</p>
                </div>
            </div>
            <div class="form-horizontal" style="margin-top: 81px;">
                 ${type == c.add ? `
                 <div class="form-group">
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-9">
                            <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><i class="fa fa-files-o" aria-hidden="true"></i>点此可复制一个已存在的环境集</p>
                                <div class="advance-format-content">
                                <div class="row">
                                <div class="col-sm-10"><select class="form-control" id="addCopySetSelect" style="width:100%"></select></div>
                                <div class="col-sm-2"><button style="width: 100%" type="button" class="btn btn-info" onclick="copySetChange()">复制</button></div>
                                </div>
                                <code>选择一个已有的环境集,并点击复制,不要忘记修改环境集的名称和对应的ENV！</code>
                                </div>
                              </div>
                        </div>
                    </div>
                 ` : ``}
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
                            <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
ENV(环境变量):
作用:提供应用所需的环境变量
书写格式:
appName=goodsService(建议) 
appName:goodsService
备注:
尽量将公共环境变量配置在此处
                                  </pre>
                                </div>
                              </div>
                        </div>
                    </div>
                    
                    <!--<div class="form-group">
                        <label class="col-sm-2 control-label">NetWork-Mtu:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type == c.view ? "disabled" : ""} id="networkMtu" placeholder="defualt:1500" class="col-sm-2 form-control" value="${type != c.add ? data.networkMtu : ""}">
                        </div>
                    </div> -->
                   
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="10">${type != c.add ? data.remark : ""}</textarea>
                        </div>
                    </div>
                   
                    ${type == c.add ? `
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveDeploySet()">保存</button>
                    <button type="button" class="btn btn-danger" onclick="clearDeploySetInput()">清空</button>
                    </span>
                    ` : type == c.edit ? `
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedDeploySet(${data.id})">保存修改</button>
                    </span>
                    ` : ""}
                </div>
`;
        }


        public exportAddSubEnvBySetIdContext(type: string, setId: number, subEnv?: any) {
            let c = this;
            return `
            <div class="panel-header window-header">
                <div class="input-group">
                    <p class="left-panel-title">环境集服务ENV配置(SetSubEnv)</p>
                </div>
            </div>
            <div class="form-horizontal" style="margin-top: 81px;">
            
                 <div class="form-group">
                        <label class="col-sm-1 control-label"></label>
                        <div class="col-sm-10">
                            <div id="sub-from-container">
                            ${c.viewSubEnv("view", subEnv)}
                            </div>
                            ${type !== c.view ? `
                        <div style="margin-top: 10px" class="icon-add"><a href="#" onclick="addSubFromBySet()"><span class="glyphicon glyphicon-plus"></span></a>点击新增配置</div>
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveSubEnvs(${setId})">保存</button>
                    </span>
                    ` : ""}
           </div>
           </div>
           </div>               
`
        }

        /**
         * 列表
         */
        public exportAgentsContext() {
            return `
            <div class="panel-header window-header">
                <div class="input-group">
                    <p class="left-panel-title">已注册Agent</p>
                </div>
            </div>
            <div class="form-horizontal" style="margin-top: 81px;">
              <ul class="list-group" id="agentList">
                  
               </ul>
            </div>
            `
        }

        public exportAgentList(agents: any) {
            let html = "";
            for (let i in agents) {
                let agent = agents[i];
                html += `<li class="list-group-item">
                    <span class="badge btn-success">${agent.name}</span>
                    <code>sessionId:[${agent.sessionId}]  ip:[${agent.ip}]</code>
                  </li>`;
            }
            return html;
        }

        public viewSubEnv(type: string = this.add || this.edit || this.view, subEnvs: any) {
            let c = this;
            const url = basePath + "/api/deploy-services";
            $.get(url, res => {
                let ops = res.context.content;
                let view = "";
                for (let index in subEnvs) {
                    let s = subEnvs[index];
                    view += c.exportAddSubEnvContext(type, ops, s);
                }
                $("#sub-from-container").append(view);
            }, "json");
            return "";
        }

        public exportAddSubEnvContext(type: string = this.add || this.edit || this.view, services: any, subEnv?: any) {
            let ops = `<option value="0">请选择服务</option>`;
            let sId = 0;
            let env = {};
            if (type !== this.add) {
                sId = subEnv.serviceId;
                env = subEnv.env;
            }
            for (let index in services) {
                let s = services[index];
                let sed = "";
                if (s.id === sId) {
                    sed = "selected"
                }
                ops += `<option  ${sed} value="${s.id}">${s.name}</option>`
            }
            return `
                <div class="form-horizontal from-group-item" style="margin-top: 20px;">
                    ${type !== this.add ? "" : `<a class="from-group-item-rm" href="javascript:void(0)"><span class="glyphicon glyphicon-remove"></span></a>`}
                    <input type="hidden" class="data-ops-id" value="${subEnv === undefined ? 0 : subEnv.id}">
                    <div class="form-group">
                        <div class="col-sm-12">
                            <select  class="form-control data-service-select" ${type !== this.add ? "disabled" : ""}>${ops}</select>
                        </div>
                    </div>   
                    <div class="form-group">
                        <div class="col-sm-12">
                            <textarea class="form-control data-env-textarea"  >${type === this.add ? "" : env}</textarea>
                        </div>
                    </div> 
                </div>
            `;
        }

        /**
         * 服务-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployServiceContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加服务" : (type == c.edit ? "修改服务" : (type == c.view ? "服务详情" : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                 ${type == c.add ? `
                  <div class="form-group">
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-9">
                            <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><i class="fa fa-files-o" aria-hidden="true"></i>点此可复制一个已存在的服务</p>
                                <div class="advance-format-content">
                                <div class="row">
                                <div class="col-sm-10"><select class="form-control" id="addCopyServiceSelect" style="width:100%"></select></div>
                                <div class="col-sm-2"><button style="width: 100%" type="button" class="btn btn-info" onclick="copyServiceChange()">复制</button></div>
                                </div>
                                <code>选择一个已有的服务,并点击复制,不要忘记修改新加的服务信息！</code>
                                </div>
                              </div>
                        </div>
                    </div> 
                 ` : ``}
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
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
服务启动的镜像名
千万注意,不要填写镜像TAG,镜像TAG应当是在部署单元中新增部署时填写
写法示例:
dapengsoa/redis-wzx(正确)
dapengsoa/redis-wzx:3.2.3.1(错误,无需填写镜像TAG)
                                  </pre>
                                </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">标签:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} data-role="tagsinput" id="labels" class="col-sm-2 form-control" value="${type != c.add ? data.labels : ""}">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
ENV(环境变量):
作用:提供应用所需的环境变量
书写格式:
appName=goodsService(建议) 
appName:goodsService
备注:
服务的环境变量最好是只针对当前服务所需要的配置即可，公共配置可配置到环境集
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">VOLUMES:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="volumes-area" class="form-control" rows="10">${type != c.add ? data.volumes : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
VOLUMES(挂载卷):
作用:应用容器与宿主机的目录映射
书写格式:
宿主机目录:容器目录
/data/log:/data/log
备注:
服务的卷挂载最好是只针对当前服务所需要的配置即可，公共配置可配置到环境集
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">PORTS:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="ports-area" class="form-control" rows="10">${type != c.add ? data.ports : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
PORTS(映射端口):
作用:应用容器与宿主机的端口映射
书写格式:
宿主机端口:容器端口
8080:8080
9999:9999
备注:
尽量在服务中明确端口的映射
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">composeLabels:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="composeLabels-area" class="form-control" rows="10">${type != c.add ? data.composeLabels : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
docker-compsoe的labels配置:
书写格式:
project.owner=struy

                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">dockerExtras:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="dockerExtras-area" class="form-control" rows="10">${type != c.add ? data.dockerExtras : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
附加的docker-compose配置,在service下级,与environment同级:
书写格式(暂不支持多行配置):
restart: on-failure:3
                                  </pre>
                                </div>
                              </div>
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
                        <button type="button" class="btn btn-success" onclick="saveDeployService()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployServiceInput()">清空</button>
                        </span>
                         ` : type == c.edit ? `
                         <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedDeployService(${data.id})">保存修改</button>
                    </span>
                         ` : ""}
                </div>
            
            `;
        }

        /**
         * 节点-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployHostContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
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
当服务运行时,所有的节点ip都会写入容器hosts
如:
host1 192.168.0.666
                                 </pre>
                                </div>
                              </div>
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
                                <input type="text" ${type == c.view ? "disabled" : ""} data-role="tagsinput" id="labels" class="col-sm-2 form-control" value="${type != c.add ? data.labels : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
可用于筛选的标签
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属环境集:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} id="setSelect" class="col-sm-2 form-control">
                                 
                                </select>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
如果选择了某个环境集，则当前添加主机隶属于所选环境集
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">是否外部机器:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view ? "disabled" : ""} id="extraSelect" class="col-sm-2 form-control">
                                  <option value="0" ${type != c.add ? (data.extra == 0 ? "selected" : "") : ""}>是</option>
                                  <option value="1" ${type != c.add ? (data.extra == 1 ? "selected" : "") : "selected"}>否</option>
                                </select>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
内部机器用于部署业务服务
外部机器用于部署类似数据库,中间件等外部服务
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
ENV(环境变量):
作用:提供应用所需的环境变量
书写格式:
appName=goodsService(建议) 
appName:goodsService
备注:
1.节点环境变量应当尽量不填写，除非长期的在某个节点存在特有配置
                                 </pre>
                                </div>
                              </div>
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
                        <button type="button" class="btn btn-success" onclick="saveDeployHost()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployHostInput()">清空</button>
                        </span>
                        ` : type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedDeployHost(${data.id})">保存修改</button>
                    </span>
                        ` : ""}
                       
                </div>
            
            `;
        }

        /**
         * 部署单元-导出添加/修改/详情模版
         * @param {string} type
         * @param {string} biz
         * @param data
         */
        public exportAddDeployUnitContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加部署单元" : (type == c.edit ? "修改部署单元" : (type == c.view ? "部署单元详情" : ""))}</p>
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
                               <select ${type == c.view || type == c.edit ? "disabled" : ""} id="setSelect" onchange="addUnitSetChanged(this)" class="col-sm-2 form-control ">
          
                                </select>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre id="setEnvView">

                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属主机:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view || type == c.edit ? "disabled" : ""} onchange="addUnitHostChanged(this)" id="hostSelect" class="col-sm-2 form-control">
                                </select>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre id="hostEnvView">

                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">所属服务:</label>
                            <div class="col-sm-9">
                               <select ${type == c.view || type == c.edit ? "disabled" : ""} onchange="addUnitServiceChanged(this)" id="serviceSelect" class="col-sm-2 form-control">
                                </select>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre id="serviceEnvView">

                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">容器名(可选):</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="containerName" class="col-sm-2 form-control" value="${type != c.add ? data.containerName : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
可选的容器名,如果不填写则默认为所选服务名,选择服务之后会自动填充容器名
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">镜像TAG:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="imageTag" class="col-sm-2 form-control" value="${type != c.add ? data.imageTag : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
本次部署的服务镜像tag,无需填写镜像名
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                       
                        <div class="form-group">
                            <label class="col-sm-2 control-label">ENV:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="env-area" class="form-control" rows="10">${type != c.add ? data.env : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
ENV(环境变量):
作用:提供应用所需的环境变量
书写格式:
appName=goodsService(建议) 
appName:goodsService
备注:
部署单元的env变量应当只有在少数情况下需要添加,如某台节点需要特殊的配置
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                      
                  
                        <div class="form-group">
                            <label class="col-sm-2 control-label">VOLUMES:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="volumes-area" class="form-control" rows="10">${type != c.add ? data.volumes : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
VOLUMES(挂载卷):
作用:应用容器与宿主机的目录映射
书写格式:
宿主机目录:容器内目录
/data/log:/data/log
备注:
部署单元的VOLUMES只有在极少数情况下需要添加,如某台节点需要特殊的配置
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">PORTS:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="ports-area" class="form-control" rows="10">${type != c.add ? data.ports : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
PORTS(映射端口):
作用:应用容器与宿主机的端口映射
书写格式:
宿主机端口:容器端口
8080:8080
备注:
部署单元的PORTS只有在极少数情况下需要添加,如某台节点需要特殊的配置
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">dockerExtras:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="dockerExtras-area" class="form-control" rows="10">${type != c.add ? data.dockerExtras : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
附加的docker-compose配置,在service下级,与env同级:
书写格式(暂不支持多行配置):
restart: on-failure:3
备注:
建议不要在此处添加此项配置
                                  </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        ${type == c.add ? `
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveDeployUnit()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearDeployUnitInput()">清空</button>
                        </span>
                        ` : type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedDeployUnit(${data.id})">保存修改</button>
                    </span>
                        ` : ""}
                </div>
            
            `;
        }


        /**
         * 环境集操作栏
         * @param value
         * @param row
         */
        public exportDeploySetActionContext(value, row) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeploySetEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeploySetEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="管理服务环境变量"  onclick="openAddSubEnvBySetId(${value},'add')"><span class="glyphicon glyphicon-folder-close"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeploySet(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 节点操作栏
         * @param value
         * @param row
         */
        public exportDeployHostActionContext(value, row) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployHostOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployHostOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployHost(${value},'${row.name}')"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 服务操作栏
         * @param value
         * @param row
         */
        public exportDeployServiceActionContext(value, row) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployServiceOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployServiceOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployService(${value},'${row.name}')"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 部署单元操作栏
         * @param value
         * @param row
         */
        public exportDeployUnitActionContext(value, row) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewDeployUnitOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewDeployUnitOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delDeployUnit(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 流水操作状态
         * @param value
         * @param row
         * @returns {string}
         */
        public exportDeployJournalFlagContext(value, row) {
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
        }


        public exportModifyBatchTagContent(publishId, eid) {
            return `
            <div style="padding: 0 5px">
                <div class="form-group" style="margin-top: 20px">
                        <label for="${publishId}">发布Tag:</label>
                        <input type="text"  id="${publishId}" class="form-control" />
                 </div>
                <div class="form-group">
                        <label for="${eid}">镜像Tag:</label>
                        <input type="text"  id="${eid}" class="form-control" />
                 </div>
                 </div>
            `
        }

        /**
         * 查看yml
         * @param value
         * @param row
         * @returns {string}
         */
        public exportDeployJournalYmlContext(value, row) {
            return `<span class="link-button-table">
 ${row.opFlag === 1 ? `<a href="javascript:void(0)" title="yml"  onclick="viewDeployJournalYml(${row.id},${row.unitId})"><span class="glyphicon glyphicon-eye-open"></span></a>` : `-`}
</span>
`
        }

        /**
         * 流水操作
         * @param value
         * @param row
         */
        public exportDeployJournalActionContext(value, row) {
            return `<span class="link-button-table">
            ${row.opFlag === 1 ? `<a href="javascript:void(0)" title="回滚"  onclick="rollbackDeploy(${value},'${row.hostName}','${row.serviceName}')"><i class="fa fa-reply-all" aria-hidden="true"></i></a>` : `-`}
            </span>`;
        }

        public exportViewDeployJournalContext() {

            let c = this;
            return `
                <div class="diff-tit" >
                <span>运行配置(只读)</span>
                <span>当前版本(只读)</span>
                </div>
                <div id="mergely" style="margin:20px 0;">
                </div>
                <div class="fixed-footer-btn" >
                </div>
            `
        }

        /**
         * 服务/主机视图
         */
        public deployViewChange(viewType: number, data: any) {
            let dep = this;
            let view = "";
            for (let em of data) {
                view += `
            <div class="col-sm-6 col-xs-12">
                <div class="panel panel-default panel-box">
                    <div class="panel-heading" style="background: #fff"><p style="text-align: center;font-size: 18px">${viewType == dep.serviceView ? em.serviceName : em.hostName + ':[' + em.hostIp + ']'}</p>
                    </div>
                    <div class="panel-body" style="overflow-y: auto;height:320px">
                         ${dep.serviceViewSubHost(viewType, viewType == dep.serviceView ? em.deploySubHostVos : em.deploySubServiceVos, em)}
                    </div>
                </div>
            </div>
            `
            }
            return view;
        }

        private serviceViewSubHost(viewType: number, sub: any, obj) {
            let dep = this;
            let subView = "";
            for (let em of sub) {
                let IdPrefix = viewType == dep.serviceView ? em.hostIp + em.containerName : obj.hostIp + em.containerName;
                subView += `<div class="row" style="border-bottom: 1px solid gainsboro;padding: 10px 0;">
                            <div class="col-sm-4 col-xs-12">
                                <p style="font-size: 18px;word-wrap: break-word;">${viewType == dep.serviceView ? em.hostName : em.serviceName}</p>
                                ${viewType == dep.serviceView ? `<p >${em.hostIp}</p>` : ""}
                                <p>容器名：<span id="${IdPrefix}-ContainerName">${em.containerName}</span></p>
                                <p>Tag：<span id="${IdPrefix}-ImageTag">none</span></p>
                            </div>
                            <div class="col-sm-6 col-xs-12">
                                <p>配置更新时间：<span id="${IdPrefix}-configUpdateTime" data-real-configUpdateBy="${em.configUpdateBy}">${dep.unix2Time(em.configUpdateBy)}</span></p>
                                <p>主机服务时间：<span id="${IdPrefix}-deployTime">${em.deployTime}</span></p>
                                <p>服务状态：<span id="${IdPrefix}-serviceStatus">${dep.realStatus(em.serviceStatus)}</span></p>
                                <p>需要更新：<span id="${IdPrefix}-needUpdate">${dep.updateStatus(em.needUpdate)}</span></p>
                                </div>
                                <div class="col-sm-2 col-xs-12">
                                    <p ><a href="javascript:void(0)" style="color: #1E9FFF" onclick="serviceYamlPreview('${IdPrefix + "-deployTime"}','${IdPrefix + "-configUpdateTime"}',${em.unitId})">升级</a></p>
                                <p ><a href="javascript:void(0)" style="color: #1E9FFF" onclick="stopService(${em.unitId})">停止</a></p>
                                <p ><a href="javascript:void(0)" style="color: #1E9FFF" onclick="restartService(${em.unitId})">重启</a></p>
                                <p ><a href="javascript:void(0)" style="color: #1E9FFF" onclick="serviceYamlPreview('${IdPrefix + "-deployTime"}','${IdPrefix + "-configUpdateTime"}',${em.unitId},'view')">预览</a></p>
                            </div>
                        </div>
            `
            }

            return subView
        }


        /**
         * 根据事件返回处理
         * @param realInfo
         */
        public processServiceStatus(realInfo: ServiceInfoRepose) {
            let t = this;
            // 构造domID
            let configUpdateId = t.el(`${realInfo.ip + realInfo.serviceName}-configUpdateTime`);
            let deployTimeId = t.el(`${realInfo.ip + realInfo.serviceName}-deployTime`);
            let serviceStatusId = t.el(`${realInfo.ip + realInfo.serviceName}-serviceStatus`);
            let needUpdateId = t.el(`${realInfo.ip + realInfo.serviceName}-needUpdate`);
            let imageTag = t.el(`${realInfo.ip + realInfo.serviceName}-ImageTag`);

            if (configUpdateId != null && deployTimeId != null && serviceStatusId != null && needUpdateId != null) {
                let realConfigupdateby = Number(configUpdateId.dataset.realConfigupdateby);
                let updateStatus = realInfo.time < realConfigupdateby;
                deployTimeId.innerHTML = t.unix2Time(realInfo.time);
                serviceStatusId.innerHTML = t.realStatus(realInfo.status ? 1 : 2);
                needUpdateId.innerHTML = t.updateStatus(updateStatus);
                imageTag.innerHTML = realInfo.tag
            }
        }

        /**
         * 服务状态
         * @param {Number} status
         * @returns {string}
         */
        private realStatus(status: Number) {
            switch (status) {
                case 1:
                    return `<span style="color: #00AA00"><i class="fa fa-cog icon-spin" aria-hidden="true"></i>运行</span>`;
                case 2:
                    return `<span style="color:#ff4d4d"><i class="fa fa-pause-circle" aria-hidden="true"></i>停止</span>`;
                default:
                    return `<span style="color:#ffd248"><i class="fa fa-pause-circle" aria-hidden="true"></i>未知</span>`;
            }
        }

        /**
         * 更新状态
         * @param {boolean} b
         */
        private updateStatus(b: boolean) {
            if (b) {
                return `<span style="color: #ff4d4d">是</span>`;
            } else {
                return `<span style="color: #00AA00">否</span>`;
            }
        }

        private el(ementId: string): HTMLElement | null {
            return document.getElementById(ementId);
        }


        /**
         * 预览yaml
         * @returns {string}
         */
        public viewDeployYamlContext(deployTime: string, updateTime: string, unitId: Number, type?: string) {
            let c = this;
            let realDeployTime = c.el(`${deployTime}`).innerHTML;
            let realUpdateTime = c.el(`${updateTime}`).innerHTML;
            return `
                <div class="diff-tit" >
                <span>运行配置(只读)[${realDeployTime}]</span>
                <span>当前版本(只读)[${realUpdateTime}]</span>
                </div>
                <div id="mergely" style="margin:30px 0;">
                </div>
                <div class="fixed-footer-btn" >
                <span class="input-group-btn panel-button-group text-center">
                ${type == undefined || type != c.view ? `
                        <button type="button" class="btn btn-success" onclick="execServiceUpdate(${unitId})">确认升级</button>
                        <button type="button" class="btn btn-danger" onclick="cancelServiceUpdate()">取消升级</button>
                        ` : ""}
                <button type="button" class="btn btn-info" onclick="downloadYaml(${unitId})">下载当前配置</button>
                 </span>
                </div>
            `

        }

        /*
        服务配置预览
         */
        public serviceCnfigView(data: any) {
            return `
<span style="color: #aa0e0e">[${data.name}配置:]</span>

<span style="color: #aa0e0e">[env]</span>
${data.env}
<span style="color: #aa0e0e">[image]</span>
${data.image}
<span style="color: #aa0e0e">[ports]</span>
${data.ports}
<span style="color: #aa0e0e">[volumes]</span>
${data.volumes}
<span style="color: #aa0e0e">[dockerExtras]</span>
${data.dockerExtras}
<span style="color: #aa0e0e">[composeLabels]</span>
${data.composeLabels}
<span style="color: #aa0e0e">[labels]</span>
${data.labels}
<span style="color: #aa0e0e">[remark]</span>
${data.remark}
            `
        }

        /*
       环境集配置预览
        */
        public setCnfigView(data: any) {
            return `
<span style="color: #aa0e0e">[${data.name}配置:]</span>

<span style="color: #aa0e0e">[env]</span>
${data.env}
<span style="color: #aa0e0e">[remark]</span>
${data.remark}
            `
        }

        /*
主机配置预览
*/
        public hostCnfigView(data: any) {
            return `
<span style="color: #aa0e0e">[${data.name}配置:]</span>

<span style="color: #aa0e0e">[ip]</span>
${data.ip}
<span style="color: #aa0e0e">[env]</span>
${data.env}
<span style="color: #aa0e0e">[remark]</span>
${data.remark}
<span style="color: #aa0e0e">[是否外部机器]</span>
${data.extra == 1 ? '否' : '是'}
            `
        }

        public unix2Time(unix: any) {
            let unixTimestamp = new Date(unix * 1000);
            return unixTimestamp.pattern("yyyy-MM-dd hh:mm:ss");
        }
    }

    export class ServiceFiles {
        add: string = "add";
        view: string = "view";
        edit: string = "edit";
        api = new api.Api();

        public exportAddServiceFilesContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加文件" : (type == c.edit ? "修改文件" : (type == c.view ? "文件详情" : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">容器内文件名:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="fileName" class="col-sm-2 form-control" value="${type != c.add ? data.fileName : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
容器内的映射文件/文件夹名(包含路径[如：/data/config/config.ini | /data/config/])
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">外部映射文件名:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="fileExtName" class="col-sm-2 form-control" value="${type != c.add ? data.fileExtName : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
宿主机的映射文件/文件夹名(文件夹应当写全路径/文件只写文件名)
文件夹:/data/logs
文件:/data/var/config.ini
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">文件内容:</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="fileContext" class="form-control" rows="10">${type != c.add ? data.fileContext : ""}</textarea>
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
                        <button type="button" class="btn btn-success" onclick="saveServiceFile()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearServiceFileInput()">清空</button>
                        </span>
                        ` : type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedServiceFile(${data.id})">保存修改</button>
                    </span>
                        ` : ""}
                       
                </div>
            
            `;
        }

        /**
         * 文件操作
         * @param value
         */
        public exportServiceFileAction(value: string, name: string) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewServiceFilesOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewServiceFilesOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="关联部署单元"  onclick="openLinkDeployUnits(${value},'${name}')"><i class="fa fa-link" aria-hidden="true"></i></a>
            <a href="javascript:void(0)" title="删除"  onclick="delServiceFiles(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        /**
         * 文件关联部署单元
         * @param vaue
         */
        public exportFileLinkUnitContext(value: string, name: string) {
            return `
             <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">关联部署单元<small>[${name}]</small></p>
                        <span class="input-group-btn panel-button-group">
                            <button type="button" class="btn btn-info"  id="linkButton" onclick="linkDeployUnits(${value})">一键关联</button>
                            <button type="button" class="btn btn-danger" id="unLinkButton" onclick="unLinkDeployUnits(${value})">一键解绑</button>
                        </span>
                    </div>
                </div>
                <div style="margin-top: 81px;">
                <input type="hidden" value="${value}" id="currFileId">
                 <div id="deploy-unit-tableToolbar">
            <span>环境集： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitSetChanged()"
                        id="setSelectView">

                </select>
            </span></span>

            <span>节点： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitHostChanged()"
                        id="hostSelectView">

                </select>
            </span></span>

            <span>服务： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitServiceChanged()"
                        id="serviceSelectView">

                </select>
            </span></span>
            <span>绑定类型： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="linkTypeChanged(${value})"
                        id="linkTypeSelect">
                        <option value="1" selected>未绑定</option>
                        <option value="2">已绑定</option>
                </select>
            </span></span>
        </div>
                
             <table id="deploy-unit-table"></table>
                </div>
               `
        }
    }

    export class System {
        add: string = "add";
        view: string = "view";
        edit: string = "edit";
        api = new api.Api();

        /**
         * 部署单元操作栏
         * @param value
         * @param row
         */
        public exportSystemUsersActionContext(value, row) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="修改"  onclick="viewSystemUserOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="关联角色"  onclick="openLinkRole(${value},'${row.username}')"><i class="fa fa-user-circle-o" aria-hidden="true"></i></a>
            <a href="javascript:void(0)" title="重置密码"  onclick="resetUserPwd(${value},'${row.username}')"><i class="fa fa-unlock-alt" aria-hidden="true"></i></a>
            <a href="javascript:void(0)" title="${row.enabled == 0 ? `禁用此账号` : `启用此账号`}"  onclick="disabledSystemUser(${value},'${row.username}',${row.enabled})">${row.enabled == 0 ? `<i class="fa fa-toggle-on" aria-hidden="true"></i>` : `<i class="fa fa-toggle-off" aria-hidden="true"></i>`}</a>
                    </span>`;
        }

        public exportAddSystemUserContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加账户" : (type == c.edit ? "修改账户" : (type == c.view ? "账户详情" : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                   <div class="form-group">
                            <label class="col-sm-2 control-label">用户名:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="username" class="col-sm-2 form-control" value="${type != c.add ? data.username : ""}">
                                ${type == c.edit ? `<code>注意:修改用户名,用户密码将会被置为当前修改的用户名!</code>` : ""}
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">昵称:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="nickname" class="col-sm-2 form-control" value="${type != c.add ? data.nickname : ""}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">邮箱:</label>
                            <div class="col-sm-9">
                                <input type="email" ${type == c.view ? "disabled" : ""} id="email" class="col-sm-2 form-control" value="${type != c.add ? data.email : ""}">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">电话:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="tel" value="${type != c.add ? data.tel : ""}" class="form-control"/>
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
                        <button type="button" class="btn btn-success" onclick="saveSystemUser()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearSystemUserInput()">清空</button>
                        </span>
                         ` : type == c.edit ? `
                         <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedSystemUser(${data.id})">保存修改</button>
                    </span>
                         ` : ""}
                </div>
            
            `;
        }

        /**
         *
         * @param {string} value
         * @param {string} name
         * @returns {string}
         */
        public exportUserLinkRoleContext(value: string, name: string) {
            return `
             <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">关联用户角色<small>[${name}]</small></p>
                        <span class="input-group-btn panel-button-group">
                        </span>
                    </div>
                </div>
                <div style="margin-top: 81px;">
                <input type="hidden" value="${value}" id="currUserId">
                
                <table id="system-role-table"></table>
                </div>
               `
        }

    }

    export class Network {
        add: string = "add";
        view: string = "view";
        edit: string = "edit";
        api = new api.Api();

        public exportNetWorkAction(value: string, name: string){
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="详情"  onclick="viewNetworkOrEditByID(${value},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="修改"  onclick="viewNetworkOrEditByID(${value},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="绑定节点"  onclick="openLinkDeployHosts(${value},'${name}')"><i class="fa fa-link" aria-hidden="true"></i></a>
            <a href="javascript:void(0)" title="同步网络至绑定节点"  onclick="sysNetworkToHost(${value},'${name}')"><i class="fa fa-plug" aria-hidden="true"></i></a>
            <a href="javascript:void(0)" title="删除"  onclick="delNetwork(${value})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`;
        }

        public exportAddNetworkContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any){

            let c = this;
            return `
           <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加网络" : (type == c.edit ? "修改网络" : (type == c.view ? "查看网络" : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">网络名称:</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="networkName" class="col-sm-2 form-control" value="${type != c.add ? data.networkName : ""}">
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
网络名称,英文
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">网桥(driver):</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="driver" class="col-sm-2 form-control" value="${type != c.add ? data.driver : "bridge"}"/>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
默认为：bridge
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">网段(subnet):</label>
                            <div class="col-sm-9">
                                <input type="text" ${type == c.view ? "disabled" : ""} id="subnet" class="form-control" value="${type != c.add ? data.subnet : ""}"/>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
例如:172.0.0.1/24
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">opt(网桥选项)</label>
                            <div class="col-sm-9">
                                <textarea ${type == c.view ? "disabled" : ""} id="opt" class="form-control" rows="10">${type != c.add ? data.opt : ""}</textarea>
                                <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
备注:
网桥可选项可选项:
com.docker.network.bridge.name=newName
com.docker.network.bridge.enable_ip_masquerade=true
com.docker.network.bridge.enable_icc=true
com.docker.network.bridge.host_binding_ipv4=192.168.10.234
com.docker.network.driver.mtu=1450

多个配置请换行配置

详见: https://docs.docker.com/engine/reference/commandline/network_create/#bridge-driver-options
                                 </pre>
                                </div>
                              </div>
                            </div>
                        </div>
                        ${type == c.add ? `
                        <span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="saveNetWork()">保存</button>
                        <button type="button" class="btn btn-danger" onclick="clearNetWorkInput()">清空</button>
                        </span>
                        ` : type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedNetWork(${data.id})">保存修改</button>
                    </span>
                        ` : ""}
                       
                </div>
            
            `;

        }

        public exportNetLinkHostContext(value: string, name: string){
            return `
             <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">关联节点<small>[${name}]</small></p>
                        <span class="input-group-btn panel-button-group">
                        </span>
                    </div>
                </div>
                <div style="margin-top: 81px;">
                <input type="hidden" value="${value}" id="currNetId">
                <div id="net-host-tableToolbar">
                    <p>
                        尝试在右侧搜索框检索主机！
                    </p>
                </div>
                <table id="net-host-table"></table>
                </div>
               `;
        }
    }
}