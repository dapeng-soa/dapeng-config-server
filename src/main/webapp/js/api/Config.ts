/// <reference path="../../plugins/ts-lib/jquerytemplate.d.ts"/>
/// <reference path="../../plugins/ts-lib/jquery.d.ts"/>

module api {
    export class Config {
        //
        servicesUrl: string = `${window.basePath}/api/services`;
        add: string = "add";
        view: string = "view";
        edit: string = "edit";
        real: string = "real";

        // 导出添加配置页面内容
        public exportAddConfigContext(type: string = this.add || this.edit || this.view || this.real, biz?: string, data?: any) {
            let c = this;
            // language=HTML
            return `
                <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加配置" : (type == c.edit ? "修改配置" : (type == c.view ? "配置详情" : (type == c.real ? "实时配置" : "")))}</p>
                    </div>
                </div>
                <div class="form-horizontal" style="margin-top: 81px;">
                ${type != c.add && type != c.real ? `
                <div class="form-group">
                        <label class="col-sm-2 control-label">更新时间:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" value="${data.updatedAt}"/>
                        </div>
                    </div>
                ` : ""} 
               
              
                    <div class="form-group">${
                `
                    <label class="col-sm-2 control-label">${type == c.add ? "服务名(全限定名):" : "服务名:"}</label>
                        <div class="col-sm-9">
                            <input type="text" ${type != c.add ? "disabled" : ""} class="form-control" id="service-name" value="${type != c.add ? data.serviceName : ""}"/>
                   
                        </div>`
                }
                    </div>
                    ${type != c.real ?
                `
                <div class="form-group">
                        <label class="col-sm-2 control-label">超时配置:</label>
                        <!--超时配置-->
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="timeout-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? data.timeoutConfig : ""}</textarea>
                              <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
超时配置：
1.全局配置在/soa/config/services节点data上
2.具体服务的配置在/soa/config/services/{serviceName}上

格式：
全局：timeout/800ms
服务：timeout/700ms,createSupplier:100ms,modifySupplier:200ms;
                                  </pre>
                                </div>
                              </div>
                        </div>
                    </div>
                `
                : ""}
                   
                    <div class="form-group">
                        <label class="col-sm-2 control-label">${type == c.real?"超时/":""}负载均衡:</label>
                        <!--负载均衡配置-->
                        <div class="col-sm-9">
                            <textarea ${type == c.view || type == c.real ? "disabled" : ""} id="loadbalance-config-area" class="col-sm-2 form-control" rows="5">${type != c.add ? (type == c.real ? data.timeoutBalanceConfig : data.loadbalanceConfig):""}</textarea>
                              <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
负载均衡配置：
1.全局配置在/soa/config/services节点data上
2.具体服务的配置在/soa/config/services/{serviceName}上

格式：
可选策略：Random/RoundRobin/LeastActive/ConsistentHash
全局：loadbalance/LeastActive
服务：loadbalance/LeastActive,createSupplier:Random,modifySupplier:RoundRobin;
                                  </pre>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--路由配置-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">路由配置:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view || type == c.real ? "disabled" : ""} id="router-config-area" class="form-control" rows="5">${type != c.add ? data.routerConfig : ""}</textarea>
                              <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
路由配置：
1.路由配置在/soa/config/routes/{serviceName}节点data上

详细文档：
https://github.com/dapeng-soa/dapeng-soa/wiki/Dapeng-Routing
                                  </pre>
                                </div>
                              </div>
                        </div>
                    </div>
                    <!--限流配置-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">限流配置:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view || type == c.real ? "disabled" : ""} id="freq-config-area" class="form-control" rows="7">${type != c.add ? data.freqConfig : ""}</textarea>
                              <div class="advance-format-item">
                                <p class="advance-format-title" onclick="toggleBlock(this)" ><span class="glyphicon glyphicon-question-sign"></span></p>
                                <div class="advance-format-content">
                                  <pre>
限流配置：
1.限流配置在/soa/config/freq/{serviceName}节点data上

格式：
[rule1]
match_app = com.foo.service1 #服务
rule_type = callerIp # 对每个请求端IP
min_interval = 60,600  # 每分钟请求数不多余600
mid_interval = 3600,10000 # 每小时请求数不超过1万
max_interval = 86400,80000 # 每天请求书不超过8万

范例：
[customerUserIds]
match_app = com.github.dapeng.hello.service.HelloService
rule_type = userId
min_interval = 60,10
mid_interval = 3600,1000
max_interval = 86400,80000

[customerUserIds]
match_app = com.github.dapeng.hello.service.HelloService
rule_type = userId[1,3,5]
min_interval = 60,10
mid_interval = 3600,10000
max_interval = 86400,80000
        
[customerUserIps]
match_app = com.github.dapeng.hello.service.HelloService
rule_type = userIp[192.168.2.1,192.168.35.36,192.162.25.3]
min_interval = 60,10
mid_interval = 3600,10000
max_interval = 86400,80000

说明：
1.app 设定了每个 app 对应的三个统计周期：min, mid, max。单位为秒。 必须满足 max = N * min, mid = M * min
2.rule_type 是限流的key类型，目前支持：
* all 对这个服务限流
* callerIp 按callerIp限流。
* callerMid 按 callMid 限流。 由于callerMid 是字符串，实际按照其 hashCode 进行限流。（具有相同的hashCode的callerMid被归一处理）
* userIp 按 userIp 进行限流
* userId 按 userId 进行限流
3.对每一个服务，可以配置多个rule，例如，可以按照 callerIp 进行限流，可以按照 callerMid 进行限流，也可以同时进行限流。
4.支持动态的更新限流规则。

详细文档：
https://github.com/dapeng-soa/dapeng-soa/wiki/DapengFreqControl
                                  </pre>
                                </div>
                              </div>
                        </div>
                    </div>
                    
                    ${type != c.real ? `
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="remark-area" class="form-control" rows="7">${type != c.add ? data.remark : ""}</textarea>
                        </div>
                    </div>
                    `:""}
                    ${type == c.add ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="saveconfig()">保存配置</button>
                    <button type="button" class="btn btn-danger" onclick="clearConfigInput()">清空配置</button>
                    </span>
                    ` : (type == c.edit ? `
                        <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="editedConfig(data.id)">保存修改</button>
                    </span>
                    ` : "")}
                </div>
                
                
            `;
        }

        /**
         * 发布历史
         * @param serviceName
         * @returns {string}
         */
        public exportPublishHistoryContext(serviceName) {
            // html
            return `
            <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">发布历史<small>${serviceName}</small></p>
                    </div>
                </div>
                
