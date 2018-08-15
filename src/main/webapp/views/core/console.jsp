<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/2/22
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>

    toggleConloseView = function () {
        var ob = $("#consoleView");
        if (!ob.hasClass("opened")) {
            openConloseView();
        } else {
            closeConloseView();
        }
    };
    openConloseView = function () {
        var ob = $("#consoleView");
        var line = $("#line");
        if (ob.hasClass("opened")) {
            return
        }
        ob.animateCss("fadeInRight").addClass("opened");
        ob.css({top: "0", right: 0 + "px"});
        line.css({position: "fixed"});

    };

    closeConloseView = function () {
        var ob = $("#consoleView");
        var line = $("#line");
        if (ob.hasClass("opened")) {
            ob.removeClass("opened");
            ob.css({
                top: "5px",
                right: -(ob.width() + Number(ob.css("padding-left").replace("px", "")) + Number(ob.css("padding-right").replace("px", ""))) + "px",
            });
            line.css({position: "absolute"});
        }
    };
</script>
<div id="consoleView" class="closed">
    <p title="控制台" onclick="toggleConloseView(this)">[console >>]</p>
    <div id="line" onmousedown="slideLine(event)"></div>
</div>

