$(function () {

});

var tableItems = [];
var rowid = 0;

// 增加一列表名
function addItem() {
    rowid++;
    var item = $('#row-tpl').attr('rowid', rowid).html();
    $("#gen-btn").before(item);
}

//删除一列表名
function removeItem(para) {
    $(para).parents('.tab-items:eq(0)').remove();
}

//生成并下载
function doSubmit() {
    $("#form").validate();
    if ($("#config-form").valid(this, '填写信息不完整。') == false) {
        return;
    }
    if (typeof($("#submitBtn")) != "undefined") {
        $("#submitBtn").attr("disabled", "disabled");
    }

    console.log($("#config-form").serialize());
    return;
    /*$.ajax({
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
     });*/
}

function changeCkbox(obj, id) {
    var checked = $(obj).is(":checked");
    if (checked) {
        $(id).val('1');
        if (id == 'isAlltable') {
            $('#add-item').hide();
            $('.tab-items').remove();
        }
    } else {
        $(id).val('0');
        if (id == 'isAlltable') {
            $('#add-item').show();
        }
    }
}