              <div class="list-group" id="publishHistory" style="margin-top: 81px;">
                </div>
            `
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
            ${row.status != 3 ? `<a href="javascript:void(0)" title="发布" onclick="publishConfig(${id})"><span class="glyphicon glyphicon-send"></span></a>` : ""}
            <a href="javascript:void(0)" title="修改"  onclick="viewOrEditByID(${id},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <a href="javascript:void(0)" title="发布历史"  onclick="viewHistory(${id},'${row.serviceName}')"><span class="glyphicon glyphicon-time"></span></a>
            <a href="javascript:void(0)" title="实时配置"  onclick="viewRealConfig('${row.serviceName}')"><span class="glyphicon glyphicon-cloud"></span></a>
            <a href="javascript:void(0)" title="详情"  onclick="viewOrEditByID(${id},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>
            <a href="javascript:void(0)" title="删除"  onclick="delConfig(${id})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }

        //导出添加/修改/apikey信息
        public exportAddApiKeyContext(type: string = this.add || this.edit || this.view, biz?: string, data?: any) {
            let c = this;
            return `
            <div class="panel-header window-header">
                    <div class="input-group">
                        <p class="left-panel-title">${type == c.add ? "添加ApiKey" : (type == c.edit ? "修改ApiKey": (type == c.view ? "ApiKey详情" : ""))}</p>
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
                            <input type="text" ${type == c.view ? "disabled" : ""} id="authApikey" class="col-sm-2 form-control">${type != c.add ? data.apiKey : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">密码:</label>
                        <div class="col-sm-9">
                            <input type="text" ${type == c.view ? "disabled" : ""} id="authPassWord" class="col-sm-2 form-control">${type != c.add ? data.password : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">业务:</label>
                        <div class="col-sm-9">
                            <input type="text"  ${type == c.view ? "disabled" : ""} id="authBiz" class="form-control" >${type != c.add ? data.biz : ""}</input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">ip规则:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id=authIps class="form-control" rows="5">${type != c.add ? data.ips : ""}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea ${type == c.view ? "disabled" : ""} id="notes" class="form-control" rows="5">${type != c.add ? data.notes : ""}</textarea>
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

        //表格操作模版
        public exportApiKeyTableActionContext(id: string, row: any) {
            return `<span class="link-button-table">
            <a href="javascript:void(0)" title="修改"  onclick="viewApiKeyOrEditByID(${id},'edit')"><span class="glyphicon glyphicon-edit"></span></a>
            <!--<a href="javascript:void(0)" title="详情"  onclick="viewApiKeyOrEditByID(${id},'view')"><span class="glyphicon glyphicon-eye-open"></span></a>-->
            <a href="javascript:void(0)" title="禁用"  onclick="delApiKey(${id})"><span class="glyphicon glyphicon-remove"></span></a>
            </span>`
        }

    }
}