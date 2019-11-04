$.ajaxSetup({
	cache : false,
	error : function(xhr, textStatus, errorThrown) {
		var msg = xhr.responseText;
		var response = JSON.parse(msg);
		var code = response.code;
		var message = response.message;
		if (code == 400) {
			layer.msg(message);
		} else if (code == 401) {
			layer.msg('未登录');
		} else if (code == 403) {
			console.log("未授权:" + message);
			layer.msg('未授权');
		} else if (code == 500) {
			layer.msg('系统错误：' + message);
		}
	}
});

function buttonDel(data, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<div class='link' title='删除' onclick='del(\"" + data +"\")'>删除</div>");

    return btn.prop("outerHTML");
}

function buttonAuting(data, permission, pers){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }

    var btn = $("<div class='link' title='审核' onclick='auting(\"" + data +"\")'>审核</div>");
    return btn.prop("outerHTML");
}

function buttonEdit(href, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	// var btn = $("<button class='layui-btn layui-btn-xs' title='编辑' onclick='window.location=\"" + href +"\"'><i class='layui-icon'>&#xe642;</i></button>");
	var btn = $("<div class='link' title='编辑' onclick='window.location=\"" + href +"\"'>编辑</div>");
	return btn.prop("outerHTML");
}
function buttonScan(href, permission, pers){
    // if(permission != ""){
    //     if ($.inArray(permission, pers) < 0) {
    //         return "";
    //     }
    // }
    var btn = $("<div class='link code' title='溯源码'>溯源码</div>");
    return btn.prop("outerHTML");
}
function buttonRecord(href, permission, pers){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }

    var btn = $("<div class='link' title='农事记录' onclick='window.location=\"" + href +"\"'>农事记录</div>");
    return btn.prop("outerHTML");
}




function deleteCurrentTab(){
	var lay_id = $(parent.document).find("ul.layui-tab-title").children("li.layui-this").attr("lay-id");
	parent.active.tabDelete(lay_id);
}
