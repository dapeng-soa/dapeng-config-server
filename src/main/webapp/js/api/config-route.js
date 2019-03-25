/*
var config = new api.Config();
var bsTable = {};
var $$ = new api.Api();
*/
var $$ = new api.Api();
var serviceList;
var versionList;
var hostList;
var storeList;
var RULE_MATCH = 'match';
var MATCH_TYPE_EQUAL = 'equal';
var MATCH_TYPE_MATCH = 'match';
var MATCH_TYPE_TRAGET = ' => ';
var RULE_ELSE = '~';
var RULE_SPLIT = ";";
var DIST_SPLIT = ",";
var RULE_OTHERWISE = "otherwise";

var isShowRule;

$(document).ready(function () {
    console.info("come on config-route.js")
    message("正在加载数据，请稍等...", "", "50000000");
    loadInitData();
});

function loadInitData() {
    var url = basePath + "/api/route/loadData";
    $$.$get(url, function (res) {
        serviceList = res.context.serviceInfoList;
        versionList = res.context.versionList;
        hostList = res.context.hostList;
        storeList = res.context.storeList;
        clearMessage();
    });
}


function message(info, title, timeOut) {
    toastr.options = {
        "closeButton": true, //是否显示关闭按钮
        "debug": false, //是否使用debug模式
        "positionClass": "toast-center-center",//弹出窗的位置
        "hideDuration": "1000",//消失的动画时间
        "timeOut": timeOut, //展现时间
        "extendedTimeOut": "1000",//加长展示时间
        "showEasing": "swing",//显示时的动画缓冲方式
        "hideEasing": "linear",//消失时的动画缓冲方式
        "showMethod": "fadeIn",//显示时的动画方式
        "hideMethod": "fadeOut" //消失时的动画方式
    }
    toastr.info(info, title);
}

function clearMessage() {
    toastr.clear();
}

function routeKeyChange(dom, index) {
    var selectValue = $(dom).selectpicker('val');
    clearAppendHtml(index, 'rule');

    var routeRule = $("#route_rule_" + index).selectpicker('val');
    if (routeRule === "match") {
        $(dom).parent().parent().next().after(getMatchRuleHtml(index));
    } else {
        $("#match_value_" + index).parent().parent().remove();
        $(dom).parent().parent().next().after(getSelectHtml(selectValue, index));
    }

    refreshElement();
}

function serviceSelectChange(dom, index) {
    var serviceFullName = $(dom).selectpicker('val');
    $("#select_method_" + index).parent().parent().remove();

    $(dom).parent().parent().after(getSelectHtml("method", index, serviceFullName));

    refreshElement();
}

function routeRuleChange(dom, index) {
    clearAppendHtml(index, 'rule');

    var route_key = $("#route_key_" + index).selectpicker('val');
    if (!route_key) {
        message("请先选择路由类型", "提示", "1000");
        return;
    }
    var routeRule = $(dom).selectpicker('val');
    switch (routeRule) {
        case 'equal':
            $("#match_value_" + index).parent().parent().remove();
            routeKeyChange($("#route_key_" + index), index);
            break;
        case 'match':
            $(dom).parent().parent().after(getMatchRuleHtml(index));
            break;
    }
}


function addRule(dom, index) {
    var btnText = $(dom).text();

    if (btnText === '添加') {
        $(dom).text("删除");
        $(dom).parent().parent().after(getRuleRowHtml(Number(index) + 1));
        refreshElement();
    }
    if (btnText === '删除') {
        $(dom).parent().parent().prev("br").remove();
        $(dom).parent().parent().remove();
    }

}

