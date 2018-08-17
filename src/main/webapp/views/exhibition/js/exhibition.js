/**
 * @author huyj
 * @Created  2018-08-03 11:06
 */
/*jQuery(document).ready(function(){
    alert(0)
    MergeTableCell();
});*/

let localurl = "exhibition/serviceCount/com.today.api.member.service.MemberService";


let service_count_time = "http://influxdb.today36524.com.cn:8087/query?pretty=true&db=dapengState&q=SELECT%20sum(%22total_calls%22)%20as%20totalCalls,sum(%22i_average_time%22)%20as%20averageTime%20%20FROM%20%22dapeng_service_process%22%20WHERE%20(%22service_name%22%20=%20%27com.today.api.member.service.MemberService%27)%20AND%20time%20%3E%20now()%20-%201m%20GROUP%20BY%20time(1m),%22method_name%22%20fill(0)";
let requestCountByService = "http://influxdb.today36524.com.cn:8087/query?pretty=true&db=dapengState&q=select%20sum(total_calls)%20as%20req_count%20from%20dapeng_service_process%20where%20time%3Enow()-1m%20group%20by%20service_name%20fill(0)";

$(document).ready(function () {
    fullScreen();
    initServiceTable();
    console.info("ready");

    $.ajax({
        type: "GET",
        /*url: service_count_time,*/
        url: requestCountByService,
        dataType: "json",
        success: function (data) {
            // parseServiceCountData(data);
            //parseServiceCountAndTime(data);
            parseRequestCountByService(data);
            console.info("success");
        },
        error: function (data) {
            console.info(data)
            console.info("error");
        }
    });
});

/*$.parseJSON( jsonstr ); //jQuery.parseJSON(jsonstr),可以将json字符串转换成json对象
 //时间转换 2018-08-09T10:24:00Z  => 2018/08-09T10:24:00Z
    console.info("2018-08-09T10:24:00Z".replace("T", " ").replace("Z", ""))
* */

function parseRequestCountByService(data) {
    /*console.info(JSON.stringify(data))*/
    // console.info(JSON.parse(JSON.stringify(data)))
    let result = new Array();

    let dataObj = data;
    let dataArr = data.results[0].series;
    for (let i = 0; i < dataArr.length; i++) {
        let itemObj = {};
        itemObj.content = dataArr[i].tags.service_name;
        itemObj.value = dataArr[i].values[0][1];
        result.push(itemObj);
    }
    console.info(JSON.stringify(result))
    return result;
}


function parseServiceCountAndTime(data) {
    /*console.info(JSON.stringify(data))*/
    // console.info(JSON.parse(JSON.stringify(data)))
    let result = new Array();

    let dataObj = data;
    let dataArr = data.results[0].series;
    console.info(dataArr)
    let count = 0;
    /*dataArr.forEach(function (item, index) {
        let method = item.tags.method_name
        //totalCalls
        let totalCalls = item.values[1][1];
        //averageTime
        let averageTime = item.values[1][2];

        //console.info(method + ":" + totalCalls + ":" + averageTime);
        let itemObj = {};
        itemObj.x = method;
        itemObj.y = totalCalls;
        itemObj.z = averageTime;
        result.push(itemObj);
    });*/

    for (let i = 0; i < dataArr.length; i++) {
        let method = dataArr[i].tags.method_name
        //totalCalls
        let totalCalls = dataArr[i].values[1][1];
        //averageTime
        let averageTime = dataArr[i].values[1][2];

        //console.info(method + ":" + totalCalls + ":" + averageTime);
        let itemObj = {};
        itemObj.x = method;
        itemObj.y = totalCalls;
        itemObj.z = averageTime;
        result.push(itemObj);
    }
    // console.info("total " + dataArr.length + "个 method");
    console.info(JSON.stringify(result))
    return result;
}

//解析服务调用次数
function parseServiceCountData(data) {
    //  console.info(JSON.stringify(data))
    //  console.info(JSON.parse(JSON.stringify(data)))

    let dataObj = data;
    let dataArr = data.results[0].series;

    //if (data.results[0] === undefined) return true;

    let result = new Array();
    dataArr.forEach(function (item, index) {
        let method = item.tags.method_name
        item.values.forEach(function (item, index) {
            let itemObj = {};
            //if (item[0] === undefined) return true;
            //时间转换 2018-08-09T10:24:00Z  => 2018/08-09T10:24:00Z
            itemObj.x = item[0].replace("T", " ").replace("Z", "");
            //console.info(method + ":" + item[0].replace("T", " ").replace("Z", "") + ":" + item[1])
            itemObj.y = item[1];
            itemObj.s = method;
            result.push(itemObj);
        });
        //console.info(method)
    });
    console.info(JSON.stringify(result))
    return result;
}


function initServiceTable() {
    layui.use('table', function () {
        let table = layui.table;
        //第一个实例
        table.render({
            elem: '#demo'
            , text: {
                none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            }
            , height: 'full-100'
            , url: 'views/exhibition/data/serviceListData.json' //数据接口
            , page: false //开启分页
            , cols: [[ //表头
                {field: 'serviceGroup', title: '服务分组', width: '10%', sort: false, align: 'center'}
                , {field: 'serviceName', title: '服务名称', width: '35%'}
                , {field: 'nodeCount', title: '节点', width: '6%', sort: false, templet: '#nodeCount', align: 'center'}
                , {field: 'healthStatus', title: '健康度', width: '10%', templet: '#healthStatus', align: 'center'}
                , {field: 'busyStatus', title: '繁忙度', width: '10%', templet: '#busyStatus', align: 'center'}
                , {field: 'updateTime', title: '更新时间', width: '25%', sort: false, templet: '#updateTime'}
            ]]
            , done: function (res, curr, count) {
                MergeTableCell($("#serviceTable").find(".layui-table-main > .layui-table")[0], 0, 0, 0);
            }
        });
    });
}

