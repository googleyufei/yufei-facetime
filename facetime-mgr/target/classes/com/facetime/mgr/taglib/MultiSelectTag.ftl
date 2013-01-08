<script language=javascript>
 	function Expand${name}(imgObj){
 		var topX=window.event.x-10-${width};
 		var topY=window.event.y+10;
 		var whichLay=eval('${layName}');
 		if (whichLay.style.display == 'none'){
 			whichLay.style.display = 'block';
 			whichLay.style.left=topX;
 			whichLay.style.top=topY;
 			imgObj.innerText='${upImg}';
 		}else{
 			whichLay.style.display = 'none';
 			imgObj.innerText='${downImg}';
 		}
 	}
 	
 	function  MultiSelect${name}(chkBox){
 		var objTxt=document.getElementById('${txtName}');
 		var obj=document.getElementById('${name}');
 		var objChks=document.getElementsByName('${chkName}');
 		
 		if(chkBox.value==''){
 			for(var i=1; i < objChks.length; i++){
 				objChks[i].checked=chkBox.checked;
 			}
 		}else{
 			if(!chkBox.checked){
 				objChks[0].checked=false;
 			}
 		}
 		
 		if(objChks[0].checked){
 			objTxt.value='${allkey}';
 			obj.value='';
 		}else{
 			var  txtInfo='';
 			var  valueInfo='';
 			for(var i=1; i < objChks.length; i++){
 				if(objChks[i].checked){
 					txtInfo += objChks[i].des+' ';
 					valueInfo += objChks[i].value+'${separator}';
 				}
 				
 				objTxt.value=txtInfo;
 				obj.value=(valueInfo == "" ? "" :  valueInfo.substring(0,valueInfo.length-1));
 			}
 		}
 	}
 </script>
<input class='underLine' readOnly='readOnly' value='<#if defaultValue??>${defaultInfo}<#else>${allkey}</#if>' name='${txtName}' id='${txtName}' style='width:${width}px;'><input type='hidden' name='${name}' id='${name}' value='<#if defaultValue??>${defaultValue}<#else>${allvalue}</#if>'><a style='CURSOR: hand' onclick=Expand${name}(this)>${upImg}</a> 
<div id='${layName}' style='Z-INDEX: 1; display:none; position: absolute'>
	<table class=tboutline cellspacing=0 cellpadding=0 bgcolor=#ffffff>
		<tbody>
			<tr>
				<td valign=top>
					<div style='overflow: auto; width:${width}px;height:${height}px'>
				 		<input onclick='MultiSelect${name}(this)' type='checkbox' <#if defaultValue?exists><#else>checked</#if> value='' name='${chkName}'/> ${allkey} <br/>
						 <#list ketSet as key>
						 	<input onclick='MultiSelect${name}(this)' type='checkbox' 
									<#if defaultValue??>
										<#list values as val><#if (val==key)> checked </#if></#list>
									<#else>
										checked
									</#if>
									value='${key}' name='${chkName}' des='${dataMap[key]}'/> ${dataMap[key]} <br/>	
						 </#list>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>			