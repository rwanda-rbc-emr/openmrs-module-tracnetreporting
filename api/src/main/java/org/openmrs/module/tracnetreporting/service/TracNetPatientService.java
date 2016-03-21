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
package org.openmrs.module.tracnetreporting.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.openmrs.Person;

/**
 *
 */
public interface TracNetPatientService {
	
	/**
	 * Exports data to the CSV File or Text File
	 * 
	 * @param request, http servlet request from the jsp page.
	 * @param response, http servlet response to the jsp page.
	 * @param indicatorsList, the list of indicators to be export to the CSV file.
	 * @param filename, the name of the CSV file.
	 * @param title, the title that the report will bear as a category of those indicators.
	 * @throws IOException
	 */
	public void exportDataToCsvFile(HttpServletRequest request, HttpServletResponse response,
	                                Map<String, Integer> indicatorsList, String filename, String title, String startDate,
	                                String endDate) throws IOException;
	
	/**
	 * Exports data to the Excel File
	 * 
	 * @param request, http servlet request from the jsp page.
	 * @param response, http servlet response to the jsp page.
	 * @param indicatorsList, the list of indicators to be export to the Excel file.
	 * @param filename, the name of the Excel file.
	 * @param title, the title that the report will bear as a category of those indicators.
	 * @throws IOException
	 */
	public void exportDataToExcelFile(HttpServletRequest request, HttpServletResponse response,
	                                  Map<String, Integer> indicatorsList, String filename, String title, String startDate,
	                                  String endDate) throws IOException;
	
