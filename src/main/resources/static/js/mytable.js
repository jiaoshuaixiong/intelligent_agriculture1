$(function(){
	$('.main-table').on('click','.delete',function(){
		layer.confirm('确认删除?', {icon: 3, title:'友情提示',shadeClose:true}, function(index){
		  layer.close(index);
		});
	})
	$('.main-table').on('click','.check',function(){
		layer.confirm('请确认审核通过?', {icon: 3, title:'友情提示',shadeClose:true}, function(index){
		  layer.close(index);
		});
	})
})
function myconfirm(msg,url){
	layui.use(['form','upload','layer'], function() {
		var form = layui.form;
        var upload = layui.upload,layer = layui.layer;
		//监听提交

        upload.render({
            elem: '#structurePicture',//绑定的元素
            url: '/productBatchess',
			type : 'post',
			data: {'productName':'123'},
            done: function(res, index, upload){ //假设code=0代表上传成功
                layer.close(layer.index); //它获取的始终是最新弹出的某个层，值是由layer内部动态递增计算的
                if(res.msg=="上传成功"){
                    //提示上传成功(关闭之后将按钮的内容变为更换图片)
                    layer.msg(res.msg, {icon: 1,time:2*1000},function () {
                        $("#structurePicture").text("更换主要课程关系结构图");//按钮标题置为上传图片
                        $("#promptDiv").css("display","none");//隐藏提示语
                        $("#imgDiv").css("display","");//显示图片框
                    });
                }

            }
		// form.on('submit(formDemo)', function(formdata) {
            //    $.ajax({
            //        type : 'post',
            //        url : '/productBatchess',
            //        data : JSON.stringify(formdata.field),
            //        success : function(data) {
            //            layer.msg("添加成功", {shift: -1, time: 1000}, function(){
            //                location.href = url;
            //            });
            //        }
            //    });
            // 	setTimeout(function() {
            // 		location.href = url
            // 	}, 1000)
            // 	return false;
		});
	})
}
