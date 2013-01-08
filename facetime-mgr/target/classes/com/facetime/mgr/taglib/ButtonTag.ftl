<#list menuList as operate>
<input 	type="${operate.types}"
		name="submit1"
		value="${operate.opername}"
		class="MyButton" accessKey="<#if operate.keys?exists>${operate.keys}<#else>#</#if>"
		onclick="<#if operate.clickname?exists>${operate.clickname};setOperId(this.id);<#else>#</#if>"
		image="<#if operate.picpath?exists>${operate.picpath}<#else>#</#if>"
		id="${operate.operid}"/>
</#list>
<script language="javascript">
	function setOperId(id){
		var str = "${rootPath}/pages/sysoperlog/write.do?_operid=" + id;
		$.get(str,function(data){
		   // do nothing
		});
	}
</script>