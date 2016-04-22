<%@ page contentType="text/html; charset=UTF-8"%>

<!-- Static navbar -->
<header id="header"
	class="navbar navbar-v navbar-static-top navbar-fixed-top animated headroom--top">
<nav class="container" role="navigation">
<div class="navbar-header">
	<button type="button" class="navbar-toggle collapsed"
		data-toggle="collapse" data-target="#navbar" aria-expanded="false"
		aria-controls="navbar">
		<span class="sr-only">导航</span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
	</button>
	<a class="navbar-brand" href="http://www.thinkweixin.com/" title="微信门户">微信门户</a>
</div>
<form class="navbar-form navbar-left" role="search"
	action="http://www.thinkweixin.com/search" id="formSearch">

	<div class="input-group">
		<div class="input-group-btn">
			<button type="button" class="btn btn-default dropdown-toggle"
				data-toggle="dropdown" id="chooseSearchType">
				文章
				<span class="caret"></span>
			</button>
			<ul class="dropdown-menu" id="searchType">
				<li id="searchArticle">
					<a href="javascript:void(0);" onclick="change(&#39;q&#39;)">文章</a>
				</li>
				<li id="searchAccount">
					<a href="javascript:void(0);" onclick="change(&#39;qa&#39;)">帐号</a>
				</li>
			</ul>
		</div>
		<!-- /btn-group -->
		<a href="javascript:void(0);"
			onclick="document.getElementById(&#39;formSearch&#39;).submit()">
			<span class="glyphicon glyphicon-search form-go"></span> </a>
		<input type="text" id="searchBox" name="q" class="form-control">

	</div>
	<!-- /input-group -->

</form>
<div id="navbar" class="navbar-collapse collapse">
	<ul class="nav navbar-nav">

		<li>
			<a href="http://www.thinkweixin.com/">最新</a>
		</li>

		<li>
			<a href="http://www.thinkweixin.com/hotarticle">爆文</a>
		</li>

		<li>
			<a href="http://www.thinkweixin.com/leaderboard" title="微信公众号排行榜">公众号排行榜</a>
		</li>

		<li>
			<a href="http://www.thinkweixin.com/articlesboard" title="微信文章排行榜">微信文章排行榜</a>
		</li>

	</ul>

</div>
<!--/.nav-collapse -->
</nav>
<script type="text/javascript">
    function change(type)
    {
        var searchBox = document.getElementById("searchBox");
        searchBox.name = type;
    }
</script>
</header>

<div class="container">
	<div class="row">
		<div class="col-xs-12 categorys">
			<ul>
				<s:iterator id="menuList1" value="menuList">
					<s:if test="isShow!=0">
						<li>
							<a href="http://www.thinkweixin.com/category/?id=104">${menuName}</a>
						</li>
					</s:if>
				</s:iterator>
				<li class="dropdown">
					<a href="javascript:void(0)" class="dropdown-toggle"
						data-toggle="dropdown"><i class="fa fa-ellipsis-h"></i><span
						class="sr-only">更多</span> </a>
					<ul class="dropdown-menu" role="menu">
						<s:iterator id="menulist2" value="menuList">
							<s:if test="isShow==0">
								<li>
									<a href="http://www.thinkweixin.com/category/?id=120">${menuName}</a>
								</li>
							</s:if>
						</s:iterator>
			</ul>
			</li>
			</ul>
		</div>
	</div>
</div>