	//********************************************************************************************
	//* 																						 *
	//* 								A. PRE-ART Data Elements								 *
	//* 								------------------------								 *
	//********************************************************************************************
	/**
	 * Total number of new pediatric patients (age <18 months) enrolled in HIV care
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderEighteenMonthsInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of patients on Cotrimoxazole Prophylaxis this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> patientsOnCotrimoProphylaxis(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of new pediatric patients (age <5 years) enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderFiveInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of new female pediatric patients (age <15 years) enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemaleUnderFifteenInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of new male pediatric patients (age <15 years) enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMaleUnderFifteenInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemaleMoreThanFifteenInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of new male adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMaleMoreThanFifteenInHivCare(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedUnderEighteenMonthsEverInHiv(String startDate, String endDate);
	
	/**
	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveEverInHiv(String startDate, String endDate);
	
	/**
	 * Total number of female pediatric patients (age <15 years) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femalePedsUnderFifteenEverInHiv(String startDate, String endDate);
	
	/**
	 * Total number of male pediatric patients (age <15 years) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> malePedsUnderFifteenEverInHiv(String startDate, String endDate);
	
	/**
	 * Total number of female adult patients (age 15 or older) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThanFifteenEverInHiv(String startDate, String endDate);
	
	/**
	 * Total number of male adult patients (age 15 or older) ever enrolled in HIV care?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThanFifteenEverInHiv(String startDate, String endDate);
	
	/**
	 * Number of new patients screened for active TB at enrollment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> patientsActiveTbAtEnrolThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of patients screened TB Positive at enrollment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> patientsTbPositiveAtEnrolThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of newly enrolled patients (age <15 years) who started TB treatment this month?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newEnrolledPedsStartTbTreatThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of newly enrolled patients (age 15+ years) who started TB treatment this month?
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newEnrolledAdultsStartTbTreatThisMonth(String startDate, String endDate) throws ParseException;
	
	public  List<Person> PedsUnderFifteenEnrolledInHiv(String startDate, String endDate) throws ParseException;
	
	public  List<Person> AdultMoreThanFifteenEnrolledInHiv(String startDate, String endDate) throws ParseException;
	
	public List<Person> PatientsInPreARVDiedThisMonth(String startDate, String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTransferredOutThisMonth(String startDate,
			String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTransferredInThisMonth(String startDate,
			String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTLostToFollowUpThisMonth(String startDate,
			String endDate) throws ParseException;
	
	public List<Person> PatientsInPreARVTLostToFollowUpNotLostThisMonth(String startDate,
			String endDate) throws ParseException;
	
	//********************************************************************************************
	//* 																						 *
	//* 								  B. ART Data Elements		  						     *
	//* 								------------------------								 *
	//********************************************************************************************
	
	/**
	 * Total number of pediatric patients (age <18 months) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsUnderEighteenMonthsCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of pediatric patients (age <5 years) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsUnderFiveCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of female pediatric patients (age <15 years) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> femalePedsUnderFifteenCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of male pediatric patients (age <15 years) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> malePedsUnderFifteenCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of pediatric patients who are on First Line Regimen
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsOnFirstLineReg(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of pediatric patients who are on Second Line Regimen
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsOnSecondLineReg(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of female adult patients (age 15 or older) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> femaleMoreThanFifteenCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of male adult patients (age 15 or older) who are currently on ARV treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> maleMoreThanFifteenCurrentOnArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of adult patients who are on First Line Regimen
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> adultOnFirstLineReg(String startDate, String endDate) throws ParseException;
	
	/**
	 * Total number of adult patients who are on Second Line Regimen
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> adultOnSecondLineReg(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients (<18 months) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderEighteenMonthStartArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients (age <5 years) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderFiveStartArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new female pediatric patients (age <15 years) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemalePedsUnderFifteenStartArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new male pediatric patients (age <15 years) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMalePedsUnderFifteenStartArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients who are WHO stage 4 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsWhoStageFourThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients who are WHO Stage 3 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsWhoStageThreeThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients who are WHO Stage 2 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsWhoStageTwoThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients who are WHO Stage 1 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsWhoStageOneThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new pediatric patients whose WHO Stage is undefined this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUndefinedWhoStageThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new female adult patients (age 15+) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemaleAdultStartiArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new male adult patients (age 15+) starting ARV treatment this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMaleAdultStartiArvThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new adult patients who are WHO stage 4 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newAdultWhoStageFourThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new adult patients who are WHO stage 3 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newAdultWhoStageThreeThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new adult patients who are WHO stage 2 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newAdultWhoStageTwoThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new adult patients who are WHO stage 1 this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newAdultWhoStageOneThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of new adult patients who are WHO stage undefined this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newAdultUndefinedWhoStageThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age <15) who have had their treatment interrupted this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvPedsFifteenInterruptTreatThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age 15+) who have had their treatment interrupted this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvAdultFifteenInterruptTreatThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age <15) who have died this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvPedsDiedThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age 15+) who have died this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvAdultDiedThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age <15) lost to followup (>3 months)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvPedsLostFollowupMoreThreeMonths(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age 15+) lost to followup (>3 months)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvAdultLostFollowupMoreThreeMonths(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of male patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> maleOnTreatTwelveAfterInitArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of female patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> femaleOnTreatTwelveAfterInitArv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age <15) who have been transferred out this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvPedsTransferredOutThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age 15+) who have been transferred out this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvAdultTransferredOutThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age <15) who have been transferred in this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvPedsTransferredInThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of ARV patients (age 15+) who have been transferred in this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> arvAdultTransferreInThisMonth(String startDate, String endDate) throws ParseException;
	
	public List<Person> PatientsInARVTLostToFollowUpNotLostThisMonthList(String startDate,
			String endDate) throws ParseException;
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	C. STIs, Opportunistic Infections and Others	 					 *
	//* 					--------------------------------------------						 *
	//********************************************************************************************
	
	/**
	 * Number of clients who received councelling and screening for STIs this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> clientsCounceledForStiThisMonth(String startDate, String endDate);
	
	/**
	 * Number of STI cases diagnosed and treated this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> stiDiagnosedThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of opportunistic infection cases treated, excluding TB, this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> opportInfectTreatedExcludeTbThisMonth(String startDate, String endDate) throws ParseException;
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	   D. Nutrition Consultation Data Elements	 					     *
	//* 					   ---------------------------------------    					     *
	//********************************************************************************************
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsUnderFiveSevereMalnutrThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition who received
	 * therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveSevereMalnutrTheurapThisMonth(String startDate, String endDate);
	
	/**
	 * Number of patients (age < 15 years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFifteenSevMalnutrTherapThisMonth(String startDate, String endDate);
	
	/**
	 * Number of patients (age 15+ years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> adultSevereMalnutrTherapThisMonth(String startDate, String endDate);
	
	
	public List<Person> numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling(String startDate, String endDate);
	
	public List<Person> numberOfPatientsWhoReceivedFamilyPlanningThisMonth(String startDate, String endDate);
	
	/**
	 * Number of pregnant women who received therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantMalnutrTherapThisMonth(String startDate, String endDate);
	
	/**
	 * Number of lactating mothers who received therapeutic or nutritional supplementation this
	 * month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> lactatingMalnutrTherapThisMonth(String startDate, String endDate);
	
	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveWithSevMalnutrThisMonth(String startDate, String endDate);
	
	/**
	 * Number of patients (age < 15 years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsTherapThisMonth(String startDate, String endDate);//
	
	/**
	 * Number of patients (age 15+ years) who received therapeutic or nutritional supplementation
	 * this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> adultTherapThisMonth(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	          A. Antenatal Data Elements	 	    				     *
	//* 					   		  --------------------------    		    			     *
	//********************************************************************************************
	
	/**
	 * Number of women with unknown HIV status presenting for first antenatal care consultation
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenUnknownHivFirstAntenatal(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of known HIV positive women presenting for first antenatal care consultation
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenKnownHivPosFirstAntenatal(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of women with unknown HIV status tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenUnknownHivTested(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive women returning for their results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosReturnRes(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive women tested for CD4
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosTestedCd4(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive pregnant women eligible for ARVs treatment
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	
	public List<Person> pregnantHivPosStartedCotrimoxazoleThisMonth(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(String startDate, String endDate) throws ParseException;
	
	
	public List<Person> pregnantHivPosEligibleArvs1(String startDate, String endDate);
	
	/**
	 * Number of HIV negative women returning for their results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> negativeWomenReturnRes(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive pregnant women
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pregnantHivPos(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive pregnant women given AZT as prophylaxis at 28 weeks
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantHivPosAztProphyAt28Weeks(String startDate, String endDate);
	
	/**
	 * Number of HIV positive pregnant women given triple therapy as prophylaxis
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantHivPosTripleTheraProphy(String startDate, String endDate);
	
	public List<Person> numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis(String startDate, String endDate);
	
	/**
	 * Number of HIV positive pregnant women eligible for treatment given ARVs
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pregnantHivPosEligibleArvs2(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of women tested for RPR
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenTestedForRpr(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of Pregnant women tested positive for RPR
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pregnantTestedPosForRpr(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of pregnant women partners tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pregnantPartnersTestedForHiv(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV negative pregnant women whose partners are tested HIV Positive
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> hivNegPregnantPartnersTestedHivPos(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of discordant couples
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> discordantCouples1(String startDate, String endDate);
	
	/**
	 * Number of partners tested HIV positive
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> partnersTestedHivPos(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	   		  B. Maternity Data Elements	 					         *
	//* 					          --------------------------    					         *
	//********************************************************************************************
	
	/**
	 * Number of deliveries expected at the facility this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> expectedDeliveriesFacilityThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of deliveries occurring at the facility this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> occuringDeliveriesFacilityThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of expected deliveries among HIV positive women
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> expectedDeliveriesAmongHivPosWomen(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive women giving birth at the facility
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosGivingBirthAtFacility(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of reported HIV positive women giving birth at home
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> reportedHivPosGivingBirthAtHome(String startDate, String endDate);
	
	/**
	 * Number of HIV positive women given Sd of tripletherapy of AZT+3TC+NVP as prophylaxis during
	 * labor
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosAzt3tcNvpDuringLabor(String startDate, String endDate);
	
	/**
	 * Number of women receiving AZT+3TC after delivery
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenReceivingAzt3tcAfterDelivery(String startDate, String endDate);
	
	/**
	 * Number of women with unknown HIV status tested for HIV during labor and delivery
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenUnknownHivStatusTestedDuringLabor1(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of women with unknown HIV status tested HIV positive during labor and delivery
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenUnknownHivStatusTestedPosDuringLabor2(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of pregnant women received a complete course of ART prophylaxis this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantReceivedCompleteCourseThisMonth(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	   	   C. HIV Exposed Infant Follow-up	    					     *
	//* 					       -------------------------------    			    		     *
	//********************************************************************************************
	
	/**
	 * Number of HIV positive women breastfeeding
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosBreastFeeding(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of HIV positive women using formula
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosUsingFormula(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born to HIV positive mothers currently enrolled in the PMTCT program
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersEnrolledPmtct(String startDate, String endDate) throws ParseException;
	
	public List<Person> infantHivNegMothersInDiscordantCouplesEnrolledPmtctList(String startDate,
			String endDate) throws ParseException;
	
	/**
	 * Number of infants born to HIV positive mothers tested at 6 weeks
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt6Weeks(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive at 6 weeks
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt6Weeks(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who are 6 weeks of age this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAged6WeeksThisMonth(String startDate, String endDate);
	
	/**
	 * Number of infants born from HIV positive mothers tested at 9 months
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt9Months(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive at 9 months
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt9Months(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born to HIV positive mothers who are 9 months of age this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAged9MonthsThisMonth(String startDate, String endDate);
	
	/**
	 * Number of infants born from HIV positive mothers tested at 18 months
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt18Months(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive at 18 months
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt18Months(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who are 18 months of age this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAgedAt18MonthsThisMonth(String startDate, String endDate);
	
	/**
	 * Number of infants born to HIV positive lost to follow up
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersLostFollowup(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born to HIV positive mothers screened for TB this month
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersScreenedTbThisMonth(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of reported deaths of infants born to HIV positive mothers
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> reportedDeadInfantHivPosMothers(String startDate, String endDate) throws ParseException;
	
	/**
	 * Number of infants born from HIV positive mothers who are clinically malnourished
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersMalnourished(String startDate, String endDate);
	
	/**
	 * Number of infants born from HIV positive mothers who received therapeutic or supplementary
	 * food
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersTherapFood(String startDate, String endDate);
	
	/**
	 * Number of Infants born from HIV positive mothers receiving Cotrimoxazole Prophylaxis at 6
	 * weeks
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersCotrimoAt6Weeks(String startDate, String endDate);
	
	/**
	 * Number of New Infants born from HIV positive mothers receiving NVP and AZT at birth
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newInfantHivPosMothersNvpAztAtBirth(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	       D. Family Planning Data Elements	 	    				     *
	//* 					       --------------------------------      					     *
	//********************************************************************************************
	
	/**
	 * Number of HIV positive women expected in family planning at the facility
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosExpectedFpAtFacility(String startDate, String endDate);
	
	/**
	 * Number of HIV positive women seen in family planning
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosSeenInFp(String startDate, String endDate);
	
	/**
	 * Number of HIV positive women partners seen in family planning
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosPartnersSeenInFp(String startDate, String endDate);
	
	/**
	 * Number of HIV positive women who are receiving modern contraceptive methods
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosReceivingModernContraceptive(String startDate, String endDate);
	
	/**
	 * Number of HIV positive women referred for family planning
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosRefferedForFp(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	         E. Submit VCT Data Elements	 					         *
	//* 					         ---------------------------    					         *
	//********************************************************************************************
	
	/**
	 * Number of new female clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleUnderFifteenCounseledTested(String startDate, String endDate);
	
	/**
	 * Number of new male clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleUnderFifteenCounseledTested(String startDate, String endDate);
	
	/**
	 * Number of new female clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleFifteenTo24CounseledTested(String startDate, String endDate);
	
	/**
	 * Number of new male clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleFifteenTo24CounseledTested(String startDate, String endDate);
	
	/**
	 * Number of new female clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleMore25CounseledTested(String startDate, String endDate);
	
	/**
	 * Number of new male clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleMore25CounseledTested(String startDate, String endDate);
	
	/**
	 * Number of couples counseled and tested
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> couplesCounseledTested(String startDate, String endDate);
	
	/**
	 * Number of discordant couples
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> discordantCouples2(String startDate, String endDate);
	
	/**
	 * Number of new female clients (age <15) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleUnderFifteenTestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of new male clients (age <15) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleUnderFifteenTestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of new female clients (ages 15-24) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemale15To24TestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of new male clients (ages 15-24) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMale15To24TestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of new female clients (ages 25+) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleMore25TestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of new male clients (ages 25+) tested and received results
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleMore25TestReceiveRes(String startDate, String endDate);
	
	/**
	 * Number of HIV Positive female clients (age <15)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosUnderFifteen(String startDate, String endDate);
	
	/**
	 * Number of HIV positive male clients (age <15)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosUnderFifteen(String startDate, String endDate);
	
	/**
	 * Number of HIV Positive female clients (ages 15-24)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosUnder15to24(String startDate, String endDate);
	
	/**
	 * Number of HIV Positive male clients (ages 15-24)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosUnder15to24(String startDate, String endDate);
	
	/**
	 * Number of HIV Positive female clients (age 25+)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosMoreThan25(String startDate, String endDate);
	
	/**
	 * Number of HIV Positive male clients (age 25+)
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosMoreThan25(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		         F. Provider-Initiated Testing (PIT) Data Elements	 					 *
	//* 				 -------------------------------------------------					     *
	//********************************************************************************************
	
	/**
	 * Number of female clients (age <15) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenCounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age <15) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenCounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age 15-24) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24CounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age 15-24) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24CounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age 25+) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25CounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age 25+) counseled and tested for HIV through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25CounseledThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenHivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenHivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24HivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24HivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25HivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25HivResThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenHivPosThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenHivPosThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24HivPosThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24HivPosThroughPit(String startDate, String endDate);
	
	/**
	 * Number of female clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25HivPosThroughPit(String startDate, String endDate);
	
	/**
	 * Number of male clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25HivPosThroughPit(String startDate, String endDate);
	
	//********************************************************************************************
	//* 																						 *
	//* 		        	            G. PEP Data Elements	 							     *
	//* 					   			--------------------    					     		 *
	//********************************************************************************************
	
	/**
	 * Number of new clients at risk of HIV infection as a result of occupational exposure
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupationExposure(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual assault
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssault(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of other non-occupational exposure
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupationExposure(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of occupational exposure who
	 * received PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupationExposurePep(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual assault who
	 * received PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssaultPep(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of other non-occupational exposure
	 * who received PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupationExposurePep(String startDate, String endDate);
	
	/**
	 * Number of clients at risk of HIV infection as a result of occupational exposure who were
	 * tested 3 months after receiving PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupExpo3MonthAfterPep(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual assault who were
	 * tested 3 months after receiving PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssault3MonthAfterPep(String startDate, String endDate);
	
	/**
	 * Number of new clients at risk of HIV infection as a result of other non-occupational exposure
	 * who were tested 3 months after receiving PEP
	 * 
	 * @param startDate, the starting date in order to get the report month
	 * @param endDate, the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupExpo3MonthAfterPep(String startDate, String endDate);

	/**
	 * Auto generated method comment
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	
	
	public  List<Person> patientsOnCotrimoProphylaxisLessThan15YearsList (String startDate,
			String endDate) throws ParseException;
	
	public List<Person> patientsOnCotrimoProphylaxisGreatherThan15YearsList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public List<Person> patientsPediatricNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public List<Person> patientsAdultNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
}
