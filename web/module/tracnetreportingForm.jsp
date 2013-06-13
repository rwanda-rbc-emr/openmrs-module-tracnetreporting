<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<!--  <openmrs:require privilege="View TRACNet Reporting" otherwise="/login.htm" redirect="/module/@MODULE_ID@/tracnetreportingForm.list" /> -->

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery-1.3.2.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/styles/ui.all.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/styles/demos.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui.core.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui.accordion.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui.tabs.js" />

<script type="text/javascript">

	$(function() {
		$("#accordion1").accordion({collapsible: true, autoHeight: false});
	});

	$(function() {
		$("#accordion2").accordion({collapsible: true, autoHeight: false});
	});

	$(function() {
		$("#tabs").tabs({collapsible: true});
	});
	
</script>

<h2><spring:message code="tracnetreporting.title" /></h2>

<%@ include file="templates/reportParameterForm.jsp"%>

<br />


	<c:set var="displaySmthg" value="${displayDivIndicators=='true'?'block':'none'}" /> 
	
	<!-- **************************************************************************** -->

	<!-- TabDiv that holds the tabs -->
	<div id="displayIndicators" style="display: ${displaySmthg};" class="demo"><!-- Tabs that holds the tabs start -->
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1"><spring:message code="tracnetreporting.category.artvariables" /></a></li>
				<li><a href="#tabs-2"><spring:message code="tracnetreporting.category.preventiontracnetdata" /></a></li>
			</ul>
			
			<!-- ************************************************************ --> 
			
			<!-- Tab-1 for ART Variables Start -->
			<div id="tabs-1"><!-- accordion-1 Div Start -->
				<div id="accordion1Div" class="demo"><!-- accordion-1 start -->
					<div id="accordion1"><!-- indicatorsPreArtDataElement start -->
					
						<%@ include file="templates/artVariablesList.jsp"%>
						
					</div><!-- accordion-1 end -->
				</div><!-- accordion-1 Div end -->
			</div><!-- Tab-1 for ART Variables End --> 
			
			<!-- ************************************************************ -->
			
			<!-- Tab-2 for Prevention Tracnet Data Start -->
			<div id="tabs-2"><!-- accordion-2 Div start -->
				<div id="accordion2Div" class="demo"><!-- accordion-2 start -->
					<div id="accordion2">
					
						<%@ include file="templates/preventionDataElementList.jsp"%>	
						
					</div><!-- accordion-2 end -->
				</div><!-- accordion-2 Div end -->
			</div><!-- Tab-2 for Prevention Tracnet Data End --> 
			
			<!-- ************************************************************ -->
			
		</div><!-- CategTabs that holds the tabs end -->
	</div><!--  TabDiv that holds the tabs --> 



<%@ include file="/WEB-INF/template/footer.jsp"%>