/*ROUTE*/
function getSelectHtml(selectType, index, serviceFullName) {
    var html = '<div class="col-md-2">';

    switch (selectType) {
        case 'method':
            if (serviceFullName) {
                html += ' <select id="select_method_' + index + '"  name="select_method_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择方法" placeholder="请选择方法" multiple>';
                serviceList.forEach(function (item, index) {
                    if (item.serviceFullName === serviceFullName) {
                        item.methodList.forEach(function (itemMethod, indexMethod) {
                            html += ' <option value="' + itemMethod + '">' + itemMethod + '</option>';
                        });
                        return false;
                    }
                })
                html += '</select> </div>';
            } else {//没有选方法
                html = getSelectHtml("serviceName_methods", index);
            }

            break;
        case 'storeId':
            html += ' <select id="select_store_' + index + '" name="select_store_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择店铺" placeholder="请选择店铺" multiple>';
            storeList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item.storeCode + '">' + item.storeName + '</option>';
            })
            html += '</select> </div>';
            break;
        case 'serviceName':
            html += ' <select id="select_service_' + index + '" name="select_service_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择服务" placeholder="请选择服务">';
            html += ' <option value="">请选择服务</option>';
            serviceList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item.serviceFullName + '">' + item.serviceName + '</option>';
            })
            html += '</select> </div>';
            break;

        case 'serviceName_methods':
            html += ' <select id="select_service_' + index + '" name="select_service_' + index + '" onchange="javascript:serviceSelectChange(this,\'' + index + '\');" class="selectpicker show-tick form-control" data-live-search="true" require="请选择服务" placeholder="请选择服务">';
            html += ' <option value="">请选择服务</option>';
            serviceList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item.serviceFullName + '">' + item.serviceName + '</option>';
            })
            html += '</select> </div>';
            break;
        case 'rule_version':
            html += ' <select id="select_version_' + index + '" name="select_version_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择版本" placeholder="请选择版本">';
            html += ' <option value="">请选择版本</option>';
            versionList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item + '">' + item + '</option>';
            })
            html += '</select> </div>';
            break;

        case 'dist_version':
            html += ' <select id="dist_version_' + index + '" name="dist_version_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择版本" placeholder="请选择版本">';
            html += ' <option value="">请选择版本</option>';
            versionList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item + '">' + item + '</option>';
            })
            html += '</select> </div>';
            break;

        case 'dist_ip':
            html += ' <select id="dist_host_' + index + '" name="dist_host_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择节点IP" placeholder="请选择节点IP">';
            html += ' <option value="">请选择节点</option>';
            hostList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item.ip + '">' + item.name + '</option>';
            })
            html += '</select> </div>';
            break;

        case 'otherwise_version':
            html += ' <select id="otherwise_version_' + index + '" name="otherwise_version_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择节点IP" placeholder="请选择节点IP">';
            html += ' <option value="">请选择版本</option>';
            versionList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item + '">' + item + '</option>';
            })
            html += '</select> </div>';
            break;

        case 'otherwise_ip':
            html += ' <select id="otherwise_host_' + index + '" name="otherwise_host_' + index + '" class="selectpicker show-tick form-control" data-live-search="true" require="请选择节点IP" placeholder="请选择节点IP">';
            html += ' <option value="">请选择节点</option>';
            hostList.forEach(function (item, index) {
                // console.info(item.serviceFullName)
                html += ' <option value="' + item.ip + '">' + item.name + '</option>';
            })
            html += '</select> </div>';
            break;
    }
    return html;
}

function clearAppendHtml(index, type) {
    switch (type) {
        case "rule":
            $("#select_method_" + index).parent().parent().remove();
            $("#select_version_" + index).parent().parent().remove();
            $("#select_store_" + index).parent().parent().remove();
            $("#match_value_" + index).parent().parent().remove();
            $("#select_service_" + index).parent().parent().remove();
            break;
        case "dist":
            //dist
            $("#dist_host_" + index).parent().parent().remove();
            $("#dist_version_" + index).parent().parent().remove();
            break;
        case "otherwise":
            //otherwise
            $("#otherwise_host_" + index).parent().parent().remove();
            $("#otherwise_version_" + index).parent().parent().remove();
            break;

    }
}

