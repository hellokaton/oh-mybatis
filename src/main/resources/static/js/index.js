$(function () {
    recoverStore();
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

    storeToCookie();

    $.ajax({
        type: 'post',
        url: '/gen',
        dataType: 'json',
        data: $("#config-form").serialize(),
        success: function (result) {
            $("#submitBtn").removeAttr("disabled");
            if (result && result.success) {
                console.log(result.payload);
                window.open(result.payload);
            } else {
                console.log(result.msg)
                alert("操作失败");
            }
        },
        error: function (result) {
            alert('操作失败');
            $("#submitBtn").removeAttr("disabled");
        }
    });
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

function recoverStore() {
    $("#config-form input").each(function (k, v) {
        $(v).val($.cookie("MC_" + $(v).attr('name')));
    });
}

function storeToCookie() {
    values = $("#config-form").serializeArray();
    var values, index;
    for (index = 0; index < values.length; ++index) {
        $.cookie("MC_" + values[index].name, values[index].value, {expires: 7});
    }
}