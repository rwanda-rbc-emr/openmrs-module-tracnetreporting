<h3><a href="#"><spring:message code="tracnetreporting.category.preartdataelement" /></a></h3>
<div>
	<div style="float: right">
		<!--  <openmrs:hasPrivilege privilege="Export TracNet Report"> -->
		<form action="tracnetreportingForm.list?id=preartdel" method="post">
			<input type="submit" name="exportPreArtData" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelPreArtData" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
		<!-- </openmrs:hasPrivilege>  -->
	</div>
	
	<table style="border: 0px">
		<c:forEach items="${indicatorsPreArtDataElement}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsPreArtDataElement_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>
<!-- indicatorsPreArtDataElement end --> 
	
<!-- indicatorsArtDataElement start -->
<h3><a href="#"><spring:message code="tracnetreporting.category.artdataelement" /></a></h3>
<div>
	<div style="float: right">
		<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
		<form action="tracnetreportingForm.list?id=artdel" method="post">
			<input type="submit" name="exportArtData" value="<spring:message code="tracnetreporting.label.exportdata" />" />
			<input type="submit" name="exportExcelArtData" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>	
		<!--  </openmrs:hasPrivilege> -->
	</div>
	
	<table>
		<c:forEach items="${indicatorsArtDataElement}" var="indicator" varStatus="num">
			<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
				<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
				<td>&nbsp;${indicatorsArtDataElement_msg[num.count-1]}</td>
				<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
			</tr>
		</c:forEach>
	</table>
</div>
<!-- indicatorsArtDataElement end --> 

						<!-- indicatorsStiOpportAndOthers start -->
						<h3><a href="#"><spring:message code="tracnetreporting.category.stiopportandothers" /></a></h3>
						<div>
							<div style="float: right">
								<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
								<form action="tracnetreportingForm.list?id=stidel" method="post">
									<input type="submit" name="exportStiOpportunData" value="<spring:message code="tracnetreporting.label.exportdata" />" />
									<input type="submit" name="exportExcelStiOpportunData" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
								</form>
								<!-- </openmrs:hasPrivilege>  -->
							</div>
							
							<table>
								<c:forEach items="${indicatorsStiOpportAndOthers}" var="indicator" varStatus="num">
									<tr class="${num.count%2!=0?'evenRow':'oddRow'}"> 
										<td><b><i>${indicator.key}</i></b>&nbsp;</td> 
										<td>&nbsp;${indicatorsStiOpportAndOthers_msg[num.count-1]}</td>
										<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
									</tr>
								</c:forEach>
							</table>
						</div>
						<!-- indicatorsStiOpportAndOthers end --> 
						
						<!-- indicatorsNutritionDataElem start -->
						<h3><a href="#"><spring:message code="tracnetreporting.category.nutritiondataelem" /></a></h3>
						<div>
							<div style="float: right">
								<!-- <openmrs:hasPrivilege privilege="Export TracNet Report">  -->
								<form action="tracnetreportingForm.list?id=nutridel" method="post">
									<input type="submit" name="exportNutritionData" value="<spring:message code="tracnetreporting.label.exportdata" />" />
									<input type="submit" name="exportExcelNutritionData" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
								</form>
								<!-- </openmrs:hasPrivilege>  -->
							</div>
							
							<table>
								<c:forEach items="${indicatorsNutritionDataElem}" var="indicator" varStatus="num">
									<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
										 <td><b><i>${indicator.key}</i></b>&nbsp;</td> 
										<td>&nbsp;${indicatorsNutritionDataElement_msg[num.count-1]}</td>
										<td><a href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetpatientList.list?indicator=${indicator.key}&startDate=${startDate}&endDate=${endDate}"><b>${indicator.value}</b></a></td>
									</tr>
								</c:forEach>
							</table>
						</div><!-- indicatorsNutritionDataElem end -->