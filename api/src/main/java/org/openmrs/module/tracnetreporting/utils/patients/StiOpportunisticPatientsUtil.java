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
package org.openmrs.module.tracnetreporting.utils.patients;

import java.util.List;

import org.openmrs.Patient;

/**
 *
 */
public class StiOpportunisticPatientsUtil {
	
	/**
	 * Number of clients who received councelling and screening for STIs this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> clientsCounceledForStiThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of STI cases diagnosed and treated this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> stiDiagnosedThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of opportunistic infection cases treated, excluding TB, this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> opportInfectTreatedExcludeTbThisMonth(String startDate, String endDate) {
		return null;
	}
}