function getMatchRuleHtml(index) {
    var html = '<div class="col-md-2">' +
        '<div class="form-group fl">' +
        '<input type="text" id="match_value_' + index + '" class="form-control" placeholder="请输入匹配规则,eg: setFoo.* ">' +
        ' </div>' +
        ' </div>';
    return html;
}

function refreshElement() {
    $('.selectpicker').selectpicker('refresh');
}

function getRuleRowHtml(index) {
    var rowHtml = '<br><div class="row">' +
        '<div class="col-md-1">' +
        '<select id="route_key_' + index + '" name="route_key_' + index + '" onchange="javascript:routeKeyChange(this,' + index + ');" class="selectpicker show-tick form-control" data-live-search="true" require="规则类型" placeholder="规则类型">' +
        ' <option value="">规则类型</option>' +
        '<option value="method">method</option>' +
        ' <option value="storeId">storeId</option>' +
        '<option value="serviceName">serviceName</option>' +
        '<option value="rule_version">version</option>' +
        '</select>' +
        '</div>' +
        '' +
        ' <div class="col-md-1">' +
        '<select id="route_rule_' + index + '" name="route_rule_' + index + '" onchange="javascript:routeRuleChange(this,\'' + index + '\');" class="selectpicker show-tick form-control" data-live-search="true" require="匹配模式" placeholder="匹配模式">' +
        ' <option value="equal">equal</option>' +
        '<option value="match">match</option>' +
        '</select>' +
        ' </div>' +
        '' +
        '<div class="col-md-1">' +
        '<button type="button" class="btn btn-primary" onclick="addRule(this,\'' + index + '\')">添加</button>' +
        '</div>' +
        '</div>';

    return rowHtml;
}


/*DIST*/
function distKeyChange(dom, index) {
    var selectValue = $(dom).selectpicker('val');
    clearAppendHtml(index, 'dist');
    $(dom).parent().parent().after(getSelectHtml(selectValue, index));

    refreshElement();
}

function addDist(dom, index) {
    var btnText = $(dom).text();

    if (btnText === '添加') {
        $(dom).text("删除");
        $(dom).parent().parent().after(getDistRowHtml(Number(index) + 1));
        refreshElement();
    }
    if (btnText === '删除') {
        $(dom).parent().parent().prev("br").remove();
        $(dom).parent().parent().remove();
    }
}

function getDistRowHtml(index) {
    var rowHtml = '<br><div class="row">' +
        '<div class="col-md-1">' +
        '<select id="dist_key_' + index + '" name="dist_key_' + index + '" onchange="javascript:distKeyChange(this,\'' + index + '\');" class="selectpicker show-tick form-control" data-live-search="true" require="路由匹配项" placeholder="路由匹配项">' +
        '<option value="">指向类型</option>' +
        '<option value="dist_ip">ip</option>' +
        '<option value="dist_version">version</option>' +
        ' </select>' +
        '</div>' +
        '' +
        ' <div class="col-md-1">' +
        '<div class="checkbox checkbox-success checkbox-inline">' +
        '<input type="checkbox" class="styled" id="inlineCheckbox' + index + '" name="inlineCheckbox' + index + '" value="option' + index + '">' +
        '<label for="inlineCheckbox' + index + '"> 取非 </label>' +
        '</div>' +
        '</div>' +
        '' +
        '<div class="col-md-1">' +
        '<button type="button" class="btn btn-primary" onclick="addDist(this,' + index + ')">添加</button>' +
        '</div>' +
        '</div>';

    return rowHtml;
}


/*otherwise*/
function otherwiseKeyChange(dom, index) {
    var selectValue = $(dom).selectpicker('val');
    clearAppendHtml(index, 'otherwise');

    $(dom).parent().parent().after(getSelectHtml(selectValue, index));
    refreshElement();
}


