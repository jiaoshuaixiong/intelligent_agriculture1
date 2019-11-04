(function(){
//	{
//  imgListParent: "imgDefault",
//  imgItemClass: "imgItem",
//  isShowPage: true
//}
    var LightBox = function(options){
//      this.imgListParent = document.getElementById(options.imgListParent);   //图片列表的父元素
		this.imgListParent = document.querySelectorAll('.'+options.imgListParent)
		console.log(this.imgListParent)
//		console.log(document.querySelectorAll('.'+options.imgListParent))
        this.imgItemClass = options.imgItemClass;   //图片的className
        this.idx = 0;  //图片的索引，初始值为0
        this.isShowPage = options.isShowPage || false;    //是否显示分页，默认不显示
        this.init();
    };
    //初始化
    LightBox.prototype.init = function(){
        this.renderDOM();
        this.smallImgsListClick();
//      this.imgListClick();
        this.nextBtnClick();
        this.prevBtnClick();
        this.closeBtnClick();
    };
    //渲染弹窗
    LightBox.prototype.renderDOM = function(){
        var imgModule = document.createElement("div");
        imgModule.id = "imgModule";

        var oHtml = "";
        oHtml += '<div class="mask"></div>';
        oHtml +=    '<div class="lightBox">';
        oHtml +=        '<div class="lightBoxContent">';
        oHtml +=            '<img src="" alt="" id="imgLoader">';
        oHtml +=            '<img alt="" id="imgLight">';
        oHtml +=        '</div>';
        oHtml +=        '<span class="btn lightBoxSprite" id="lightBoxPrev"></span>';
        oHtml +=        '<span class="btn lightBoxSprite" id="lightBoxNext"></span>';
        oHtml +=        '<span class="closeBtn lightBoxSprite" id="closeBtn"></span>';
        oHtml +=        '<div class="lightBoxPagination" id="lightBoxPagination"></div>';
        oHtml +=    '</div>';

        imgModule.innerHTML = oHtml;
        document.body.appendChild(imgModule);
    };
    //分页
    LightBox.prototype.pagination = function(idx){
        var imgList = this.getByClass(this.imgListParent, this.imgItemClass);
//      console.log(imgList)
        var pagination = document.getElementById("lightBoxPagination");
        var page = "";

        for(var i = 0; i < imgList.length; i++){
            if(idx == i){
                page += '<span class="current"></span>';
            }else{
                page += '<span></span>';
            }            
        }
        if(this.isShowPage){
            pagination.innerHTML = page;
        }        
    };
//我的改造点击图片弹出弹窗
    LightBox.prototype.smallImgsListClick = function(){
		var myimglist = this.getByClass('document',this.imgItemClass);
		console.log(myimglist)	
		var imgModule = document.getElementById("imgModule");
		var self = this;
    	for(var i=0;i<myimglist.length;i++){
    		myimglist[i].onmouseenter = function(){
//  			console.log(this.parentNode.children)
    			var newimglist = this.parentNode.children;
    			console.log(newimglist)
    			for(var i = 0; i < newimglist.length; i++){
		            newimglist[i].index = i;
		            newimglist[i].onclick = function(){
//		            	console.log(1)
		                imgModule.style.display = "block";
		                var src = this.getAttribute("data-src");
		                self.idx = this.index;
		                self.imgLoad(src);
		                self.pagination(self.idx);
		            }
		        }
    		}
    	}
    }
    //点击图片弹出弹窗
//  LightBox.prototype.imgListClick = function(){
//      var imgList = this.getByClass(this.imgListParent, this.imgItemClass);
//      var imgModule = document.getElementById("imgModule");
//      var self = this;
//		console.log(imgList)
//      for(var i = 0; i < imgList.length; i++){
//          imgList[i].index = i;
//			
//          imgList[i].onclick = function(){
//          	console.log(1)
//              imgModule.style.display = "block";
//              var src = this.getAttribute("data-src");
//              self.idx = this.index;
//
//              self.imgLoad(src);
//              self.pagination(self.idx);
//          }
//      }
//  };
    //上一张
    LightBox.prototype.prevBtnClick = function(){
        var prevBtn = document.getElementById("lightBoxPrev");
        var self = this;

        prevBtn.onclick = function(){
            var imgList = self.getByClass(self.imgListParent, self.imgItemClass);
            
            self.idx--;

            if(self.idx < 0){  
                self.idx = imgList.length - 1;  
            }

            var src = imgList[self.idx].getAttribute("data-src");
            self.imgLoad(src);
            self.pagination(self.idx);
        }
    };
    //下一张
    LightBox.prototype.nextBtnClick = function(){
        var nextBtn = document.getElementById("lightBoxNext");
        var self = this;
        
        nextBtn.onclick = function(){
            var imgList = self.getByClass(self.imgListParent, self.imgItemClass);

            self.idx++;            

            if(self.idx >= imgList.length){  
                self.idx = 0;  
            }

            var src = imgList[self.idx].getAttribute("data-src");
            self.imgLoad(src);
            self.pagination(self.idx);
        }
    };
    //图片预加载
    LightBox.prototype.imgLoad = function(src, callback){
        var imgLight = document.getElementById("imgLight");
        var loader = document.getElementById("imgLoader");
        loader.style.display = "block";
        // imgLight.src = "";

        var img = new Image();
        img.onload = function(){
            loader.style.display = "none";
            imgLight.src = src;
        };
        img.src = src;
    };
    //关闭弹窗
    LightBox.prototype.closeBtnClick = function(){
        var closeBtn = document.getElementById("closeBtn");
        var imgModule = document.getElementById("imgModule");
        
        closeBtn.onclick = function(){
            imgModule.style.display = "none"; 
        }
    };
    //封装获取元素函数
    LightBox.prototype.getByClass = function(oParent, oClass){
        var oEle = document.getElementsByTagName("*");
        var oResult = [];
        for(var i = 0; i < oEle.length; i++){
            if(oEle[i].className == oClass){
                oResult.push(oEle[i]);
            }
        }
        return oResult;
    };
    window.LightBox = LightBox;

})();
new LightBox({
    imgListParent: "imgDefault",
    imgItemClass: "imgItem",
    isShowPage: true
});