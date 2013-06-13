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
 * The TracNet Indicators Service deal with Indicators. -DEFINITION: ... as in a
 * data quality indicator or a key performance indicator (KPI). An indicator
 * shows whether a metric has a successful or unsuccessful outcome. For
 * instance, an indicator might be a (Metric)
 * "Number of HIV+ patients that are not currently on ARV Treatment" and
 * (Target) "Value should be 0". This indicator would provide us with a
 * numerical value for the count and a boolean value to indicate whether the
 * target was met, as well as some context (e.g. a list of HIV+ patients not
 * currently on treatment). Indicators should be used in interactive tools like
 * Data Quality reports, but should not be confused with Metrics or Measures
 * used in our general reporting use cases.
 * 
 * @author Kamonyo
 */
public interface TracNetIndicatorService {

	/**
	 * Exports data to the CSV File or Text File
	 * 
	 * @param request
	 *            , http servlet request from the jsp page.
	 * @param response
	 *            , http servlet response to the jsp page.
	 * @param indicatorsList
	 *            , the list of indicators to be export to the CSV file.
	 * @param filename
	 *            , the name of the CSV file.
	 * @param title
	 *            , the title that the report will bear as a category of those
	 *            indicators.
	 * @throws IOException
	 */
	public void exportDataToCsvFile(HttpServletRequest request,
			HttpServletResponse response, Map<String, Integer> indicatorsList,
			String filename, String title, String startDate, String endDate)
			throws IOException;

	/**
	 * Exports data to the Excel File
	 * 
	 * @param request
	 *            , http servlet request from the jsp page.
	 * @param response
	 *            , http servlet response to the jsp page.
	 * @param indicatorsList
	 *            , the list of indicators to be export to the Excel file.
	 * @param filename
	 *            , the name of the Excel file.
	 * @param title
	 *            , the title that the report will bear as a category of those
	 *            indicators.
	 * @throws IOException
	 */
	public void exportDataToExcelFile(HttpServletRequest request,
			HttpServletResponse response, Map<String, Integer> indicatorsList,
			String filename, String title, String startDate, String endDate)
			throws IOException;