/******************************生成路由*/
function buildRule() {
    isShowRule = true;

    var routeRule = '';
    /*生成 路由Key 规则*/
    routeRule += buildRuleKey();

    /*生成 路由 指向规则*/
    routeRule += MATCH_TYPE_TRAGET + buildRuleDist();

    /*生成 路由 otherwise规则 */
    routeRule += "<br>" + buildRuleOtherwise();

    if (isShowRule) {
        $("#showRuleDiv").html(routeRule);
        $("#myModal").modal("show");
        //console.info(routeRule)
    }

    /* var info = "method match 'getServiceMetadata';cookie_storeId match '17007125','17007136','17007012','17007001','17007002','17007003' => ip'192.168.37.200' <br>" +
         "otherwise => ~ip'192.168.37.200'";
     $("#showRuleDiv").html(info);
     $("#myModal").modal("show");
     */
}

/****************************规则生成 */

/*生成完整路由 规则*/
function buildRuleKey() {
    var rowRule = '';
    $("#route_rule_panel").children("div").each(function (index, elem) {
        rowRule += buildRuleKeyRow(index, elem) + RULE_SPLIT;
    })
    rowRule = rowRule.substring(0, rowRule.length - 1);
    return rowRule;
}

/*生成一行 路由规则 字符串*/
function buildRuleKeyRow(index, elem) {
    if (!validateRowRule(index, elem)) {
        return '';
    }

    var buildRuleInfo = '';
    var routeRuleKey = $("select[id^='route_key_']", elem).val();
    buildRuleInfo += getRouteRuleKey(routeRuleKey);
    buildRuleInfo += getRouteRuleKey(RULE_MATCH);

    var matchType = $("select[id^='route_rule_']", elem).val();
    if (matchType === MATCH_TYPE_EQUAL) {

        var _ruleValue = getRuleKeyValue(index, elem, routeRuleKey);
        _ruleValue = _ruleValue.toString().replace(/,/g, "','");
        buildRuleInfo += '\'' + _ruleValue + '\'';
    }
    if (matchType === MATCH_TYPE_MATCH) {
        buildRuleInfo += 'r\'' + $("input[id^='match_value_']", elem).val() + '\'';
    }
    // console.info(buildRuleInfo)
    return buildRuleInfo;
}

/*验证路由规则   输入是否合格*/
function validateRowRule(index, elem) {
    //检查是否选择路由类型
    var routeRuleKey = $("select[id^='route_key_']", elem).val();
    if (!routeRuleKey) {
        message("请选择路由规则类型", "提示", "2000");
        isShowRule = false;
        return false;
    }

    //检查匹配模式  正则匹配模式
    var matchType = $("select[id^='route_rule_']", elem).val();
    if (matchType === MATCH_TYPE_MATCH) {
        if (!$("input[id^='match_value_']", elem).val()) {
            message("请输入正则匹配规则", "提示", "2000");
            isShowRule = false;
            return false;
        }
    }

    //检查 匹配模式  EQUAL模式
    if (matchType === MATCH_TYPE_EQUAL) {
        if (!getRuleKeyValue(index, elem, routeRuleKey)) {
            message("请选择路由规则的值", "提示", "2000");
            isShowRule = false;
            return false;
        }
    }

    return true;
}


/****************************指向生成 */
function buildRuleDist() {
    var distInfo = '';
    $("#route_dist_panel").children("div").each(function (index, elem) {
        distInfo += buildDistRow(index, elem) + DIST_SPLIT;
    })
    distInfo = distInfo.substring(0, distInfo.length - 1);
    return distInfo;
}

/*生成一行 路由指向规则 字符串*/
function buildDistRow(index, elem) {
    if (!validateDistRow(index, elem)) {
        return '';
    }

    var buildDistInfo = '';
    if ($("input[id^='inlineCheckbox']", elem).is(':checked')) {
        buildDistInfo += RULE_ELSE;
    }

    var routeDistKey = $("select[id^='dist_key_']", elem).val();
    buildDistInfo += getRouteRuleKey(routeDistKey);

    var _distValue = getRuleKeyValue(index, elem, routeDistKey);
    _distValue = _distValue.toString().replace(/,/g, "','");
    buildDistInfo += '\'' + _distValue + '\'';
    return buildDistInfo;
}

