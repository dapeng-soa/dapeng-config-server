var $$ = new api.Api();

$(document).ready(function () {

});

var saveBuildTask = function (hostId) {
    console.log(hostId);
    // test 获取依赖
    var serviceId = $("#buildService").val();
    console.log(serviceId);
    var url = basePath + "/api/build/depends/" + serviceId;
    $$.$get(url, function (res) {
        console.log(res);
    })

};

var openAddBuildExecModle = function () {

};