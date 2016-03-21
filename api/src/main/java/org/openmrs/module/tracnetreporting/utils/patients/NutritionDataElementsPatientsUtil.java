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
public class NutritionDataElementsPatientsUtil {
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pedsUnderFiveSevereMalnutrThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition who received
	 * therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pedsUnderFiveSevereMalnutrTheurapThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of patients (age < 15 years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pedsUnderFifteenSevMalnutrTherapThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of patients (age 15+ years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> adultSevereMalnutrTherapThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of pregnant women who received therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pregnantMalnutrTherapThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of lactating mothers who received therapeutic or nutritional supplementation this
	 * month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> lactatingMalnutrTherapThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pedsUnderFiveWithSevMalnutrThisMonth(String startDate, String endDate) {
		return null;
	}
	
	/**
	 * Number of patients (age < 15 years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> pedsTherapThisMonth(String startDate, String endDate) {
		return null;
	}//
	
	/**
	 * Number of patients (age 15+ years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public static List<Patient> adultTherapThisMonth(String startDate, String endDate) {
		return null;
	}
}
