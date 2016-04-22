<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
	<%@ include file="/common/meta.jsp"%>
	<body>
		<%@ include file="/common/header.jsp"%>
			<tr>
				<td height="20" colspan="2" class="text">
					ID
				</td>
				<td height="20" colspan="2" class="text">
					名称
				</td>
			</tr>
			<s:iterator id="accountlist" value="accountlist">
				<tr>
					<td height="20" colspan="2" class="text">
						<s:property value="accountId" />
					</td>
					<td height="20" colspan="2" class="text">
						<s:property value="accountName" />
					</td>
				</tr>
			</s:iterator>
		<%@ include file="/common/footer.jsp"%>
	</body>
</html>