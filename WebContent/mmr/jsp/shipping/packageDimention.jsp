<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<SCRIPT language="JavaScript">

	function sameAsAbove2(pos) {
		pos = pos-1;
			
			var packageTypesName = 'packageTypes'+pos;
			var lengthName = 'shippingOrder.packageArray[' + pos + '].length';
			var widthName = 'shippingOrder.packageArray[' + pos + '].width';
			var heightName = 'shippingOrder.packageArray[' + pos + '].height';
			var weightName = 'shippingOrder.packageArray[' + pos + '].weight';
			var descrName = 'shippingOrder.packageArray[' + pos + '].description';
			var codName = 'shippingOrder.packageArray[' + pos + '].codAmount';
			var insName = 'shippingOrder.packageArray[' + pos + '].insuranceAmount';
			
			//alert(document.getElementById(packageTypesName));
			if(document.getElementById(packageTypesName)!=null)
			{
				var packageTypeElement = document.getElementById(packageTypesName);
				var packageType = packageTypeElement.options[packageTypeElement.selectedIndex].value;
			}
			var length = document.getElementById(lengthName).value;
			var width = document.getElementById(widthName).value;
			var height = document.getElementById(heightName).value;
			var weight = document.getElementById(weightName).value;
			var descr = document.getElementById(descrName).value;
			var cod = document.getElementById(codName).value;
			var ins = document.getElementById(insName).value;
	  				
			var x = pos+1;
			if(document.getElementById(packageTypesName)!=null)
				document.getElementById('packageTypes'+x).value = packageType;
			document.getElementById('shippingOrder.packageArray[' + x + '].length').value = length;
			document.getElementById('shippingOrder.packageArray[' + x + '].width').value = width;
			document.getElementById('shippingOrder.packageArray[' + x + '].height').value = height;
			document.getElementById('shippingOrder.packageArray[' + x + '].weight').value = weight;
			document.getElementById('shippingOrder.packageArray[' + x + '].description').value = descr;
			document.getElementById('shippingOrder.packageArray[' + x + '].codAmount').value = cod;
			document.getElementById('shippingOrder.packageArray[' + x + '].insuranceAmount').value = ins;
  		}

	function allTheSame2(rows) {
			var packageTypesName = 'packageTypes'+0;
			var lengthName = 'shippingOrder.packageArray[' + 0 + '].length';
			var widthName = 'shippingOrder.packageArray[' + 0 + '].width';
			var heightName = 'shippingOrder.packageArray[' + 0 + '].height';
			var weightName = 'shippingOrder.packageArray[' + 0 + '].weight';
			var descrName = 'shippingOrder.packageArray[' + 0 + '].description';
			var codName = 'shippingOrder.packageArray[' + 0 + '].codAmount';
			var insName = 'shippingOrder.packageArray[' + 0 + '].insuranceAmount';
			
			//alert(document.getElementById(packageTypesName));
			if(document.getElementById(packageTypesName)!=null)
			{
				var packageTypeElement = document.getElementById(packageTypesName);
				var packageType = packageTypeElement.options[packageTypeElement.selectedIndex].value;
			}
			var length = document.getElementById(lengthName).value;
			var width = document.getElementById(widthName).value;
			var height = document.getElementById(heightName).value;
			var weight = document.getElementById(weightName).value;
			var descr = document.getElementById(descrName).value;
			var cod = document.getElementById(codName).value;
			var ins = document.getElementById(insName).value;

	 	
	  		
	  		for(var x=1;x<rows;x++) {
	  			if(document.getElementById(packageTypesName)!=null)
	  				document.getElementById('packageTypes'+x).value = packageType;
	  			document.getElementById('shippingOrder.packageArray[' + x + '].length').value = length;
  				document.getElementById('shippingOrder.packageArray[' + x + '].width').value = width;
  				document.getElementById('shippingOrder.packageArray[' + x + '].height').value = height;
  				document.getElementById('shippingOrder.packageArray[' + x + '].weight').value = weight;
  				document.getElementById('shippingOrder.packageArray[' + x + '].description').value = descr;
				document.getElementById('shippingOrder.packageArray[' + x + '].codAmount').value = cod;
				document.getElementById('shippingOrder.packageArray[' + x + '].insuranceAmount').value = ins;
	  		}
	  	}

	function validateOrder(packageType,numPackages) {	
		//var packageType = packageType;
		var numPackages = numPackages;
		//var packageType = ${shippingOrder.packageTypeId};
		//var numPackages = %{shippingOrder.quantity};	
		
		var error = '';
			
		for(var x=0;x<numPackages;x++) {
			
			var lengthName = 'shippingOrder.packageArray[' + x + '].length';
			var widthName = 'shippingOrder.packageArray[' + x + '].width';
			var heightName = 'shippingOrder.packageArray[' + x + '].height';
			var weightName = 'shippingOrder.packageArray[' + x + '].weight';
			var descrName = 'shippingOrder.packageArray[' + x + '].description';
			var codName = 'shippingOrder.packageArray[' + x + '].codAmount';
			var insName = 'shippingOrder.packageArray[' + x + '].insuranceAmount';
			
			var length = document.getElementById(lengthName).value;
			var width = document.getElementById(widthName).value;
			var height = document.getElementById(heightName).value;
			var weight = document.getElementById(weightName).value;
			var descr = document.getElementById(descrName).value;
			var cod = document.getElementById(codName).value;
			var ins = document.getElementById(insName).value;

			//alert(length + '/' + width + '/' + height + '/' + weight + '/' + descr);								
			var packageNumber = x+1;
			
			if(length.charAt(0) == '' || !isAllDigits(length) || length<=0) {	
				
				var msg =  'Package #' + packageNumber + ' has an invalid length\n';
				error += msg;
				
			}
			if(width.charAt(0) == ''  || !isAllDigits(width) || width<=0 ) {
				var msg =  'Package #' + packageNumber + ' has an invalid width\n';
				error += msg;
				
			}
			if(height.charAt(0) == '' || !isAllDigits(height) || height<=0) {
				var msg =  'Package #' + packageNumber + ' has an invalid height\n';
				error += msg;
			}
			if(weight.charAt(0) == ''  || !isAllDigits(weight) || weight<=0) {
				var msg =  'Package #' + packageNumber + ' has an invalid weight\n';
				error += msg;
			}
			if(cod.charAt(0) == ''  || !isAllDigits(cod) || cod<=0) {
				var msg =  'Package #' + packageNumber + ' has an invalid cod\n';
				error += msg;
			}
			if(ins.charAt(0) == ''  || !isAllDigits(ins) || ins<=0) {
				var msg =  'Package #' + packageNumber + ' has an invalid ins\n';
				error += msg;
			}
			//pallet		
			//if(packageType == 4) {
			//	if(descr == '') {
			//		var msg =  'Package #' + packageNumber + ' has no description\n';
			////		error += msg;
			//	}
			//}	
				
		}
		
		if(error != '') {
			alert(error);
			return false;
		}
	
		return true;	
	}
	
	function submitform(method)
	{
	 //alert("AHA");	
	 document.orderForm.action = "shipment.stageThree.action?method="+method;
	 document.orderForm.submit();
	}  
	
	function populateDimensions(index, val){
		var lengthid = "shippingOrder.packageArray["+index+"].length";
		var widthid = "shippingOrder.packageArray["+index+"].width";
		var heightid = "shippingOrder.packageArray["+index+"].height";
		var weightid = "shippingOrder.packageArray["+index+"].weight";
		var descid = "shippingOrder.packageArray["+index+"].description";
		
		//alert('show::'+index+"| value::"+val);
		
		ajax_Country_desc=ajaxFunction();	 
		ajax_Country_desc.onreadystatechange=function()
	  	{
		   if(ajax_Country_desc.readyState==4 && ajax_Country_desc.status==200)
			{
						
			response_desc=ajax_Country_desc.responseText;
			//alert("Response: \n"+response_desc);
			js_stateid=document.getElementById("hide_this");
			//alert(js_stateid);
			js_stateid.innerHTML= response_desc;
			
			var sess_length= document.getElementById("pt_length").value;
			//alert("sess_length::"+sess_length);
			document.getElementById(lengthid).value=sess_length;
			
			var sess_width= document.getElementById("pt_width").value;
			//alert("sess_width::"+sess_width);
			document.getElementById(widthid).value=sess_width;
						
			var sess_height= document.getElementById("pt_height").value;
			//alert("sess_height::"+sess_height);
			document.getElementById(heightid).value=sess_height;
			
			var sess_weight= document.getElementById("pt_weight").value;
			//alert("sess_weight::"+sess_weight);
			document.getElementById(weightid).value=sess_weight;
			
			var sess_desc= document.getElementById("pt_desc").value;
			//alert("sess_desc::"+sess_desc);
			document.getElementById(descid).value=sess_desc;
			}			
			
	  }	
	  	if(val > 0)
	  	{
			url="populatePackageTypes.action?index="+val;
			ajax_Country_desc.open("GET",url,true);
			ajax_Country_desc.send(this);
		}
		else //reset the values
		{
			document.getElementById(lengthid).value="1";
			document.getElementById(widthid).value="1";
			document.getElementById(heightid).value="1";
			document.getElementById(weightid).value="1.0";
			document.getElementById(descid).value="";
		}
		
			   
		}
