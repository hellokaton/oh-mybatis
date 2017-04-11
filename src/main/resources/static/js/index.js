$(function () {
    initConfig();// 加载配置
});

function initConfig() {
    $("#form").validate();
    setDefaultValues();//加载cookie值
}

// 增加一列表名
function addItem() {
    var item = "";
    item += "<div class='form-group'>"
        + "<label class='col-lg-2 control-label'>表名</label>"
        + "<div class='col-xs-3'><input type='text' name='tableNames' class='form-control' /></div>"
        + "<label class='col-lg-2 control-label'>模型名</label>"
        + "<div class='col-xs-3'><input type='text' name='modelNames' class='form-control' /></div>"
        + "<a class='btn btn-success btn-xs' onclick='addItem()' title='增加'><span class='fui-check'>&nbsp;增加</span></a>&nbsp;"
        + "<a class='btn btn-danger btn-xs' onclick='redItem(this)' title='删除'><span class='fui-cross'>&nbsp;删除</span></a>"
        + "</div>";
    $("#form").append(item);
}

//删除一列表名
function redItem(para) {
    $(para).parent().remove();
}

//生成并下载
function doSubmit() {
    if ($("#form").valid(this, '填写信息不完整。') == false) {
        return;
    }
    if (typeof($("#submitBtn")) != "undefined") {
        $("#submitBtn").attr("disabled", "disabled");
    }
    $.ajax({
        type: 'post',
        url: '/gen',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data, textStatus) {
            $("#submitBtn").removeAttr("disabled");
            data = JSON.parse(data);
            if (data.rspCode == "000001") {
                alert("数据库连接失败，请检查您的数据库地址和数据库名");
            } else if (data.rspCode == "000002") {
                alert("数据库连接错误");
            } else if (data.rspCode == "000003") {
                alert("数据库连接错误");
            } else if (data.rspCode == "000004") {
                alert("发生错误，请检查您的用户名或密码");
            } else if (data.rspCode == "000005") {
                alert("操作失败");
            } else {
                //alert("操作成功");
                window.open(basePath + "/tmp" + data.zipName);
            }
        },
        error: function (data, textStatus) {
            alert('操作失败');
            $("#submitBtn").removeAttr("disabled");
        }
    });
}
