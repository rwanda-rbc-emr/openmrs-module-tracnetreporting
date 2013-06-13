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
package org.openmrs.module.tracnetreporting.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;

/**
 *
 */
public class Testing {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		String dStr = "25/05/1998";
		cal.setTime(sdf.parse(dStr));
		
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.MONTH, -3);
		String date = sdf.format(cal.getTime());
		Date dd = sd.parse(date);
		
		System.out.println(dd.getTime());
		
		

	}

}
