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
package org.openmrs.module.tracnetreporting.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.tracnetreporting.service.ContextProvider;
import org.openmrs.module.tracnetreporting.service.TracNetIndicatorService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * This controller backs the /web/module/tracnetreportingForm.jsp page. This
 * controller is tied to that jsp page in the
 * /metadata/moduleApplicationContext.xml file
 */
public class TracNetReportingFormController extends
		ParameterizableViewController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	private String startingDate, endingDate;

	private final Map<String, Integer> indicatorsPreArtDataElement = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsArtDataElement = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsStiOpportAndOthers = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsNutritionDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsAntenatalDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsMaternityDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsHivExposedInfantFollowup = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsFamilyPlanningDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsSubmitVctDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsProviderTestPitDataElem = new TreeMap<String, Integer>();
	private final Map<String, Integer> indicatorsPepDataElem = new TreeMap<String, Integer>();

	private TracNetIndicatorService service;

	// private SettingSessionFactoryService sessionFactory;

	/**
	 * Returns any extra data in a key-->value pair kind of way
	 * 
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();
		boolean displayDivIndicators = false;
		service = Context.getService(TracNetIndicatorService.class);

		if (request.getParameter("startDate") != null
				&& !request.getParameter("startDate").equals("")
				&& request.getParameter("endDate") != null
				& !request.getParameter("endDate").equals("")) {

			String[] startDateWords = request.getParameter("startDate").split(
					"/");
			String[] endDateWords = request.getParameter("endDate").split("/");
			startingDate = request.getParameter("startDate");
			endingDate = request.getParameter("endDate");

			// Tells the jsp (displayDiv div) to display the list of all
			// indicators
			displayDivIndicators = true;

			// Gets reversed date format comparing to what we get from
			// parameters (startDate, endDate)
			String startDate = startDateWords[2] + "/" + startDateWords[1]
					+ "/" + startDateWords[0];
			String endDate = endDateWords[2] + "/" + endDateWords[1] + "/"
					+ endDateWords[0];

			// ********** A. PRE-ART Data Elements **********
			indicatorsPreArt(service, startDate, endDate, mav, request);

			// ********** B. ART Data Elements **********
			indicatorsArtDataElement(service, startDate, endDate, mav, request);

			// ********** C. STIs, Opportunistic Infections and Others
			// **********
			indicatorsStiOpportAndOthers(service, startDate, endDate, mav,
					request);

			// ********** D. Nutrition Consultation Data Elements **********
			indicatorsNutritionDataElem(service, startDate, endDate, mav,
					request);

			// ********** A. Antenatal Data Elements **********
			indicatorsAntenatalDataElem(service, startDate, endDate, mav,
					request);

			// ********** B. Maternity Data Elements **********
			indicatorsMaternityDataElem(service, startDate, endDate, mav,
					request);

			// ********** C. HIV Exposed Infant Follow-up **********
			indicatorsHivExposedInfantFollowup(service, startDate, endDate,
					mav, request);

			// ********** D. Family Planning Data Elements **********
			indicatorsFamilyPlanningDataElem(service, startDate, endDate, mav,
					request);

			// ********** E. Submit VCT Data Elements **********
			indicatorsSubmitVctDataElem(service, startDate, endDate, mav,
					request);

			// ********** F. Provider-Initiated Testing (PIT) Data Elements
			// **********
			indicatorsProviderTestPitDataElem(service, startDate, endDate, mav,
					request);

			// ********** G. PEP Data Elements **********
			indicatorsPepDataElem(service, startDate, endDate, mav, request);

			mav.addObject("startDate", startingDate);
			mav.addObject("endDate", endingDate);

		} else {
			mav.addObject("msg", ContextProvider
					.getMessage("tracnetreporting.msg.warningmessage"));
		}

		// Exporting data to CSV file.

		exportDataToCsvFile(request, response, startingDate, endingDate);

		// Exporting data to Excel file.

		exportDataToExcelFile(request, response, startingDate, endingDate);

		// setting the viewName via spring (moduleApplicationContext.xml)
		mav.setViewName(getViewName());

		// setting the objects (indicators lists) to be accessed on the page

		mav.addObject("indicatorsPreArtDataElement",
				indicatorsPreArtDataElement);
		mav.addObject("indicatorsArtDataElement", indicatorsArtDataElement);
		mav.addObject("indicatorsStiOpportAndOthers",
				indicatorsStiOpportAndOthers);
		mav.addObject("indicatorsNutritionDataElem",
				indicatorsNutritionDataElem);
		mav.addObject("indicatorsAntenatalDataElem",
				indicatorsAntenatalDataElem);
		mav.addObject("indicatorsMaternityDataElem",
				indicatorsMaternityDataElem);
		mav.addObject("indicatorsHivExposedInfantFollowup",
				indicatorsHivExposedInfantFollowup);
		mav.addObject("indicatorsFamilyPlanningDataElem",
				indicatorsFamilyPlanningDataElem);
		mav.addObject("indicatorsSubmitVctDataElem",
				indicatorsSubmitVctDataElem);
		mav.addObject("indicatorsProviderTestPitDataElem",
				indicatorsProviderTestPitDataElem);
		mav.addObject("indicatorsPepDataElem", indicatorsPepDataElem);

		mav.addObject("displayDivIndicators", displayDivIndicators);

		return mav;

	}

	/**
	 * Exports all data to CSV file by selecting the category to be reported.
	 * 
	 * @param request
	 * @param response
	 * @param startingDate
	 *            , the starting date of the period to be reported
	 * @param endingDate
	 *            , the ending date of the period to be reported
	 * @throws IOException
	 */
	private void exportDataToCsvFile(HttpServletRequest request,
			HttpServletResponse response, String startingDate, String endingDate)
			throws IOException {

		// -----------------------------1 .ART
		// VARIABLES----------------------------

		if (request.getParameter("exportPreArtData") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsPreArtDataElement,
							"Pre-ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.preartdataelement"),
							startingDate, endingDate);

		if (request.getParameter("exportArtData") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsArtDataElement,
							"ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.artdataelement"),
							startingDate, endingDate);

		if (request.getParameter("exportStiOpportunData") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsStiOpportAndOthers,
							"STIs, Opportunistic Infections",
							ContextProvider
									.getMessage("tracnetreporting.category.stiopportandothers"),
							startingDate, endingDate);

		if (request.getParameter("exportNutritionData") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsNutritionDataElem,
							"Nutrition Consultation",
							ContextProvider
									.getMessage("tracnetreporting.category.nutritiondataelem"),
							startingDate, endingDate);

		// -------------------PREVENTION DATA ELEMENTS FOR
		// TRACNET---------------------

		if (request.getParameter("exportAntenatalDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsAntenatalDataElem,
							"Antenatal Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.antenataldataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportMaternityDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsMaternityDataElem,
							"Maternity Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.maternitydataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportInfantFollowup") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsHivExposedInfantFollowup,
							"HIV Exposed Infant Follow-up",
							ContextProvider
									.getMessage("tracnetreporting.category.hivexposedinfantfollowup"),
							startingDate, endingDate);

		if (request.getParameter("exportFamilyPlanningDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsFamilyPlanningDataElem,
							"Family Planning Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.familyplandataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportVctDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsSubmitVctDataElem,
							"Submit VCT Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.subminvctdataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportPitDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsProviderTestPitDataElem,
							"Provider-Initiated Testing (PIT) Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pitdataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportPepDataElem") != null)
			service
					.exportDataToCsvFile(
							request,
							response,
							indicatorsPepDataElem,
							"PEP Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pepdataelem"),
							startingDate, endingDate);

	}

	/**
	 * Exports all data to Excel file by selecting the category to be reported.
	 * 
	 * @param request
	 * @param response
	 * @param startingDate
	 *            , the starting date of the period to be reported
	 * @param endingDate
	 *            , the ending date of the period to be reported
	 * @throws IOException
	 */
	private void exportDataToExcelFile(HttpServletRequest request,
			HttpServletResponse response, String startingDate, String endingDate)
			throws IOException {

		// -----------------------------1. ART
		// VARIABLES----------------------------

		// List<String> preart_msg = new ArrayList<String>();

		if (request.getParameter("exportExcelPreArtData") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsPreArtDataElement,
							"Pre-ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.preartdataelement"),
							startingDate, endingDate);

		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.category.preartdataelement"));
		//		
		// mav.addObject("indicatorsPreArtDataElement_msg", preart_msg);

		if (request.getParameter("exportExcelArtData") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsArtDataElement,
							"ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.artdataelement"),
							startingDate, endingDate);

		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.category.artdataelement"));

		if (request.getParameter("exportExcelStiOpportunData") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsStiOpportAndOthers,
							"STIs, Opportunistic Infections",
							ContextProvider
									.getMessage("tracnetreporting.category.stiopportandothers"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelNutritionData") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsNutritionDataElem,
							"Nutrition Consultation",
							ContextProvider
									.getMessage("tracnetreporting.category.nutritiondataelem"),
							startingDate, endingDate);

		// -------------------2. PREVENTION DATA ELEMENTS FOR
		// TRACNET---------------------

		if (request.getParameter("exportExcelAntenatalDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsAntenatalDataElem,
							"Antenatal Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.antenataldataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelMaternityDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsMaternityDataElem,
							"Maternity Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.maternitydataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelInfantFollowup") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsHivExposedInfantFollowup,
							"HIV Exposed Infant Follow-up",
							ContextProvider
									.getMessage("tracnetreporting.category.hivexposedinfantfollowup"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelFamilyPlanningDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsFamilyPlanningDataElem,
							"Family Planning Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.familyplandataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelVctDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsSubmitVctDataElem,
							"Submit VCT Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.subminvctdataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelPitDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsProviderTestPitDataElem,
							"Provider-Initiated Testing (PIT) Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pitdataelem"),
							startingDate, endingDate);

		if (request.getParameter("exportExcelPepDataElem") != null)
			service
					.exportDataToExcelFile(
							request,
							response,
							indicatorsPepDataElem,
							"PEP Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pepdataelem"),
							startingDate, endingDate);

	}

	/**
	 * Populates PRE-ART Data Elements indicators list
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Pre-ART Data elements
	 * @throws ParseException
	 */

	private void indicatorsPreArt(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		// indicatorsPreArtDataElement = new TreeMap<String, Integer>();
		List<String> preart_msg = new ArrayList<String>();

		indicatorsPreArtDataElement.put("1.A-01", service
				.newPedsUnderEighteenMonthsInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newPedsUnderEighteenMonthsInHivCare"));

		indicatorsPreArtDataElement.put("1.A-02", service
				.newPedsUnderFiveInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newPedsUnderFiveInHivCare"));

		indicatorsPreArtDataElement.put("1.A-03", service
				.PedsUnderFifteenEnrolledInHiv(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PedsUnderFifteenEnrolledInHiv"));

		indicatorsPreArtDataElement.put("1.A-04", service
				.newFemaleUnderFifteenInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newFemaleUnderFifteenInHivCare"));

		indicatorsPreArtDataElement.put("1.A-05", service
				.newMaleUnderFifteenInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newMaleUnderFifteenInHivCare"));

		indicatorsPreArtDataElement.put("1.A-06", service
				.AdultMoreThanFifteenEnrolledInHiv(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.AdultMoreThanFifteenEnrolledInHiv"));

		indicatorsPreArtDataElement.put("1.A-07", service
				.newFemaleMoreThanFifteenInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newFemaleMoreThanFifteenInHivCare"));

		indicatorsPreArtDataElement.put("1.A-08", service
				.newMaleMoreThanFifteenInHivCare(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newMaleMoreThanFifteenInHivCare"));

		// indicatorsPreArtDataElement.put("A-09", service
		// .pedUnderEighteenMonthsEverInHiv(startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.pedUnderEighteenMonthsEverInHiv"));
		//
		// indicatorsPreArtDataElement.put("A-10",
		// service.pedsUnderFiveEverInHiv(
		// startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.pedsUnderFiveEverInHiv"));
		//
		// indicatorsPreArtDataElement.put("A-11", service
		// .femalePedsUnderFifteenEverInHiv(startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.femalePedsUnderFifteenEverInHiv"));
		//
		// indicatorsPreArtDataElement.put("A-12", service
		// .malePedsUnderFifteenEverInHiv(startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.malePedsUnderFifteenEverInHiv"));
		//
		// indicatorsPreArtDataElement.put("A-13", service
		// .femaleMoreThanFifteenEverInHiv(startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.femaleMoreThanFifteenEverInHiv"));
		//
		// indicatorsPreArtDataElement.put("A-14", service
		// .maleMoreThanFifteenEverInHiv(startDate, endDate));
		// preart_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.maleMoreThanFifteenEverInHiv"));

		indicatorsPreArtDataElement.put("1.A-09", service
				.patientsOnCotrimoProphylaxis(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.patientsOnCotrimoProphylaxis"));

		indicatorsPreArtDataElement.put("1.A-10", service
				.patientsActiveTbAtEnrolThisMonth(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.patientsActiveTbAtEnrolThisMonth"));

		indicatorsPreArtDataElement.put("1.A-11", service
				.patientsTbPositiveAtEnrolThisMonth(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.patientsTbPositiveAtEnrolThisMonth"));

		indicatorsPreArtDataElement.put("1.A-12", service
				.newEnrolledPedsStartTbTreatThisMonth(startDate, endDate));
		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newEnrolledPedsStartTbTreatThisMonth"));

		indicatorsPreArtDataElement.put("1.A-13", service
				.newEnrolledAdultsStartTbTreatThisMonth(startDate, endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.newEnrolledAdultsStartTbTreatThisMonth"));

		indicatorsPreArtDataElement.put("1.A-14", service
				.PatientsInPreARVDiedThisMonth(startDate, endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVDiedThisMonth"));

		indicatorsPreArtDataElement.put("1.A-15", service
				.PatientsInPreARVTransferredInThisMonth(startDate, endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTransferredInThisMonth"));

		indicatorsPreArtDataElement.put("1.A-16", service
				.PatientsInPreARVTransferredOutThisMonth(startDate, endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTransferredOutThisMonth"));

		indicatorsPreArtDataElement.put("1.A-17", service
				.PatientsInPreARVTLostToFollowUpThisMonth(startDate, endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTLostToFollowUpThisMonth"));

		indicatorsPreArtDataElement.put("1.A-18", service
				.PatientsInPreARVTLostToFollowUpNotLostThisMonth(startDate,
						endDate));

		preart_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTLostToFollowUpNotLostThisMonth"));

		/*
		 * indicatorsPreArtDataElement.put("1.A-19",
		 * service.patientsOnCotrimoProphylaxisLessThan15Years(startDate,
		 * endDate));
		 * 
		 * preart_msg .add(ContextProvider.getMessage(
		 * "tracnetreporting.indicator.preart.patientsPedsCurrentlyOnCotrimoProphylaxis"
		 * ));
		 * 
		 * indicatorsPreArtDataElement.put("1.A-20", service
		 * .patientsOnCotrimoProphylaxisGreatherThan15Years(startDate,
		 * endDate));
		 * 
		 * preart_msg .add(ContextProvider.getMessage(
		 * "tracnetreporting.indicator.preart.patientsAdultsCurrentlyOnCotrimoProphylaxis"
		 * ));
		 */

		mav.addObject("indicatorsPreArtDataElement_msg", preart_msg);
		request.getSession().setAttribute("preartdel_msg", preart_msg);

	}

	/**
	 * Populates ART Data Elements indicators list
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of ART Data elements
	 * @throws ParseException
	 */
	private void indicatorsArtDataElement(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> art_msg = new ArrayList<String>();

		indicatorsArtDataElement.put("1.B-01", service
				.pedsUnderEighteenMonthsCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.pedsUnderEighteenMonthsCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-02", service
				.pedsUnderFiveCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.pedsUnderFiveCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-03", service
				.femalePedsUnderFifteenCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.femalePedsUnderFifteenCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-04", service
				.malePedsUnderFifteenCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.malePedsUnderFifteenCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-05", service.pedsOnFirstLineReg(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.pedsOnFirstLineReg"));

		indicatorsArtDataElement.put("1.B-06", service.pedsOnSecondLineReg(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.pedsOnSecondLineReg"));

		indicatorsArtDataElement.put("1.B-07", service
				.femaleMoreThanFifteenCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.femaleMoreThanFifteenCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-08", service
				.maleMoreThanFifteenCurrentOnArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.maleMoreThanFifteenCurrentOnArv"));

		indicatorsArtDataElement.put("1.B-09", service.adultOnFirstLineReg(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.adultOnFirstLineReg"));

		indicatorsArtDataElement.put("1.B-10", service.adultOnSecondLineReg(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.adultOnSecondLineReg"));

		indicatorsArtDataElement
				.put("1.B-11", service
						.newPedsUnderEighteenMonthStartArvThisMonth(startDate,
								endDate));
		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsUnderEighteenMonthStartArvThisMonth"));

		indicatorsArtDataElement.put("1.B-12", service
				.newPedsUnderFiveStartArvThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsUnderFiveStartArvThisMonth"));

		indicatorsArtDataElement
				.put("1.B-13", service
						.newFemalePedsUnderFifteenStartArvThisMonth(startDate,
								endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newFemalePedsUnderFifteenStartArvThisMonth"));

		/*
		 * indicatorsArtDataElement.put("1.B-14", service
		 * .newMalePedsUnderFifteenStartArvThisMonth(startDate, endDate));
		 * 
		 * art_msg .add(ContextProvider.getMessage(
		 * "tracnetreporting.indicator.art.newMalePedsUnderFifteenStartArvThisMonth"
		 * ));
		 */

		indicatorsArtDataElement.put("1.B-14", service
				.newPedsWhoStageFourThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsWhoStageFourThisMonth"));

		indicatorsArtDataElement.put("1.B-15", service
				.newPedsWhoStageThreeThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsWhoStageThreeThisMonth"));

		indicatorsArtDataElement.put("1.B-16", service
				.newPedsWhoStageTwoThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsWhoStageTwoThisMonth"));

		indicatorsArtDataElement.put("1.B-17", service
				.newPedsWhoStageOneThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsWhoStageOneThisMonth"));

		indicatorsArtDataElement.put("1.B-18", service
				.newPedsUndefinedWhoStageThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newPedsUndefinedWhoStageThisMonth"));

		indicatorsArtDataElement.put("1.B-19", service
				.newFemaleAdultStartiArvThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newFemaleAdultStartiArvThisMonth"));

		indicatorsArtDataElement.put("1.B-20", service
				.newMaleAdultStartiArvThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newMaleAdultStartiArvThisMonth"));
		indicatorsArtDataElement.put("1.B-21", service
				.newAdultWhoStageFourThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newAdultWhoStageFourThisMonth"));

		indicatorsArtDataElement.put("1.B-22", service
				.newAdultWhoStageThreeThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newAdultWhoStageThreeThisMonth"));

		indicatorsArtDataElement.put("1.B-23", service
				.newAdultWhoStageTwoThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newAdultWhoStageTwoThisMonth"));

		indicatorsArtDataElement.put("1.B-24", service
				.newAdultWhoStageOneThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newAdultWhoStageOneThisMonth"));
		indicatorsArtDataElement.put("1.B-25", service
				.newAdultUndefinedWhoStageThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.newAdultUndefinedWhoStageThisMonth"));
		indicatorsArtDataElement.put("1.B-26", service
				.arvPedsFifteenInterruptTreatThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvPedsFifteenInterruptTreatThisMonth"));

		indicatorsArtDataElement.put("1.B-27", service
				.arvAdultFifteenInterruptTreatThisMonth(startDate, endDate));
		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvAdultFifteenInterruptTreatThisMonth"));

		indicatorsArtDataElement.put("1.B-28", service.arvPedsDiedThisMonth(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvPedsDiedThisMonth"));

		indicatorsArtDataElement.put("1.B-29", service.arvAdultDiedThisMonth(
				startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvAdultDiedThisMonth"));

		indicatorsArtDataElement.put("1.B-30", service
				.arvPedsLostFollowupMoreThreeMonths(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvPedsLostFollowupMoreThreeMonths"));

		indicatorsArtDataElement.put("1.B-31", service
				.arvAdultLostFollowupMoreThreeMonths(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvAdultLostFollowupMoreThreeMonths"));

		indicatorsArtDataElement.put("1.B-32", service
				.maleOnTreatTwelveAfterInitArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.maleOnTreatTwelveAfterInitArv"));

		indicatorsArtDataElement.put("1.B-33", service
				.femaleOnTreatTwelveAfterInitArv(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.femaleOnTreatTwelveAfterInitArv"));

		indicatorsArtDataElement.put("1.B-34", service
				.arvPedsTransferredOutThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvPedsTransferredOutThisMonth"));

		indicatorsArtDataElement.put("1.B-35", service
				.arvAdultTransferredOutThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvAdultTransferredOutThisMonth"));

		indicatorsArtDataElement.put("1.B-36", service
				.arvPedsTransferredInThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvPedsTransferredInThisMonth"));

		indicatorsArtDataElement.put("1.B-37", service
				.arvAdultTransferreInThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.arvAdultTransferreInThisMonth"));

		indicatorsArtDataElement.put("1.B-38", service
				.PatientsInARVTLostToFollowUpNotLostThisMonth(startDate,
						endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.PatientsInARVTLostToFollowUpNotLostThisMonth"));

		mav.addObject("indicatorsArtDataElement_msg", art_msg);

		indicatorsArtDataElement.put("1.B-39", service
				.patientsPediatricNewOnSecondLineThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.patientsPediatricNewOnSecondLineThisMonth"));

		mav.addObject("indicatorsArtDataElement_msg", art_msg);

		indicatorsArtDataElement.put("1.B-40", service
				.patientsAdultNewOnSecondLineThisMonth(startDate, endDate));

		art_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.art.patientsAdultNewOnSecondLineThisMonth"));

		mav.addObject("indicatorsArtDataElement_msg", art_msg);

		request.getSession().setAttribute("artdel_msg", art_msg);

	}

	/**
	 * Populates STIs, Opportunistic Infections and Others
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators STIs, Opportunistic Infections and Others
	 */
	private void indicatorsStiOpportAndOthers(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> sti_msg = new ArrayList<String>();

		indicatorsStiOpportAndOthers.put("1.C-01", service
				.clientsCounceledForStiThisMonth(startDate, endDate));
		sti_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.clientsCounceledForStiThisMonth"));

		indicatorsStiOpportAndOthers.put("1.C-02", service
				.stiDiagnosedThisMonth(startDate, endDate));
		sti_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.stiDiagnosedThisMonth"));

		indicatorsStiOpportAndOthers.put("1.C-03", service
				.opportInfectTreatedExcludeTbThisMonth(startDate, endDate));
		sti_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.opportInfectTreatedExcludeTbThisMonth"));

		mav.addObject("indicatorsStiOpportAndOthers_msg", sti_msg);

		request.getSession().setAttribute("stidel_msg", sti_msg);

	}

	/**
	 * Populates Nutrition Consultation Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Nutrition Consultation Data elements
	 */
	private void indicatorsNutritionDataElem(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> nutrition_msg = new ArrayList<String>();

		indicatorsNutritionDataElem.put("1.D-01", service
				.pedsUnderFiveSevereMalnutrThisMonth(startDate, endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pedsUnderFiveSevereMalnutrThisMonth"));

		indicatorsNutritionDataElem
				.put("1.D-02", service
						.pedsUnderFiveSevereMalnutrTheurapThisMonth(startDate,
								endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pedsUnderFiveSevereMalnutrTheurapThisMonth"));

		indicatorsNutritionDataElem.put("1.D-03", service
				.pedsUnderFifteenSevMalnutrTherapThisMonth(startDate, endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pedsUnderFifteenSevMalnutrTherapThisMonth"));

		indicatorsNutritionDataElem.put("1.D-04", service
				.adultSevereMalnutrTherapThisMonth(startDate, endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.adultSevereMalnutrTherapThisMonth"));

		indicatorsNutritionDataElem.put("1.D-05", service
				.numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling(
						startDate, endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling"));

		indicatorsNutritionDataElem.put("1.D-06", service
				.numberOfPatientsWhoReceivedFamilyPlanningThisMonth(startDate,
						endDate));
		nutrition_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.numberOfPatientsWhoReceivedFamilyPlanningThisMonth"));

		// indicatorsNutritionDataElem.put("1.D-05", service
		// .pregnantMalnutrTherapThisMonth(startDate, endDate));
		// nutrition_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.pregnantMalnutrTherapThisMonth"));
		//		 
		// indicatorsNutritionDataElem.put("1.D-06", service
		// .lactatingMalnutrTherapThisMonth(startDate, endDate));
		// nutrition_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.lactatingMalnutrTherapThisMonth"));
		//		
		// indicatorsNutritionDataElem.put("1.D-07", service
		// .pedsUnderFiveWithSevMalnutrThisMonth(startDate, endDate));
		// nutrition_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.pedsUnderFiveWithSevMalnutrThisMonth"));
		//		  
		// indicatorsNutritionDataElem.put("1.D-08", service
		// .pedsTherapThisMonth(startDate, endDate));
		// nutrition_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.pedsTherapThisMonth"));
		//		
		// indicatorsNutritionDataElem.put("1.D-09", service
		// .adultTherapThisMonth(startDate, endDate));
		// nutrition_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.adultTherapThisMonth"));

		mav.addObject("indicatorsNutritionDataElement_msg", nutrition_msg);

		request.getSession().setAttribute("nutridel_msg", nutrition_msg);

	}

	/**
	 * Populates Antenatal Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Antenatal Data elements
	 * @throws ParseException
	 */
	private void indicatorsAntenatalDataElem(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> antenatal_msg = new ArrayList<String>();

		indicatorsAntenatalDataElem.put("2.A-01", service
				.womenUnknownHivFirstAntenatal(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenUnknownHivFirstAntenatal"));

		indicatorsAntenatalDataElem.put("2.A-02", service
				.womenKnownHivPosFirstAntenatal(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenKnownHivPosFirstAntenatal"));

		indicatorsAntenatalDataElem.put("2.A-03", service
				.womenUnknownHivTested(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenUnknownHivTested"));

		indicatorsAntenatalDataElem.put("2.A-04", service.womenHivPosReturnRes(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.womenHivPosReturnRes"));

		indicatorsAntenatalDataElem.put("2.A-05", service.womenHivPosTestedCd4(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.womenHivPosTestedCd4"));

		indicatorsAntenatalDataElem.put("2.A-06", service
				.pregnantHivPosEligibleArvs1(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantHivPosEligibleArvs1"));

		indicatorsAntenatalDataElem.put("2.A-07", service
				.negativeWomenReturnRes(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.negativeWomenReturnRes"));

		indicatorsAntenatalDataElem.put("2.A-08", service.pregnantHivPos(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.pregnantHivPos"));

		/*
		 * indicatorsAntenatalDataElem.put("2.A-09", service
		 * .pregnantHivPosAztProphyAt28Weeks(startDate, endDate)); antenatal_msg
		 * .add(ContextProvider
		 * .getMessage("tracnetreporting.indicator.pregnantHivPosAztProphyAt28Weeks"
		 * ));
		 */

		indicatorsAntenatalDataElem.put("2.A-09", service
				.pregnantHivPosTripleTheraProphy(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantHivPosTripleTheraProphy"));
		
		indicatorsAntenatalDataElem.put("2.A-10", service
				.numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis"));

		indicatorsAntenatalDataElem.put("2.A-11", service
				.pregnantHivPosEligibleArvs2(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantHivPosEligibleArvs2"));

		indicatorsAntenatalDataElem.put("2.A-12", service.womenTestedForRpr(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.womenTestedForRpr"));

		indicatorsAntenatalDataElem.put("2.A-13", service
				.pregnantTestedPosForRpr(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantTestedPosForRpr"));

		indicatorsAntenatalDataElem.put("2.A-14", service
				.pregnantPartnersTestedForHiv(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantPartnersTestedForHiv"));

		indicatorsAntenatalDataElem.put("2.A-15", service
				.hivNegPregnantPartnersTestedHivPos(startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.hivNegPregnantPartnersTestedHivPos"));

		indicatorsAntenatalDataElem.put("2.A-16", service.discordantCouples1(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.discordantCouples1"));

		indicatorsAntenatalDataElem.put("2.A-17", service.partnersTestedHivPos(
				startDate, endDate));
		antenatal_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.partnersTestedHivPos"));

		indicatorsAntenatalDataElem.put("2.A-18",
				service.pregnantHivPosStartedCotrimoxazoleThisMonth(startDate,
						endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.pregnantHivPosStartedCotrimoxazoleThisMonth"));

		indicatorsAntenatalDataElem
				.put(
						"2.A-19",
						service
								.NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(
										startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth"));

		indicatorsAntenatalDataElem
				.put(
						"2.A-20",
						service
								.NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(
										startDate, endDate));
		antenatal_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth"));

		mav.addObject("indicatorsAntenatalDataElement_msg", antenatal_msg);

		request.getSession().setAttribute("andel_msg", antenatal_msg);
	}

	/**
	 * Populates Maternity Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Maternity Data elements
	 * @throws ParseException
	 */
	private void indicatorsMaternityDataElem(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> maternity_msg = new ArrayList<String>();

		indicatorsMaternityDataElem.put("2.B-01", service
				.expectedDeliveriesFacilityThisMonth(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.expectedDeliveriesFacilityThisMonth"));

		indicatorsMaternityDataElem.put("2.B-02", service
				.occuringDeliveriesFacilityThisMonth(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.occuringDeliveriesFacilityThisMonth"));

		indicatorsMaternityDataElem.put("2.B-03", service
				.expectedDeliveriesAmongHivPosWomen(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.expectedDeliveriesAmongHivPosWomen"));

		indicatorsMaternityDataElem.put("2.B-04", service
				.womenHivPosGivingBirthAtFacility(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosGivingBirthAtFacility"));

		indicatorsMaternityDataElem.put("2.B-05", service
				.reportedHivPosGivingBirthAtHome(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.reportedHivPosGivingBirthAtHome"));

		indicatorsMaternityDataElem.put("2.B-06", service
				.womenHivPosAzt3tcNvpDuringLabor(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosAzt3tcNvpDuringLabor"));

		indicatorsMaternityDataElem.put("2.B-07", service
				.womenReceivingAzt3tcAfterDelivery(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenReceivingAzt3tcAfterDelivery"));

		indicatorsMaternityDataElem.put("2.B-08", service
				.womenUnknownHivStatusTestedDuringLabor1(startDate, endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenUnknownHivStatusTestedDuringLabor1"));

		indicatorsMaternityDataElem
				.put("2.B-09", service
						.womenUnknownHivStatusTestedPosDuringLabor2(startDate,
								endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenUnknownHivStatusTestedPosDuringLabor2"));

		/*
		 * indicatorsMaternityDataElem.put("2.B-10", service
		 * .pregnantReceivedCompleteCourseThisMonth(startDate, endDate));
		 * maternity_msg .add(ContextProvider.getMessage(
		 * "tracnetreporting.indicator.pregnantReceivedCompleteCourseThisMonth"
		 * ));
		 */

		indicatorsMaternityDataElem
				.put("2.B-10", service
						.NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis(startDate,
								endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis"));

		indicatorsMaternityDataElem
				.put("2.B-11", service
						.NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth(startDate,
								endDate));
		maternity_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth"));

		mav.addObject("indicatorMaternityDataElement_msg", maternity_msg);

		request.getSession().setAttribute("mdel_msg", maternity_msg);

	}

	/**
	 * Populates HIV Exposed Infant Follow-up
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of HIV Exposed Infant Follow-up Data
	 *         elements
	 * @throws ParseException
	 */
	private void indicatorsHivExposedInfantFollowup(

	TracNetIndicatorService service, String startDate, String endDate,
			ModelAndView mav, HttpServletRequest request) throws ParseException {

		List<String> hivexposed_msg = new ArrayList<String>();

		indicatorsHivExposedInfantFollowup.put("2.C-01", service
				.womenHivPosBreastFeeding(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosBreastFeeding"));

		indicatorsHivExposedInfantFollowup.put("2.C-02", service
				.womenHivPosUsingFormula(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosUsingFormula"));

		indicatorsHivExposedInfantFollowup.put("2.C-03", service
				.infantHivPosMothersEnrolledPmtct(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersEnrolledPmtct"));

		indicatorsHivExposedInfantFollowup.put("2.C-04", service
				.infantHivPosMothersTestedAt6Weeks(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt6Weeks"));

		indicatorsHivExposedInfantFollowup.put("2.C-05", service
				.infantHivPosMothersTestedPosAt6Weeks(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt6Weeks"));

		indicatorsHivExposedInfantFollowup.put("2.C-06", service
				.infantHivPosMothersAged6WeeksThisMonth(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersAged6WeeksThisMonth"));

		indicatorsHivExposedInfantFollowup.put("2.C-07", service
				.infantHivPosMothersTestedAt9Months(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt9Months"));

		indicatorsHivExposedInfantFollowup.put("2.C-08", service
				.infantHivPosMothersTestedPosAt9Months(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt9Months"));

		indicatorsHivExposedInfantFollowup.put("2.C-09", service
				.infantHivPosMothersAged9MonthsThisMonth(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersAged9MonthsThisMonth"));
		indicatorsHivExposedInfantFollowup.put("2.C-10", service
				.infantHivPosMothersTestedAt18Months(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt18Months"));

		indicatorsHivExposedInfantFollowup.put("2.C-11", service
				.infantHivPosMothersTestedPosAt18Months(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt18Months"));

		indicatorsHivExposedInfantFollowup
				.put("2.C-12", service
						.infantHivPosMothersAgedAt18MonthsThisMonth(startDate,
								endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersAgedAt18MonthsThisMonth"));

		indicatorsHivExposedInfantFollowup.put("2.C-13", service
				.infantHivPosMothersLostFollowup(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersLostFollowup"));

		indicatorsHivExposedInfantFollowup.put("2.C-14", service
				.infantHivPosMothersScreenedTbThisMonth(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersScreenedTbThisMonth"));

		indicatorsHivExposedInfantFollowup.put("2.C-15", service
				.reportedDeadInfantHivPosMothers(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.reportedDeadInfantHivPosMothers"));

		indicatorsHivExposedInfantFollowup.put("2.C-16", service
				.infantHivPosMothersMalnourished(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersMalnourished"));

		indicatorsHivExposedInfantFollowup.put("2.C-17", service
				.infantHivPosMothersTherapFood(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersTherapFood"));

		indicatorsHivExposedInfantFollowup.put("2.C-18", service
				.infantHivPosMothersCotrimoAt6Weeks(startDate, endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivPosMothersCotrimoAt6Weeks"));

		// indicatorsHivExposedInfantFollowup.put("2.C-19", service
		// .newInfantHivPosMothersNvpAztAtBirth(startDate, endDate));
		// hivexposed_msg
		// .add(ContextProvider
		// .getMessage("tracnetreporting.indicator.newInfantHivPosMothersNvpAztAtBirth"));

		indicatorsHivExposedInfantFollowup.put("2.C-19", service
				.infantHivNegMothersInDiscordantCouplesEnrolledPmtct(startDate,
						endDate));
		hivexposed_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.infantHivNegMothersInDiscordantCouplesEnrolledPmtct"));

		mav.addObject("indicatorsHivExposedInfantFollowup_msg", hivexposed_msg);

		request.getSession().setAttribute("ifp_msg", hivexposed_msg);
	}

	/**
	 * Populates Family Planning Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Family Planning Data elements
	 */
	private void indicatorsFamilyPlanningDataElem(
			TracNetIndicatorService service, String startDate, String endDate,
			ModelAndView mav, HttpServletRequest request) throws ParseException {

		List<String> familyplanning_msg = new ArrayList<String>();

		indicatorsFamilyPlanningDataElem.put("2.D-01", service
				.womenHivPosExpectedFpAtFacility(startDate, endDate));
		familyplanning_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosExpectedFpAtFacility"));

		indicatorsFamilyPlanningDataElem.put("2.D-02", service
				.womenHivPosSeenInFp(startDate, endDate));
		familyplanning_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.womenHivPosSeenInFp"));

		indicatorsFamilyPlanningDataElem.put("2.D-03", service
				.womenHivPosPartnersSeenInFp(startDate, endDate));
		familyplanning_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosPartnersSeenInFp"));

		indicatorsFamilyPlanningDataElem.put("2.D-04", service
				.womenHivPosReceivingModernContraceptive(startDate, endDate));
		familyplanning_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosReceivingModernContraceptive"));

		indicatorsFamilyPlanningDataElem.put("2.D-05", service
				.womenHivPosRefferedForFp(startDate, endDate));
		familyplanning_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.womenHivPosRefferedForFp"));

		mav.addObject("indicatorsFamilyPlanningDataElem_msg",
				familyplanning_msg);

		request.getSession().setAttribute("fpel_msg", familyplanning_msg);
	}

	/**
	 * Populates Submit VCT Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Submit VCT Data elements
	 */
	private void indicatorsSubmitVctDataElem(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> vct_msg = new ArrayList<String>(); // +18505707088

		indicatorsSubmitVctDataElem.put("2.E-01", service
				.newFemaleUnderFifteenCounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemaleUnderFifteenCounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-02", service
				.newMaleUnderFifteenCounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMaleUnderFifteenCounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-03", service
				.newFemaleFifteenTo24CounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemaleFifteenTo24CounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-04", service
				.newMaleFifteenTo24CounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMaleFifteenTo24CounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-05", service
				.newFemaleMore25CounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemaleMore25CounseledTested"));
		indicatorsSubmitVctDataElem.put("2.E-06", service
				.newMaleMore25CounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMaleMore25CounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-07", service
				.couplesCounseledTested(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.couplesCounseledTested"));

		indicatorsSubmitVctDataElem.put("2.E-08", service.discordantCouples2(
				startDate, endDate));
		vct_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.discordantCouples2"));

		indicatorsSubmitVctDataElem.put("2.E-09", service
				.newFemaleUnderFifteenTestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemaleUnderFifteenTestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-10", service
				.newMaleUnderFifteenTestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMaleUnderFifteenTestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-11", service
				.newFemale15To24TestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemale15To24TestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-12", service
				.newMale15To24TestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMale15To24TestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-13", service
				.newFemaleMore25TestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newFemaleMore25TestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-14", service
				.newMaleMore25TestReceiveRes(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newMaleMore25TestReceiveRes"));

		indicatorsSubmitVctDataElem.put("2.E-15", service
				.femaleHivPosUnderFifteen(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleHivPosUnderFifteen"));
		indicatorsSubmitVctDataElem.put("2.E-16", service
				.maleHivPosUnderFifteen(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleHivPosUnderFifteen"));

		indicatorsSubmitVctDataElem.put("2.E-17", service
				.femaleHivPosUnder15to24(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleHivPosUnder15to24"));

		indicatorsSubmitVctDataElem.put("2.E-18", service
				.maleHivPosUnder15to24(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleHivPosUnder15to24"));

		indicatorsSubmitVctDataElem.put("2.E-19", service
				.femaleHivPosMoreThan25(startDate, endDate));
		vct_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleHivPosMoreThan25"));

		indicatorsSubmitVctDataElem.put("2.E-20", service.maleHivPosMoreThan25(
				startDate, endDate));
		vct_msg.add(ContextProvider
				.getMessage("tracnetreporting.indicator.maleHivPosMoreThan25"));

		mav.addObject("indicatorsSubmitVctDataElem_msg", vct_msg);

		request.getSession().setAttribute("vctdel_msg", vct_msg);

	}

	/**
	 * Populates Provider-Initiated Testing (PIT) Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of Provider-Initiated Testing (PIT) Data
	 *         elements
	 */
	private void indicatorsProviderTestPitDataElem(
			TracNetIndicatorService service, String startDate, String endDate,
			ModelAndView mav, HttpServletRequest request) {

		List<String> pit_msg = new ArrayList<String>(); // +18505707088

		indicatorsProviderTestPitDataElem.put("2.F-01", service
				.femaleUnderFifteenCounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleUnderFifteenCounseledThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-02", service
				.maleUnderFifteenCounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleUnderFifteenCounseledThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-03", service
				.female15To24CounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.female15To24CounseledThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-04", service
				.male15To24CounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.male15To24CounseledThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-05", service
				.femaleMoreThan25CounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleMoreThan25CounseledThroughPit"));
		indicatorsProviderTestPitDataElem.put("2.F-06", service
				.maleMoreThan25CounseledThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleMoreThan25CounseledThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-07", service
				.femaleUnderFifteenHivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleUnderFifteenHivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-08", service
				.maleUnderFifteenHivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleUnderFifteenHivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-09", service
				.female15To24HivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.female15To24HivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-10", service
				.male15To24HivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.male15To24HivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-11", service
				.femaleMoreThan25HivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleMoreThan25HivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-12", service
				.maleMoreThan25HivResThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleMoreThan25HivResThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-13", service
				.femaleUnderFifteenHivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleUnderFifteenHivPosThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-14", service
				.maleUnderFifteenHivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleUnderFifteenHivPosThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-15", service
				.female15To24HivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.female15To24HivPosThroughPit"));
		indicatorsProviderTestPitDataElem.put("2.F-16", service
				.male15To24HivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.male15To24HivPosThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-17", service
				.femaleMoreThan25HivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.femaleMoreThan25HivPosThroughPit"));

		indicatorsProviderTestPitDataElem.put("2.F-18", service
				.maleMoreThan25HivPosThroughPit(startDate, endDate));
		pit_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.maleMoreThan25HivPosThroughPit"));

		mav.addObject("indicatorsProviderTestPitDataElem_msg", pit_msg);

		request.getSession().setAttribute("pitdel_msg", pit_msg);

	}

	/**
	 * Populates PEP Data Elements
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the list of indicators of PEP Data elements
	 */
	private void indicatorsPepDataElem(TracNetIndicatorService service,
			String startDate, String endDate, ModelAndView mav,
			HttpServletRequest request) throws ParseException {

		List<String> pep_msg = new ArrayList<String>(); 

		indicatorsPepDataElem.put("2.G-01", service
				.newAtRiskHivOccupationExposure(startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOccupationExposure"));

		indicatorsPepDataElem.put("2.G-02", service.newAtRiskHivRapeAssault(
				startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssault"));

		indicatorsPepDataElem.put("2.G-03", service
				.newAtRiskHivOtherNoneOccupationExposure(startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupationExposure"));

		indicatorsPepDataElem.put("2.G-04", service
				.newAtRiskHivOccupationExposurePep(startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOccupationExposurePep"));

		indicatorsPepDataElem.put("2.G-05", service.newAtRiskHivRapeAssaultPep(
				startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssaultPep"));
		indicatorsPepDataElem
				.put("2.G-06", service
						.newAtRiskHivOtherNoneOccupationExposurePep(startDate,
								endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupationExposurePep"));

		indicatorsPepDataElem.put("2.G-07", service
				.newAtRiskHivOccupExpo3MonthAfterPep(startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOccupExpo3MonthAfterPep"));

		indicatorsPepDataElem.put("2.G-08", service
				.newAtRiskHivRapeAssault3MonthAfterPep(startDate, endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssault3MonthAfterPep"));

		indicatorsPepDataElem.put("2.G-09", service
				.newAtRiskHivOtherNoneOccupExpo3MonthAfterPep(startDate,
						endDate));
		pep_msg
				.add(ContextProvider
						.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupExpo3MonthAfterPep"));

		mav.addObject("indicatorsPepDataElem_msg", pep_msg);

		request.getSession().setAttribute("pepdel_msg", pep_msg);

	}

}
