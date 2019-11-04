;(function (doc, win) {
  var docEl = doc.documentElement,
    resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    recalc = function () {
      var clientWidth = docEl.clientWidth;
      if (!clientWidth) return;
      // if(clientWidth>=450) clientWidth = 450;
      docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
      //这里的750可以随便改，但最好满足跟设计稿统一
      //比如:
      // 1.现在需要在1080的尺寸下开发
      // 2.我们先把设计稿等比压缩或者等比拉伸至1080
      // 3.然后把当前页面中750改成1080即可
      // 为什么这么改，只是方便我们使用，比如现在设计稿上面是28号的字体，我们在页面上写0.28rem;也就是等比缩小一百倍，拿来好计算，然后直接写到页面中去。怎么检查是否正确，打开你要开发的尺寸，他的根目录大小为整数50即可。
      // 这里的其他代码基本为兼容性写法而已，无需关心
    };
  if (!doc.addEventListener) return;
  win.addEventListener(resizeEvt, recalc, false);
  doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);