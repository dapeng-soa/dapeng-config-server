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
        /*var term = new Terminal({
            useStyle: true,
            convertEol: true,
            screenKeys: false,
            cursorBlink: false,
            visualBell: false,
            disableStdin: true,
            colors: Terminal.xtermColors
        });
        term.open(document.getElementById('terminal'));
        for(var i =0 ;i<1000;i++){
            term.write(i+'Hello from \x1B[1;3;31mxterm.js\x1B[0m $ \r\n')
        }*/

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
    slideLine = function (e) {
        var body = document.body, oconsoleView = document.getElementById("consoleView"),
            oLine = document.getElementById("line");
        var disX = (e || event).clientX;
        oLine.width = oLine.offsetWidth;
        document.onmousemove = function (e) {
            var iT = oLine.width + ((e || event).clientX - disX);
            var e = e || window.event, tarnameb = e.target || e.srcElement;
            var maxT = body.clientWidth - oLine.offsetWidth;
            var base = (maxT - disX);
            if (iT > maxT) {
                iT = maxT
            }
            oLine.style.right = (base - oLine.width - iT) < (300 - oLine.width) ? 300 - oLine.width : (base - oLine.width - iT) + "px";
            oconsoleView.style.width = base - iT + "px";
            return false
        };
        document.onmouseup = function () {
            document.onmousemove = null;
            document.onmouseup = null;
            oLine.releaseCapture && oLine.releaseCapture()
        };
        oLine.setCapture && oLine.setCapture();
        return false
    };
</script>
<div id="consoleView" class="closed">
    <p title="控制台" onclick="toggleConloseView(this)">[console >>]</p>
    <div id="line" onmousedown="slideLine(event)"></div>
    <%--<div id="terminal" style="width: 100%;height: 100%;"></div>--%>
</div>

