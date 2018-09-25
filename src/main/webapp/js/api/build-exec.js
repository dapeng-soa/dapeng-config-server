var $$ = new api.Api();
var build = new api.Build();

$(document).ready(function () {

});

// 保存构建任务
var saveBuildTask = function (hostId) {
    var serviceId = $("#buildService").val();
    console.log(serviceId);
    console.log(hostId);
};

// 当所选服务变更
var serviceSelectChange = function () {
    // test 获取依赖
    var serviceId = $("#buildService").val();
    var url = basePath + "/api/build/depends/" + serviceId;
    $$.$get(url, function (res) {
        var depends = res.context;
        var context = build.buildTaskDependsContext(depends);
        $("#dependsService").html(context);
    })
};