</script>


<div id="dimensions">
<div id="pckg_results">
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<mmr:message messageId="label.shippingOrderTab.dimension"/>:
</div>
<div id="pckg_result_tbl">
	<s:include value="hidden_packageTypes.jsp"/>
		  <table border="0" width="1080px" cellspacing="0" cellpadding="4" style="margin-left:10px">

                  <tr bgcolor="#cccccc">
					<td class="tableTitle2">#</td>
					<s:if test="%{#session.PackageTypes.size > 0}">
						<td class="tableTitle2">Choose a PackageType</td>
					</s:if>
	                <td class="tableTitle2">L(in)</td>
	                <td class="tableTitle2">W(in)</td>
	                <td class="tableTitle2">H(in)</td>
	                <td class="tableTitle2">Wt (lbs)</td>
	                <td class="tableTitle2">COD Amt ($)</td>
	                <td class="tableTitle2">Ins Amt ($)</td>
					<s:if test='%{shippingOrder.packageTypeId.type == "type_pallet"}'>
                    	<td class="tableTitle2">Freight Class</td>
                    	<td class="tableTitle2">Type</td>
					</s:if>    
					<td class="tableTitle2">Description</td>
                	<td class="tableTitle2"></td>
               
				
				</tr>
                
				<s:set name="packageType" value="%{shippingOrder.packageTypeId.type}"/>
				<s:iterator  value="shippingOrder.packages"  status="counterIndex">
 				<tr>
					<td class="pckg_text_03"><s:property value="%{#counterIndex.index+1}"/></td>
					<s:if test="%{#session.PackageTypes.size > 0}">
						<td class="pckg_text_03"><s:select id="packageTypes%{#counterIndex.index}" name="shippingOrder.packages[%{#counterIndex.index}].type" cssClass="text_01_combo_big" cssStyle="width:138px;" listKey="packageTypeId" listValue="packageName" list="#session.PackageTypes" headerKey="-1" headerValue=" " onchange="populateDimensions(%{#counterIndex.index}, this.value);"/></td>
					</s:if>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].length" name="shippingOrder.packages[%{#counterIndex.index}].length"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].length}"/></td>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].width" name="shippingOrder.packages[%{#counterIndex.index}].width"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].width}"/></td>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].height" name="shippingOrder.packages[%{#counterIndex.index}].height"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].height}"/></td>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].weight" name="shippingOrder.packages[%{#counterIndex.index}].weight"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].weight}"/></td>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].codAmount" name="shippingOrder.packages[%{#counterIndex.index}].codAmount" cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].codAmount}"/></td>
	                <td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].insuranceAmount" name="shippingOrder.packages[%{#counterIndex.index}].insuranceAmount"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.packages[#counterIndex.index].insuranceAmount}"/></td>
					<!-- Package Type Pallet = 4 -->
					<s:if test='%{#packageType == "type_pallet"}'>
                    	<td class="text_01">
                    	<s:select cssClass="text_01_combo_big" name="shippingOrder.packages[%{#counterIndex.index}].freightClass" list="{50,55,60,65,70,77,77.5,85,92.5,100,110,125,150,175,200,250,300,400}" value="%{#session.shippingOrder.packages[#counterIndex.index].freightClass}" cssStyle="width: 45px;"/>
                    		
      					</td>
                    	
                    	<td class="text_01">
                    	<s:select cssClass="text_01_combo_big" name="shippingOrder.packages[%{#counterIndex.index}].type" list="{'Pallet','Carton','Crate','Drum','Boxes','Rolls','Pipes/TubesBales','Bags','Cylinder','Pails,Reels', 'Other'}" value="%{#session.shippingOrder.packages[#counterIndex.index].type}" cssStyle="width: 144px;"/>
                    	</td>
					</s:if>      
					<td class="text_01"><s:textfield id="shippingOrder.packageArray[%{#counterIndex.index}].description" name="shippingOrder.packages[%{#counterIndex.index}].description" value="%{#session.shippingOrder.packages[#counterIndex.index].description}" cssClass="text_02_tf" theme="simple" size="20"/></td>
                 	<td class="pckg_text_03">
					
					<s:if test="%{#counterIndex.index == 0}">
						<a href="javascript:void(0)" onClick="allTheSame2('35')"/>All same</a>
					</s:if>
					
					<s:else>
					
					<a href="javascript:void(0)" onClick="sameAsAbove2('<s:property value="%{#counterIndex.index}"/>')"  />As above
					<s:param name="view" value="<s:property value='%{#counterIndex.index}'/>"  />
					</a> 
					</s:else>
					</td>
                </tr>
                </s:iterator>
              </table>			
	
	</div>
	<div id="pckg_res_tbl_end"></div>
	</div>