/*路由指向 规则输入是否合格*/
function validateDistRow(index, elem) {
    //检查是否选择路由类型
    var routeDistKey = $("select[id^='dist_key_']", elem).val();
    if (!routeDistKey) {
        message("请选择路由指向类型", "提示", "2000");
        isShowRule = false;
        return false;
    }

    //检查 路由指向类型的      匹配值
    if (!getRuleKeyValue(index, elem, routeDistKey)) {
        message("请选择路由指向的值", "提示", "2000");
        isShowRule = false;
        return false;
    }

    return true;
}


/****************************otherwise生成 */
function buildRuleOtherwise() {
    var elem = $("#route_otherwise_panel").children("div");

    //检查是否选择Otherwise
    var otherwiseChecked = $("input[id^='otherwiseCheckbox']", elem).is(':checked');
    if (!otherwiseChecked) {
        return "";
    }

    if (!validateOtherwiseInput(elem)) {
        return '';
    }

    var buildOtherWiseInfo = RULE_OTHERWISE + MATCH_TYPE_TRAGET;

    if ($("input[id^='otherwiseElse']", elem).is(':checked')) {
        buildOtherWiseInfo += RULE_ELSE;
    }

    var otherwiseDistKey = $("select[id^='otherwise_key_']", elem).val();
    buildOtherWiseInfo += getRouteRuleKey(otherwiseDistKey);

    var _otherwiseValue = getRuleKeyValue(null, elem, otherwiseDistKey);
    _otherwiseValue = _otherwiseValue.toString().replace(/,/g, "','");
    buildOtherWiseInfo += '\'' + _otherwiseValue + '\'';
    return buildOtherWiseInfo;
}

/*验证路由规则   输入是否合格*/
function validateOtherwiseInput(elem) {
    /* //检查是否选择Otherwise
     var otherwiseChecked = $("input[id^='otherwiseCheckbox']", elem).is(':checked');
     if (otherwiseChecked) {*/
    //检查是否选择   otherwise路由类型
    var otherWiseKey = $("select[id^='otherwise_key_']", elem).val();
    if (!otherWiseKey) {
        message("请选择 otherwise 指向类型", "提示", "2000");
        isShowRule = false;
        return false;
    }

    //检查 路由指向类型的      匹配值
    if (!getRuleKeyValue(null, elem, otherWiseKey)) {
        message("请选择路由 otherwise 指向的值", "提示", "2000");
        isShowRule = false;
        return false;
    }
    // }
    return true;
}


/*获得所选路由类型的  规则的值*/
function getRuleKeyValue(index, elem, routeRuleKey) {
    var ruleType = '';
    switch (routeRuleKey) {
        /*rule*/
        case "method":
            ruleType = "select_method_";
            break;

        case "storeId":
            ruleType = "select_store_";
            break;

        case "serviceName":
            ruleType = "select_service_";
            break;

        case "rule_version":
            ruleType = "select_version_";
            break;

        /*dist*/
        case "dist_ip":
            ruleType = "dist_host_";
            break;
        case "dist_version":
            ruleType = "dist_version_";
            break;

        /*dist*/
        case "otherwise_ip":
            ruleType = "otherwise_host_";
            break;
        case "otherwise_version":
            ruleType = "otherwise_version_";
            break;
    }
    return $("select[id^='" + ruleType + "']", elem).val();
}

/*路由 关键字*/
function getRouteRuleKey(type) {
    switch (type) {
        case "match":
            return "match ";

        case "method":
            return "method ";

        case "serviceName":
            return "service ";

        case "rule_version":
            return "version ";

        case "storeId":
            return "cookie_storeId ";

        /*dist*/
        case "dist_ip":
            return "ip";
        case "dist_version":
            return "v";

        /*otherwise*/
        case "otherwise_ip":
            return "ip";
        case "otherwise_version":
            return "v";

        default:
            return '';
    }
}


