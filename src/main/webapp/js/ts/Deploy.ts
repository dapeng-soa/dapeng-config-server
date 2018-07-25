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

        /**
         * 服务视图
         */
        public deployServiceView(){

        }

        /**
         * 主机视图
         */
        public deployHostView(){

        }
        /**
         * 预览yaml
         * @returns {string}
         */
        public viewDeployYamlContext(){
            return `
<div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">预览部署yaml</p>
                    </div>
                </div>
            <pre style="margin-top: 81px">
services:
    sss:
    container_name: adminService
    environment:
      LANG: zh_CN.UTF-8
      TZ: CST-8
      cas_server_url: https://sso.today36524.com
      fluent_bit_enable: "true"
      kafka_consumer_host: 192.168.10.129:9092,192.168.20.103:9092,192.168.20.114:9092
      redis_host_ip: redis_host
      redis_host_port: '6008'
      serviceName: adminService
      slow_service_check_enable: "true"
      soa_container_ip: 192.168.10.130
      soa_container_port: '9083'
      soa_core_pool_size: '100'
      soa_jmxrmi_enable: "false"
      soa_monitor_enable: "true"
      soa_service_timeout: '10000'
      soa_transactional_enable: "false"
      soa_zookeeper_host: 192.168.10.129:2181,192.168.20.103:2181,192.168.20.114:2181
    extra_hosts:
      db-master: 192.168.20.100
      fluentd: 192.168.20.200
      fluentdStandby: 192.168.10.132
      redis_host: 192.168.20.125
      soa_zookeeper: 192.168.10.129
    image: docker.today36524.com.cn:5000/biz/admin_service:48d4b33
    labels:
      project.depends: mysql,zookeeper,idGenService
      project.operation: sh buildAdmin.sh
      project.owner: XUSHENG
      project.source: http://pms.today36524.com.cn:8083/central-services/admin.git@@master
    ports:
    - 9083:9083/tcp
    restart: on-failure:3
    volumes:
    - /data/logs/dapeng/admin-service:/dapeng-container/logs:rw
    - /data/var/fluent/adminService/logs.db:/fluent-bit/db/logs.db:rw
    - /home/today/tscompose/config/fluent-bit-dapeng.conf:/opt/fluent-bit/etc/fluent-bit.conf:rw
</pre>
<div >
<span class="input-group-btn panel-button-group text-center">
                        <button type="button" class="btn btn-success" onclick="">升级</button>
                        </span>
</div>
            `

        }

    }
}