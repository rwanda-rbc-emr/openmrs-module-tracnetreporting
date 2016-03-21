/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.tracnetreporting.utils.exports;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ExportToCsvUtil {
	
	/**
	 * Exports data to the CSV File or Text File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToCsvFile(java.util.Map)
	 */
	public static void exportDataToCsvFile(HttpServletRequest request, HttpServletResponse response,
	                                Map<String, Integer> indicatorsList, String filename, String title, String startDate,
	                                String endDate) throws IOException {
		
		ServletOutputStream outputStream = response.getOutputStream();
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		outputStream.println("" + title + "(Between " + startDate + " and " + endDate + ")");
		outputStream.println();
		outputStream.println("# , Indicator Name , Indicator");
		outputStream.println();
		int count = 0;
		for (String indicator : indicatorsList.keySet()) {
			count++;
			outputStream.println(count + " , " + indicator.toString() + " , " + indicatorsList.get(indicator).toString());
		}
		
		outputStream.flush();
		outputStream.close();
	}
	
}
