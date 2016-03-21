<form action="tracnetreportingForm.list" method="get">
	<br />

	<div id="manageIndicPref">
		<b class="boxHeader"><spring:message code="tracnetreporting.manage.period" /></b>
		<div id="choosePeriod" class="box">
			<table>
				<tr>
					<td><spring:message code="tracnetreporting.label.startdate" /></td>
					<td><input id="startDate" name="startDate" value="${startDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
					<td><spring:message code="tracnetreporting.label.enddate" /></td>
					<td><input id="endDate" name="endDate" value="${endDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
					<td><input type="submit" value="Submit" /></td>
				</tr>
			</table>
		</div>
	</div>

</form>