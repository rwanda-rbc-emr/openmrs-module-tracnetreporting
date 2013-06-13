<h3><a href="#"><spring:message code="tracnetreporting.category.antenataldataelem" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
			<form action="tracnetreportingForm.list?id=andel" method="post">
				<input type="submit" name="exportAntenatalDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
				<input type="submit" name="exportExcelAntenatalDataElem" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
			</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsAntenatalDataElem}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsAntenatalDataElement_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
	</div>
				
<h3><a href="#"><spring:message code="tracnetreporting.category.maternitydataelem" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
			<form action="tracnetreportingForm.list?id=mdel" method="post">
				<input type="submit" name="exportMaternityDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
				<input type="submit" name="exportExcelMaternityDataElem" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
			</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsMaternityDataElem}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorMaternityDataElement_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>
					
<h3><a href="#"><spring:message code="tracnetreporting.category.hivexposedinfantfollowup" /></a></h3>
<div>
	<div style="float: right">
		<!--  <openmrs:hasPrivilege privilege="Export TracNet Report"> -->
			<form action="tracnetreportingForm.list?id=ifp" method="post">
				<input type="submit" name="exportInfantFollowup" value="<spring:message code="tracnetreporting.label.exportdata" />" />
				<input type="submit" name="exportExcelInfantFollowup" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
			</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsHivExposedInfantFollowup}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsHivExposedInfantFollowup_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>

<h3><a href="#"><spring:message code="tracnetreporting.category.familyplandataelem" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
		<form action="tracnetreportingForm.list?id=fpel" method="post">
			<input type="submit" name="exportFamilyPlanningDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelFamilyPlanningDataElem" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsFamilyPlanningDataElem}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsFamilyPlanningDataElem_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>

<h3><a href="#"><spring:message code="tracnetreporting.category.subminvctdataelem" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
		<form action="tracnetreportingForm.list?id=vctdel" method="post">
			<input type="submit" name="exportVctDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelVctDataElem" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
						
	<table>
		<c:forEach items="${indicatorsSubmitVctDataElem}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsSubmitVctDataElem_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>

<h3><a href="#"><spring:message code="tracnetreporting.category.pitdataelem" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
		<form action="tracnetreportingForm.list?id=pitdel" method="post">
			<input type="submit" name="exportPitDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelPitDataElem" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsProviderTestPitDataElem}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsProviderTestPitDataElem_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>
			
<h3><a href="#"><spring:message code="tracnetreporting.category.pepdataelem" /></a></h3>
<div>
	<div style="float: right">
		<!--  <openmrs:hasPrivilege privilege="Export TracNet Report"> -->
		 <form action="tracnetreportingForm.list?id=pepdel" method="post">
			<input type="submit" name="exportPepDataElem" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelPreArtData" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table>
	<c:forEach items="${indicatorsPepDataElem}" var="indicator" varStatus="num">
		<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
			<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsPepDataElem_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>