	// ********************************************************************************************
	// * *
	// * A. PRE-ART Data Elements *
	// * ------------------------ *
	// ********************************************************************************************
	/**
	 * Total number of new pediatric patients (age <18 months) enrolled in HIV
	 * care
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newPedsUnderEighteenMonthsInHivCare(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new pediatric patients (age <5 years) enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newPedsUnderFiveInHivCare(String startDate, String endDate) throws ParseException;

	/**
	 * Total number of new female pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newFemaleUnderFifteenInHivCare(String startDate, String endDate) throws ParseException;

	/**
	 * Total number of new male pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newMaleUnderFifteenInHivCare(String startDate, String endDate) throws ParseException;

	/**
	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newFemaleMoreThanFifteenInHivCare(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new male adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int newMaleMoreThanFifteenInHivCare(String startDate, String endDate) throws ParseException;

	/**
	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedUnderEighteenMonthsEverInHiv(String startDate, String endDate);

	/**
	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderFiveEverInHiv(String startDate, String endDate);

	/**
	 * Total number of female pediatric patients (age <15 years) ever enrolled
	 * in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femalePedsUnderFifteenEverInHiv(String startDate, String endDate);

	/**
	 * Total number of male pediatric patients (age <15 years) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int malePedsUnderFifteenEverInHiv(String startDate, String endDate);

	/**
	 * Total number of female adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleMoreThanFifteenEverInHiv(String startDate, String endDate);

	/**
	 * Total number of male adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleMoreThanFifteenEverInHiv(String startDate, String endDate);

	/**
	 * Number of patients on Cotrimoxazole Prophylaxis this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int patientsOnCotrimoProphylaxis(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new patients screened for active TB at enrollment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int patientsActiveTbAtEnrolThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of patients screened TB Positive at enrollment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int patientsTbPositiveAtEnrolThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of newly enrolled patients (age <15 years) who started TB
	 * treatment this month?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newEnrolledPedsStartTbTreatThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of newly enrolled patients (age 15+ years) who started TB
	 * treatment this month?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newEnrolledAdultsStartTbTreatThisMonth(String startDate,
			String endDate) throws ParseException;

	public int PedsUnderFifteenEnrolledInHiv(String startDate, String endDate)
			throws ParseException;

	public int AdultMoreThanFifteenEnrolledInHiv(String startDate,
			String endDate) throws ParseException;

	public int PatientsInPreARVDiedThisMonth(String startDate, String endDate) throws ParseException;

	public int PatientsInPreARVTransferredOutThisMonth(String startDate,
			String endDate) throws ParseException;

	public int PatientsInPreARVTransferredInThisMonth(String startDate,
			String endDate) throws ParseException;

	public int PatientsInPreARVTLostToFollowUpThisMonth(String startDate,
			String endDate) throws ParseException;
	
	public int PatientsInPreARVTLostToFollowUpNotLostThisMonth(String startDate,
			String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * B. ART Data Elements *
	// * ------------------------ *
	// ********************************************************************************************

	/**
	 * Total number of pediatric patients (age <18 months) who are currently on
	 * ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderEighteenMonthsCurrentOnArv(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of pediatric patients (age <5 years) who are currently on
	 * ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderFiveCurrentOnArv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of female pediatric patients (age <15 years) who are
	 * currently on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femalePedsUnderFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of male pediatric patients (age <15 years) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int malePedsUnderFifteenCurrentOnArv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of pediatric patients who are on First Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int pedsOnFirstLineReg(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of pediatric patients who are on Second Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int pedsOnSecondLineReg(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of female adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleMoreThanFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of male adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleMoreThanFifteenCurrentOnArv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of adult patients who are on First Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int adultOnFirstLineReg(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of adult patients who are on Second Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int adultOnSecondLineReg(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new pediatric patients (<18 months) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsUnderEighteenMonthStartArvThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients (age <5 years) starting ARV treatment
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsUnderFiveStartArvThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new female pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newFemalePedsUnderFifteenStartArvThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new male pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newMalePedsUnderFifteenStartArvThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO stage 4 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsWhoStageFourThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 3 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsWhoStageThreeThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 2 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsWhoStageTwoThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 1 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsWhoStageOneThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new pediatric patients whose WHO Stage is undefined this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newPedsUndefinedWhoStageThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new female adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newFemaleAdultStartiArvThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new male adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newMaleAdultStartiArvThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 4 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newAdultWhoStageFourThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 3 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newAdultWhoStageThreeThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 2 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newAdultWhoStageTwoThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 1 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newAdultWhoStageOneThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage undefined this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int newAdultUndefinedWhoStageThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have had their treatment interrupted
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvPedsFifteenInterruptTreatThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have had their treatment interrupted
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvAdultFifteenInterruptTreatThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have died this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvPedsDiedThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have died this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvAdultDiedThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age <15) lost to followup (>3 months)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int arvPedsLostFollowupMoreThreeMonths(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) lost to followup (>3 months)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int arvAdultLostFollowupMoreThreeMonths(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of male patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int maleOnTreatTwelveAfterInitArv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of female patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int femaleOnTreatTwelveAfterInitArv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have been transferred out this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvPedsTransferredOutThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have been transferred out this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvAdultTransferredOutThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have been transferred in this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvPedsTransferredInThisMonth(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have been transferred in this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int arvAdultTransferreInThisMonth(String startDate, String endDate)
			throws ParseException;
	
	
	public int PatientsInARVTLostToFollowUpNotLostThisMonth(String startDate,
			String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * C. STIs, Opportunistic Infections and Others *
	// * -------------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of clients who received councelling and screening for STIs this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int clientsCounceledForStiThisMonth(String startDate, String endDate);

	/**
	 * Number of STI cases diagnosed and treated this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int stiDiagnosedThisMonth(String startDate, String endDate) throws ParseException;

	/**
	 * Number of opportunistic infection cases treated, excluding TB, this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int opportInfectTreatedExcludeTbThisMonth(String startDate,
			String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * D. Nutrition Consultation Data Elements *
	// * --------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int pedsUnderFiveSevereMalnutrThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition who
	 * received therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderFiveSevereMalnutrTheurapThisMonth(String startDate,
			String endDate);

	/**
	 * Number of patients (age < 15 years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderFifteenSevMalnutrTherapThisMonth(String startDate,
			String endDate);

	/**
	 * Number of patients (age 15+ years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int adultSevereMalnutrTherapThisMonth(String startDate,
			String endDate);
	
	
	public int numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling(String startDate,
			String endDate);
	
	public int numberOfPatientsWhoReceivedFamilyPlanningThisMonth(String startDate,
			String endDate);

	/**
	 * Number of pregnant women who received therapeutic or nutritional
	 * supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pregnantMalnutrTherapThisMonth(String startDate, String endDate);

	/**
	 * Number of lactating mothers who received therapeutic or nutritional
	 * supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int lactatingMalnutrTherapThisMonth(String startDate, String endDate);

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsUnderFiveWithSevMalnutrThisMonth(String startDate,
			String endDate);

	/**
	 * Number of patients (age < 15 years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pedsTherapThisMonth(String startDate, String endDate);//

	/**
	 * Number of patients (age 15+ years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int adultTherapThisMonth(String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * A. Antenatal Data Elements *
	// * -------------------------- *
	// ********************************************************************************************

	/**
	 * Number of women with unknown HIV status presenting for first antenatal
	 * care consultation
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenUnknownHivFirstAntenatal(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of known HIV positive women presenting for first antenatal care
	 * consultation
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenKnownHivPosFirstAntenatal(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of women with unknown HIV status tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenUnknownHivTested(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive women returning for their results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenHivPosReturnRes(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive women tested for CD4
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenHivPosTestedCd4(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive pregnant women eligible for ARVs treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	
	public int pregnantHivPosStartedCotrimoxazoleThisMonth(String startDate, String endDate) throws ParseException;
	
	public int NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(String startDate, String endDate) throws ParseException;
	
	public int NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(String startDate, String endDate) throws ParseException;
	
	public int pregnantHivPosEligibleArvs1(String startDate, String endDate);

	/**
	 * Number of HIV negative women returning for their results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int negativeWomenReturnRes(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive pregnant women
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int pregnantHivPos(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive pregnant women given AZT as prophylaxis at 28
	 * weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pregnantHivPosAztProphyAt28Weeks(String startDate, String endDate);

	/**
	 * Number of HIV positive pregnant women given triple therapy as prophylaxis
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pregnantHivPosTripleTheraProphy(String startDate, String endDate);
	public int numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis(String startDate, String endDate);

	/**
	 * Number of HIV positive pregnant women eligible for treatment given ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int pregnantHivPosEligibleArvs2(String startDate, String endDate) throws ParseException;

	/**
	 * Number of women tested for RPR
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenTestedForRpr(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of Pregnant women tested positive for RPR
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int pregnantTestedPosForRpr(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of pregnant women partners tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int pregnantPartnersTestedForHiv(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV negative pregnant women whose partners are tested HIV
	 * Positive
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int hivNegPregnantPartnersTestedHivPos(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of discordant couples
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int discordantCouples1(String startDate, String endDate);

	/**
	 * Number of partners tested HIV positive
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int partnersTestedHivPos(String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * B. Maternity Data Elements *
	// * -------------------------- *
	// ********************************************************************************************

	/**
	 * Number of deliveries expected at the facility this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int expectedDeliveriesFacilityThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of deliveries occurring at the facility this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int occuringDeliveriesFacilityThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of expected deliveries among HIV positive women
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int expectedDeliveriesAmongHivPosWomen(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV positive women giving birth at the facility
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenHivPosGivingBirthAtFacility(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of reported HIV positive women giving birth at home
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int reportedHivPosGivingBirthAtHome(String startDate, String endDate);

	/**
	 * Number of HIV positive women given Sd of tripletherapy of AZT+3TC+NVP as
	 * prophylaxis during labor
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosAzt3tcNvpDuringLabor(String startDate, String endDate);

	/**
	 * Number of women receiving AZT+3TC after delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenReceivingAzt3tcAfterDelivery(String startDate,
			String endDate);

	/**
	 * Number of women with unknown HIV status tested for HIV during labor and
	 * delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenUnknownHivStatusTestedDuringLabor1(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of women with unknown HIV status tested HIV positive during labor
	 * and delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException
	 */
	public int womenUnknownHivStatusTestedPosDuringLabor2(String startDate,
			String endDate) throws ParseException;
	
