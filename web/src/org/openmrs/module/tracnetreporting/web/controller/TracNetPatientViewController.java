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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.tracnetreporting.service.ContextProvider;
import org.openmrs.module.tracnetreporting.service.TracNetIndicatorService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *This controller backs the /web/module/tracnetpatientsList.jsp page. This
 * controller is tied to that jsp page in the
 * /metadata/moduleApplicationContext.xml file
 */
public class TracNetPatientViewController extends ParameterizableViewController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	private String startDate, endDate, indicatorValue;

	// private ArrayList<Person> patientsList = null;
	private ArrayList<Person> patientsListPreArt = null;
	private ArrayList<Person> patientsListArt = null;
	private ArrayList<Person> patientsListStiOpportAndOthers = null;
	private ArrayList<Person> patientsListNutrition = null;
	private ArrayList<Person> patientsListAntenatal = null;
	private ArrayList<Person> patientsListMaternity = null;
	private ArrayList<Person> patientsListInfantFollowup = null;
	private ArrayList<Person> patientsListFamilyPlanning = null;
	private ArrayList<Person> patientsListSubmitVct = null;
	private ArrayList<Person> patientsListProviderTestPit = null;
	private ArrayList<Person> patientsListPep = null;
	private TracNetIndicatorService service;

	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();
		service = Context.getService(TracNetIndicatorService.class);
		// DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		if (request.getParameter("indicator") != null
				&& request.getParameter("startDate") != null
				&& request.getParameter("endDate") != null) {

			log.info("indicator+++++++++++++++++++++++++"
					+ request.getParameter("indicator"));

			String[] startDateWords = request.getParameter("startDate").split(
					"/");
			String[] endDateWords = request.getParameter("endDate").split("/");

			indicatorValue = request.getParameter("indicator");

			// Gets reversed date format comparing to what we get from
			// parameters (startDate, endDate)
			startDate = startDateWords[2] + "/" + startDateWords[1] + "/"
					+ startDateWords[0];
			endDate = endDateWords[2] + "/" + endDateWords[1] + "/"
					+ endDateWords[0];

			// ********** A. PRE-ART Data Elements **********
			log.info(",,,,,,,,,,,,,,,,,,,,,,,,,,, "
					+ (patientsPreArtDataElements(indicatorValue, startDate,
							endDate, mav) != null));
			if (patientsPreArtDataElements(indicatorValue, startDate, endDate,
					mav) != null) {

				log.info("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

				patientsListPreArt = new ArrayList<Person>();
				patientsListPreArt.addAll(patientsPreArtDataElements(
						indicatorValue, startDate, endDate, mav));
				if (request.getParameter("trackingPatients") != null)
					service
							.exportPatientsListToExcelFile(
									request,
									response,
									patientsListPreArt,
									"Pre-ART Data Elements report",
									ContextProvider
											.getMessage("tracnetreporting.category.preartdataelement"),
									startDate, endDate);

				// if (request.getParameter("export") != null &&
				// request.getParameter("export").compareTo("xls")==0)
				// {
				//					
				//					
				// log.info("fffffffffffffffffffffffffffffffffffff excel");
				// service
				// .exportPatientsListToExcelFile(
				// request,
				// response,
				// patientsListPreArt,
				// "Pre-ART Data Elements report",
				// ContextProvider
				// .getMessage("tracnetreporting.category.preartdataelement"),
				// startDate, endDate);
				// log.info("exporttttttttttttttttttttttttttttttttttttt"+patientsListPreArt);
				//					
				//					
				// }
				// else if (request.getParameter("export") != null &&
				// request.getParameter("export").compareTo("csv")==0){
				//					
				// log.info("fffffffffffffffffffffffffffffffffffff csv");
				//					
				// }
			}

			// ********** B. ART Data Elements **********
			if (patientsArtDataElement(indicatorValue, startDate, endDate, mav) != null) {
				patientsListArt = new ArrayList<Person>();
				patientsListArt.addAll(patientsArtDataElement(indicatorValue,
						startDate, endDate, mav));
				if (request.getParameter("exportPatientsToExcel") != null)
					service
							.exportPatientsListToExcelFile(
									request,
									response,
									patientsListArt,
									"ART Data Elements report",
									ContextProvider
											.getMessage("tracnetreporting.category.artdataelement"),
									startDate, endDate);
			}

			// ********** C. STIs, Opportunistic Infections and Others
			// **********
			if (patientsStiOpportAndOthers(indicatorValue, startDate, endDate,
					mav) != null) {
				patientsListStiOpportAndOthers = new ArrayList<Person>();
				patientsListStiOpportAndOthers
						.addAll(patientsStiOpportAndOthers(indicatorValue,
								startDate, endDate, mav));
			}

			// ********** D. Nutrition Consultation Data Elements **********
			if (patientsNutritionDataElem(indicatorValue, startDate, endDate,
					mav) != null) {
				patientsListNutrition = new ArrayList<Person>();
				patientsListNutrition.addAll(patientsNutritionDataElem(
						indicatorValue, startDate, endDate, mav));
			}

			// ********** A. Antenatal Data Elements **********
			if (patientsAntenatalDataElem(indicatorValue, startDate, endDate,
					mav) != null) {
				patientsListAntenatal = new ArrayList<Person>();
				patientsListAntenatal.addAll(patientsAntenatalDataElem(
						indicatorValue, startDate, endDate, mav));

				if (request.getParameter("trackingPatients") != null)
					service
							.exportPatientsListToExcelFile(
									request,
									response,
									patientsListAntenatal,
									"Antenatal Data Elements report",
									ContextProvider
											.getMessage("tracnetreporting.category.antenataldataelem"),
									startDate, endDate);

			}

			// ********** B. Maternity Data Elements **********
			if (patientsMaternityDataElem(indicatorValue, startDate, endDate,
					mav) != null) {
				patientsListMaternity = new ArrayList<Person>();
				patientsListMaternity.addAll(patientsMaternityDataElem(
						indicatorValue, startDate, endDate, mav));

				if (request.getParameter("trackingPatients") != null)
					service
							.exportPatientsListToExcelFile(
									request,
									response,
									patientsListMaternity,
									"Maternity Data Elements report",
									ContextProvider
											.getMessage("tracnetreporting.category.maternitydataelem"),
									startDate, endDate);
			}

			// ********** C. HIV Exposed Infant Follow-up **********
			if (patientsHivExposedInfantFollowup(indicatorValue, startDate,
					endDate, mav) != null) {
				patientsListInfantFollowup = new ArrayList<Person>();
				patientsListInfantFollowup
						.addAll(patientsHivExposedInfantFollowup(
								indicatorValue, startDate, endDate, mav));
			}

			// ********** D. Family Planning Data Elements **********
			if (patientsFamilyPlanningDataElem(indicatorValue, startDate,
					endDate, mav) != null) {
				patientsListFamilyPlanning = new ArrayList<Person>();
				patientsListFamilyPlanning
						.addAll(patientsFamilyPlanningDataElem(indicatorValue,
								startDate, endDate, mav));
			}

			// ********** E. Submit VCT Data Elements **********
			if (patientsSubmitVctDataElem(indicatorValue, startDate, endDate,
					mav) != null) {
				patientsListSubmitVct = new ArrayList<Person>();
				patientsListSubmitVct.addAll(patientsSubmitVctDataElem(
						indicatorValue, startDate, endDate, mav));
			}

			// ********** F. Provider-Initiated Testing (PIT) Data Elements
			// **********
			if (patientsProviderTestPitDataElem(indicatorValue, startDate,
					endDate, mav) != null) {
				patientsListProviderTestPit = new ArrayList<Person>();
				patientsListProviderTestPit
						.addAll(patientsProviderTestPitDataElem(indicatorValue,
								startDate, endDate, mav));
			}

			// ********** G. PEP Data Elements **********
			if (patientsPepDataElem(indicatorValue, startDate, endDate, mav) != null) {
				patientsListPep = new ArrayList<Person>();
				patientsListPep.addAll(patientsPepDataElem(indicatorValue,
						startDate, endDate, mav));
			}

		}

		// Exporting to Excel.
		// if (request.getParameter("exportPatientsToExcel") != null)
		// exportPatientsListsToExcelFile(request, response, startDate,
		// endDate);

		// setting the viewName via spring (moduleApplicationContext.xml)
		mav.setViewName(getViewName());

		mav.addObject("PreArtDataElements", patientsListPreArt);
		mav.addObject("ArtDataElement", patientsListArt);
		mav.addObject("StiOpportAndOthers", patientsListStiOpportAndOthers);
		mav.addObject("NutritionDataElem", patientsListNutrition);
		mav.addObject("AntenatalDataElem", patientsListAntenatal);
		mav.addObject("MaternityDataElem", patientsListMaternity);
		mav.addObject("HivExposedInfantFollowup", patientsListInfantFollowup);
		mav.addObject("FamilyPlanningDataElem", patientsListFamilyPlanning);
		mav.addObject("SubmitVctDataElem", patientsListSubmitVct);
		mav.addObject("ProviderTestPitDataElem", patientsListProviderTestPit);
		mav.addObject("PepDataElem", patientsListPep);

		return mav;
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
	private void exportPatientsListsToExcelFile(HttpServletRequest request,
			HttpServletResponse response, String startingDate, String endingDate)
			throws IOException {

		// -----------------------------ART
		// VARIABLES----------------------------

		if (request.getParameter("trackingPatients") != null
				&& request.getParameter("trackingPatients").equals(
						"preArtPatients")) {

			// patientsListPreArt = null;
			patientsListArt = null;
			patientsListStiOpportAndOthers = null;
			patientsListNutrition = null;
			patientsListAntenatal = null;
			patientsListMaternity = null;
			patientsListInfantFollowup = null;
			patientsListFamilyPlanning = null;
			patientsListSubmitVct = null;
			patientsListProviderTestPit = null;
			patientsListPep = null;

			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListPreArt,
							"Pre-ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.preartdataelement"),
							startingDate, endingDate);
		}

		if (request.getParameter("trackingPatients") != null
				&& request.getParameter("trackingPatients").equals(
						"artPatients")) {

			patientsListPreArt = null;
			// patientsListArt = null;
			patientsListStiOpportAndOthers = null;
			patientsListNutrition = null;
			patientsListAntenatal = null;
			patientsListMaternity = null;
			patientsListInfantFollowup = null;
			patientsListFamilyPlanning = null;
			patientsListSubmitVct = null;
			patientsListProviderTestPit = null;
			patientsListPep = null;

			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListArt,
							"ART Data Elements report",
							ContextProvider
									.getMessage("tracnetreporting.category.artdataelement"),
							startingDate, endingDate);
		}

		if (patientsListStiOpportAndOthers != null) {

			patientsListPreArt = null;
			patientsListArt = null;
			// patientsListStiOpportAndOthers = null;
			patientsListNutrition = null;
			patientsListAntenatal = null;
			patientsListMaternity = null;
			patientsListInfantFollowup = null;
			patientsListFamilyPlanning = null;
			patientsListSubmitVct = null;
			patientsListProviderTestPit = null;
			patientsListPep = null;

			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListStiOpportAndOthers,
							"STIs, Opportunistic Infections",
							ContextProvider
									.getMessage("tracnetreporting.category.stiopportandothers"),
							startingDate, endingDate);
		}

		if (patientsListNutrition != null) {

			patientsListPreArt = null;
			patientsListArt = null;
			patientsListStiOpportAndOthers = null;
			// patientsListNutrition = null;
			patientsListAntenatal = null;
			patientsListMaternity = null;
			patientsListInfantFollowup = null;
			patientsListFamilyPlanning = null;
			patientsListSubmitVct = null;
			patientsListProviderTestPit = null;
			patientsListPep = null;

			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListNutrition,
							"Nutrition Consultation",
							ContextProvider
									.getMessage("tracnetreporting.category.nutritiondataelem"),
							startingDate, endingDate);
		}

		// -------------------PREVENTION DATA ELEMENTS FOR
		// TRACNET---------------------

		if (patientsListAntenatal != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListAntenatal,
							"Antenatal Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.antenataldataelem"),
							startingDate, endingDate);

		if (patientsListMaternity != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListMaternity,
							"Maternity Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.maternitydataelem"),
							startingDate, endingDate);

		if (patientsListInfantFollowup != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListInfantFollowup,
							"HIV Exposed Infant Follow-up",
							ContextProvider
									.getMessage("tracnetreporting.category.hivexposedinfantfollowup"),
							startingDate, endingDate);

		if (patientsListFamilyPlanning != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListFamilyPlanning,
							"Family Planning Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.familyplandataelem"),
							startingDate, endingDate);

		if (patientsListSubmitVct != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListSubmitVct,
							"Submit VCT Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.subminvctdataelem"),
							startingDate, endingDate);

		if (patientsListProviderTestPit != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListProviderTestPit,
							"Provider-Initiated Testing (PIT) Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pitdataelem"),
							startingDate, endingDate);

		if (patientsListPep != null)
			service
					.exportPatientsListToExcelFile(
							request,
							response,
							patientsListPep,
							"PEP Data Elements",
							ContextProvider
									.getMessage("tracnetreporting.category.pepdataelem"),
							startingDate, endingDate);

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
	private void exportPatientsListsToCsvFile(HttpServletRequest request,
			HttpServletResponse response, String startingDate, String endingDate)
			throws IOException {

	}

	/**
	 * Populates PRE-ART Data Elements patients list
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 * @throws ParseException
	 */
	private ArrayList<Person> patientsPreArtDataElements(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("1.A-01")) {
			patientsList
					.addAll(service.newPedsUnderEighteenMonthsInHivCareList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newPedsUnderEighteenMonthsInHivCare"));
		} else if (indicatorValue.equals("1.A-02")) {
			patientsList.addAll(service.newPedsUnderFiveInHivCareList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newPedsUnderFiveInHivCare"));
		}

		else if (indicatorValue.equals("1.A-03")) {
			patientsList.addAll(service.PedsUnderFifteenEnrolledInHivList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PedsUnderFifteenEnrolledInHiv"));
		}

		else if (indicatorValue.equals("1.A-04")) {
			patientsList.addAll(service.newFemaleUnderFifteenInHivCareList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newFemaleUnderFifteenInHivCare"));
		} else if (indicatorValue.equals("1.A-05")) {
			patientsList.addAll(service.newMaleUnderFifteenInHivCareList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newMaleUnderFifteenInHivCare"));
		}

		else if (indicatorValue.equals("1.A-06")) {
			patientsList.addAll(service.AdultMoreThanFifteenEnrolledInHivList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.AdultMoreThanFifteenEnrolledInHiv"));
		} else if (indicatorValue.equals("1.A-07")) {
			patientsList.addAll(service.newFemaleMoreThanFifteenInHivCareList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newFemaleMoreThanFifteenInHivCare"));
		} else if (indicatorValue.equals("1.A-08")) {
			patientsList.addAll(service.newMaleMoreThanFifteenInHivCareList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newMaleMoreThanFifteenInHivCare"));
		}
		// else if (indicatorValue
		// .equals("A-09")) {
		// patientsList.addAll(service.pedUnderEighteenMonthsEverInHivList(
		// startDate, endDate));
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.pedUnderEighteenMonthsEverInHiv"));
		// }
		// else if (indicatorValue
		// .equals("A-10")) {
		// patientsList.addAll(service.pedsUnderFiveEverInHivList(startDate,
		// endDate));
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.pedsUnderFiveEverInHiv"));
		// }
		// else if (indicatorValue
		// .equals("A-11")) {
		// patientsList.addAll(service.femalePedsUnderFifteenEverInHivList(
		// startDate, endDate));
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.femalePedsUnderFifteenEverInHiv"));
		// }
		// else if (indicatorValue
		// .equals("A-12")) {
		// patientsList.addAll(service.malePedsUnderFifteenEverInHivList(
		// startDate, endDate));
		//			
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.malePedsUnderFifteenEverInHiv"));
		// }
		// else if (indicatorValue
		// .equals("A-13")) {
		// patientsList.addAll(service.femaleMoreThanFifteenEverInHivList(
		// startDate, endDate));
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.femaleMoreThanFifteenEverInHiv"));
		// }
		// else if (indicatorValue
		// .equals("A-14")) {
		// patientsList.addAll(service.maleMoreThanFifteenEverInHivList(
		// startDate, endDate));
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.preart.maleMoreThanFifteenEverInHiv"));
		// }
		else if (indicatorValue.equals("1.A-09")) {
			patientsList.addAll(service.patientsOnCotrimoProphylaxisList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.patientsOnCotrimoProphylaxis"));
		} else if (indicatorValue.equals("1.A-10")) {
			patientsList.addAll(service.patientsActiveTbAtEnrolThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.patientsActiveTbAtEnrolThisMonth"));
		}

		else if (indicatorValue.equals("1.A-11")) {
			patientsList.addAll(service.patientsTbPositiveAtEnrolThisMonthList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.patientsTbPositiveAtEnrolThisMonth"));
		} else if (indicatorValue.equals("1.A-12")) {
			patientsList.addAll(service
					.newEnrolledPedsStartTbTreatThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newEnrolledPedsStartTbTreatThisMonth"));
		} else if (indicatorValue.equals("1.A-13")) {
			patientsList.addAll(service
					.newEnrolledAdultsStartTbTreatThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.newEnrolledAdultsStartTbTreatThisMonth"));
		}

		else if (indicatorValue.equals("1.A-14")) {
			patientsList.addAll(service.PatientsInPreARVDiedThisMonthList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVDiedThisMonth"));
		}

		else if (indicatorValue.equals("1.A-15")) {
			patientsList.addAll(service
					.PatientsInPreARVTransferredInThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTransferredInThisMonth"));
		}

		else if (indicatorValue.equals("1.A-16")) {
			patientsList.addAll(service
					.PatientsInPreARVTransferredOutThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTransferredOutThisMonth"));
		}

		else if (indicatorValue.equals("1.A-17")) {
			patientsList.addAll(service
					.PatientsInPreARVTLostToFollowUpThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTLostToFollowUpThisMonth"));
		}

		else if (indicatorValue.equals("1.A-18")) {
			patientsList.addAll(service
					.PatientsInPreARVTLostToFollowUpNotLostThisMonthList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.PatientsInPreARVTLostToFollowUpNotLostThisMonth"));
		}

		else if (indicatorValue.equals("1.A-19")) {
			patientsList.addAll(service
					.patientsOnCotrimoProphylaxisLessThan15YearsList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.patientsPedsCurrentlyOnCotrimoProphylaxis"));
		}

		else if (indicatorValue.equals("1.A-20")) {
			patientsList.addAll(service
					.patientsOnCotrimoProphylaxisGreatherThan15YearsList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.preart.patientsAdultsCurrentlyOnCotrimoProphylaxis"));
		}

		return patientsList;
	}

	/**
	 * Populates ART Data Elements patients list
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 * @throws ParseException
	 */
	private ArrayList<Person> patientsArtDataElement(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("1.B-01")) {

			patientsList
					.addAll(service.pedsUnderEighteenMonthsCurrentOnArvList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.pedsUnderEighteenMonthsCurrentOnArv"));

		} else if (indicatorValue.equals("1.B-02")) {
			patientsList.addAll(service.pedsUnderFiveCurrentOnArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.pedsUnderFiveCurrentOnArv"));

		} else if (indicatorValue.equals("1.B-03")) {
			patientsList.addAll(service.femalePedsUnderFifteenCurrentOnArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.femalePedsUnderFifteenCurrentOnArv"));
		} else if (indicatorValue.equals("1.B-04")) {

			patientsList.addAll(service.malePedsUnderFifteenCurrentOnArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.malePedsUnderFifteenCurrentOnArv"));
		} else if (indicatorValue.equals("1.B-05")) {
			patientsList.addAll(service.pedsOnFirstLineRegList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.pedsOnFirstLineReg"));

		} else if (indicatorValue.equals("1.B-06")) {

			patientsList.addAll(service.pedsOnSecondLineRegList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.pedsOnSecondLineReg"));

		} else if (indicatorValue.equals("1.B-07")) {
			patientsList.addAll(service.femaleMoreThanFifteenCurrentOnArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.femaleMoreThanFifteenCurrentOnArv"));
		} else if (indicatorValue.equals("1.B-08")) {

			patientsList.addAll(service.maleMoreThanFifteenCurrentOnArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.maleMoreThanFifteenCurrentOnArv"));

		} else if (indicatorValue.equals("1.B-09")) {
			patientsList.addAll(service.adultOnFirstLineRegList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.adultOnFirstLineReg"));

		} else if (indicatorValue.equals("1.B-10")) {
			patientsList.addAll(service.adultOnSecondLineRegList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.adultOnSecondLineReg"));

		} else if (indicatorValue.equals("1.B-11")) {

			patientsList.addAll(service
					.newPedsUnderEighteenMonthStartArvThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsUnderEighteenMonthStartArvThisMonth"));

		} else if (indicatorValue.equals("1.B-12")) {

			patientsList.addAll(service.newPedsUnderFiveStartArvThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsUnderFiveStartArvThisMonth"));

		} else if (indicatorValue.equals("1.B-13")) {
			patientsList.addAll(service
					.newFemalePedsUnderFifteenStartArvThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newFemalePedsUnderFifteenStartArvThisMonth"));

		}
		/*
		 * else if (indicatorValue.equals("1.B-14")) {
		 * patientsList.addAll(service
		 * .newMalePedsUnderFifteenStartArvThisMonthList(startDate, endDate));
		 * mav .addObject( "listTitle", ContextProvider.getMessage(
		 * "tracnetreporting.indicator.art.newMalePedsUnderFifteenStartArvThisMonth"
		 * ));
		 * 
		 * }
		 */
		else if (indicatorValue.equals("1.B-14")) {

			patientsList.addAll(service.newPedsWhoStageFourThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsWhoStageFourThisMonth"));
		} else if (indicatorValue.equals("1.B-15")) {

			patientsList.addAll(service.newPedsWhoStageThreeThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsWhoStageThreeThisMonth"));

		} else if (indicatorValue.equals("1.B-16")) {

			patientsList.addAll(service.newPedsWhoStageTwoThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsWhoStageTwoThisMonth"));

		} else if (indicatorValue.equals("1.B-17")) {

			patientsList.addAll(service.newPedsWhoStageOneThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsWhoStageOneThisMonth"));

		} else if (indicatorValue.equals("1.B-18")) {
			patientsList.addAll(service.newPedsUndefinedWhoStageThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newPedsUndefinedWhoStageThisMonth"));

		} else if (indicatorValue.equals("1.B-19")) {

			patientsList.addAll(service.newFemaleAdultStartiArvThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newFemaleAdultStartiArvThisMonth"));

		} else if (indicatorValue.equals("1.B-20")) {

			patientsList.addAll(service.newMaleAdultStartiArvThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newMaleAdultStartiArvThisMonth"));

		} else if (indicatorValue.equals("1.B-21")) {

			patientsList.addAll(service.newAdultWhoStageFourThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newAdultWhoStageFourThisMonth"));

		} else if (indicatorValue.equals("1.B-22")) {

			patientsList.addAll(service.newAdultWhoStageThreeThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newAdultWhoStageThreeThisMonth"));

		} else if (indicatorValue.equals("1.B-23")) {

			patientsList.addAll(service.newAdultWhoStageTwoThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newAdultWhoStageTwoThisMonth"));

		} else if (indicatorValue.equals("1.B-24")) {

			patientsList.addAll(service.newAdultWhoStageOneThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newAdultWhoStageOneThisMonth"));

		} else if (indicatorValue.equals("1.B-25")) {

			patientsList.addAll(service.newAdultUndefinedWhoStageThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.newAdultUndefinedWhoStageThisMonth"));

		} else if (indicatorValue.equals("1.B-26")) {

			patientsList.addAll(service
					.arvPedsFifteenInterruptTreatThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvPedsFifteenInterruptTreatThisMonth"));

		} else if (indicatorValue.equals("1.B-27")) {

			patientsList.addAll(service
					.arvAdultFifteenInterruptTreatThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvAdultFifteenInterruptTreatThisMonth"));

		} else if (indicatorValue.equals("1.B-28")) {

			patientsList.addAll(service.arvPedsDiedThisMonthList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvPedsDiedThisMonth"));

		} else if (indicatorValue.equals("1.B-29")) {

			patientsList.addAll(service.arvAdultDiedThisMonthList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvAdultDiedThisMonth"));

		} else if (indicatorValue.equals("1.B-30")) {

			patientsList.addAll(service.arvPedsLostFollowupMoreThreeMonthsList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvPedsLostFollowupMoreThreeMonths"));

		} else if (indicatorValue.equals("1.B-31")) {

			patientsList
					.addAll(service.arvAdultLostFollowupMoreThreeMonthsList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvAdultLostFollowupMoreThreeMonths"));

		} else if (indicatorValue.equals("1.B-32")) {

			patientsList.addAll(service.maleOnTreatTwelveAfterInitArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.maleOnTreatTwelveAfterInitArv"));

		} else if (indicatorValue.equals("1.B-33")) {

			patientsList.addAll(service.femaleOnTreatTwelveAfterInitArvList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.femaleOnTreatTwelveAfterInitArv"));

		} else if (indicatorValue.equals("1.B-34")) {

			patientsList.addAll(service.arvPedsTransferredOutThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvPedsTransferredOutThisMonth"));

		} else if (indicatorValue.equals("1.B-35")) {

			patientsList.addAll(service.arvAdultTransferredOutThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvAdultTransferredOutThisMonth"));

		} else if (indicatorValue.equals("1.B-36")) {

			patientsList.addAll(service.arvPedsTransferredInThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvPedsTransferredInThisMonth"));

		} else if (indicatorValue.equals("1.B-37")) {

			patientsList.addAll(service.arvAdultTransferreInThisMonthList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.arvAdultTransferreInThisMonth"));
		}

		else if (indicatorValue.equals("1.B-38")) {

			patientsList.addAll(service
					.PatientsInARVTLostToFollowUpNotLostThisMonthList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.PatientsInARVTLostToFollowUpNotLostThisMonth"));
		}

		else if (indicatorValue.equals("1.B-39")) {

			patientsList.addAll(service
					.patientsAdultNewOnSecondLineThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.patientsPediatricNewOnSecondLineThisMonth"));
		}

		else if (indicatorValue.equals("1.B-40")) {

			patientsList.addAll(service
					.patientsAdultNewOnSecondLineThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.art.patientsAdultNewOnSecondLineThisMonth"));
		}

		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsStiOpportAndOthers(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("1.C-01")) {

			patientsList.addAll(service.clientsCounceledForStiThisMonthList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.clientsCounceledForStiThisMonth"));

		} else if (indicatorValue.equals("1.C-02")) {
			patientsList.addAll(service.stiDiagnosedThisMonthList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.stiDiagnosedThisMonth"));
		} else if (indicatorValue.equals("1.C-03")) {

			patientsList.addAll(service
					.opportInfectTreatedExcludeTbThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.opportInfectTreatedExcludeTbThisMonth"));
		}

		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsNutritionDataElem(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("1.D-01")) {

			patientsList
					.addAll(service.pedsUnderFiveSevereMalnutrThisMonthList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pedsUnderFiveSevereMalnutrThisMonth"));

		} else if (indicatorValue.equals("1.D-02")) {
			patientsList.addAll(service
					.pedsUnderFiveSevereMalnutrTheurapThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pedsUnderFiveSevereMalnutrTheurapThisMonth"));
		} else if (indicatorValue.equals("1.D-03")) {

			patientsList.addAll(service
					.pedsUnderFifteenSevMalnutrTherapThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pedsUnderFifteenSevMalnutrTherapThisMonth"));
		}

		else if (indicatorValue.equals("1.D-04")) {

			patientsList.addAll(service.adultSevereMalnutrTherapThisMonthList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.adultSevereMalnutrTherapThisMonth"));
		}

		else if (indicatorValue.equals("1.D-05")) {

			patientsList
					.addAll(service
							.numberOfPatientsWhoReceivedFollowUpAndAdherenceCounsellingList(
									startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling"));
		}

		else if (indicatorValue.equals("1.D-06")) {

			patientsList.addAll(service
					.numberOfPatientsWhoReceivedFamilyPlanningThisMonthList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.numberOfPatientsWhoReceivedFamilyPlanningThisMonth"));

		}
		// else if (indicatorValue
		// .equals("1.D-05"))
		// {
		// patientsList
		// .addAll(service.pregnantMalnutrTherapThisMonthList(
		// startDate, endDate));
		// mav.addObject("listTitle",ContextProvider
		// .getMessage("tracnetreporting.indicator.pregnantMalnutrTherapThisMonth"));
		// }
		// else if (indicatorValue
		// .equals("1.D-06"))
		// {
		//			
		// patientsList
		// .addAll(service.lactatingMalnutrTherapThisMonthList(
		// startDate, endDate));
		// mav.addObject("listTitle",ContextProvider
		// .getMessage("tracnetreporting.indicator.lactatingMalnutrTherapThisMonth"));
		// }
		//		   
		// if (indicatorValue
		// .equals("1.D-07")) {
		//			
		// patientsList
		// .addAll(service.pedsUnderFiveWithSevMalnutrThisMonthList(
		// startDate, endDate));
		//
		// mav.addObject("listTitle", ContextProvider
		// .getMessage("tracnetreporting.indicator.pedsUnderFiveWithSevMalnutrThisMonth"));
		//			
		// } else if (indicatorValue
		// .equals("1.D-08"))
		// {
		// patientsList
		// .addAll(service.pedsTherapThisMonthList(
		// startDate, endDate));
		// mav.addObject("listTitle",ContextProvider
		// .getMessage("tracnetreporting.indicator.pedsTherapThisMonth"));
		// }
		// else if (indicatorValue
		// .equals("1.D-09"))
		// {
		//			
		// patientsList
		// .addAll(service.adultTherapThisMonthList(
		// startDate, endDate));
		// mav.addObject("listTitle",ContextProvider
		// .getMessage("tracnetreporting.indicator.adultTherapThisMonth"));
		// }

		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 * @throws ParseException
	 */
	private ArrayList<Person> patientsAntenatalDataElem(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("2.A-01")) {

			patientsList.addAll(service.womenUnknownHivFirstAntenatalList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenUnknownHivFirstAntenatal"));

		} else if (indicatorValue.equals("2.A-02")) {
			patientsList.addAll(service.womenKnownHivPosFirstAntenatalList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenKnownHivPosFirstAntenatal"));
		} else if (indicatorValue.equals("2.A-03")) {

			patientsList.addAll(service.womenUnknownHivTestedList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenUnknownHivTested"));
		}

		if (indicatorValue.equals("2.A-04")) {

			patientsList.addAll(service.womenHivPosReturnResList(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosReturnRes"));

		} else if (indicatorValue.equals("2.A-05")) {
			patientsList.addAll(service.womenHivPosTestedCd4List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosTestedCd4"));
		} else if (indicatorValue.equals("2.A-06")) {

			patientsList.addAll(service.pregnantHivPosEligibleArvs1List(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantHivPosEligibleArvs1"));
		}

		if (indicatorValue.equals("2.A-07")) {

			patientsList.addAll(service.negativeWomenReturnResList(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.negativeWomenReturnRes"));

		} 
		
		else if (indicatorValue.equals("2.A-08")) {
			patientsList.addAll(service.pregnantHivPosList(startDate, endDate));
			mav.addObject("listTitle", ContextProvider
					.getMessage("tracnetreporting.indicator.pregnantHivPos"));
			
			
		} 
		
		/*else if (indicatorValue.equals("2.A-09")) {

			patientsList.addAll(service.pregnantHivPosAztProphyAt28WeeksList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantHivPosAztProphyAt28Weeks"));}*/

		 else if (indicatorValue.equals("2.A-09")) {
			patientsList.addAll(service.pregnantHivPosTripleTheraProphyList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantHivPosTripleTheraProphy"));
		} 
		
		 else if (indicatorValue.equals("2.A-10")) {
				patientsList.addAll(service.numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxisList(
						startDate, endDate));
				mav
						.addObject(
								"listTitle",
								ContextProvider
										.getMessage("tracnetreporting.indicator.numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis"));
		} 
		 
		 else if (indicatorValue.equals("2.A-11")) {

			patientsList.addAll(service.pregnantHivPosEligibleArvs2List(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantHivPosEligibleArvs2"));
		}

		if (indicatorValue.equals("2.A-12")) {

			patientsList.addAll(service.womenTestedForRprList(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenTestedForRpr"));

		} else if (indicatorValue.equals("2.A-13")) {
			patientsList.addAll(service.pregnantTestedPosForRprList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantTestedPosForRpr"));
		} else if (indicatorValue.equals("2.B-14")) {

			patientsList.addAll(service.pregnantPartnersTestedForHivList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantPartnersTestedForHiv"));
		}

		if (indicatorValue.equals("2.A-15")) {

			patientsList.addAll(service.hivNegPregnantPartnersTestedHivPosList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.hivNegPregnantPartnersTestedHivPos"));

		} else if (indicatorValue.equals("2.A-16")) {
			patientsList.addAll(service.discordantCouples1List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.discordantCouples1"));
		} else if (indicatorValue.equals("2.A-17")) {

			patientsList.addAll(service.partnersTestedHivPosList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.partnersTestedHivPos"));
		}

		else if (indicatorValue.equals("2.A-18")) {

			patientsList.addAll(service
					.pregnantHivPosStartedCotrimoxazoleThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantHivPosStartedCotrimoxazoleThisMonth"));
		}

		else if (indicatorValue.equals("2.A-19")) {

			patientsList
					.addAll(service
							.NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonthList(
									startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth"));
		}

		else if (indicatorValue.equals("2.A-20")) {

			patientsList
					.addAll(service
							.NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonthList(
									startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth"));
		}

		return patientsList;

	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 * @throws ParseException
	 */
	private ArrayList<Person> patientsMaternityDataElem(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("2.B-01")) {

			patientsList
					.addAll(service.expectedDeliveriesFacilityThisMonthList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.expectedDeliveriesFacilityThisMonth"));

		} else if (indicatorValue.equals("2.B-02")) {
			patientsList
					.addAll(service.occuringDeliveriesFacilityThisMonthList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.occuringDeliveriesFacilityThisMonth"));
		} else if (indicatorValue.equals("2.B-03")) {

			patientsList.addAll(service.expectedDeliveriesAmongHivPosWomenList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.expectedDeliveriesAmongHivPosWomen"));
		}

		if (indicatorValue.equals("2.B-04")) {

			patientsList.addAll(service.womenHivPosGivingBirthAtFacilityList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosGivingBirthAtFacility"));

		} else if (indicatorValue.equals("2.B-05")) {
			patientsList.addAll(service.reportedHivPosGivingBirthAtHomeList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.reportedHivPosGivingBirthAtHome"));
		} else if (indicatorValue.equals("2.B-06")) {

			patientsList.addAll(service.womenHivPosAzt3tcNvpDuringLaborList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.lactatingMalnutrTherapThisMonth"));
		}

		if (indicatorValue.equals("2.B-07")) {

			patientsList.addAll(service.womenReceivingAzt3tcAfterDeliveryList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenReceivingAzt3tcAfterDelivery"));

		} else if (indicatorValue.equals("2.B-08")) {
			patientsList.addAll(service
					.womenUnknownHivStatusTestedDuringLabor1List(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenUnknownHivStatusTestedDuringLabor1"));
		} 
		
		else if (indicatorValue.equals("2.B-09")) {

			patientsList.addAll(service
					.womenUnknownHivStatusTestedPosDuringLabor2List(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenUnknownHivStatusTestedPosDuringLabor2"));

		} 
		
		/*else if (indicatorValue.equals("2.B-10")) {
			patientsList.addAll(service
					.pregnantReceivedCompleteCourseThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.pregnantReceivedCompleteCourseThisMonth"));
		}*/
		
		else if (indicatorValue.equals("2.B-10")) {

			patientsList.addAll(service
					.NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxisList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis"));

		} 
		
		else if (indicatorValue.equals("2.B-11")) {

			patientsList.addAll(service
					.NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth"));

		} 

		return patientsList;

	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsHivExposedInfantFollowup(
			String indicatorValue, String startDate, String endDate,
			ModelAndView mav) throws ParseException {
		ArrayList<Person> patientsList = new ArrayList<Person>();

		if (indicatorValue.equals("2.C-01")) {

			patientsList.addAll(service.womenHivPosBreastFeedingList(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosBreastFeeding"));

		} else if (indicatorValue.equals("2.C-02")) {
			patientsList.addAll(service.womenHivPosUsingFormulaList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosUsingFormula"));
		} else if (indicatorValue.equals("2.C-03")) {

			patientsList.addAll(service.infantHivPosMothersEnrolledPmtctList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersEnrolledPmtct"));
		}

		if (indicatorValue.equals("2.C-04")) {

			patientsList.addAll(service.infantHivPosMothersTestedAt6WeeksList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt6Weeks"));

		} else if (indicatorValue.equals("2.C-05")) {
			patientsList.addAll(service
					.infantHivPosMothersTestedPosAt6WeeksList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt6Weeks"));
		} else if (indicatorValue.equals("2.C-06")) {

			patientsList.addAll(service
					.infantHivPosMothersAged6WeeksThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersAged6WeeksThisMonth"));
		}

		if (indicatorValue.equals("2.C-07")) {

			patientsList.addAll(service.infantHivPosMothersTestedAt9MonthsList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt9Months"));

		} else if (indicatorValue.equals("2.C-08")) {
			patientsList.addAll(service
					.infantHivPosMothersTestedPosAt9MonthsList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt9Months"));
		} else if (indicatorValue.equals("2.C-09")) {

			patientsList.addAll(service
					.infantHivPosMothersAged9MonthsThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersAged9MonthsThisMonth"));

		} else if (indicatorValue.equals("2.C-10")) {
			patientsList
					.addAll(service.infantHivPosMothersTestedAt18MonthsList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedAt18Months"));
		}
		if (indicatorValue.equals("2.C-11")) {

			patientsList.addAll(service
					.infantHivPosMothersTestedPosAt18MonthsList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTestedPosAt18Months"));

		} else if (indicatorValue.equals("2.C-12")) {
			patientsList.addAll(service
					.infantHivPosMothersAgedAt18MonthsThisMonthList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersAgedAt18MonthsThisMonth"));
		} else if (indicatorValue.equals("2.C-13")) {

			patientsList.addAll(service.infantHivPosMothersLostFollowupList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersLostFollowup"));
		}

		if (indicatorValue.equals("2.C-14")) {

			patientsList.addAll(service
					.infantHivPosMothersScreenedTbThisMonthList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersScreenedTbThisMonth"));

		} else if (indicatorValue.equals("2.C-15")) {
			patientsList.addAll(service.reportedDeadInfantHivPosMothersList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.reportedDeadInfantHivPosMothers"));
		} else if (indicatorValue.equals("2.C-16")) {

			patientsList.addAll(service.infantHivPosMothersMalnourishedList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersMalnourished"));
		}

		if (indicatorValue.equals("2.C-17")) {

			patientsList.addAll(service.infantHivPosMothersTherapFoodList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersTherapFood"));

		} else if (indicatorValue.equals("2.C-18")) {
			patientsList.addAll(service.infantHivPosMothersCotrimoAt6WeeksList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivPosMothersCotrimoAt6Weeks"));
		}
		// else if (indicatorValue
		// .equals("2.C-19"))
		// {
		//			
		// patientsList
		// .addAll(service.newInfantHivPosMothersNvpAztAtBirthList(
		// startDate, endDate));
		// mav.addObject("listTitle",ContextProvider
		// .getMessage("tracnetreporting.indicator.newInfantHivPosMothersNvpAztAtBirth"));
		//		
		// }

		else if (indicatorValue.equals("2.C-19")) {

			patientsList.addAll(service
					.infantHivNegMothersInDiscordantCouplesEnrolledPmtctList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.infantHivNegMothersInDiscordantCouplesEnrolledPmtct"));

		}

		return patientsList;

	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsFamilyPlanningDataElem(
			String indicatorValue, String startDate, String endDate,
			ModelAndView mav) throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();
		if (indicatorValue.equals("2.D-01")) {

			patientsList.addAll(service.womenHivPosExpectedFpAtFacilityList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosExpectedFpAtFacility"));

		} else if (indicatorValue.equals("2.D-02")) {
			patientsList.addAll(service.womenHivPosSeenInFpList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosSeenInFp"));
		} else if (indicatorValue.equals("2.D-03")) {

			patientsList.addAll(service.womenHivPosPartnersSeenInFpList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosPartnersSeenInFp"));
		}

		if (indicatorValue.equals("2.D-04")) {

			patientsList.addAll(service
					.womenHivPosReceivingModernContraceptiveList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosReceivingModernContraceptive"));

		} else if (indicatorValue.equals("2.D-05")) {
			patientsList.addAll(service.womenHivPosRefferedForFpList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.womenHivPosRefferedForFp"));
		}

		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsSubmitVctDataElem(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();
		if (indicatorValue.equals("2.E-01")) {

			patientsList.addAll(service
					.newFemaleUnderFifteenCounseledTestedList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemaleUnderFifteenCounseledTested"));

		} else if (indicatorValue.equals("2.E-02")) {
			patientsList.addAll(service.newMaleUnderFifteenCounseledTestedList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMaleUnderFifteenCounseledTested"));
		} else if (indicatorValue.equals("2.E-03")) {

			patientsList
					.addAll(service.newFemaleFifteenTo24CounseledTestedList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemaleFifteenTo24CounseledTested"));
		}

		if (indicatorValue.equals("2.E-04")) {

			patientsList.addAll(service.newMaleFifteenTo24CounseledTestedList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMaleFifteenTo24CounseledTested"));

		} else if (indicatorValue.equals("2.E-05")) {
			patientsList.addAll(service.newFemaleMore25CounseledTestedList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemaleMore25CounseledTested"));

		} else if (indicatorValue.equals("2.E-06")) {

			patientsList.addAll(service.newMaleMore25CounseledTestedList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMaleMore25CounseledTested"));
		}

		if (indicatorValue.equals("2.E-07")) {

			patientsList.addAll(service.couplesCounseledTestedList(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.couplesCounseledTested"));

		} else if (indicatorValue.equals("2.E-08")) {
			patientsList.addAll(service.discordantCouples2List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.discordantCouples2"));
		} else if (indicatorValue.equals("2.E-09")) {

			patientsList
					.addAll(service.newFemaleUnderFifteenTestReceiveResList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemaleUnderFifteenTestReceiveRes"));

		} else if (indicatorValue.equals("2.E-10")) {
			patientsList.addAll(service.newMaleUnderFifteenTestReceiveResList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMaleUnderFifteenTestReceiveRes"));
		}
		if (indicatorValue.equals("2.E-11")) {

			patientsList.addAll(service.newFemale15To24TestReceiveResList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemale15To24TestReceiveRes"));

		} else if (indicatorValue.equals("2.E-12")) {
			patientsList.addAll(service.newMale15To24TestReceiveResList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMale15To24TestReceiveRes"));
		} else if (indicatorValue.equals("2.E-13")) {

			patientsList.addAll(service.newFemaleMore25TestReceiveResList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newFemaleMore25TestReceiveRes"));
		}

		if (indicatorValue.equals("2.E-14")) {

			patientsList.addAll(service.newMaleMore25TestReceiveResList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newMaleMore25TestReceiveRes"));

		} else if (indicatorValue.equals("2.E-15")) {
			patientsList.addAll(service.femaleHivPosUnderFifteenList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleHivPosUnderFifteen"));
		} else if (indicatorValue.equals("2.E-16")) {

			patientsList.addAll(service.maleHivPosUnderFifteenList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleHivPosUnderFifteen"));
		}

		if (indicatorValue.equals("2.E-17")) {

			patientsList.addAll(service.femaleHivPosUnder15to24List(startDate,
					endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleHivPosUnder15to24"));

		} else if (indicatorValue.equals("2.E-18")) {
			patientsList.addAll(service.maleHivPosUnder15to24List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleHivPosUnder15to24"));
		} else if (indicatorValue.equals("2.E-19")) {

			patientsList.addAll(service.femaleHivPosMoreThan25List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleHivPosMoreThan25"));

		} else if (indicatorValue.equals("2.E-20")) {
			patientsList.addAll(service.maleHivPosMoreThan25List(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleHivPosMoreThan25"));
		}
		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsProviderTestPitDataElem(
			String indicatorValue, String startDate, String endDate,
			ModelAndView mav) throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();
		if (indicatorValue.equals("2.F-01")) {

			patientsList.addAll(service
					.femaleUnderFifteenCounseledThroughPitList(startDate,
							endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleUnderFifteenCounseledThroughPit"));

		} else if (indicatorValue.equals("2.F-02")) {
			patientsList
					.addAll(service.maleUnderFifteenCounseledThroughPitList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleUnderFifteenCounseledThroughPit"));
		} else if (indicatorValue.equals("2.F-03")) {

			patientsList.addAll(service.female15To24CounseledThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.female15To24CounseledThroughPit"));
		}

		if (indicatorValue.equals("2.F-04")) {

			patientsList.addAll(service.male15To24CounseledThroughPitList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.male15To24CounseledThroughPit"));

		} else if (indicatorValue.equals("2.F-05")) {
			patientsList
					.addAll(service.femaleMoreThan25CounseledThroughPitList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleMoreThan25CounseledThroughPit"));

		} else if (indicatorValue.equals("2.F-06")) {

			patientsList.addAll(service.maleMoreThan25CounseledThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleMoreThan25CounseledThroughPit"));
		}

		if (indicatorValue.equals("2.F-07")) {

			patientsList.addAll(service.femaleUnderFifteenHivResThroughPitList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleUnderFifteenHivResThroughPit"));

		} else if (indicatorValue.equals("2.F-08")) {
			patientsList.addAll(service.maleUnderFifteenHivResThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleUnderFifteenHivResThroughPit"));
		} else if (indicatorValue.equals("2.F-09")) {

			patientsList.addAll(service.female15To24HivResThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.female15To24HivResThroughPit"));

		} else if (indicatorValue.equals("2.F-10")) {
			patientsList.addAll(service.male15To24HivResThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.male15To24HivResThroughPit"));
		}
		if (indicatorValue.equals("2.F-11")) {

			patientsList.addAll(service.femaleMoreThan25HivResThroughPitList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleMoreThan25HivResThroughPit"));

		} else if (indicatorValue.equals("2.F-12")) {
			patientsList.addAll(service.maleMoreThan25HivResThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleMoreThan25HivResThroughPit"));
		} else if (indicatorValue.equals("2.F-13")) {

			patientsList.addAll(service.femaleUnderFifteenHivPosThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleUnderFifteenHivPosThroughPit"));
		}

		if (indicatorValue.equals("2.F-14")) {

			patientsList.addAll(service.maleUnderFifteenHivPosThroughPitList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleUnderFifteenHivPosThroughPit"));

		} else if (indicatorValue.equals("2.F-15")) {
			patientsList.addAll(service.female15To24HivPosThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.female15To24HivPosThroughPit"));
		} else if (indicatorValue.equals("2.F-16")) {

			patientsList.addAll(service.male15To24HivPosThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.male15To24HivPosThroughPit"));
		}

		if (indicatorValue.equals("2.F-17")) {

			patientsList.addAll(service.femaleMoreThan25HivPosThroughPitList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.femaleMoreThan25HivPosThroughPit"));

		} else if (indicatorValue.equals("2.F-18")) {
			patientsList.addAll(service.maleMoreThan25HivPosThroughPitList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.maleMoreThan25HivPosThroughPit"));
		}

		return patientsList;
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param indicatorValue
	 * @param startDate
	 * @param endDate
	 */
	private ArrayList<Person> patientsPepDataElem(String indicatorValue,
			String startDate, String endDate, ModelAndView mav)
			throws ParseException {

		ArrayList<Person> patientsList = new ArrayList<Person>();
		if (indicatorValue.equals("2.G-01")) {

			patientsList.addAll(service.newAtRiskHivOccupationExposureList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOccupationExposure"));

		} else if (indicatorValue.equals("2.G-02")) {
			patientsList.addAll(service.newAtRiskHivRapeAssaultList(startDate,
					endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssault"));
		} else if (indicatorValue.equals("2.G-03")) {

			patientsList.addAll(service
					.newAtRiskHivOtherNoneOccupationExposureList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupationExposure"));
		}

		if (indicatorValue.equals("2.G-04")) {

			patientsList.addAll(service.newAtRiskHivOccupationExposurePepList(
					startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOccupationExposurePep"));

		} else if (indicatorValue.equals("2.G-05")) {
			patientsList.addAll(service.newAtRiskHivRapeAssaultPepList(
					startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssaultPep"));

		} else if (indicatorValue.equals("2.G-06")) {

			patientsList.addAll(service
					.newAtRiskHivOtherNoneOccupationExposurePepList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupationExposurePep"));
		}

		if (indicatorValue.equals("2.G-07")) {

			patientsList
					.addAll(service.newAtRiskHivOccupExpo3MonthAfterPepList(
							startDate, endDate));

			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOccupExpo3MonthAfterPep"));

		} else if (indicatorValue.equals("2.G-08")) {
			patientsList.addAll(service
					.newAtRiskHivRapeAssault3MonthAfterPepList(startDate,
							endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivRapeAssault3MonthAfterPep"));
		} else if (indicatorValue.equals("2.G-09")) {

			patientsList.addAll(service
					.newAtRiskHivOtherNoneOccupExpo3MonthAfterPepList(
							startDate, endDate));
			mav
					.addObject(
							"listTitle",
							ContextProvider
									.getMessage("tracnetreporting.indicator.newAtRiskHivOtherNoneOccupExpo3MonthAfterPep"));

		}

		return patientsList;
	}
}
