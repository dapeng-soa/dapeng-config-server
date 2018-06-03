<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/2/22
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--model--%>
<div id="struct-model">
    <div class="struct-model-container animated fadeIn">
        <a class="back-to-previous" href="javascript:void(0)" onclick="backPreviousModle()"><span
                class="glyphicon glyphicon-arrow-left"></span>返回</a>
        <a class="model-close" title="关闭" onclick=closeModel()><span
                class="glyphicon glyphicon-remove"></span></a>
        <div class="struct-model-context">
        </div>
    </div>
</div>

<script>
    window.$previousModle = [];
    var $callbackOnModel = undefined;
    /**
     * 打开弹窗并添加关闭回调事件
     * @param closeCallback
     */
    window.toggleModel = function (closeCallback) {
        var sb = $("#struct-model,body");
        var opened = "model-opened";
        if (window.$previousModle.length <= 1) {
            $(".back-to-previous").hide();
        }
        if (sb.hasClass(opened)) {
            sb.removeClass(opened)
        }else {
            setTimeout(function () {
                sb.addClass(opened);
                initModel();
            }, 100);
            if (closeCallback !== undefined) {
                $callbackOnModel = closeCallback;
            }
        }
    };

    //关闭弹出框
    window.closeModel = function () {
        window.$previousModle = [];
        var model = this;
        var sb = $("#struct-model,body");
        var opened = "model-opened";
        sb.removeClass(opened);
        model.$callbackOnModel();
        $callbackOnModel = undefined;
    };

    window.initModel = function () {
        setTimeout(function () {
            $("#struct-model .struct-model-context").scrollTop(0);
        }, 10);
    };

    /**
     * 回到上一个弹窗内容
     */
    window.backPreviousModle = function () {
        var maxIndex = window.$previousModle.length - 1;
        maxIndex !== 1 ? void(0) : $(".back-to-previous").hide();
        if (maxIndex >= 1) {
            window.$previousModle.pop();
            maxIndex = window.$previousModle.length - 1;
            $("#struct-model .struct-model-context").html(window.$previousModle[maxIndex]);
        }
    };

    window.initModelContext = function (context, closeCallback) {
        $(".struct-model-context").html(context);
        toggleModel(closeCallback);
    }

</script>
