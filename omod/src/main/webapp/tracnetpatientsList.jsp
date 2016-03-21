<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<!-- <openmrs:require privilege="View TRACNet Reporting" otherwise="/login.htm" redirect="/module/@MODULE_ID@/tracnetreportingForm.list" />  -->

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.dataTables.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/styles/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/styles/demo_table.css" />

<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$('#example').dataTable( {
					"sPaginationType": "full_numbers"
				} );
			} );
</script>

<h2><spring:message code="tracnetreporting.label.title.patients" /></h2>
<br/>
<!--<p>
 <a
	href="${pageContext.request.contextPath}/module/tracnetreporting/tracnetreportingForm.list?submitPeriod=&startDate=${startDate}&endDate=${endDate}"><spring:message
	code="tracnetreporting.label.back.link" /></a></p> -->
	
<!-- <form action="tracnetpatientList.list?export=csv" method="post"> -->

	<c:set var="patientsToBeExported" value="" />
	<input type="hidden" name="trackingPatients" value="${patientsToBeExported}" />
	
	<b class="boxHeader">${listTitle}</b>
	<!--   <div style="float: right">
		<form action="tracnetpatientList.list?indicator=${param.indicator}&startDate=${param.startDate}&endDate=${param.endDate}&export=csv" method="get">
			<input type="submit" name="exportArtData" value="<spring:message code="tracnetreporting.label.exportdata" />" />
		</form>	
		
		<form action="tracnetpatientList.list?indicator=${param.indicator}&startDate=${param.startDate}&endDate=${param.endDate}&export=xls" method="get">
			<input type="submit" name="exportPatientsToExcel" value="<spring:message code="tracnetreporting.label.exportexcel" />" />
		</form>
	</div> 
	

	
	<c:set var="counter" value="0" />  -->

<!--</form> -->

	<table class="patientEncounters" cellspacing="0" cellpadding="2" height="">
		<tr>
			<th class="tableTitle" colspan="7"></th>
		</tr>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
		
			<!-- The header of the table -->
			<thead>
				<tr>
					<th></th>
					<th><spring:message code="tracnetreporting.label.patient.id" /></th>
					<th><spring:message code="tracnetreporting.label.patient.givenName" /></th>
					<th><spring:message code="tracnetreporting.label.patient.familyName" /></th>
					<th><spring:message code="tracnetreporting.label.patient.age" /></th>
					<th><spring:message code="tracnetreporting.label.patient.gender" /></th>
					<th><spring:message code="tracnetreporting.label.patient.dashboard" /></th>
				</tr>
			</thead>		
			<!-- The body of the table -->
			<tbody>
			
				<c:if test="${PreArtDataElements!=null}">
					<c:set var="patientsToBeExported" value="preArtPatients" />
				</c:if>	
				
				<c:forEach items="${PreArtDataElements}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.patientIdentifier}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${ArtDataElements!=null}">
					<c:set var="patientsToBeExported" value="artPatients" />
				</c:if>	
				
				<c:forEach items="${ArtDataElement}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.patientIdentifier}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${StiOpportAndOthers!=null}">
					<c:set var="patientsToBeExported" value="stiPatients" />
				</c:if>	
				
				<c:forEach items="${StiOpportAndOthers}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${NutritionDataElem!=null}">
					<c:set var="patientsToBeExported" value="nutritionPatients" />
				</c:if>	
				
				<c:forEach items="${NutritionDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${AntenatalDataElem!=null}">
					<c:set var="patientsToBeExported" value="antenatalPatients" />
				</c:if>	
				
				<c:forEach items="${AntenatalDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				<c:if test="${MaternityDataElem!=null}">
					<c:set var="patientsToBeExported" value="maternityPatients" />
				</c:if>	
				
				<c:forEach items="${MaternityDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${HivExposedInfantFollowup!=null}">
					<c:set var="patientsToBeExported" value="hivexposedPatient" />
				</c:if>
				
				<c:forEach items="${HivExposedInfantFollowup}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.patientIdentifier}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${FamilyPlanningDataElem!=null}">
					<c:set var="patientsToBeExported" value="fpPatient" />
				</c:if>
				
				<c:forEach items="${FamilyPlanningDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${SubmitVctDataElem!=null}">
					<c:set var="patientsToBeExported" value="vctPatient" />
				</c:if>
				
				<c:forEach items="${SubmitVctDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../module/vcttrac/vctClientDashboard.form?clientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${ProviderTestPitDataElem!=null}">
					<c:set var="patientsToBeExported" value="pitPatient" />
				</c:if>
				<c:forEach items="${ProviderTestPitDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../module/vcttrac/vctClientDashboard.form?clientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
				<c:if test="${PepDataElem!=null}">
					<c:set var="patientsToBeExported" value="pepPatient" />
				</c:if>
				
				<c:forEach items="${PepDataElem}" var="patient" varStatus="num">
					<tr class="${num.count%2!=0?'evenRow':'oddRow'}">
						<td>${num.count}.</td>
						<td>${patient.personId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.age} </td>
						<td><img border="0" src="<c:if test="${patient.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
						<td><a href="../../patientDashboard.form?patientId=${patient.personId}"><spring:message code="tracnetreporting.label.patient.view" /></a></td>
					</tr>
				</c:forEach>
				
			</tbody>
			
		</table>
		
	</table>
	<br \>



<%@ include file="/WEB-INF/template/footer.jsp"%>