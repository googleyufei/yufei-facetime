<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="22">${msgTotal}${recordCount}&nbsp;&nbsp;&nbsp;${msgSizeOfPage}
			<select id="pageSize" name="pageSize" onchange="if(checks()) GotoPage();">
				<option value="10" <#if (pageSize == 10)> selected </#if> >10</option>
				<#list pageRank as rank>
					<option value="${rank}" <#if (rank==pageSize)>selected</#if>>${rank}</option>
				</#list>
				</select>&nbsp;${msgSize}&nbsp;&nbsp;
		</td>
		<td width="21%" height="22" valign="middle">
			<div align="center">
				<table border= "0" cellspacing= "0" cellpadding="0">
					<tr>
						<td width="20">
							<#if (pageNum>1) >
								<img src="${imgBasePath}first.gif" style="CURSOR:hand" onClick="MoveFirst()"/>
							<#else>
								<img src="${imgBasePath}unfirst.gif"/>
							</#if>
						</td>
						<td width="14">
							<#if (pageNum > 1)>
								<img src="${imgBasePath}forward.gif" style="CURSOR:hand" onClick="MovePrevious()"/>
							<#else>
								<img src="${imgBasePath}unforward.gif"/>
							</#if>
						</td>
						<td width="46" valign="botton">
							<table class="nav_outline" width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr><td><div align="center">${pageNum}/${pageCount}</div></td></tr>
							</table>
						</td>
						<td width="12" valign="right">
							<div align="right">
								<#if (pageCount >0) && (pageNum < pageCount)>
									<img src="${imgBasePath}backward.gif" style="CURSOR:hand" onClick="MoveNext()"/>
								<#else>
									<img src="${imgBasePath}unbackward.gif"/>
								</#if>
							</div>
						</td>
						<td width="19">
							<div align="right">
								<#if (pageCount>0) && (pageNum<pageCount)>
									<img src="${imgBasePath}last.gif" style="CURSOR:hand" onClick="MoveLast()"/></td>
								<#else>
									<img src="${imgBasePath}unlast.gif"/>
								</#if>
							</div>
						</td>
					</tr>
				 </table>
			</div>
		</td>

		<td height="22">&nbsp;&nbsp;${msgGo}
			<input type="text" name="toPage" id="toPage" maxlength="4"
				   class="nav_input" value="${pageNum}"
				   onfocus="this.select()"
				   onKeyPress="if(window.event.keyCode==13){ if(checks()) GotoPage(); else return false;}"/>${msgPage}&nbsp;&nbsp;
			<img src="${imgBasePath}view_search.gif" style="CURSOR:hand" onClick="if (checks()) GotoPage();"/>
		</td>
	</tr>
</table>

<script language="javascript">
	function MovePrevious(){
		if(${pageNum} - 1 >= 1){
			${formName}.toPage.value=${pageNum}-1;
			${formName}.pageSize.value=${pageSize};
			<#if action?exists>
				${formName}.action="${rootPath}${action};
			</#if>
			${submitFunction};
		}
	}

	function MoveNext(){
		if(${pageNum} + 1 <= ${pageCount}){
			${formName}.toPage.value=${pageNum}+1;
			${formName}.pageSize.value=${pageSize};
			<#if action?exists>
				${formName}.action="${rootPath}${action};
			</#if>
			${submitFunction};
		}
	}

	function MoveFirst(){
		if(${pageNum} > 1){
			${formName}.toPage.value=1;
			${formName}.pageSize.value=${pageSize};
			<#if action?exists>
				${formName}.action="${rootPath}${action};
			</#if>
			${submitFunction};
		}
	}

	function MoveLast(){
		if(${pageNum} < ${pageCount}){
			${formName}.toPage.value=${pageCount};
			${formName}.pageSize.value=${pageSize};
			<#if action?exists>
				${formName}.action="${rootPath}${action};
			</#if>
			${submitFunction};
		}
	}

	function GotoPage(){
			${formName}.toPage.value=formList.toPage.value;
			${formName}.pageSize.value=formList.pageSize.value;
			<#if action?exists>
				${formName}.action="${rootPath}${action};
			</#if>
			${submitFunction};
	}

	function checks(){
		var pattern = "^[+]?\d+$";
		var itemNameValue=formList.toPage.value;
		if (pattern.test(itemNameValue)){
			return true;
		}else{
			alert("${pageNumErr}");
			formList.toPage.focus();
			return false;
		}
	}
</script>