//合并单元格
function MergeTableCell($table, startRow, endRow, col) {
    //const tb = document.getElementById(tableId);
    //设置为0时,检索所有行
    if (endRow === 0) {
        endRow = $table.rows.length - 1;
    }
    //指定数据行索引大于表格行数
    if (endRow >= $table.rows.length) {
        return;
    }
    //检测指定的列索引是否超出表格列数
    if (col >= $table.rows[0].cells.length) {
        return;
    }
    //循环需要判断的数据行
    for (let i = startRow; i < endRow; i++) {
        //如果当前行与下一行数据值相同，则进行前面列的判断
        if ($table.rows[startRow].cells[col].innerHTML === $table.rows[i + 1].cells[col].innerHTML) {
            let Same = true;
            //循环跟前面的所有的同级数据行进行判断
            for (let j = col; j > 0; j--) {
                if ($table.rows[startRow].cells[j - 1].innerHTML !== $table.rows[i + 1].cells[j - 1].innerHTML) {
                    Same = false;
                    break;
                }
            }
            //如果前面的同级数据行的值均相同，则进行单元格的合并
            if (true === Same) {
                //如果相同则删除下一行的第0列单元格
                //tb.rows[i + 1].cells[col].style.display = 'none';
                $table.rows[i + 1].deleteCell(col)
                //更新rowSpan属性
                $table.rows[startRow].cells[col].rowSpan = ($table.rows[startRow].cells[col].rowSpan | 0) + 1;
            }
            else {
                //增加起始行
                startRow = i + 1;
            }
        }
        else {
            //增加起始行
            startRow = i + 1;
        }
    }
}

function fullScreen() {
    let de = document.documentElement;
    //调整兼容性
    de.requestFullScreen =
        de.requestFullScreen ||
        de.webkitRequestFullScreen ||
        de.mozRequestFullScreen;
    //计时器调用（无效）
    /*setTimeout(function () {
        de.requestFullScreen();
    }, 1);*/
    //点击事件调用（有效）
    de.onclick = function () {
        de.requestFullScreen();
    };
}


/******************************************************************/
let testStr = "{\n" +
    "  \"results\": [\n" +
    "    {\n" +
    "      \"statement_id\": 0,\n" +
    "      \"series\": [\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"getCardDetailService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"giveMemberCoupon\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              2\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"giveMemberCouponCallback\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"listCardByMemberIdService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"listCardConsume\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"listCouponKeysByFkIdsService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              6\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"listScoreConsumeByMemberIdService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              5\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberAuthenticationCodeService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              40\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              34\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              34\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              42\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              36\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              45\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              44\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              38\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              57\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              26\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberAuthenticationService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              15\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              8\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              9\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              13\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              12\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              15\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              8\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              8\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              15\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberCouponByFkIdService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              29\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              20\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              9\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              28\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              29\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              22\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              24\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              27\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberCouponListByStatusService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              25\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              9\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              22\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              21\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              11\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              6\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberCouponQueryListService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              7\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              2\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberGrantCouponService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              5\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              5\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              4\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberModifyService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberOrderCancelService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberOrderWriteOffService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              167\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              149\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              157\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              173\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              158\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              124\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              157\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              161\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              139\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              139\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberPayService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              8\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              4\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              7\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberQueryDetailByFkIdService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              2\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberQueryDetailByOpenIdOrTel\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              168\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              125\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              124\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              165\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              156\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              156\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              161\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              118\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              121\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              127\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberQueryDetailService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              9\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              11\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              15\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              12\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              12\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              14\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              13\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberRegisterByOpenIdService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"memberUseCouponByCouponIdNewService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"modifyMemberTelService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              1\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"modifyUnionIdByOpenId\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              3\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              4\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"orderRelationMemberService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              177\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              154\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              173\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              189\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              166\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              139\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              169\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              170\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              147\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              148\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"queryMemberCouponGiveStatus\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              2\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"receiveMemberCoupon\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              1\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              0\n" +
    "            ]\n" +
    "          ]\n" +
    "        },\n" +
    "        {\n" +
    "          \"name\": \"dapeng_service_process\",\n" +
    "          \"tags\": {\n" +
    "            \"method_name\": \"scoreIncreaseService\"\n" +
    "          },\n" +
    "          \"columns\": [\n" +
    "            \"time\",\n" +
    "            \"sum\"\n" +
    "          ],\n" +
    "          \"values\": [\n" +
    "            [\n" +
    "              \"2018-08-10T01:58:00Z\",\n" +
    "              0\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T01:59:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:00:00Z\",\n" +
    "              9\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:01:00Z\",\n" +
    "              11\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:02:00Z\",\n" +
    "              15\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:03:00Z\",\n" +
    "              12\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:04:00Z\",\n" +
    "              12\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:05:00Z\",\n" +
    "              14\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:06:00Z\",\n" +
    "              10\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:07:00Z\",\n" +
    "              6\n" +
    "            ],\n" +
    "            [\n" +
    "              \"2018-08-10T02:08:00Z\",\n" +
    "              13\n" +
    "            ]\n" +
    "          ]\n" +
    "        }\n" +
    "      ]\n" +
    "    }\n" +
    "  ]\n" +
    "}"
