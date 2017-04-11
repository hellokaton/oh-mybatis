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
        success: function (result) {
            $("#submitBtn").removeAttr("disabled");
            if (result && result.success) {
                console.log(result.payload);
                window.open("/static/temp/" + result.payload);
            } else {
                console.log(data)
                alert("操作失败");
            }
        },
        error: function (data, textStatus) {
            alert('操作失败');
            $("#submitBtn").removeAttr("disabled");
        }
    });
}
