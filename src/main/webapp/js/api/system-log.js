var bsTable = {};
var $$ = new api.Api();
$(document).ready(function () {
    initDeployJournals();
    initOps();
    setTimeout(function () {
        var img = $(".CodeContainer>span>img");
        if (img.attr("src") === window.ImgExpanded) {
            img.click();
        }
    }, 200);
});

function initDeployJournals() {
    var queryUrl = basePath + '/api/system-logs';
    var table = new BSTable("system-log-table", queryUrl, setColumns());
    table.params = function (ps) {
        ps.opName = $("#opNameSelect").find("option:selected").val();
        ps.operator = $("#operatorSelect").find("option:selected").val();
        ps.resultType = $("#resultTypeSelect").find("option:selected").val();
        ps.opType = $("#opTypeSelect").find("option:selected").val();
        ps.sort = "operTime";
        return ps;
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.init();
    bsTable = table;
}

setColumns = function () {
    return [{
        checkbox: false,
        visible: false//是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'operName',
        title: '操作名称',
        sortable: true
    }, {
        field: 'operator',
        title: '操作人账号'
    }, {
        field: 'operParams',
        title: '操作参数',
        formatter: operParamsFormatter
    }, {
        field: 'operResult',
        title: '操作结果',
        align: 'center'
    }, {
        field: 'resultMsg',
        title: '结果消息'
    }, {
        field: 'operTime',
        title: '操作时间',
        sortable: true
    }]
};

initOps = function () {
    var url = basePath + "/api/system-logs/ops";
    $$.$get(url, function (res) {
        var opNames = res.context.opNames;
        var perators = res.context.perators;
        var nameOps = "<option value=''>请选择</option>";
        var peratorOps = "<option value=''>请选择</option>";
        for (var i in opNames) {
            nameOps += "<option value='" + opNames[i] + "'>" + opNames[i] + "</option>"
        }
        for (var i in perators) {
            peratorOps += "<option value='" + perators[i] + "'>" + perators[i] + "</option>"
        }
        $("#opNameSelect").html(nameOps).selectpicker('refresh');
        $("#operatorSelect").html(peratorOps).selectpicker('refresh');
    })
};


operParamsFormatter = function (value, row, index) {
    return getFormatedJsonHTML(JSON.parse(value));
};


reloadTable = function () {
    bsTable.refresh();
};

