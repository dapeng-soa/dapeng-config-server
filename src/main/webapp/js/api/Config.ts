/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>

module api {
    export class Config {
        //
        servicesUrl: string = `${window.basePath}/api/services`;
        add: string = "add";
        view: string = "view";
        edit: string = "edit";

        // 导出添加配置页面内容
        public exportAddConfigContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            // language=HTML
            return `
                <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加配置" : (type == c.edit ? "修改配置:" + biz : (type == c.view ? "配置详情:" + biz : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                ${type != c.add ? `
                <div class="form-group">
                        <label class="col-sm-2 control-label">更新时间:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" value="${data.updatedAt}"/>
                        </div>
                    </div>
                ` : ""} 
                       ${
                type != c.add ? `
                <div class="form-group">
                        <label class="col-sm-2 control-label">版本号:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" value="${data.version}"/>
                        </div>
                    </div>
                ` : ""
                }
                    <div class="form-group">${
                `
                    <label class="col-sm-2 control-label">${type == c.add ? "服务名(全限定名):" : "服务名:"}</label>
                        <div class="col-sm-9">
                          
                        <!--<select class="form-control" id="services-sel">
                            </select>-->
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" id="service-name" value="${type != c.add ? data.serviceName : ""}"/>
                   
                        </div>`
                }
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">超时配置:</label>
                        <!--超时配置-->
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="timeout-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? data.timeoutConfig : ""}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">负载均衡:</label>
                        <!--负载均衡配置-->
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="loadbalance-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? data.loadbalanceConfig : ""}</textarea>
                        </div>
                    </div>
                    <!--路由配置-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">路由配置:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="router-config-area" class="form-control" rows="5">${type != c.add ? data.routerConfig : ""}</textarea>
                        </div>
                    </div>
                    <!--限流配置-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">限流配置:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="freq-config-area" class="form-control" rows="7">${type != c.add ? data.freqConfig : ""}</textarea>
                        </div>
                    </div>
                    ${type == c.add ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveconfig()">保存配置</button>
                    <button type="button" class="btn btn-danger" onclick="clearConfigInput()">清空配置</button>
                    </span>
                    ` : (type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedConfig(data.id)">保存修改</button>
                    <button type="button" class="btn btn-danger" onclick="editedAndPublish(data.id)">修改并发布</button>
                    </span>
                    ` : "")}
                </div>
            `;
        }

        // 初始化服务信息
        public initServices() {
            const config = this;
            let optionsEl = "";
            $.get(config.servicesUrl, function (res) {
                let services = res.context;
                for (const s of services) {
                    optionsEl += `<option value=${s}>${s}</option>`
                }
                $("#services-sel").html(optionsEl);
            }, "json");
        }

        //表格操作模版
        public exportTableActionContext(id: string, row: any) {
            return `<span class="link-button-table">
            ${row.status != 3 ? `<a href="javascript:void(0)" onclick="publishConfig(${id})">发布</a>` : ""}
            <a href="javascript:void(0)"  onclick="viewOrEditByID(${id},'edit')">修改</a>
            <a href="javascript:void(0)"  onclick="viewHistory(${id})">历史</a>
            <a href="javascript:void(0)"  onclick="viewOrEditByID(${id},'view')">详情</a>
            </span>`
        }

        //导出添加/修改/apikey信息
        public exportAddApiKeyContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
            <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加ApiKey" : (type == c.edit ? "修改ApiKey:" + biz : (type == c.view ? "ApiKey详情:" + biz : ""))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
               ${type != c.add ? ` <div class="form-group">
                        <label class="col-sm-2 control-label">更新时间:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" value="${data.updatedAt}"/>
                        </div>
                    </div>` : ""}
               <div class="form-group">
                        <label class="col-sm-2 control-label">ApiKey:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type == c.view ? "disabled" : ""} id="timeout-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? data.timeoutConfig : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">密码:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type == c.view ? "disabled" : ""} id="loadbalance-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? data.loadbalanceConfig : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">业务:</label>
                        <div class="col-sm-9">
                            <input type="text"  ${type == c.view ? "disabled" : ""} id="router-config-area" class="form-control" >${type != c.add ? data.routerConfig : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ip规则:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="router-config-area" class="form-control" rows="5">${type != c.add ? data.routerConfig : ""}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="router-config-area" class="form-control" rows="5">${type != c.add ? data.routerConfig : ""}</textarea>
                        </div>
                    </div>
                    ${type == c.add ? `
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveApiKey()">保存</button>
                    <button type="button" class="btn btn-danger" onclick="clearApiKeyInput()">清空</button>
                    </span>` : ""}
                    </div>
                    
            `
        }

    }
}