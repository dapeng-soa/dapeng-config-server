$(document).ready(function () {
    initSetList();
});
var deploy = new api.Deploy();

/**
 * 初始化set
 * @constructor
 */
initSetList = function (id) {
    var curl = basePath + "/api/deploy-sets";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (i === 0) {
                    seled = "selected"
                }
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#setSelect").html(html);
        }
    }, "json");
};

// updateService
updateService = function () {

};
// checkService
checkService = function () {

};

// stopService
stopService = function () {

};

// restartService
restartService = function () {

};

