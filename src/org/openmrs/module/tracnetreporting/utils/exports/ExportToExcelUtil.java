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
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 */
public class ExportToExcelUtil {
	
	/**
	 * Exports data to the Excel File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToExcelFile(java.util.Map)
	 */
	
	@SuppressWarnings("deprecation")
	public static void exportDataToExcelFile(HttpServletRequest request, HttpServletResponse response,
	                                         Map<String, Integer> indicatorsList, String filename, String title,
	                                         String startDate, String endDate) throws IOException {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		HSSFSheet sheet = workbook.createSheet(title);
		int count = 0;
		sheet.setDisplayRowColHeadings(true);
		
		//Setting Style
		HSSFFont font = workbook.createFont();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFFont.COLOR_RED);
		cellStyle.setFillForegroundColor((short) 0xA);
		
		// Title
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("");
		row.setRowStyle(cellStyle);
		row.createCell((short) 1).setCellValue("" + title + "(Between " + startDate + " and " + endDate + ")");
		
		// Headers
		row = sheet.createRow((short) 2);
		row.createCell((short) 0).setCellValue("#");
		row.createCell((short) 1).setCellValue("INDICATOR NAME");
		row.createCell((short) 2).setCellValue("INDICATOR");
		
		for (String indicator : indicatorsList.keySet()) {
			count++;
			row = sheet.createRow((short) count + 3);
			row.createCell((short) 0).setCellValue(count);
			row.createCell((short) 1).setCellValue(indicator.toString());
			row.createCell((short) 2).setCellValue(indicatorsList.get(indicator).toString());
		}
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}
	
}
