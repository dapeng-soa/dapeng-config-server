/**
 * @author huyj
 * @Created  2018-08-03 11:06
 */
/*jQuery(document).ready(function(){
    alert(0)
    MergeTableCell();
});*/

$(document).ready(function () {
    fullScreen();
    initServiceTable();
});


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