	public int NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis(String startDate,
			String endDate) throws ParseException;
	
	public int NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of pregnant women received a complete course of ART prophylaxis
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int pregnantReceivedCompleteCourseThisMonth(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * C. HIV Exposed Infant Follow-up *
	// * ------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of HIV positive women breastfeeding
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int womenHivPosBreastFeeding(String startDate, String endDate) throws ParseException;

	/**
	 * Number of HIV positive women using formula
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int womenHivPosUsingFormula(String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers currently enrolled in the
	 * PMTCT program
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersEnrolledPmtct(String startDate, String endDate) throws ParseException;
	
	public int infantHivNegMothersInDiscordantCouplesEnrolledPmtct(String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers tested at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedAt6Weeks(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedPosAt6Weeks(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are 6 weeks of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersAged6WeeksThisMonth(String startDate,
			String endDate);

	/**
	 * Number of infants born from HIV positive mothers tested at 9 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedAt9Months(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 9 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedPosAt9Months(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers who are 9 months of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersAged9MonthsThisMonth(String startDate,
			String endDate);

	/**
	 * Number of infants born from HIV positive mothers tested at 18 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedAt18Months(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 18 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersTestedPosAt18Months(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are 18 months of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersAgedAt18MonthsThisMonth(String startDate,
			String endDate);

	/**
	 * Number of infants born to HIV positive lost to follow up
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersLostFollowup(String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers screened for TB this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int infantHivPosMothersScreenedTbThisMonth(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of reported deaths of infants born to HIV positive mothers
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 * @throws ParseException 
	 */
	public int reportedDeadInfantHivPosMothers(String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are clinically
	 * malnourished
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersMalnourished(String startDate, String endDate);

	/**
	 * Number of infants born from HIV positive mothers who received therapeutic
	 * or supplementary food
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersTherapFood(String startDate, String endDate);

	/**
	 * Number of Infants born from HIV positive mothers receiving Cotrimoxazole
	 * Prophylaxis at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int infantHivPosMothersCotrimoAt6Weeks(String startDate,
			String endDate);

	/**
	 * Number of New Infants born from HIV positive mothers receiving NVP and
	 * AZT at birth
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newInfantHivPosMothersNvpAztAtBirth(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * D. Family Planning Data Elements *
	// * -------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of HIV positive women expected in family planning at the facility
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosExpectedFpAtFacility(String startDate, String endDate);

	/**
	 * Number of HIV positive women seen in family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosSeenInFp(String startDate, String endDate);

	/**
	 * Number of HIV positive women partners seen in family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosPartnersSeenInFp(String startDate, String endDate);

	/**
	 * Number of HIV positive women who are receiving modern contraceptive
	 * methods
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosReceivingModernContraceptive(String startDate,
			String endDate);

	/**
	 * Number of HIV positive women referred for family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int womenHivPosRefferedForFp(String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * E. Submit VCT Data Elements *
	// * --------------------------- *
	// ********************************************************************************************

	/**
	 * Number of new female clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemaleUnderFifteenCounseledTested(String startDate,
			String endDate);

	/**
	 * Number of new male clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMaleUnderFifteenCounseledTested(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemaleFifteenTo24CounseledTested(String startDate,
			String endDate);

	/**
	 * Number of new male clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMaleFifteenTo24CounseledTested(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemaleMore25CounseledTested(String startDate, String endDate);

	/**
	 * Number of new male clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMaleMore25CounseledTested(String startDate, String endDate);

	/**
	 * Number of couples counseled and tested
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int couplesCounseledTested(String startDate, String endDate);

	/**
	 * Number of discordant couples
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int discordantCouples2(String startDate, String endDate);

	/**
	 * Number of new female clients (age <15) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemaleUnderFifteenTestReceiveRes(String startDate,
			String endDate);

	/**
	 * Number of new male clients (age <15) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMaleUnderFifteenTestReceiveRes(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 15-24) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemale15To24TestReceiveRes(String startDate, String endDate);

	/**
	 * Number of new male clients (ages 15-24) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMale15To24TestReceiveRes(String startDate, String endDate);

	/**
	 * Number of new female clients (ages 25+) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newFemaleMore25TestReceiveRes(String startDate, String endDate);

	/**
	 * Number of new male clients (ages 25+) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newMaleMore25TestReceiveRes(String startDate, String endDate);

	/**
	 * Number of HIV Positive female clients (age <15)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleHivPosUnderFifteen(String startDate, String endDate);

	/**
	 * Number of HIV positive male clients (age <15)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleHivPosUnderFifteen(String startDate, String endDate);

	/**
	 * Number of HIV Positive female clients (ages 15-24)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleHivPosUnder15to24(String startDate, String endDate);

	/**
	 * Number of HIV Positive male clients (ages 15-24)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleHivPosUnder15to24(String startDate, String endDate);

	/**
	 * Number of HIV Positive female clients (age 25+)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleHivPosMoreThan25(String startDate, String endDate);

	/**
	 * Number of HIV Positive male clients (age 25+)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleHivPosMoreThan25(String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * F. Provider-Initiated Testing (PIT) Data Elements *
	// * ------------------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of female clients (age <15) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleUnderFifteenCounseledThroughPit(String startDate,
			String endDate);

	/**
	 * Number of male clients (age <15) counseled and tested for HIV through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleUnderFifteenCounseledThroughPit(String startDate,
			String endDate);

	/**
	 * Number of female clients (age 15-24) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int female15To24CounseledThroughPit(String startDate, String endDate);

	/**
	 * Number of male clients (age 15-24) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int male15To24CounseledThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (age 25+) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleMoreThan25CounseledThroughPit(String startDate,
			String endDate);

	/**
	 * Number of male clients (age 25+) counseled and tested for HIV through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleMoreThan25CounseledThroughPit(String startDate,
			String endDate);

	/**
	 * Number of female clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleUnderFifteenHivResThroughPit(String startDate,
			String endDate);

	/**
	 * Number of male clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleUnderFifteenHivResThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int female15To24HivResThroughPit(String startDate, String endDate);

	/**
	 * Number of male clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int male15To24HivResThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleMoreThan25HivResThroughPit(String startDate, String endDate);

	/**
	 * Number of male clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleMoreThan25HivResThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleUnderFifteenHivPosThroughPit(String startDate,
			String endDate);

	/**
	 * Number of male clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleUnderFifteenHivPosThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int female15To24HivPosThroughPit(String startDate, String endDate);

	/**
	 * Number of male clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int male15To24HivPosThroughPit(String startDate, String endDate);

	/**
	 * Number of female clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int femaleMoreThan25HivPosThroughPit(String startDate, String endDate);

	/**
	 * Number of male clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int maleMoreThan25HivPosThroughPit(String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * G. PEP Data Elements *
	// * -------------------- *
	// ********************************************************************************************

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOccupationExposure(String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivRapeAssault(String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOtherNoneOccupationExposure(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOccupationExposurePep(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivRapeAssaultPep(String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOtherNoneOccupationExposurePep(String startDate,
			String endDate);

	/**
	 * Number of clients at risk of HIV infection as a result of occupational
	 * exposure who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOccupExpo3MonthAfterPep(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivRapeAssault3MonthAfterPep(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the number of all matched patients
	 */
	public int newAtRiskHivOtherNoneOccupExpo3MonthAfterPep(String startDate,
			String endDate);

	// ***********************************************************************************************
	// This is for displaying the list of patients matching the conditions.
	// ***********************************************************************************************

	/**
	 * Exports data to the CSV File or Text File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToCsvFile(java.util.Map)
	 */
	public void exportPatientsListToCsvFile(HttpServletRequest request,
			HttpServletResponse response, List<Person> patientsList,
			String filename, String title, String startDate, String endDate)
			throws IOException;

	/**
	 * Exports data to the Excel File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToExcelFile(java.util.Map)
	 */

	public void exportPatientsListToExcelFile(HttpServletRequest request,
			HttpServletResponse response, List<Person> patientsList,
			String filename, String title, String startDate, String endDate)
			throws IOException;

	// ********************************************************************************************
	// * *
	// * A. PRE-ART Data Elements *
	// * ------------------------ *
	// ********************************************************************************************
	/**
	 * Total number of new pediatric patients (age <18 months) enrolled in HIV
	 * care
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderEighteenMonthsInHivCareList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of patients on Cotrimoxazole Prophylaxis this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> patientsOnCotrimoProphylaxisList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new pediatric patients (age <5 years) enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newPedsUnderFiveInHivCareList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new female pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemaleUnderFifteenInHivCareList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new male pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMaleUnderFifteenInHivCareList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newFemaleMoreThanFifteenInHivCareList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of new male adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> newMaleMoreThanFifteenInHivCareList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedUnderEighteenMonthsEverInHivList(String startDate,
			String endDate);

	/**
	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV
	 * care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pedsUnderFiveEverInHivList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of female pediatric patients (age <15 years) ever enrolled
	 * in HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femalePedsUnderFifteenEverInHivList(String startDate,
			String endDate);

	/**
	 * Total number of male pediatric patients (age <15 years) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> malePedsUnderFifteenEverInHivList(String startDate,
			String endDate);

	/**
	 * Total number of female adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThanFifteenEverInHivList(String startDate,
			String endDate);

	/**
	 * Total number of male adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThanFifteenEverInHivList(String startDate,
			String endDate);

	/**
	 * Number of new patients screened for active TB at enrollment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> patientsActiveTbAtEnrolThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of patients screened TB Positive at enrollment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> patientsTbPositiveAtEnrolThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of newly enrolled patients (age <15 years) who started TB
	 * treatment this month?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newEnrolledPedsStartTbTreatThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of newly enrolled patients (age 15+ years) who started TB
	 * treatment this month?
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newEnrolledAdultsStartTbTreatThisMonthList(
			String startDate, String endDate) throws ParseException;

	public List<Person> PedsUnderFifteenEnrolledInHivList(String startDate,
			String endDate) throws ParseException;

	public List<Person> AdultMoreThanFifteenEnrolledInHivList(String startDate,
			String endDate) throws ParseException;

	public List<Person> PatientsInPreARVDiedThisMonthList(String startDate, String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTransferredOutThisMonthList(String startDate,
			String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTransferredInThisMonthList(String startDate,
			String endDate) throws ParseException;

	public List<Person> PatientsInPreARVTLostToFollowUpThisMonthList(String startDate,
			String endDate) throws ParseException;
	
	public List<Person> PatientsInPreARVTLostToFollowUpNotLostThisMonthList(String startDate,
			String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * B. ART Data Elements *
	// * ------------------------ *
	// ********************************************************************************************

	/**
	 * Total number of pediatric patients (age <18 months) who are currently on
	 * ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderEighteenMonthsCurrentOnArvList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Total number of pediatric patients (age <5 years) who are currently on
	 * ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveCurrentOnArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of female pediatric patients (age <15 years) who are
	 * currently on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femalePedsUnderFifteenCurrentOnArvList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Total number of male pediatric patients (age <15 years) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> malePedsUnderFifteenCurrentOnArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of pediatric patients who are on First Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pedsOnFirstLineRegList(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of pediatric patients who are on Second Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pedsOnSecondLineRegList(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of female adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThanFifteenCurrentOnArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of male adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThanFifteenCurrentOnArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Total number of adult patients who are on First Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> adultOnFirstLineRegList(String startDate, String endDate)
			throws ParseException;

	/**
	 * Total number of adult patients who are on Second Line Regimen
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> adultOnSecondLineRegList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients (<18 months) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsUnderEighteenMonthStartArvThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients (age <5 years) starting ARV treatment
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsUnderFiveStartArvThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new female pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newFemalePedsUnderFifteenStartArvThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of new male pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newMalePedsUnderFifteenStartArvThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO stage 4 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsWhoStageFourThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 3 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsWhoStageThreeThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 2 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsWhoStageTwoThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients who are WHO Stage 1 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsWhoStageOneThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new pediatric patients whose WHO Stage is undefined this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newPedsUndefinedWhoStageThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new female adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newFemaleAdultStartiArvThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new male adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newMaleAdultStartiArvThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 4 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newAdultWhoStageFourThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 3 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newAdultWhoStageThreeThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 2 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newAdultWhoStageTwoThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage 1 this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newAdultWhoStageOneThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of new adult patients who are WHO stage undefined this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> newAdultUndefinedWhoStageThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have had their treatment interrupted
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvPedsFifteenInterruptTreatThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have had their treatment interrupted
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvAdultFifteenInterruptTreatThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have died this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvPedsDiedThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have died this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvAdultDiedThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) lost to followup (>3 months)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> arvPedsLostFollowupMoreThreeMonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) lost to followup (>3 months)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> arvAdultLostFollowupMoreThreeMonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of male patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> maleOnTreatTwelveAfterInitArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of female patients on treatment 12 months after initiation of ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> femaleOnTreatTwelveAfterInitArvList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have been transferred out this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvPedsTransferredOutThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have been transferred out this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvAdultTransferredOutThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age <15) who have been transferred in this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvPedsTransferredInThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of ARV patients (age 15+) who have been transferred in this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> arvAdultTransferreInThisMonthList(String startDate,
			String endDate) throws ParseException;
	
	public List<Person> PatientsInARVTLostToFollowUpNotLostThisMonthList(String startDate,
			String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * C. STIs, Opportunistic Infections and Others *
	// * -------------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of clients who received councelling and screening for STIs this
	 * month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> clientsCounceledForStiThisMonthList(String startDate,
			String endDate);

	/**
	 * Number of STI cases diagnosed and treated this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> stiDiagnosedThisMonthList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of opportunistic infection cases treated, excluding TB, this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> opportInfectTreatedExcludeTbThisMonthList(
			String startDate, String endDate) throws ParseException;

	// ********************************************************************************************
	// * *
	// * D. Nutrition Consultation Data Elements *
	// * --------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pedsUnderFiveSevereMalnutrThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition who
	 * received therapeutic or nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveSevereMalnutrTheurapThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of patients (age < 15 years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFifteenSevMalnutrTherapThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of patients (age 15+ years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> adultSevereMalnutrTherapThisMonthList(String startDate,
			String endDate);
	
	public List<Person> numberOfPatientsWhoReceivedFollowUpAndAdherenceCounsellingList(String startDate,
			String endDate);
	
	public List<Person> numberOfPatientsWhoReceivedFamilyPlanningThisMonthList(String startDate,
			String endDate);

	/**
	 * Number of pregnant women who received therapeutic or nutritional
	 * supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantMalnutrTherapThisMonthList(String startDate,
			String endDate);

	/**
	 * Number of lactating mothers who received therapeutic or nutritional
	 * supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> lactatingMalnutrTherapThisMonthList(String startDate,
			String endDate);

	/**
	 * Number of pediatric patients (age < 5 years) with severe malnutrition
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsUnderFiveWithSevMalnutrThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of patients (age < 15 years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pedsTherapThisMonthList(String startDate, String endDate);//

	/**
	 * Number of patients (age 15+ years) who received therapeutic or
	 * nutritional supplementation this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> adultTherapThisMonthList(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * A. Antenatal Data Elements *
	// * -------------------------- *
	// ********************************************************************************************

	/**
	 * Number of women with unknown HIV status presenting for first antenatal
	 * care consultation
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenUnknownHivFirstAntenatalList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of known HIV positive women presenting for first antenatal care
	 * consultation
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenKnownHivPosFirstAntenatalList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of women with unknown HIV status tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenUnknownHivTestedList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV positive women returning for their results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenHivPosReturnResList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV positive women tested for CD4
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenHivPosTestedCd4List(String startDate,
			String endDate) throws ParseException;
	
	public List<Person> pregnantHivPosStartedCotrimoxazoleThisMonthList(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonthList(String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonthList(String startDate, String endDate) throws ParseException;

	/**
	 * Number of HIV positive pregnant women eligible for ARVs treatment
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantHivPosEligibleArvs1List(String startDate,
			String endDate);

	/**
	 * Number of HIV negative women returning for their results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> negativeWomenReturnResList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV positive pregnant women
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pregnantHivPosList(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of HIV positive pregnant women given AZT as prophylaxis at 28
	 * weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantHivPosAztProphyAt28WeeksList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive pregnant women given triple therapy as prophylaxis
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantHivPosTripleTheraProphyList(String startDate,
			String endDate);
	
	public List<Person> numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxisList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive pregnant women eligible for treatment given ARVs
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> pregnantHivPosEligibleArvs2List(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of women tested for RPR
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenTestedForRprList(String startDate, String endDate)
			throws ParseException;

	/**
	 * Number of Pregnant women tested positive for RPR
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pregnantTestedPosForRprList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of pregnant women partners tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> pregnantPartnersTestedForHivList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV negative pregnant women whose partners are tested HIV
	 * Positive
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> hivNegPregnantPartnersTestedHivPosList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of discordant couples
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> discordantCouples1List(String startDate, String endDate);

	/**
	 * Number of partners tested HIV positive
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> partnersTestedHivPosList(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * B. Maternity Data Elements *
	// * -------------------------- *
	// ********************************************************************************************

	/**
	 * Number of deliveries expected at the facility this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> expectedDeliveriesFacilityThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of deliveries occurring at the facility this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> occuringDeliveriesFacilityThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of expected deliveries among HIV positive women
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> expectedDeliveriesAmongHivPosWomenList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of HIV positive women giving birth at the facility
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenHivPosGivingBirthAtFacilityList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of reported HIV positive women giving birth at home
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> reportedHivPosGivingBirthAtHomeList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive women given Sd of tripletherapy of AZT+3TC+NVP as
	 * prophylaxis during labor
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosAzt3tcNvpDuringLaborList(String startDate,
			String endDate);

	/**
	 * Number of women receiving AZT+3TC after delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenReceivingAzt3tcAfterDeliveryList(String startDate,
			String endDate);

	/**
	 * Number of women with unknown HIV status tested for HIV during labor and
	 * delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenUnknownHivStatusTestedDuringLabor1List(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of women with unknown HIV status tested HIV positive during labor
	 * and delivery
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException
	 */
	public List<Person> womenUnknownHivStatusTestedPosDuringLabor2List(
			String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxisList(
			String startDate, String endDate) throws ParseException;
	
	public List<Person> NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of pregnant women received a complete course of ART prophylaxis
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> pregnantReceivedCompleteCourseThisMonthList(
			String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * C. HIV Exposed Infant Follow-up *
	// * ------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of HIV positive women breastfeeding
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosBreastFeedingList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of HIV positive women using formula
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> womenHivPosUsingFormulaList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers currently enrolled in the
	 * PMTCT program
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersEnrolledPmtctList(String startDate,
			String endDate) throws ParseException;
	
	public List<Person> infantHivNegMothersInDiscordantCouplesEnrolledPmtctList(String startDate,
			String endDate) throws ParseException;
	
	

	/**
	 * Number of infants born to HIV positive mothers tested at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt6WeeksList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt6WeeksList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are 6 weeks of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAged6WeeksThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of infants born from HIV positive mothers tested at 9 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt9MonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 9 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt9MonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers who are 9 months of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAged9MonthsThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of infants born from HIV positive mothers tested at 18 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedAt18MonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who tested HIV positive
	 * at 18 months
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersTestedPosAt18MonthsList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are 18 months of age
	 * this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersAgedAt18MonthsThisMonthList(
			String startDate, String endDate);

	/**
	 * Number of infants born to HIV positive lost to follow up
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersLostFollowupList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born to HIV positive mothers screened for TB this month
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> infantHivPosMothersScreenedTbThisMonthList(
			String startDate, String endDate) throws ParseException;

	/**
	 * Number of reported deaths of infants born to HIV positive mothers
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 * @throws ParseException 
	 */
	public List<Person> reportedDeadInfantHivPosMothersList(String startDate,
			String endDate) throws ParseException;

	/**
	 * Number of infants born from HIV positive mothers who are clinically
	 * malnourished
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersMalnourishedList(String startDate,
			String endDate);

	/**
	 * Number of infants born from HIV positive mothers who received therapeutic
	 * or supplementary food
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersTherapFoodList(String startDate,
			String endDate);

	/**
	 * Number of Infants born from HIV positive mothers receiving Cotrimoxazole
	 * Prophylaxis at 6 weeks
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> infantHivPosMothersCotrimoAt6WeeksList(
			String startDate, String endDate);

	/**
	 * Number of New Infants born from HIV positive mothers receiving NVP and
	 * AZT at birth
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newInfantHivPosMothersNvpAztAtBirthList(
			String startDate, String endDate);

	// ********************************************************************************************
	// * *
	// * D. Family Planning Data Elements *
	// * -------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of HIV positive women expected in family planning at the facility
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosExpectedFpAtFacilityList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive women seen in family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosSeenInFpList(String startDate, String endDate);

	/**
	 * Number of HIV positive women partners seen in family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosPartnersSeenInFpList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive women who are receiving modern contraceptive
	 * methods
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosReceivingModernContraceptiveList(
			String startDate, String endDate);

	/**
	 * Number of HIV positive women referred for family planning
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> womenHivPosRefferedForFpList(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * E. Submit VCT Data Elements *
	// * --------------------------- *
	// ********************************************************************************************

	/**
	 * Number of new female clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleUnderFifteenCounseledTestedList(
			String startDate, String endDate);

	/**
	 * Number of new male clients (age <15) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleUnderFifteenCounseledTestedList(
			String startDate, String endDate);

	/**
	 * Number of new female clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleFifteenTo24CounseledTestedList(
			String startDate, String endDate);

	/**
	 * Number of new male clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleFifteenTo24CounseledTestedList(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleMore25CounseledTestedList(String startDate,
			String endDate);

	/**
	 * Number of new male clients (ages 25+) counseled and tested for HIV
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleMore25CounseledTestedList(String startDate,
			String endDate);

	/**
	 * Number of couples counseled and tested
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> couplesCounseledTestedList(String startDate,
			String endDate);

	/**
	 * Number of discordant couples
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> discordantCouples2List(String startDate, String endDate);

	/**
	 * Number of new female clients (age <15) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleUnderFifteenTestReceiveResList(
			String startDate, String endDate);

	/**
	 * Number of new male clients (age <15) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleUnderFifteenTestReceiveResList(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 15-24) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemale15To24TestReceiveResList(String startDate,
			String endDate);

	/**
	 * Number of new male clients (ages 15-24) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMale15To24TestReceiveResList(String startDate,
			String endDate);

	/**
	 * Number of new female clients (ages 25+) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newFemaleMore25TestReceiveResList(String startDate,
			String endDate);

	/**
	 * Number of new male clients (ages 25+) tested and received results
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newMaleMore25TestReceiveResList(String startDate,
			String endDate);

	/**
	 * Number of HIV Positive female clients (age <15)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosUnderFifteenList(String startDate,
			String endDate);

	/**
	 * Number of HIV positive male clients (age <15)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosUnderFifteenList(String startDate,
			String endDate);

	/**
	 * Number of HIV Positive female clients (ages 15-24)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosUnder15to24List(String startDate,
			String endDate);

	/**
	 * Number of HIV Positive male clients (ages 15-24)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosUnder15to24List(String startDate,
			String endDate);

	/**
	 * Number of HIV Positive female clients (age 25+)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleHivPosMoreThan25List(String startDate,
			String endDate);

	/**
	 * Number of HIV Positive male clients (age 25+)
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleHivPosMoreThan25List(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * F. Provider-Initiated Testing (PIT) Data Elements *
	// * ------------------------------------------------- *
	// ********************************************************************************************

	/**
	 * Number of female clients (age <15) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenCounseledThroughPitList(
			String startDate, String endDate);

	/**
	 * Number of male clients (age <15) counseled and tested for HIV through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenCounseledThroughPitList(
			String startDate, String endDate);

	/**
	 * Number of female clients (age 15-24) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24CounseledThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of male clients (age 15-24) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24CounseledThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age 25+) counseled and tested for HIV through
	 * PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25CounseledThroughPitList(
			String startDate, String endDate);

	/**
	 * Number of male clients (age 25+) counseled and tested for HIV through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25CounseledThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenHivResThroughPitList(
			String startDate, String endDate);

	/**
	 * Number of male clients (age <15) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenHivResThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24HivResThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of male clients (age 15-24) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24HivResThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25HivResThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of male clients (age 25+) who received HIV results through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25HivResThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleUnderFifteenHivPosThroughPitList(
			String startDate, String endDate);

	/**
	 * Number of male clients (age <15) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleUnderFifteenHivPosThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> female15To24HivPosThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of male clients (ages 15-24) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> male15To24HivPosThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of female clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> femaleMoreThan25HivPosThroughPitList(String startDate,
			String endDate);

	/**
	 * Number of male clients (age 25+) tested HIV positive through PIT
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> maleMoreThan25HivPosThroughPitList(String startDate,
			String endDate);

	// ********************************************************************************************
	// * *
	// * G. PEP Data Elements *
	// * -------------------- *
	// ********************************************************************************************

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupationExposureList(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssaultList(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupationExposureList(
			String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupationExposurePepList(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssaultPepList(String startDate,
			String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure who received PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupationExposurePepList(
			String startDate, String endDate);

	/**
	 * Number of clients at risk of HIV infection as a result of occupational
	 * exposure who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOccupExpo3MonthAfterPepList(
			String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/sexual
	 * assault who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivRapeAssault3MonthAfterPepList(
			String startDate, String endDate);

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure who were tested 3 months after receiving PEP
	 * 
	 * @param startDate
	 *            , the starting date in order to get the report month
	 * @param endDate
	 *            , the ending date in order to get the report month
	 * @return the list of all matched patients
	 */
	public List<Person> newAtRiskHivOtherNoneOccupExpo3MonthAfterPepList(
			String startDate, String endDate);
	
	
	public int patientsOnCotrimoProphylaxisLessThan15Years(String startDate,
			String endDate) throws ParseException;
	
	public  List<Person> patientsOnCotrimoProphylaxisLessThan15YearsList (String startDate,
			String endDate) throws ParseException;
	
	public int patientsOnCotrimoProphylaxisGreatherThan15Years(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public List<Person> patientsOnCotrimoProphylaxisGreatherThan15YearsList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public int patientsPediatricNewOnSecondLineThisMonth(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public List<Person> patientsPediatricNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public int patientsAdultNewOnSecondLineThisMonth(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;
	
	public List<Person> patientsAdultNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException;

}
