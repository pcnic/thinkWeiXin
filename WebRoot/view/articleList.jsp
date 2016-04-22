<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
	<%@ include file="/common/meta.jsp"%>
	<body>
		<%@ include file="/common/header.jsp"%>
	<div class="container">
        <div class="row">
            <div class="col-xs-12 col-md-8 articles main">
            <s:iterator value="articleList">    
            <article>
                  <div class="articleThumbnail pull-left"><a target="_blank" title="买604辆劳斯莱斯和7000辆豪车，地球最任性的人非他莫属！" href="http://www.thinkweixin.com/v/c3a180"><img src="../files/c3a180.jpg" width="100%" height="100%" alt="买604辆劳斯莱斯和7000辆豪车，地球最任性的人非他莫属！" title="买604辆劳斯莱斯和7000辆豪车，地球最任性的人非他莫属！"></a><a class="type-sign" href="http://www.thinkweixin.com/category/?id=119">兴趣·爱好</a></div>
                  <div class="articleInfo">
                      <header><h2><a target="_blank" href="http://www.thinkweixin.com/v/c3a180">${articleId}</a></h2></header>
                      <p class="des">文莱苏丹哈吉•哈桑纳尔•博尔基亚，是世界上最富裕的人之一，他居住的努洛伊曼皇宫面积是英国女王白金汉宫的4倍大</p>
                      <footer>
                          <author><a href="http://www.thinkweixin.com/a/b7j4y">奢侈品</a></author> • 
                          <time pubdate="" datetime="42分钟前" class="postDate">42分钟前</time> • 
                          <span><a href="http://www.thinkweixin.com/?page=2#" onclick="window.open(&#39;http://service.weibo.com/share/share.php?url=http://www.thinkweixin.com/v/c3a180&amp;title=【买604辆劳斯莱斯和7000辆豪车，地球最任性的人非他莫属！】文莱苏丹哈吉•哈桑纳尔•博尔基亚，是世界上最富裕的人之一，他居住的努洛伊曼皇宫面积是英国女王白金汉宫的4倍大&amp;appkey=115590404&amp;ralateUid=1832155780&#39;, &#39;_blank&#39;, &#39;width=550,height=370&#39;); recordOutboundLink(this, &#39;Share&#39;, &#39;weibo.com&#39;);" class="tb">分享</a></span>
                      </footer>
                  </div>
              </article>
            </s:iterator>  
            <s:if test="articlelist!=null">
                 <nav><ul class="pagination pagination-v">
            	<li><a href="http://www.thinkweixin.com/?page=1">上一页</a></li>
                <li><a href="http://www.thinkweixin.com/?page=1">1</a></li><li class="active"><a>2<span class="sr-only">(current)</span></a></li><li><a href="http://www.thinkweixin.com/?page=3">3</a></li><li><a href="http://www.thinkweixin.com/?page=4">4</a></li><li><a href="http://www.thinkweixin.com/?page=5">5</a></li><li><a href="http://www.thinkweixin.com/?page=6">6</a></li><li class="disabled"><a href="http://www.thinkweixin.com/?page=2#">. . .</a></li><li><a href="http://www.thinkweixin.com/?page=6455">6455</a></li>
                <li><a href="http://www.thinkweixin.com/?page=3">下一页</a></li></ul></nav>
            </s:if>       
                
            </div>
            <div class="col-xs-12 col-md-4 side">

	</div>


		<%@ include file="/common/footer.jsp"%>
	</body>
</html>