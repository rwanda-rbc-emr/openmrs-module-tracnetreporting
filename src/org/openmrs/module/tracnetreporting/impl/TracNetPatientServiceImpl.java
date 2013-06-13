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
package org.openmrs.module.tracnetreporting.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.tracnetreporting.GlobalProperties;
import org.openmrs.module.tracnetreporting.service.ConstantValues;
import org.openmrs.module.tracnetreporting.service.TracNetPatientService;

/**
 *
 */
public class TracNetPatientServiceImpl implements TracNetPatientService {

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory2;

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory2(SessionFactory sessionFactory2) {
		// TracNetIndicatorService service =
		// Context.getService(TracNetIndicatorService.class);
		this.sessionFactory2 = sessionFactory2;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory2() {
		return sessionFactory2;
	}

	public String addDaysToDate(String date, int days) throws ParseException {
		// Initialize with date if it was specified
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		try {
			if (date != null)
				cal.setTime(df.parse(date));

			cal.add(Calendar.DAY_OF_WEEK, days);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return df.format(cal.getTime());
	}

	public String addDaysToDateUsingDays(String date, int days)
			throws ParseException {
		// Initialize with date if it was specified
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		try {
			if (date != null)
				cal.setTime(df.parse(date));

			cal.add(Calendar.DAY_OF_MONTH, days);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return df.format(cal.getTime());
	}

	// ---------------------------------A. PRE-ART Data
	// Elements----------------------------------------------

	/**
	 * Number of patients on Cotrimoxazole Prophylaxis this month
	 * 
	 * @throws ParseException
	 * 
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsOnCotrimoProphylaxis(java.util.Date,
	 *      java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<Person> patientsOnCotrimoProphylaxis(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							//+ "inner join person pe on pg.patient_id = pe.person_id "
							//+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							/*+ "inner join drug_order do on ord.order_id = do.order_id "
							+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of new pediatric patients (age <18 months) enrolled in HIV
	 * care
	 * 
	 * @throws ParseException
	 * 
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthsInHivCare(java.util.Date,
	 *      java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsUnderEighteenMonthsInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			// SQLQuery query1 = session
			// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
			// + "inner join person pe on pg.patient_id = pe.person_id "
			// + "inner join patient pa on pg.patient_id = pa.patient_id "
			// + "inner join orders ord on pg.patient_id = ord.patient_id "
			// + "inner join drug_order do on ord.order_id = do.order_id "
			// + "inner join drug d on do.drug_inventory_id = d.drug_id "
			// + "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
			// + endDate
			// + "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 2 "
			// + " and d.concept_id IN ("
			// + GlobalProperties.gpGetListOfProphylaxisDrugs()
			// + ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
			// + "and pa.voided = 0 and pg.date_enrolled >= '"
			// + startDate
			// + "'  and pg.date_enrolled <= '"
			// + endDate
			// + "' and pg.date_completed is null and pg.program_id = "
			// + Integer.parseInt(GlobalProperties
			// .gpGetHIVProgramId()));
			//
			// List<Integer> patientIds1 = query1.list();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 2 "
							+ " and pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{
								SQLQuery queryExited = session
										.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetExitFromCareConceptId())
												+ " and (cast(o.obs_datetime as DATE)) < '"
												+ endDate
												+ "'"
												+ " and o.voided = 0 and o.person_id= "
												+ patientId);

								List<Integer> patientIds3 = queryExited.list();

								if ((patientIds3.size() == 0)) {

									SQLQuery queryTransferredIn = session
											.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTransferredInConceptId())
													+ " and o.voided = 0 and o.person_id= "
													+ patientId);

									List<Integer> patientIds4 = queryTransferredIn
											.list();

									if (patientIds4.size() == 0) {
										patientIdsList.add(patientId);

									}
								}
							}
						}
					}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of female adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleMoreThanFifteenEverInHiv(String startDate,
			String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pe.gender = 'F' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") "
							+ " and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "'  and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if ((patientIds2.size() == 0)) {

					patientIdsList.add(patientId);

				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of female pediatric patients (age <15 years) ever enrolled
	 * in HIV care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femalePedsUnderFifteenEverInHiv(String startDate,
			String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") "
							+ " and pe.gender = 'F' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					patientIdsList.add(patientId);

				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of male adult patients (age 15 or older) ever enrolled in
	 * HIV care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleMoreThanFifteenEverInHiv(String startDate,
			String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") "
							+ " and pe.gender = 'M' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe. voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					patientIdsList.add(patientId);

				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of male pediatric patients (age <15 years) ever enrolled in
	 * HIV care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> malePedsUnderFifteenEverInHiv(String startDate,
			String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") "
							+ " and pe.gender = 'M' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					patientIdsList.add(patientId);

				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of newly enrolled patients (age 15+ years) who started TB
	 * treatment this month?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledAdultsStartTbTreatThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newEnrolledAdultsStartTbTreatThisMonth(
			String startDate, String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfTBDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDateEnrolled = session
						.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
								+ patientId);
				List<Date> dateEnrolled = queryDateEnrolled.list();

				if (dateEnrolled.get(0) != null) {

					if ((dateEnrolled.get(0).getTime() >= newStartDate
							.getTime())
							&& (dateEnrolled.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryMinStartDate = session
								.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
										+ " inner join drug_order do on ord.order_id = do.order_id "
										/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
										+ " where ord.concept_id IN ("
										+ GlobalProperties.gpGetListOfTBDrugs()
										+ ") "
										+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
										+ patientId);

						List<Date> patientIdsMinStartDate = queryMinStartDate
								.list();

						if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
								.getTime())
								&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
										.getTime()) {

							SQLQuery queryExited = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetExitFromCareConceptId())
											+ " and (cast(o.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and o.voided = 0 and o.person_id= "
											+ patientId);

							List<Integer> patientIds3 = queryExited.list();

							if ((patientIds3.size() == 0)) {

								patientIdsList.add(patientId);

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of newly enrolled patients (age <15 years) who started TB
	 * treatment this month?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledPedsStartTbTreatThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newEnrolledPedsStartTbTreatThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfTBDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDateEnrolled = session
						.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
								+ patientId);
				List<Date> dateEnrolled = queryDateEnrolled.list();

				if (dateEnrolled.get(0) != null) {

					if ((dateEnrolled.get(0).getTime() >= newStartDate
							.getTime())
							&& (dateEnrolled.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryMinStartDate = session
								.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
										+ " inner join drug_order do on ord.order_id = do.order_id "
										/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
										+ " where ord.concept_id IN ("
										+ GlobalProperties.gpGetListOfTBDrugs()
										+ ") "
										+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
										+ patientId);

						List<Date> patientIdsMinStartDate = queryMinStartDate
								.list();

						if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
								.getTime())
								&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
										.getTime()) {

							SQLQuery queryExited = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetExitFromCareConceptId())
											+ " and (cast(o.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and o.voided = 0 and o.person_id= "
											+ patientId);

							List<Integer> patientIds3 = queryExited.list();

							if ((patientIds3.size() == 0)) {

								patientIdsList.add(patientId);

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleMoreThanFifteenInHivCare(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleMoreThanFifteenInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));
		


	try {
		
		Session session = getSessionFactory2().getCurrentSession();


			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pg.voided = 0 and pe.voided = 0 "
							+ " and pa.voided = 0 and pe.gender = 'F' and pg.date_enrolled >= '"
							+ startDate 
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(max(date_enrolled) as DATE) from patient_program where (select cast(max(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{

							SQLQuery queryExited = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetExitFromCareConceptId())
											+ " and (cast(o.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and o.voided = 0 and o.person_id="
											+ patientId);

							List<Integer> patientIds3 = queryExited.list();

							if (patientIds3.size() == 0) {

								/*SQLQuery queryDate1 = session
										.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
												+ "(select(cast(max(encounter_datetime)as Date))) <= '"
												+ endDate
												+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
												+ patientId);

								List<Date> maxEnocunterDateTime = queryDate1
										.list();

								SQLQuery queryDate2 = session
										.createSQLQuery("select cast(max(value_datetime) as DATE ) "
												+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
												+ endDate
												+ "' and concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetReturnVisitDateConceptId())
												+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
												+ patientId);

								List<Date> maxReturnVisitDay = queryDate2
										.list();

								if (((maxReturnVisitDay.get(0)) != null)
										&& (maxEnocunterDateTime.get(0) != null)) {

									if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxEnocunterDateTime
											.get(0).getTime()) <= newEndDate
											.getTime())
											|| ((maxReturnVisitDay.get(0)
													.getTime()) >= threeMonthsBeforeEndDate
													.getTime() && (maxReturnVisitDay
													.get(0).getTime()) <= newEndDate
													.getTime())) {

										patientIdsList.add(patientId);

									}
								}

								else if (((maxReturnVisitDay.get(0)) == null)
										&& (maxEnocunterDateTime.get(0) != null)) {

									if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime()
											&& (maxEnocunterDateTime.get(0)
													.getTime()) <= newEndDate
													.getTime()) {

										patientIdsList.add(patientId);

									}
								} else if (((maxReturnVisitDay.get(0) != null))
										&& (maxReturnVisitDay.get(0).getTime() > newEndDate
												.getTime()))

								{*/
									patientIdsList.add(patientId);

								}
							}
						}
					}
			//}
			
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		
}

		catch (Exception e) {
			e.printStackTrace();
		}

			

		return patients;
	}

	/**
	 * Total number of new female pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleUnderFifteenInHivCare(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleUnderFifteenInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and pe.gender = 'F' and pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{
								SQLQuery queryExited = session
										.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetExitFromCareConceptId())
												+ " and (cast(o.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and o.voided = 0 and o.person_id= "
												+ patientId);

								List<Integer> patientIds3 = queryExited.list();

								if ((patientIds3.size() == 0)) {

									SQLQuery queryTransferredIn = session
											.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTransferredInConceptId())
													+ " and o.voided = 0 and o.person_id= "
													+ patientId);

									List<Integer> patientIds4 = queryTransferredIn
											.list();

									if (patientIds4.size() == 0) {

										patientIdsList.add(patientId);

									}
								}
							}
						}
					}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of new male pediatric patients (age <15 years) enrolled in
	 * HIV care?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleUnderFifteenInHivCare(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleUnderFifteenInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and pe.gender = 'M' and pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{


								SQLQuery queryExited = session
										.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetExitFromCareConceptId())
												+ " and (cast(o.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and o.person_id= "
												+ patientId);

								List<Integer> patientIds3 = queryExited.list();

								if ((patientIds3.size() == 0)) {

									SQLQuery queryTransferredIn = session
											.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTransferredInConceptId())
													+ " and o.voided = 0 and o.person_id= "
													+ patientId);

									List<Integer> patientIds4 = queryTransferredIn
											.list();

									if (patientIds4.size() == 0) {

										patientIdsList.add(patientId);

									}
								}
							}
						}
					}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of new pediatric patients (age <5 years) enrolled in HIV
	 * care?
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveInHivCare(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsUnderFiveInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 4 "
							+ " and pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "'  and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(min(date_enrolled) as DATE) from patient_program where (select cast(min(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{

								SQLQuery queryExited = session
										.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetExitFromCareConceptId())
												+ " and (cast(o.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and o.voided = 0 and o.person_id= "
												+ patientId);

								List<Integer> patientIds3 = queryExited.list();

								if ((patientIds3.size() == 0)) {

									SQLQuery queryTransferredIn = session
											.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTransferredInConceptId())
													+ " and o.voided = 0 and o.person_id= "
													+ patientId);

									List<Integer> patientIds4 = queryTransferredIn
											.list();

									if (patientIds4.size() == 0) {

										patientIdsList.add(patientId);

									}
								}
							}
						}
					}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	/**
	 * Number of new patients screened for active TB at enrollment this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsActiveTbAtEnrolThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> patientsActiveTbAtEnrolThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTBScreeningConceptId())
							+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and o.voided = 0 and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetTBScreeningConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfTBScreening = queryDate.list();

					if (dateOfTBScreening.size() != 0)
						if ((dateOfTBScreening.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfTBScreening.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return patients;
	}

	/**
	 * Number of patients screened TB Positive at enrollment this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsTbPositiveAtEnrolThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> patientsTbPositiveAtEnrolThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTBScreeningConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPositiveAsResultToHIVTestConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetTBScreeningConceptId())
									+ " and value_coded= "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetPositiveAsResultToHIVTestConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfTBScreening = queryDate.list();

					if (dateOfTBScreening.size() != 0)
						if ((dateOfTBScreening.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfTBScreening.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV
	 * care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedUnderEighteenMonthsEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedUnderEighteenMonthsEverInHiv(String startDate,
			String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 2 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					patientIdsList.add(patientId);

				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV
	 * care?
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveEverInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedsUnderFiveEverInHiv(String startDate, String endDate) {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 4 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and ord.voided = 0 and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					patientIdsList.add(patientId);

				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	// ---------------------------------Exporting Data to CSV and Excel
	// Files----------------------------------------------

	/**
	 * Exports data to the CSV File or Text File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToCsvFile(java.util.Map)
	 */
	@Override
	public void exportDataToCsvFile(HttpServletRequest request,
			HttpServletResponse response, Map<String, Integer> indicatorsList,
			String filename, String title, String startDate, String endDate)
			throws IOException {

		ServletOutputStream outputStream = response.getOutputStream();
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");
		outputStream.println("" + title + "(Between " + startDate + " and "
				+ endDate + ")");
		outputStream.println();
		outputStream.println("# , Indicator Name , Indicator");
		outputStream.println();
		int count = 0;
		for (String indicator : indicatorsList.keySet()) {
			count++;
			outputStream.println(count + " , " + indicator.toString() + " , "
					+ indicatorsList.get(indicator).toString());
		}

		outputStream.flush();
		outputStream.close();
	}

	/**
	 * Exports data to the Excel File
	 * 
	 * @throws IOException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#exportDataToExcelFile(java.util.Map)
	 */

	@SuppressWarnings("deprecation")
	public void exportDataToExcelFile(HttpServletRequest request,
			HttpServletResponse response, Map<String, Integer> indicatorsList,
			String filename, String title, String startDate, String endDate)
			throws IOException {

		log.info("exporttttttttttttttttttttttttttttttt" + indicatorsList);

		HSSFWorkbook workbook = new HSSFWorkbook();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");
		HSSFSheet sheet = workbook.createSheet(title);
		int count = 0;
		sheet.setDisplayRowColHeadings(true);

		// Setting Style
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
		row.createCell((short) 1).setCellValue(
				"" + title + "(Between " + startDate + " and " + endDate + ")");

		// Headers
		row = sheet.createRow((short) 2);
		row.createCell((short) 0).setCellValue("#");
		row.createCell((short) 1).setCellValue("INDICATOR NAME");
		row.createCell((short) 2).setCellValue("INDICATOR");

		// for (String indicator : indicatorsList.keySet()) {
		// count++;
		// row = sheet.createRow((short) count + 3);
		// row.createCell((short) 0).setCellValue(count);
		// row.createCell((short) 1).setCellValue(indicator.toString());
		// row.createCell((short) 2).setCellValue(
		// indicatorsList.get(indicator).toString());
		// }
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

	// ---------------------------------B. ART Data
	// Elements----------------------------------------------------

	/**
	 * Total number of adult patients who are on First Line Regimen
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultOnFirstLineReg(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> adultOnFirstLineReg(String startDate, String endDate)
			throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 15 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfFirstLineDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfSecondLineDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);

							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of ARV patients (age 15+) lost to followup (>3 months)
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultLostFollowupMoreThreeMonths(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvAdultLostFollowupMoreThreeMonths(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.date_completed is null and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						}

						else {
							patientIdsList.add(patientId);
						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						}

						else {
							patientIdsList.add(patientId);
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of ARV patients (age 15+) who have been transferred in this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferreInThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvAdultTransferreInThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTransferredInConceptId())
							+ " and o.value_coded = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetYesAsAnswerToTransferredInConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetTransferredInConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateTransferredIn = queryDate.list();

					if (dateTransferredIn.size() != 0)
						if ((dateTransferredIn.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateTransferredIn.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of ARV patients (age 15+) who have been transferred out this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferredOutThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvAdultTransferredOutThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromTransferredOutConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDate = session
						.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and value_coded= "
								+ Integer
										.parseInt(GlobalProperties
												.gpGetExitFromTransferredOutConceptId())
								+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
								+ patientId);

				List<Date> dateOfTransferredOut = queryDate.list();

				if (dateOfTransferredOut.size() != 0)
					if ((dateOfTransferredOut.get(0).getTime() >= newStartDate
							.getTime())
							&& (dateOfTransferredOut.get(0).getTime() <= newEndDate
									.getTime())) {

						patientIdsList.add(patientId);
					}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of ARV patients (age <15) who have died this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsDiedThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvPedsDiedThisMonth(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and ord.discontinued_date is not null and ord.discontinued_reason = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDate = session
						.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and value_coded= "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareDiedConceptId())
								+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
								+ patientId);

				List<Date> dateOfDeath = queryDate.list();

				if (dateOfDeath.size() != 0)
					if ((dateOfDeath.get(0).getTime() >= newStartDate.getTime())
							&& (dateOfDeath.get(0).getTime() <= newEndDate
									.getTime())) {

						patientIdsList.add(patientId);
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of ARV patients (age <15) who have had their treatment interrupted
	 * this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsFifteenInterruptTreatThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvPedsFifteenInterruptTreatThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and ord.discontinued = 1 and pg.date_enrolled <= '"
							+ endDate
							+ "' and ord.discontinued_date is not null and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDrugs = session
							.createSQLQuery("select count(*) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Integer> drugsPerPatient = queryDrugs.list();

					SQLQuery queryDrugsDiscontinued = session
							.createSQLQuery("select count(*) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") and ord.voided = 0 and ord.discontinued = 1 and ord.patient_id = "
									+ patientId);

					List<Integer> drugsDiscontinuedPerPatient = queryDrugsDiscontinued
							.list();

					if (drugsPerPatient.get(0) == drugsDiscontinuedPerPatient
							.get(0)) {

						SQLQuery queryDate = session
								.createSQLQuery("select cast(discontinued_date as DATE) from orders ord "
										+ "inner join drug_order do on ord.order_id = do.order_id "
										/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
										+ " where ord.concept_id IN ("
										+ GlobalProperties
												.gpGetListOfARVsDrugs()
										+ ") and (select cast(discontinued_date as DATE)) is not null and ord.voided = 0 and ord.patient_id = "
										+ patientId);

						List<Date> dateOfDiscontinuedDrugs = queryDate.list();

						boolean n = true;

						for (Date d : dateOfDiscontinuedDrugs) {

							if ((d.getTime() >= newStartDate.getTime())
									&& (d.getTime() <= newEndDate.getTime())) {
								;
							} else {
								n = false;
								break;
							}

						}

						if (n == true) {
							patientIdsList.add(patientId);
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of ARV patients (age <15) lost to followup (>3 months)
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsLostFollowupMoreThreeMonths(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvPedsLostFollowupMoreThreeMonths(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		@SuppressWarnings("unused")
		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						}

						else {
							patientIdsList.add(patientId);
						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						}

						else {
							patientIdsList.add(patientId);
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of ARV patients (age <15) who have been transferred in this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredInThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvPedsTransferredInThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTransferredInConceptId())
							+ " and o.value_coded = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetYesAsAnswerToTransferredInConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetTransferredInConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateTransferredIn = queryDate.list();

					if (dateTransferredIn.size() != 0)
						if ((dateTransferredIn.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateTransferredIn.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of ARV patients (age <15) who have been transferred out this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredOutThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvPedsTransferredOutThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromTransferredOutConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDate = session
						.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and value_coded= "
								+ Integer
										.parseInt(GlobalProperties
												.gpGetExitFromTransferredOutConceptId())
								+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
								+ patientId);

				List<Date> dateOfTransferredOut = queryDate.list();

				if (dateOfTransferredOut.size() != 0)
					if ((dateOfTransferredOut.get(0).getTime() >= newStartDate
							.getTime())
							&& (dateOfTransferredOut.get(0).getTime() <= newEndDate
									.getTime())) {

						patientIdsList.add(patientId);
					}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of female adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleMoreThanFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 15 "
							+ " and pe.gender = 'F' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ " and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of female patients on treatment 12 months after initiation of ARVs
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleOnTreatTwelveAfterInitArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleOnTreatTwelveAfterInitArv(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date oneYearBeforeStartDate = df.parse(addDaysToDate(startDate, -12));

		Date oneYearBeforeEndDate = df.parse(addDaysToDate(endDate, -12));

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where pe.gender = 'F' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ df.format(oneYearBeforeStartDate)
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ df.format(oneYearBeforeEndDate)
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if (patientIdsMinStartDate.size() != 0) {

						Date minStartDate = patientIdsMinStartDate.get(0);

						if ((minStartDate.getTime() >= oneYearBeforeStartDate
								.getTime())
								&& (minStartDate.getTime() <= oneYearBeforeEndDate
										.getTime())) {

							SQLQuery queryDate1 = session
									.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
											+ "(select(cast(max(encounter_datetime)as Date))) <= '"
											+ endDate
											+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
											+ patientId);

							List<Date> maxEnocunterDateTime = queryDate1.list();

							SQLQuery queryDate2 = session
									.createSQLQuery("select cast(max(value_datetime) as DATE ) "
											+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
											+ endDate
											+ "' and concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetReturnVisitDateConceptId())
											+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
											+ patientId);

							List<Date> maxReturnVisitDay = queryDate2.list();

							if (((maxReturnVisitDay.get(0)) != null)
									&& (maxEnocunterDateTime.get(0) != null)) {

								if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxEnocunterDateTime
										.get(0).getTime()) <= newEndDate
										.getTime())
										|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
												.getTime() && (maxReturnVisitDay
												.get(0).getTime()) <= newEndDate
												.getTime())) {

									patientIdsList.add(patientId);

								}
							}

							else if (((maxReturnVisitDay.get(0)) == null)
									&& (maxEnocunterDateTime.get(0) != null)) {

								if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime()
										&& (maxEnocunterDateTime.get(0)
												.getTime()) <= newEndDate
												.getTime()) {

									patientIdsList.add(patientId);

								}
							} else if (((maxReturnVisitDay.get(0) != null))
									&& (maxReturnVisitDay.get(0).getTime() > newEndDate
											.getTime()))

							{
								patientIdsList.add(patientId);

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of female pediatric patients (age <15 years) who are
	 * currently on ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femalePedsUnderFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newEndDate = df.parse(endDate);

		@SuppressWarnings("unused")
		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and pe.gender = 'F' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of male adult patients (age 15 or older) who are currently
	 * on ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleMoreThanFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newEndDate = df.parse(endDate);

		@SuppressWarnings("unused")
		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pe.gender = 'M' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of male patients on treatment 12 months after initiation of ARVs
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleOnTreatTwelveAfterInitArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleOnTreatTwelveAfterInitArv(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date oneYearBeforeStartDate = df.parse(addDaysToDate(startDate, -12));

		Date oneYearBeforeEndDate = df.parse(addDaysToDate(endDate, -12));

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where pe.gender = 'M' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ df.format(oneYearBeforeStartDate)
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ df.format(oneYearBeforeEndDate)
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if (patientIdsMinStartDate.size() != 0) {

						Date minStartDate = patientIdsMinStartDate.get(0);

						if ((minStartDate.getTime() >= oneYearBeforeStartDate
								.getTime())
								&& (minStartDate.getTime() <= oneYearBeforeEndDate
										.getTime())) {

							SQLQuery queryDate1 = session
									.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
											+ "(select(cast(max(encounter_datetime)as Date))) <= '"
											+ endDate
											+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
											+ patientId);

							List<Date> maxEnocunterDateTime = queryDate1.list();

							SQLQuery queryDate2 = session
									.createSQLQuery("select cast(max(value_datetime) as DATE ) "
											+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
											+ endDate
											+ "' and concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetReturnVisitDateConceptId())
											+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
											+ patientId);

							List<Date> maxReturnVisitDay = queryDate2.list();

							if (((maxReturnVisitDay.get(0)) != null)
									&& (maxEnocunterDateTime.get(0) != null)) {

								if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxEnocunterDateTime
										.get(0).getTime()) <= newEndDate
										.getTime())
										|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
												.getTime() && (maxReturnVisitDay
												.get(0).getTime()) <= newEndDate
												.getTime())) {

									patientIdsList.add(patientId);

								}
							}

							else if (((maxReturnVisitDay.get(0)) == null)
									&& (maxEnocunterDateTime.get(0) != null)) {

								if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime()
										&& (maxEnocunterDateTime.get(0)
												.getTime()) <= newEndDate
												.getTime()) {

									patientIdsList.add(patientId);

								}
							} else if (((maxReturnVisitDay.get(0) != null))
									&& (maxReturnVisitDay.get(0).getTime() > newEndDate
											.getTime()))

							{
								patientIdsList.add(patientId);

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of male pediatric patients (age <15 years) who are currently
	 * on ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> malePedsUnderFifteenCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and pe.gender = 'M' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of new adult patients who are WHO stage undefined this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultUndefinedWhoStageThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAdultUndefinedWhoStageThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetUnknownStageConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new adult patients who are WHO stage 4 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageFourThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAdultWhoStageFourThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageFourAdultConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new adult patients who are WHO stage 1 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageOneThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAdultWhoStageOneThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageOneAdultConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new adult patients who are WHO stage 3 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageThreeThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAdultWhoStageThreeThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageThreeAdultConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new adult patients who are WHO stage 2 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageTwoThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAdultWhoStageTwoThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageTwoAdultConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if (patientIds2.size() == 0)

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()) {

						SQLQuery queryDate = session
								.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and value_coded= "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetWhoStageTwoAdultConceptId())
										+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0)
							if ((dateOfWhoStage.get(0).getTime() >= newStartDate
									.getTime())
									&& (dateOfWhoStage.get(0).getTime() <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);
							}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new female adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleAdultStartiArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleAdultStartiArvThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pe.gender = 'F' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()) {

						patientIdsList.add(patientId);
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of new female pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemalePedsUnderFifteenStartArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemalePedsUnderFifteenStartArvThisMonth(
			String startDate, String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime())

					{
						patientIdsList.add(patientId);
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Number of new male adult patients (age 15+) starting ARV treatment this
	 * month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleAdultStartiArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleAdultStartiArvThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pe.gender = 'M' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if (patientIds2.size() == 0)

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()) {
						{
							patientIdsList.add(patientId);
						}
					}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new male pediatric patients (age <15 years) starting ARV
	 * treatment this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMalePedsUnderFifteenStartArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMalePedsUnderFifteenStartArvThisMonth(
			String startDate, String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and pe.gender = 'M' and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided =  0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()) {
						patientIdsList.add(patientId);
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients whose WHO Stage is undefined this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUndefinedWhoStageThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsUndefinedWhoStageThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetUnknownStageConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}

			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients (<18 months) starting ARV treatment this
	 * month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthStartArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsUnderEighteenMonthStartArvThisMonth(
			String startDate, String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 2 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime())

					{
						patientIdsList.add(patientId);
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients (age <5 years) starting ARV treatment
	 * this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveStartArvThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsUnderFiveStartArvThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 4 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") "
									+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime())

					{
						patientIdsList.add(patientId);
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients who are WHO stage 4 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageFourThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsWhoStageFourThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageFourPedsConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}

			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients who are WHO Stage 1 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageOneThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsWhoStageOneThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageOnePedsConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}

			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients who are WHO Stage 3 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageThreeThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsWhoStageThreeThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0  person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageThreePedsConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}

			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Number of new pediatric patients who are WHO Stage 2 this month
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageTwoThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newPedsWhoStageTwoThisMonth(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") "
							+ "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetWhoStageConceptId())
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();
			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				SQLQuery queryMinStartDate = session
						.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
								+ " inner join drug_order do on ord.order_id = do.order_id "
								/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ " where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") "
								+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
								+ patientId);

				List<Date> patientIdsMinStartDate = queryMinStartDate.list();

				if ((patientIds2.size() == 0)
						&& (patientIdsMinStartDate.size() != 0))

					if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
							.getTime())
							&& (patientIdsMinStartDate.get(0).getTime() <= newEndDate
									.getTime()))

					{

						SQLQuery queryDate = session
								.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWhoStageConceptId())
										+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfWhoStage = queryDate.list();

						if (dateOfWhoStage.size() != 0) {
							SQLQuery valueCoded = session
									.createSQLQuery("select value_coded from obs where concept_id = "
											+ Integer.parseInt(GlobalProperties
													.gpGetWhoStageConceptId())
											+ " and (select(cast(obs_datetime as Date))) = "
											+ "'"
											+ dateOfWhoStage.get(0)
											+ " ' "
											+ " and value_coded is not null and voided = 0 and person_id = "
											+ patientId);

							List<Integer> PatientValueCoded = valueCoded.list();

							if (PatientValueCoded.size() != 0)
								if (PatientValueCoded.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetWhoStageTwoPedsConceptId())) {

									patientIdsList.add(patientId);
								}
						}
					}

			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * Total number of pediatric patients who are on First Line Regimen
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnFirstLineReg(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedsOnFirstLineReg(String startDate, String endDate)
			throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfFirstLineDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfSecondLineDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);

							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of pediatric patients who are on Second Line Regimen
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnSecondLineReg(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedsOnSecondLineReg(String startDate, String endDate)
			throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ "  and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfSecondLineDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {
					// try {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of pediatric patients (age <18 months) who are currently on
	 * ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderEighteenMonthsCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedsUnderEighteenMonthsCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 2 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * Total number of pediatric patients (age <5 years) who are currently on
	 * ARV treatment
	 * 
	 * @throws ParseException
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveCurrentOnArv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pedsUnderFiveCurrentOnArv(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 4 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	// ---------------------------------C. STIs, Opportunistic Infections and
	// Others-----------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#clientsCounceledForStiThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> clientsCounceledForStiThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#opportInfectTreatedExcludeTbThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> opportInfectTreatedExcludeTbThisMonth(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetOpportunisticInfectionsConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryOpportunisticInfectionTB = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetOpportunisticInfectionsConceptId())
									+ " and o.value_coded = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetOpportunisticInfectionTBConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIdsqueryOpportunisticInfectionTB = queryOpportunisticInfectionTB
							.list();

					if (patientIdsqueryOpportunisticInfectionTB.size() == 0) {

						SQLQuery queryDate = session
								.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetOpportunisticInfectionsConceptId())
										+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateOfOpportunisticInfections = queryDate
								.list();

						if (dateOfOpportunisticInfections.size() != 0)
							if ((dateOfOpportunisticInfections.get(0).getTime() >= newStartDate
									.getTime())
									&& (dateOfOpportunisticInfections.get(0)
											.getTime() <= newEndDate.getTime())) {

								patientIdsList.add(patientId);
							}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#stiDiagnosedThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> stiDiagnosedThisMonth(String startDate, String endDate)
			throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pg.voided = 0 and pe.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and pg.date_enrolled >= '"
							+ startDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetOpportunisticInfectionsConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetOpportunisticInfectionSTIConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetEndDateOfOpportunisticInfectionSTIConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfEndDateOfSTI = queryDate.list();

					if (dateOfEndDateOfSTI.size() != 0)
						if ((dateOfEndDateOfSTI.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfEndDateOfSTI.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	// ---------------------------------D. Nutrition Consultation Data
	// Elements-----------------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultSevereMalnutrTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> adultSevereMalnutrTherapThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<Person> numberOfPatientsWhoReceivedFollowUpAndAdherenceCounselling(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Person> numberOfPatientsWhoReceivedFamilyPlanningThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> adultTherapThisMonth(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#lactatingMalnutrTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> lactatingMalnutrTherapThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pedsTherapThisMonth(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFifteenSevMalnutrTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pedsUnderFifteenSevMalnutrTherapThisMonth(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveSevereMalnutrTheurapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pedsUnderFiveSevereMalnutrTheurapThisMonth(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveSevereMalnutrThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pedsUnderFiveSevereMalnutrThisMonth(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			double weight;

			double height;

			double resultWeightHeight;

			double resultWeightAge;

			double age;

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 4 "
							+ " and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				weight = 0;

				height = 0;

				resultWeightHeight = 0;

				resultWeightAge = 0;

				age = 0;

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0)

				{

					SQLQuery queryDateWeight = session
							.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetWeightConceptId())
									+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfWeight = queryDateWeight.list();

					if (dateOfWeight.size() != 0) {
						SQLQuery valueNumericOfWeight = session
								.createSQLQuery("select value_numeric from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetWeightConceptId())
										+ " and (select(cast(obs_datetime as Date))) = "
										+ "'"
										+ dateOfWeight.get(0)
										+ " ' "
										+ " and value_numeric is not null and voided = 0 and person_id = "
										+ patientId);

						List<Double> PatientValueNumericOfWeight = valueNumericOfWeight
								.list();

						if (PatientValueNumericOfWeight.size() != 0) {

							weight = PatientValueNumericOfWeight.get(0);
						}

					}

					SQLQuery queryDateHeight = session
							.createSQLQuery("select (cast(max(obs_datetime) as DATE)) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetHeightConceptId())
									+ " and (select (cast(max(obs_datetime) as DATE))) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfHeight = queryDateHeight.list();

					if (dateOfHeight.size() != 0) {
						SQLQuery valueNumericOfHeight = session
								.createSQLQuery("select value_numeric from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetHeightConceptId())
										+ " and (select(cast(obs_datetime as Date))) = "
										+ "'"
										+ dateOfHeight.get(0)
										+ " ' "
										+ " and value_numeric is not null and voided = 0 and person_id = "
										+ patientId);

						List<Double> PatientValueNumericOfHeight = valueNumericOfHeight
								.list();

						if (PatientValueNumericOfHeight.size() != 0) {

							height = PatientValueNumericOfHeight.get(0);
						}
					}

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded IN ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ "and pg.date_completed is null and pg.patient_id = "
									+ patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {

						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0)

						{

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery queryAge = session
											.createSQLQuery("select DATE_FORMAT(FROM_DAYS(TO_DAYS('2010-01-01') - TO_DAYS(pe.birthdate)), '%Y')+0 < 5 "
													+ " from person pe where pe.voided = 0 and pe.person_id="
													+ patientId);

									List<Double> patientIdsAge = queryAge
											.list();

									if (patientIdsAge.size() != 0) {

										age = patientIdsAge.get(0);
									}

									resultWeightHeight = weight / height;

									resultWeightAge = weight / age;

									if ((resultWeightHeight < -3)
											|| (resultWeightAge < -3)) {

										patientIdsList.add(patientId);
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveWithSevMalnutrThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pedsUnderFiveWithSevMalnutrThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantMalnutrTherapThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pregnantMalnutrTherapThisMonth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	// ---------------------------------A. Antenatal Data
	// Elements------------------------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#discordantCouples1(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> discordantCouples1(String startDate, String endDate) {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTestingStatusOfPartnerConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id= "
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded IN ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {

						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded IN ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultDatePartner = session
									.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetTestingStatusOfPartnerConceptId())
											+ " and o.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
											+ endDate
											+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
											+ patientId);
							List<Date> HivTestResultDatePartner = queryHIVResultDatePartner
									.list();

							if (HivTestResultDatePartner.size() != 0) {

								SQLQuery queryHIVResultConcept = session
										.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded IN ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
												+ HivTestResultDate.get(0)
												+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
												+ patientId);

								List<Integer> HivTestResultConcept = queryHIVResultConcept
										.list();

								if (HivTestResultConcept.size() != 0) {

									SQLQuery queryHIVResultConceptPartner = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and o.value_coded IN ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDatePartner
															.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConceptPartner = queryHIVResultConceptPartner
											.list();

									if (HivTestResultConceptPartner.size() != 0) {

										if (((HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetNegativeAsResultToHIVTestConceptId())) && (HivTestResultConceptPartner
												.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())))
												|| ((HivTestResultConcept
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) && (HivTestResultConceptPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetNegativeAsResultToHIVTestConceptId())))) {

											patientIdsList.add(patientId);
										}
									}
								}
							}
						}

					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;

	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#hivNegPregnantPartnersTestedHivPos(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> hivNegPregnantPartnersTestedHivPos(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTestingStatusOfPartnerConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id= "
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded IN ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {

						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded IN ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultDatePartner = session
									.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetTestingStatusOfPartnerConceptId())
											+ " and o.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
											+ endDate
											+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided= 0 and o.person_id="
											+ patientId);
							List<Date> HivTestResultDatePartner = queryHIVResultDatePartner
									.list();

							if (HivTestResultDatePartner.size() != 0) {

								if ((HivTestResultDatePartner.get(0).getTime() >= newStartDate
										.getTime())
										&& (HivTestResultDatePartner.get(0)
												.getTime() <= newEndDate
												.getTime())) {

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded IN ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);

									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										SQLQuery queryHIVResultConceptPartner = session
												.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded IN ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
														+ HivTestResultDatePartner
																.get(0)
														+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
														+ patientId);
										List<Integer> HivTestResultConceptPartner = queryHIVResultConceptPartner
												.list();

										if (HivTestResultConceptPartner.size() != 0) {

											if ((HivTestResultConcept.get(0) == Integer
													.parseInt(GlobalProperties
															.gpGetNegativeAsResultToHIVTestConceptId()))
													&& (HivTestResultConceptPartner
															.get(0) == Integer
															.parseInt(GlobalProperties
																	.gpGetPositiveAsResultToHIVTestConceptId()))) {

												patientIdsList.add(patientId);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#negativeWomenReturnRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> negativeWomenReturnRes(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "inner join patient_program pg on pe.person_id = pg.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetDateResultOfHIVTestReceivedConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and ob.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVResultReceivedDate = session
							.createSQLQuery("select cast(min(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetDateResultOfHIVTestReceivedConceptId())
									+ " and (select cast(min(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
									+ patientId);
					List<Date> HivTestResultReceivedDate = queryHIVResultReceivedDate
							.list();

					if (HivTestResultReceivedDate.size() != 0) {

						if ((HivTestResultReceivedDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (HivTestResultReceivedDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pe.person_id from person pe "
											+ "inner join obs ob on pe.person_id = ob.person_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and ob.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and ob.voided = 0 and pe.person_id = "
											+ patientId);

							List<Integer> patientIds3 = query3.list();

							if (patientIds3.size() != 0) {
								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(min(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(min(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(min(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetNegativeAsResultToHIVTestConceptId())) {

											patientIdsList.add(patientId);
										}

									}
								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#partnersTestedHivPos(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> partnersTestedHivPos(String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		// try {
		//
		// Session session = getSessionFactory2().getCurrentSession();
		// SQLQuery query = session
		// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
		// + "inner join obs ob on pg.patient_id = ob.person_id "
		// + "inner join person pe on pg.patient_id = pe.person_id "
		// + "inner join patient pa on pg.patient_id = pa.patient_id "
		// + "inner join encounter enc on pg.patient_id = enc.patient_id "
		// + "where pe.gender = 'f' and ob.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetTestingStatusOfPartnerConceptId())
		// + " and (cast(ob.obs_datetime as DATE)) >= '"
		// + startDate
		// + "' AND (cast(ob.obs_datetime as DATE)) <= '"
		// + endDate
		// + "' and (cast(pg.date_enrolled as DATE)) <= '"
		// + endDate
		// +
		// "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 "
		// + " and pg.date_completed is null ");
		//
		// // Getting the size of the returned LIST which equals to the COUNTS
		// // needed
		// List<Integer> patientIds = query.list();
		//
		// for (Integer patientId : patientIds) {
		//
		// SQLQuery query2 = session
		// .createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetExitFromCareConceptId())
		// + " and o.person_id=" + patientId);
		//
		// List<Integer> patientIds2 = query2.list();
		//
		// if (patientIds2.size() == 0) {
		//
		// SQLQuery query3 = session
		// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
		// + "inner join obs ob on pg.patient_id = ob.person_id "
		// + "inner join person pe on pg.patient_id = pe.person_id "
		// + "inner join patient pa on pg.patient_id = pa.patient_id "
		// + "where ob.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and (cast(ob.obs_datetime as DATE)) <= '"
		// + endDate
		// + "' and ob.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (cast(pg.date_enrolled as DATE)) <= '"
		// + endDate
		// +
		// "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
		// + "and pg.date_completed is null and pg.patient_id = "
		// + patientId);
		//
		// List<Integer> patientIds3 = query3.list();
		//
		// if (patientIds3.size() != 0) {
		// SQLQuery queryHIVResultDate = session
		// .createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
		// + endDate
		// + "' and o.person_id="
		// + patientId);
		// List<Date> HivTestResultDate = queryHIVResultDate
		// .list();
		//
		// SQLQuery queryHIVResultDatePartner = session
		// .createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetTestingStatusOfPartnerConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
		// + endDate
		// + "' and o.person_id="
		// + patientId);
		// List<Date> HivTestResultDatePartner = queryHIVResultDatePartner
		// .list();
		//
		// SQLQuery queryHIVResultConcept = session
		// .createSQLQuery("select o.value_coded from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) = '"
		// + HivTestResultDate.get(0)
		// + "' and o.person_id= " + patientId);
		// List<Integer> HivTestResultConcept = queryHIVResultConcept
		// .list();
		//
		// SQLQuery queryHIVResultConceptPartner = session
		// .createSQLQuery("select o.value_coded from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetTestingStatusOfPartnerConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) = '"
		// + HivTestResultDatePartner.get(0)
		// + "' and o.person_id= " + patientId);
		// List<Integer> HivTestResultConceptPartner =
		// queryHIVResultConceptPartner
		// .list();
		//
		// if ((HivTestResultConcept.get(0) != null)
		// && (HivTestResultConceptPartner.get(0) != null)) {
		//
		// if ((HivTestResultConcept.get(0) == Integer
		// .parseInt(GlobalProperties
		// .gpGetPositiveAsResultToHIVTestConceptId()))
		// && (HivTestResultConceptPartner.get(0) == Integer
		// .parseInt(GlobalProperties
		// .gpGetPositiveAsResultToHIVTestConceptId()))) {
		//
		// Person patient = Context.getPersonService()
		// .getPerson(patientId);
		// patients.add(patient);
		// }
		// }
		// }
		// }
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantHivPos(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pregnantHivPos(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate + "' and o.person_id=" + patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0)

						{

							if ((HivTestResultDate.get(0).getTime() >= newStartDate
									.getTime())
									&& (HivTestResultDate.get(0).getTime() <= newEndDate
											.getTime())) {

								SQLQuery queryHIVResultConcept = session
										.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
												+ HivTestResultDate.get(0)
												+ "' and o.value_coded is not null and o.person_id= "
												+ patientId);
								List<Integer> HivTestResultConcept = queryHIVResultConcept
										.list();

								if (HivTestResultConcept.size() != 0) {

									if (HivTestResultConcept.get(0) == Integer
											.parseInt(GlobalProperties
													.gpGetPositiveAsResultToHIVTestConceptId())) {

										patientIdsList.add(patientId);
									}

								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantHivPosAztProphyAt28Weeks(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pregnantHivPosAztProphyAt28Weeks(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantHivPosEligibleArvs1(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pregnantHivPosEligibleArvs1(String startDate,
			String endDate) {

		double val = 0;

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "where o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetCD4CountConceptId())
							+ " and pe.gender = 'f' and (cast(o.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and pg.voided = 0 and pe.voided = 0 and pa.voided = 0 and o.voided = 0 ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate + "' and o.person_id=" + patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query2Date = session
							.createSQLQuery("select cast(max(obs_datetime) as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetCD4CountConceptId())
									+ " and (select cast(max(obs_datetime) as DATE)) <= '"
									+ endDate
									+ "' and (select cast(max(obs_datetime) as DATE)) is not null and person_id = "
									+ patientId);

					List<Date> maxObsDateTimeCD4Count = query2Date.list();

					if (maxObsDateTimeCD4Count.size() != 0) {

						SQLQuery query3 = session
								.createSQLQuery("select value_numeric from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetCD4CountConceptId())
										+ " and obs_datetime = '"
										+ maxObsDateTimeCD4Count.get(0)
										+ "' and value_numeric is not null and person_id = "
										+ patientId);

						List<Double> maxValueNumericCD4Count = query3.list();

						if (maxValueNumericCD4Count.size() != 0)

						{

							// val = maxValueNumericCD4Count.get(0);

							if (maxValueNumericCD4Count.get(0) < 350.0) {

								SQLQuery query4 = session
										.createSQLQuery("select distinct pg.patient_id from patient_program pg "
												+ "inner join person pe on pg.patient_id = pe.person_id "
												+ "inner join patient pa on pg.patient_id = pa.patient_id "
												+ "inner join obs o on pe.person_id = o.person_id "
												+ "inner join orders ord on pg.patient_id = ord.patient_id "
												+ "inner join drug_order do on ord.order_id = do.order_id "
												/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
												+ "where ord.concept_id IN ("
												+ GlobalProperties
														.gpGetListOfARVsDrugs()
												+ ") and (cast(ord.start_date as DATE)) <= '"
												+ endDate
												+ "' and pg.program_id= "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetPMTCTProgramId())
												+ " and pe.gender = 'f' and pg.patient_id =  "
												+ patientId);

								List<Integer> patientIds4 = query4.list();

								if (patientIds4.size() == 0) {

									patientIdsList.add(patientId);

								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantHivPosEligibleArvs2(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pregnantHivPosEligibleArvs2(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0)

						{

							if ((HivTestResultDate.get(0).getTime() >= newStartDate
									.getTime())
									&& (HivTestResultDate.get(0).getTime() <= newEndDate
											.getTime())) {

								SQLQuery queryHIVResultConcept = session
										.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
												+ HivTestResultDate.get(0)
												+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
												+ patientId);
								List<Integer> HivTestResultConcept = queryHIVResultConcept
										.list();

								if (HivTestResultConcept.size() != 0) {

									if (HivTestResultConcept.get(0) == Integer
											.parseInt(GlobalProperties
													.gpGetPositiveAsResultToHIVTestConceptId())) {

										SQLQuery queryMinStartDate = session
												.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
														+ " inner join drug_order do on ord.order_id = do.order_id "
														/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
														+ " where ord.concept_id IN ("
														+ GlobalProperties
																.gpGetListOfARVsDrugs()
														+ ") "
														+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
														+ patientId);

										List<Date> patientIdsMinStartDate = queryMinStartDate
												.list();

										if (patientIdsMinStartDate.size() != 0) {
											if ((patientIdsMinStartDate.get(0)
													.getTime() >= newStartDate
													.getTime())
													&& patientIdsMinStartDate
															.get(0).getTime() <= newEndDate
															.getTime()) {

												patientIdsList.add(patientId);
											}

										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantHivPosTripleTheraProphy(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pregnantHivPosTripleTheraProphy(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantPartnersTestedForHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pregnantPartnersTestedForHiv(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		// List<Integer> patientIdsList = new ArrayList<Integer>();
		//
		// SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		//
		// Date newEndDate = df.parse(endDate);
		//
		// Date newStartDate = df.parse(startDate);
		//
		// Session session = getSessionFactory2().getCurrentSession();
		// SQLQuery query = session
		// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
		// + "inner join obs ob on pg.patient_id = ob.person_id "
		// + "inner join person pe on pg.patient_id = pe.person_id "
		// + "inner join patient pa on pg.patient_id = pa.patient_id "
		// + "inner join encounter enc on pg.patient_id = enc.patient_id "
		// + "where pe.gender = 'f' and ob.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetTestingStatusOfPartnerConceptId())
		// + " and (cast(ob.obs_datetime as DATE)) >= '"
		// + startDate
		// + "' AND (cast(ob.obs_datetime as DATE)) <= '"
		// + endDate
		// + "' and (cast(pg.date_enrolled as DATE)) <= '"
		// + endDate
		// +
		// "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 "
		// + " and pg.program_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetPMTCTProgramId())
		// + " and pg.date_completed is null ");
		//
		// // Getting the size of the returned LIST which equals to the COUNTS
		// // needed
		// List<Integer> patientIds = query.list();
		//
		// for (Integer patientId : patientIds) {
		//
		// SQLQuery query2 = session
		// .createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetExitFromCareConceptId())
		// + " and o.person_id=" + patientId);
		//
		// List<Integer> patientIds2 = query2.list();
		//
		// if (patientIds2.size() == 0) {
		//
		// SQLQuery queryPartnerHIVTest = session
		// .createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetTestingStatusOfPartnerConceptId())
		// + " and o.person_id= " + patientId);
		// List<Date> partnerHIVTestDate = queryPartnerHIVTest.list();
		//
		// if (partnerHIVTestDate.get(0) != null) {
		//
		// if ((partnerHIVTestDate.get(0).getTime() >= newStartDate
		// .getTime())
		// && (partnerHIVTestDate.get(0).getTime() <= newEndDate
		// .getTime())) {
		//
		// patientIdsList.add(patientId);
		// }
		// }
		// }
		// }
		//
		// for (Integer patientId : patientIdsList) {
		// patients.add(Context.getPersonService().getPerson(patientId));
		// }

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantTestedPosForRpr(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pregnantTestedPosForRpr(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetRapidPlasminReagentConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryCD4CountTest = session
							.createSQLQuery("select cast(max(o.obs_datetime) as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetRapidPlasminReagentConceptId())
									+ " and o.value_coded IN ("
									+ GlobalProperties
											.gpGetListOfAnswersToRapidPlasminReagent()
									+ ") and (select cast(max(o.obs_datetime) as DATE)) is not null and o.voided= 0 and o.person_id= "
									+ patientId);
					List<Date> rprTestDate = queryCD4CountTest.list();

					if (rprTestDate.size() != 0) {

						if ((rprTestDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (rprTestDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery queryHIVResult = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetRapidPlasminReagentConceptId())
											+ " and o.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToRapidPlasminReagent()
											+ ") and o.obs_datetime = '"
											+ rprTestDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id="
											+ patientId);

							List<Integer> rprTestResult = queryHIVResult.list();

							if (rprTestResult.size() != 0) {

								if (rprTestResult.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetreactiveAsfAnswerToRapidPlasminReagentConceptIdConceptId())) {

									patientIdsList.add(patientId);
								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosReturnRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosReturnRes(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetDateResultOfHIVTestReceivedConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and ob.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVResultReceivedDate = session
							.createSQLQuery("select cast(min(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetDateResultOfHIVTestReceivedConceptId())
									+ " and (select cast(min(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
									+ patientId);
					List<Date> HivTestResultReceivedDate = queryHIVResultReceivedDate
							.list();

					if (HivTestResultReceivedDate.size() != 0) {

						if ((HivTestResultReceivedDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (HivTestResultReceivedDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pe.person_id from person pe "
											+ "inner join obs ob on pe.person_id = ob.person_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and ob.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and ob.voided = 0 and pe.person_id = "
											+ patientId);

							List<Integer> patientIds3 = query3.list();

							if (patientIds3.size() != 0) {
								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(min(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(min(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(min(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())) {

											patientIdsList.add(patientId);
										}

									}
								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosTestedCd4(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosTestedCd4(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetCD4CountConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 "
							+ " and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryCD4CountTest = session
							.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetCD4CountConceptId())
									+ " and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id= "
									+ patientId);
					List<Date> cd4CountTestDate = queryCD4CountTest.list();

					if (cd4CountTestDate.size() != 0) {

						if ((cd4CountTestDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (cd4CountTestDate.get(0).getTime() <= newEndDate
										.getTime())) {
							SQLQuery query3 = session
									.createSQLQuery("select distinct pg.patient_id from patient_program pg "
											+ "inner join obs ob on pg.patient_id = ob.person_id "
											+ "inner join person pe on pg.patient_id = pe.person_id "
											+ "inner join patient pa on pg.patient_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and (cast(ob.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and ob.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (cast(pg.date_enrolled as DATE)) <= '"
											+ endDate
											+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
											+ " and pg.patient_id = "
											+ patientId);

							List<Integer> patientIds3 = query3.list();

							if (patientIds3.size() != 0) {

								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
												+ endDate
												+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())) {

											patientIdsList.add(patientId);
										}
									}
								}
							}
						}

					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenKnownHivPosFirstAntenatal(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenKnownHivPosFirstAntenatal(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetCPNEncounterId())
							+ " and pe.gender = 'f' AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and enc.voided=0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinCPNEncounter = session
							.createSQLQuery("select cast(min(encounter_datetime)as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetCPNEncounterId())
									+ " and (select cast(min(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id ="
									+ patientId);

					List<Date> minCPNEncounter = queryMinCPNEncounter.list();

					if (minCPNEncounter.size() != 0) {

						if ((minCPNEncounter.get(0).getTime() >= newStartDate
								.getTime())
								&& (minCPNEncounter.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pg.patient_id from patient_program pg "
											+ "inner join obs ob on pg.patient_id = ob.person_id "
											+ "inner join person pe on pg.patient_id = pe.person_id "
											+ "inner join patient pa on pg.patient_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and (cast(ob.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and ob.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (cast(pg.date_enrolled as DATE)) <= '"
											+ endDate
											+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
											+ " and pg.patient_id = "
											+ patientId);

							List<Integer> patientIds3 = query3.list();

							if (patientIds3.size() != 0) {
								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded IN ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
												+ endDate
												+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								SQLQuery queryHIVResultConcept = session
										.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded IN ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
												+ HivTestResultDate.get(0)
												+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
												+ patientId);
								List<Integer> HivTestResultConcept = queryHIVResultConcept
										.list();

								if (HivTestResultConcept.size() != 0) {

									if (HivTestResultConcept.get(0) == Integer
											.parseInt(GlobalProperties
													.gpGetPositiveAsResultToHIVTestConceptId())) {

										patientIdsList.add(patientId);

									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenTestedForRpr(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenTestedForRpr(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetRapidPlasminReagentConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryRPRTest = session
							.createSQLQuery("select cast(max(o.obs_datetime) as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetRapidPlasminReagentConceptId())
									+ " and (select cast(max(o.obs_datetime) as DATE)) is not null and o.voided  = 0 and o.person_id= "
									+ patientId);
					List<Date> rprTestDate = queryRPRTest.list();

					if (rprTestDate.size() != 0) {

						if ((rprTestDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (rprTestDate.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenUnknownHivFirstAntenatal(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenUnknownHivFirstAntenatal(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "inner join patient pa on pe.person_id = pa.patient_id "
							+ "inner join patient_program pg on pe.person_id = pg.patient_id "
							+ "inner join encounter enc on pe.person_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetCPNEncounterId())
							+ " AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and pe.gender ='f' and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinCPNEncounter = session
							.createSQLQuery("select cast(min(encounter_datetime)as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetCPNEncounterId())
									+ " and (select cast(min(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id ="
									+ patientId);

					List<Date> minCPNEncounter = queryMinCPNEncounter.list();

					if (minCPNEncounter.size() != 0) {

						if ((minCPNEncounter.get(0).getTime() >= newStartDate
								.getTime())
								&& (minCPNEncounter.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pe.person_id from person pe "
											+ "inner join obs ob on pe.person_id = ob.person_id "
											+ "inner join patient pa on pe.person_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and voided = 0 and pe.person_id = "
											+ patientId);

							List<Integer> patientIds3 = query3.list();

							if (patientIds3.size() == 0) {

								patientIdsList.add(patientId);
							}

						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenUnknownHivTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenUnknownHivTested(String startDate, String endDate)
			throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "inner join patient_program pg on pe.person_id = pg.patient_id "
							+ "where ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetResultForHIVTestConceptId())
							+ " AND (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and pe.gender ='f' and ob.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVResultDate = session
							.createSQLQuery("select cast(min(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (select cast(min(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
									+ patientId);

					List<Date> HivTestResultDate = queryHIVResultDate.list();

					if (HivTestResultDate.size() != 0) {

						if ((HivTestResultDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (HivTestResultDate.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}

					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	// ---------------------------------B. Maternity Data
	// Elements----------------------------------

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#expectedDeliveriesAmongHivPosWomen(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> expectedDeliveriesAmongHivPosWomen(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetCPNEncounterId())
							+ " and ob.concept_id = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetEstimatedDateOfConfinementConceptId())
							+ " and (cast(ob.value_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.value_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 "
							+ " and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMaternity = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join encounter enc on pg.patient_id = enc.patient_id "
									+ "where enc.encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and voided = 0 and pg.patient_id = "
									+ patientId);

					// Getting the size of the returned LIST which equals to the
					// COUNTS
					// needed
					List<Integer> patientIds3 = queryMaternity.list();

					if (patientIds3.size() == 0) {

						SQLQuery queryEstimatedDateOfDelivery = session
								.createSQLQuery("select cast(max(o.value_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetEstimatedDateOfConfinementConceptId())
										+ " and (select cast(max(o.value_datetime)as DATE)) is not null and o.voided= 0 and o.person_id= "
										+ patientId);
						List<Date> estimateDateOfDelivery = queryEstimatedDateOfDelivery
								.list();

						if (estimateDateOfDelivery.size() != 0) {

							if ((estimateDateOfDelivery.get(0).getTime() >= newStartDate
									.getTime())
									&& (estimateDateOfDelivery.get(0).getTime() <= newEndDate
											.getTime())) {

								SQLQuery query3 = session
										.createSQLQuery("select distinct pg.patient_id from patient_program pg "
												+ "inner join obs ob on pg.patient_id = ob.person_id "
												+ "inner join person pe on pg.patient_id = pe.person_id "
												+ "inner join patient pa on pg.patient_id = pa.patient_id "
												+ "where ob.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and (cast(ob.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and ob.value_coded IN ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (cast(pg.date_enrolled as DATE)) <= '"
												+ endDate
												+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
												+ "and pg.date_completed is null and pg.patient_id = "
												+ patientId);

								List<Integer> patientIds4 = query3.list();

								if (patientIds4.size() != 0) {

									SQLQuery queryHIVResultDate = session
											.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
													+ endDate
													+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided= 0 and o.person_id="
													+ patientId);
									List<Date> HivTestResultDate = queryHIVResultDate
											.list();

									if (HivTestResultDate.size() != 0)

									{

										SQLQuery queryHIVResultConcept = session
												.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetResultForHIVTestConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
														+ HivTestResultDate
																.get(0)
														+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
														+ patientId);
										List<Integer> HivTestResultConcept = queryHIVResultConcept
												.list();

										if (HivTestResultConcept.size() != 0) {

											if (HivTestResultConcept.get(0) == Integer
													.parseInt(GlobalProperties
															.gpGetPositiveAsResultToHIVTestConceptId())) {

												patientIdsList.add(patientId);
											}
										}

									}

								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#expectedDeliveriesFacilityThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> expectedDeliveriesFacilityThisMonth(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {
			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetCPNEncounterId())
							+ " and ob.concept_id = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetEstimatedDateOfConfinementConceptId())
							+ " and (cast(ob.value_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(ob.value_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 "
							+ " and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId()));

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided= 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMaternity = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join encounter enc on pg.patient_id = enc.patient_id "
									+ "where enc.encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and pg.voided = 0 and enc.voided = 0 and pg.patient_id = "
									+ patientId);

					// Getting the size of the returned LIST which equals to the
					// COUNTS
					// needed
					List<Integer> patientIds3 = queryMaternity.list();

					if (patientIds3.size() == 0) {

						SQLQuery queryEstimatedDateOfDelivery = session
								.createSQLQuery("select cast(max(o.value_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetEstimatedDateOfConfinementConceptId())
										+ " and (select cast(max(o.value_datetime)as DATE)) is not null and o.voided = 0 and o.person_id= "
										+ patientId);
						List<Date> estimateDateOfDelivery = queryEstimatedDateOfDelivery
								.list();

						if (estimateDateOfDelivery.size() != 0) {

							if ((estimateDateOfDelivery.get(0).getTime() >= newStartDate
									.getTime())
									&& (estimateDateOfDelivery.get(0).getTime() <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);
							}
						}

					}

				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#occuringDeliveriesFacilityThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> occuringDeliveriesFacilityThisMonth(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetMaternityEncounterId())
							+ " and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetDateOfConfinementConceptId())
							+ " and (cast(enc.encounter_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryEstimatedDateOfDelivery = session
							.createSQLQuery("select cast(encounter_datetime as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and (select cast(encounter_datetime as DATE)) is not null and voided = 0 and patient_id= "
									+ patientId);

					List<Date> dateOfDelivery = queryEstimatedDateOfDelivery
							.list();

					if (dateOfDelivery.size() != 0) {

						if ((dateOfDelivery.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfDelivery.get(0).getTime() <= newEndDate
										.getTime())) {
							patientIdsList.add(patientId);
						}

					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pregnantReceivedCompleteCourseThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> pregnantReceivedCompleteCourseThisMonth(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#reportedHivPosGivingBirthAtHome(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> reportedHivPosGivingBirthAtHome(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosAzt3tcNvpDuringLabor(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> womenHivPosAzt3tcNvpDuringLabor(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosGivingBirthAtFacility(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosGivingBirthAtFacility(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetMaternityEncounterId())
							+ " and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetDateOfConfinementConceptId())
							+ " and (cast(enc.encounter_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided=0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryEstimatedDateOfDelivery = session
							.createSQLQuery("select cast(encounter_datetime as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and (select cast(encounter_datetime as DATE)) is not null and voided = 0 and patient_id= "
									+ patientId);
					List<Date> dateOfDelivery = queryEstimatedDateOfDelivery
							.list();

					if (dateOfDelivery.size() != 0) {

						if ((dateOfDelivery.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfDelivery.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pg.patient_id from patient_program pg "
											+ "inner join obs ob on pg.patient_id = ob.person_id "
											+ "inner join person pe on pg.patient_id = pe.person_id "
											+ "inner join patient pa on pg.patient_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and (cast(ob.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and ob.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (cast(pg.date_enrolled as DATE)) <= '"
											+ endDate
											+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
											+ " and pg.patient_id = "
											+ patientId);

							List<Integer> patientIds4 = query3.list();

							if (patientIds4.size() != 0) {

								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
												+ endDate
												+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())) {

											patientIdsList.add(patientId);
										}

									}
								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenReceivingAzt3tcAfterDelivery(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> womenReceivingAzt3tcAfterDelivery(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenUnknownHivStatusTestedDuringLabor1(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenUnknownHivStatusTestedDuringLabor1(
			String startDate, String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "inner join patient pa on pe.person_id = pa.patient_id "
							+ "where ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVTestInDeliveryRoomConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "'"
							+ " and (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and ob.value_numeric = 1 ");

			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVTestInDeliveryRoom = session
							.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetHIVTestInDeliveryRoomConceptId())
									+ " and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.value_numeric = 1 and o.person_id= "
									+ patientId);
					List<Date> testInDeliveryRoomDate = queryHIVTestInDeliveryRoom
							.list();

					if (testInDeliveryRoomDate.size() != 0) {

						if ((testInDeliveryRoomDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (testInDeliveryRoomDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery queryHIVTest = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.obs_datetime < '"
											+ testInDeliveryRoomDate.get(0)
											+ "' and o.voided = 0 and o.person_id="
											+ patientId);

							List<Integer> patientIds3 = queryHIVTest.list();

							if (patientIds3.size() == 0) {

								patientIdsList.add(patientId);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Integer patientId : patientIdsList) {
			patients.add(Context.getPersonService().getPerson(patientId));
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenUnknownHivStatusTestedPosDuringLabor2(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenUnknownHivStatusTestedPosDuringLabor2(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery query = session
					.createSQLQuery("select distinct pe.person_id from person pe "
							+ "inner join obs ob on pe.person_id = ob.person_id "
							+ "inner join patient pa on pe.person_id = pa.patient_id "
							+ "where ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVTestInDeliveryRoomConceptId())
							+ " and (cast(ob.obs_datetime as DATE)) >= '"
							+ startDate
							+ "'"
							+ " and (cast(ob.obs_datetime as DATE)) <= '"
							+ endDate
							+ "' and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and ob.value_numeric = 1 ");

			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVTestInDeliveryRoom = session
							.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetHIVTestInDeliveryRoomConceptId())
									+ " and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.value_numeric = 1 and o.person_id= "
									+ patientId);
					List<Date> testInDeliveryRoomDate = queryHIVTestInDeliveryRoom
							.list();

					if (testInDeliveryRoomDate.size() != 0) {

						if ((testInDeliveryRoomDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (testInDeliveryRoomDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery queryHIVTest = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.obs_datetime < '"
											+ testInDeliveryRoomDate.get(0)
											+ "' and o.voided = 0 and o.person_id="
											+ patientId);

							List<Integer> patientIds3 = queryHIVTest.list();

							if (patientIds3.size() == 0) {

								SQLQuery query4 = session
										.createSQLQuery("select distinct pe.person_id from person pe "
												+ "inner join obs ob on pe.person_id = ob.person_id "
												+ "where ob.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and (cast(ob.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and ob.value_coded IN ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and pe.voided = 0 and ob.voided = 0 and pe.person_id = "
												+ patientId);

								List<Integer> patientIds4 = query4.list();

								if (patientIds4.size() != 0) {

									SQLQuery queryHIVResultDate = session
											.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
													+ endDate
													+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
													+ patientId);
									List<Date> HivTestResultDate = queryHIVResultDate
											.list();

									if (HivTestResultDate.size() != 0)

									{

										if ((HivTestResultDate.get(0).getTime() >= newStartDate
												.getTime())
												&& (HivTestResultDate.get(0)
														.getTime() <= newEndDate
														.getTime())) {

											SQLQuery queryHIVResultConcept = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetResultForHIVTestConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConcept = queryHIVResultConcept
													.list();

											if (HivTestResultConcept.size() != 0) {

												if (HivTestResultConcept.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													patientIdsList
															.add(patientId);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	// ---------------------------------C. HIV Exposed Infant
	// Follow-up----------------------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersAged6WeeksThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersAged6WeeksThisMonth(
			String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		// try {
		//
		// Session session = getSessionFactory2().getCurrentSession();
		// SQLQuery query = session
		// .createSQLQuery("select distinct rel.person_a from relationship rel "
		// + "inner join person pe on rel.person_a = pe.person_id "
		// + "where pe.gender = 'f' "
		// + " and rel.voided = 0 and pe.voided = 0 ");
		//
		// // Getting the size of the returned LIST which equals to the COUNTS
		// // needed
		// List<Integer> patientIds = query.list();
		//
		// for (Integer patientId : patientIds) {
		//
		// SQLQuery query2 = session
		// .createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetExitFromCareConceptId())
		// + " and o.person_id=" + patientId);
		//
		// List<Integer> patientIds2 = query2.list();
		//
		// if (patientIds2.size() == 0) {
		//
		// SQLQuery query3 = session
		// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
		// + "inner join obs ob on pg.patient_id = ob.person_id "
		// + "inner join person pe on pg.patient_id = pe.person_id "
		// + "inner join patient pa on pg.patient_id = pa.patient_id "
		// + "where ob.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and (cast(ob.obs_datetime as DATE)) <= '"
		// + endDate
		// + "' and ob.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (cast(pg.date_enrolled as DATE)) <= '"
		// + endDate
		// +
		// "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
		// + "and pg.date_completed is null and pg.patient_id = "
		// + patientId);
		//
		// List<Integer> patientIds3 = query3.list();
		//
		// if (patientIds3.size() != 0) {
		// SQLQuery queryHIVResultDate = session
		// .createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
		// + endDate
		// + "' and o.person_id="
		// + patientId);
		// List<Date> HivTestResultDate = queryHIVResultDate
		// .list();
		//
		// SQLQuery queryHIVResultConcept = session
		// .createSQLQuery("select o.value_coded from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) = '"
		// + HivTestResultDate.get(0)
		// + "' and o.person_id= " + patientId);
		// List<Integer> HivTestResultConcept = queryHIVResultConcept
		// .list();
		//
		// if (HivTestResultConcept.get(0) != null) {
		//
		// if (HivTestResultConcept.get(0) == Integer
		// .parseInt(GlobalProperties
		// .gpGetPositiveAsResultToHIVTestConceptId())) {
		//
		// SQLQuery infantHIVPositiveInPMTCT = session
		// .createSQLQuery("select distinct rel.person_b from relationship rel "
		// + "inner join person pe on rel.person_b = pe.person_id "
		// + "inner join encounter en on rel.person_b = en.patient_id "
		// + "inner join patient_program pg on rel.person_b = pg.patient_id "
		// + "where rel.person_a = "
		// + patientId
		// + " and pe.birthdate >= '"
		// + startDate
		// + "' and pe.birthdate <= '"
		// + startDate
		// + "' and SELECT DATEDIFF(('"
		// + endDate
		// + "'),pe.birthdate)/30 < 2 ");
		// List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
		// .list();
		//
		// if (infantHIVPositive.get(0) != null) {
		//
		// Person patient = Context.getPersonService()
		// .getPerson(patientId);
		// patients.add(patient);
		// }
		// }
		//
		// }
		// }
		// }
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersAged9MonthsThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersAged9MonthsThisMonth(
			String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		// try {
		//
		// Session session = getSessionFactory2().getCurrentSession();
		// SQLQuery query = session
		// .createSQLQuery("select distinct rel.person_a from relationship rel "
		// + "inner join person pe on rel.person_a = pe.person_id "
		// + "where pe.gender = 'f' "
		// + " and rel.voided = 0 and pe.voided = 0 ");
		//
		// // Getting the size of the returned LIST which equals to the COUNTS
		// // needed
		// List<Integer> patientIds = query.list();
		//
		// for (Integer patientId : patientIds) {
		//
		// SQLQuery query2 = session
		// .createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetExitFromCareConceptId())
		// + " and o.person_id=" + patientId);
		//
		// List<Integer> patientIds2 = query2.list();
		//
		// if (patientIds2.size() == 0) {
		//
		// SQLQuery query3 = session
		// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
		// + "inner join obs ob on pg.patient_id = ob.person_id "
		// + "inner join person pe on pg.patient_id = pe.person_id "
		// + "inner join patient pa on pg.patient_id = pa.patient_id "
		// + "where ob.concept_id = "
		// + Integer.parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and (cast(ob.obs_datetime as DATE)) <= '"
		// + endDate
		// + "' and ob.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (cast(pg.date_enrolled as DATE)) <= '"
		// + endDate
		// +
		// "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
		// + "and pg.date_completed is null and pg.patient_id = "
		// + patientId);
		//
		// List<Integer> patientIds3 = query3.list();
		//
		// if (patientIds3.size() != 0) {
		// SQLQuery queryHIVResultDate = session
		// .createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
		// + endDate
		// + "' and o.person_id="
		// + patientId);
		// List<Date> HivTestResultDate = queryHIVResultDate
		// .list();
		//
		// SQLQuery queryHIVResultConcept = session
		// .createSQLQuery("select o.value_coded from obs o where o.concept_id = "
		// + Integer
		// .parseInt(GlobalProperties
		// .gpGetResultForHIVTestConceptId())
		// + " and o.value_coded IN ("
		// + GlobalProperties
		// .gpGetListOfAnswersToResultOfHIVTestAsIntegers()
		// + ") and (select cast(max(o.obs_datetime)as DATE)) = '"
		// + HivTestResultDate.get(0)
		// + "' and o.person_id= " + patientId);
		// List<Integer> HivTestResultConcept = queryHIVResultConcept
		// .list();
		//
		// if (HivTestResultConcept.get(0) != null) {
		//
		// if (HivTestResultConcept.get(0) == Integer
		// .parseInt(GlobalProperties
		// .gpGetPositiveAsResultToHIVTestConceptId())) {
		//
		// SQLQuery infantHIVPositiveInPMTCT = session
		// .createSQLQuery("select distinct rel.person_b from relationship rel "
		// + "inner join person pe on rel.person_b = pe.person_id "
		// + "inner join encounter en on rel.person_b = en.patient_id "
		// + "inner join patient_program pg on rel.person_b = pg.patient_id "
		// + "where rel.person_a = "
		// + patientId
		// + " and pe.birthdate >= '"
		// + startDate
		// + "' and pe.birthdate <= '"
		// + startDate
		// + "' and SELECT DATEDIFF(('"
		// + endDate
		// + "'),pe.birthdate)/30 = 9 ");
		// List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
		// .list();
		//
		// if (infantHIVPositive.get(0) != null) {
		//
		// Person patient = Context.getPersonService()
		// .getPerson(patientId);
		// patients.add(patient);
		// }
		// }
		//
		// }
		// }
		// }
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersAgedAt18MonthsThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersAgedAt18MonthsThisMonth(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersCotrimoAt6Weeks(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersCotrimoAt6Weeks(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersEnrolledPmtct(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersEnrolledPmtct(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ "inner join person pe on rel.person_b = pe.person_id "
													+ "inner join patient_program pg on rel.person_b = pg.patient_id "
													+ "where rel.person_a = "
													+ patientId
													+ " and pg.program_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())

													+ " and (cast(pg.date_enrolled as DATE)) >= '"
													+ startDate
													+ "' and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate + "' ");
									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() != 0) {

												SQLQuery queryExposedInfantInPMTCT = session
														.createSQLQuery("select (cast(pg.date_enrolled as DATE)) from patient_program pg"
																+ " where pg.patient_id = "
																+ patientIdsInfant
																+ " and (select (cast(pg.date_enrolled as DATE))) is not null and pg.voided = 0 and pg.program_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId()));

												List<Date> exposedInfantInPMTCT = queryExposedInfantInPMTCT
														.list();

												if ((exposedInfantInPMTCT
														.get(0).getTime() >= newStartDate
														.getTime())
														&& (exposedInfantInPMTCT
																.get(0)
																.getTime() <= newEndDate
																.getTime())) {

													patientIdsList
															.add(infantHIVPositive
																	.get(0));
												}
											}
										}

									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersLostFollowup(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersLostFollowup(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and pe.voided = 0 and en.voided= 0 and rel.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery queryDate1 = session
														.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
																+ "(select(cast(max(encounter_datetime)as Date))) <= '"
																+ endDate
																+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
																+ patientIdsInfant);

												List<Date> maxEnocunterDateTime = queryDate1
														.list();

												SQLQuery queryDate2 = session
														.createSQLQuery("select cast(max(value_datetime) as DATE ) "
																+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
																+ endDate
																+ "' and concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetReturnVisitDateConceptId())
																+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
																+ patientIdsInfant);

												List<Date> maxReturnVisitDay = queryDate2
														.list();

												if (((maxReturnVisitDay.get(0)) != null)
														&& (maxEnocunterDateTime
																.get(0) != null)) {

													if (((maxEnocunterDateTime
															.get(0).getTime()) >= threeMonthsBeforeEndDate
															.getTime() && (maxEnocunterDateTime
															.get(0).getTime()) <= newEndDate
															.getTime())
															|| ((maxReturnVisitDay
																	.get(0)
																	.getTime()) >= threeMonthsBeforeEndDate
																	.getTime() && (maxReturnVisitDay
																	.get(0)
																	.getTime()) <= newEndDate
																	.getTime())) {

														patientsNotLostToFollowUp
																.add(patientId);

													}

													else {

														patientIdsList
																.add(patientIdsInfant);
													}
												}

												else if (((maxReturnVisitDay
														.get(0)) == null)
														&& (maxEnocunterDateTime
																.get(0) != null)) {

													if (((maxEnocunterDateTime
															.get(0).getTime()) >= threeMonthsBeforeEndDate
															.getTime() && (maxEnocunterDateTime
															.get(0).getTime()) <= newEndDate
															.getTime())) {

														patientsNotLostToFollowUp
																.add(patientId);

													}

													else {
														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}

								}
							}
							if (HivTestResultConcept.get(0) == Integer
									.parseInt(GlobalProperties
											.gpGetNegativeAsResultToHIVTestConceptId())) {

								SQLQuery queryTestingStatusOfPartner = session
										.createSQLQuery("select distinct pg.patient_id from patient_program pg "
												+ "inner join obs ob on pg.patient_id = ob.person_id "
												+ "inner join person pe on pg.patient_id = pe.person_id "
												+ "inner join patient pa on pg.patient_id = pa.patient_id "
												+ "where ob.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetTestingStatusOfPartnerConceptId())
												+ " and (cast(ob.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and ob.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (cast(pg.date_enrolled as DATE)) <= '"
												+ endDate
												+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
												+ "and pg.date_completed is null and pg.patient_id = "
												+ patientId);

								List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
										.list();

								if (patientIdsTestingStatusOfPartner.size() != 0) {

									SQLQuery queryHIVResultDateTestingStatusOfPartner = session
											.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
													+ endDate
													+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
													+ patientId);
									List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
											.list();

									if (HivTestResultDateHIVResultDateTestingStatusOfPartner
											.size() != 0) {

										SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
												.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
														+ HivTestResultDate
																.get(0)
														+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
														+ patientId);
										List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
												.list();

										if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
												.size() != 0) {

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.get(0) == Integer
													.parseInt(GlobalProperties
															.gpGetPositiveAsResultToHIVTestConceptId())) {

												SQLQuery infantHIVPositiveInPMTCT = session
														.createSQLQuery("select distinct rel.person_b from relationship rel "
																+ " inner join person pe on rel.person_b = pe.person_id "
																+ " inner join encounter en on rel.person_b = en.patient_id "
																+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																+ " where rel.person_a = "
																+ patientId
																+ " and pg.program_id ="
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId())
																+ " and rel.voided = 0 and rel.person_a = "
																+ patientId);

												List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
														.list();

												if (infantHIVPositive.size() != 0) {

													for (Integer patientIdsInfant : infantHIVPositive) {

														SQLQuery queryInfantExited = session
																.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																		+ Integer
																				.parseInt(GlobalProperties
																						.gpGetExitFromCareConceptId())
																		+ " and o.voided = 0 and o.person_id="
																		+ patientIdsInfant);

														List<Integer> patientIds2InfantExited = queryInfantExited
																.list();

														if (patientIds2InfantExited
																.size() == 0) {

															SQLQuery queryDate1 = session
																	.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
																			+ "(select(cast(max(encounter_datetime)as Date))) <= '"
																			+ endDate
																			+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
																			+ patientIdsInfant);

															List<Date> maxEnocunterDateTime = queryDate1
																	.list();

															SQLQuery queryDate2 = session
																	.createSQLQuery("select cast(max(value_datetime) as DATE ) "
																			+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
																			+ endDate
																			+ "' and concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetReturnVisitDateConceptId())
																			+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
																			+ patientIdsInfant);

															List<Date> maxReturnVisitDay = queryDate2
																	.list();

															if (((maxReturnVisitDay
																	.get(0)) != null)
																	&& (maxEnocunterDateTime
																			.get(0) != null)) {

																if (((maxEnocunterDateTime
																		.get(0)
																		.getTime()) >= threeMonthsBeforeEndDate
																		.getTime() && (maxEnocunterDateTime
																		.get(0)
																		.getTime()) <= newEndDate
																		.getTime())
																		|| ((maxReturnVisitDay
																				.get(0)
																				.getTime()) >= threeMonthsBeforeEndDate
																				.getTime() && (maxReturnVisitDay
																				.get(0)
																				.getTime()) <= newEndDate
																				.getTime())) {

																	patientsNotLostToFollowUp
																			.add(patientId);

																}

																else {
																	patientIdsList
																			.add(patientIdsInfant);
																}
															}

															else if ((maxReturnVisitDay
																	.size() == 0)
																	&& (maxEnocunterDateTime
																			.get(0) != null)) {

																if (((maxEnocunterDateTime
																		.get(0)
																		.getTime()) >= threeMonthsBeforeEndDate
																		.getTime() && (maxEnocunterDateTime
																		.get(0)
																		.getTime()) <= newEndDate
																		.getTime())) {

																	patientsNotLostToFollowUp
																			.add(patientId);

																}

																else {
																	patientIdsList
																			.add(patientIdsInfant);
																}
															}
														}
													}

												}

											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersMalnourished(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersMalnourished(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersScreenedTbThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersScreenedTbThisMonth(
			String startDate, String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided= 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery queryInfantTestedPCRPosAt6Weeks = session
														.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																+ "inner join obs ob on pg.patient_id = ob.person_id "
																+ "inner join person pe on pg.patient_id = pe.person_id "
																+ "inner join patient pa on pg.patient_id = pa.patient_id "
																+ "where ob.concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetTBScreeningConceptId())
																+ " and (cast(ob.obs_datetime as DATE)) >= '"
																+ startDate
																+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																+ endDate
																+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																+ "and pg.date_completed is null and pg.patient_id = "
																+ patientIdsInfant);

												List<Integer> patientIdsQueryInfantTestedPCRPosAt6Weeks = queryInfantTestedPCRPosAt6Weeks
														.list();

												if (patientIdsQueryInfantTestedPCRPosAt6Weeks
														.size() != 0) {

													SQLQuery queryHIVResultDateForInfantTested = session
															.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetTBScreeningConceptId())
																	+ " and (select cast(max(o.obs_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																	+ patientIdsInfant);

													List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
															.list();

													if ((HIVResultDateForInfantTested
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (HIVResultDateForInfantTested
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided= 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and rel.voided = 0 and pe.voided =0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery queryInfantTestedPCRPosAt6Weeks = session
																		.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																				+ "inner join obs ob on pg.patient_id = ob.person_id "
																				+ "inner join person pe on pg.patient_id = pe.person_id "
																				+ "inner join patient pa on pg.patient_id = pa.patient_id "
																				+ "where ob.concept_id = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetTBScreeningConceptId())
																				+ " and (cast(ob.obs_datetime as DATE)) >= '"
																				+ startDate
																				+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																				+ endDate
																				+ "' and (cast(pg.date_enrolled as DATE)) <= '"
																				+ endDate
																				+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																				+ "and pg.date_completed is null and pg.patient_id = "
																				+ patientIdsInfant);

																List<Integer> patientIdsQueryInfantTestedPCRPosAt6Weeks = queryInfantTestedPCRPosAt6Weeks
																		.list();

																if (patientIdsQueryInfantTestedPCRPosAt6Weeks
																		.size() != 0) {

																	SQLQuery queryHIVResultDateForInfantTested = session
																			.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																					+ Integer
																							.parseInt(GlobalProperties
																									.gpGetTBScreeningConceptId())
																					+ " and (select cast(max(o.obs_datetime)as DATE)) <= '"
																					+ endDate
																					+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																					+ patientIdsInfant);

																	List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
																			.list();

																	if ((HIVResultDateForInfantTested
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (HIVResultDateForInfantTested
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		patientIdsList
																				.add(patientIdsInfant);
																	}
																}
															}
														}
													}
												}

											}

										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedAt18Months(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedAt18Months(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetSerologyAt18MonthId())
													+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
													+ startDate
													+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery QueryInfantInPMTCTTestedAt18Months = session
														.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																+ " where pg.program_id ="
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId())
																+ " and en.encounter_type = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetSerologyAt18MonthId())
																+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided= 0 and pg.voided = 0 and en.patient_id = "
																+ patientIdsInfant);

												List<Date> infantInPMTCTTestedAt18Months = QueryInfantInPMTCTTestedAt18Months
														.list();

												if (infantInPMTCTTestedAt18Months
														.size() != 0) {

													if ((infantInPMTCTTestedAt18Months
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (infantInPMTCTTestedAt18Months
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetSerologyAt9MonthId())
																	+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
																	+ startDate
																	+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery QueryInfantInPMTCTTestedAt18Months = session
																		.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																				+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																				+ " where pg.program_id ="
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetPMTCTProgramId())
																				+ " and en.encounter_type = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetSerologyAt18MonthId())
																				+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided = 0 and pg.voided = 0 and en.patient_id = "
																				+ patientIdsInfant);

																List<Date> infantInPMTCTTestedAt18Months = QueryInfantInPMTCTTestedAt18Months
																		.list();

																if (infantInPMTCTTestedAt18Months
																		.size() != 0) {

																	if ((infantInPMTCTTestedAt18Months
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (infantInPMTCTTestedAt18Months
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		patientIdsList
																				.add(patientIdsInfant);
																	}

																}
															}
														}
													}
												}
											}

										}
									}
								}

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedAt6Weeks(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedAt6Weeks(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPCREncounterId())
													+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
													+ startDate
													+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery QueryInfantInPMTCTTestedAt6Weeks = session
														.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																+ " where pg.program_id ="
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId())
																+ " and en.encounter_type = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPCREncounterId())
																+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided = 0 and en.patient_id = "
																+ patientIdsInfant);

												List<Date> infantInPMTCTTestedAt6Weeks = QueryInfantInPMTCTTestedAt6Weeks
														.list();

												if (infantInPMTCTTestedAt6Weeks
														.size() != 0) {

													if ((infantInPMTCTTestedAt6Weeks
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (infantInPMTCTTestedAt6Weeks
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPCREncounterId())
																	+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
																	+ startDate
																	+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery QueryInfantInPMTCTTestedAt6Weeks = session
																		.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																				+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																				+ " where pg.program_id ="
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetPMTCTProgramId())
																				+ " and en.encounter_type = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetPCREncounterId())
																				+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided = 0 and pg.voided = 0 and en.patient_id = "
																				+ patientIdsInfant);

																List<Date> infantInPMTCTTestedAt6Weeks = QueryInfantInPMTCTTestedAt6Weeks
																		.list();

																if (infantInPMTCTTestedAt6Weeks
																		.size() != 0) {

																	if ((infantInPMTCTTestedAt6Weeks
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (infantInPMTCTTestedAt6Weeks
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		patientIdsList
																				.add(patientIdsInfant);
																	}

																}
															}
														}
													}
												}
											}

										}
									}
								}

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedAt9Months(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedAt9Months(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetSerologyAt9MonthId())
													+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
													+ startDate
													+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery QueryInfantInPMTCTTestedAt9Months = session
														.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																+ " where pg.program_id ="
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId())
																+ " and en.encounter_type = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetSerologyAt9MonthId())
																+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided = 0 and pg.voided = 0 and en.patient_id = "
																+ patientIdsInfant);

												List<Date> infantInPMTCTTestedAt9Months = QueryInfantInPMTCTTestedAt9Months
														.list();

												if (infantInPMTCTTestedAt9Months
														.size() != 0) {

													if ((infantInPMTCTTestedAt9Months
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (infantInPMTCTTestedAt9Months
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetSerologyAt9MonthId())
																	+ " and (select cast(min(en.encounter_datetime)as DATE)) >= '"
																	+ startDate
																	+ "' and (select cast(min(en.encounter_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery QueryInfantInPMTCTTestedAt9Months = session
																		.createSQLQuery("select (cast(min(en.encounter_datetime)as DATE)) from encounter en "
																				+ " inner join patient_program pg on en.patient_id = pg.patient_id "
																				+ " where pg.program_id ="
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetPMTCTProgramId())
																				+ " and en.encounter_type = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetSerologyAt9MonthId())
																				+ " and (select (cast(min(en.encounter_datetime)as DATE))) is not null and en.voided = 0 and pg.voided = 0 and en.patient_id = "
																				+ patientIdsInfant);

																List<Date> infantInPMTCTTestedAt9Months = QueryInfantInPMTCTTestedAt9Months
																		.list();

																if (infantInPMTCTTestedAt9Months
																		.size() != 0) {

																	if ((infantInPMTCTTestedAt9Months
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (infantInPMTCTTestedAt9Months
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		patientIdsList
																				.add(patientIdsInfant);
																	}

																}
															}
														}
													}
												}
											}

										}
									}
								}

							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedPosAt18Months(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedPosAt18Months(
			String startDate, String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetSerologyAt18MonthId())
													+ " and (select cast(en.encounter_datetime as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery queryInfantTestedPCRPosAt18Months = session
														.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																+ "inner join obs ob on pg.patient_id = ob.person_id "
																+ "inner join person pe on pg.patient_id = pe.person_id "
																+ "inner join patient pa on pg.patient_id = pa.patient_id "
																+ "where ob.concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetResultForHIVTestConceptId())
																+ " and (cast(ob.obs_datetime as DATE)) >= '"
																+ startDate
																+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																+ endDate
																+ "' and ob.value_coded in ("
																+ GlobalProperties
																		.gpGetListOfAnswersToResultOfHIVTest()
																+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																+ endDate
																+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																+ "and pg.date_completed is null and pg.patient_id = "
																+ patientIdsInfant);

												List<Integer> patientIdsQueryInfantTestedPCRPosAt18Months = queryInfantTestedPCRPosAt18Months
														.list();

												if (patientIdsQueryInfantTestedPCRPosAt18Months
														.size() != 0) {

													SQLQuery queryHIVResultDateForInfantTested = session
															.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetResultForHIVTestConceptId())
																	+ " and o.value_coded in ("
																	+ GlobalProperties
																			.gpGetListOfAnswersToResultOfHIVTest()
																	+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																	+ patientIdsInfant);

													List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
															.list();

													if ((HIVResultDateForInfantTested
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (HIVResultDateForInfantTested
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														SQLQuery queryHIVResultConceptForInfantTested = session
																.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																		+ Integer
																				.parseInt(GlobalProperties
																						.gpGetResultForHIVTestConceptId())
																		+ " and o.value_coded in ("
																		+ GlobalProperties
																				.gpGetListOfAnswersToResultOfHIVTest()
																		+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																		+ HivTestResultDate
																				.get(0)
																		+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																		+ patientIdsInfant);
														List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																.list();

														if (HIVResultConceptForInfantTested
																.size() != 0) {

															if (HIVResultConceptForInfantTested
																	.get(0) == Integer
																	.parseInt(GlobalProperties
																			.gpGetPositiveAsResultToHIVTestConceptId())) {

																patientIdsList
																		.add(patientIdsInfant);
															}
														}
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetSerologyAt9MonthId())
																	+ " and (select cast(en.encounter_datetime as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery queryInfantTestedPCRPosAt9Months = session
																		.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																				+ "inner join obs ob on pg.patient_id = ob.person_id "
																				+ "inner join person pe on pg.patient_id = pe.person_id "
																				+ "inner join patient pa on pg.patient_id = pa.patient_id "
																				+ "where ob.concept_id = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetResultForHIVTestConceptId())
																				+ " and (cast(ob.obs_datetime as DATE)) >= '"
																				+ startDate
																				+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																				+ endDate
																				+ "' and ob.value_coded in ("
																				+ GlobalProperties
																						.gpGetListOfAnswersToResultOfHIVTest()
																				+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																				+ endDate
																				+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																				+ "and pg.date_completed is null and pg.patient_id = "
																				+ patientIdsInfant);

																List<Integer> patientIdsQueryInfantTestedPCRPosAt9Months = queryInfantTestedPCRPosAt9Months
																		.list();

																if (patientIdsQueryInfantTestedPCRPosAt9Months
																		.size() != 0) {

																	SQLQuery queryHIVResultDateForInfantTested = session
																			.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																					+ Integer
																							.parseInt(GlobalProperties
																									.gpGetResultForHIVTestConceptId())
																					+ " and o.value_coded in ("
																					+ GlobalProperties
																							.gpGetListOfAnswersToResultOfHIVTest()
																					+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																					+ endDate
																					+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																					+ patientIdsInfant);

																	List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
																			.list();

																	if ((HIVResultDateForInfantTested
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (HIVResultDateForInfantTested
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		SQLQuery queryHIVResultConceptForInfantTested = session
																				.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																						+ Integer
																								.parseInt(GlobalProperties
																										.gpGetResultForHIVTestConceptId())
																						+ " and o.value_coded in ("
																						+ GlobalProperties
																								.gpGetListOfAnswersToResultOfHIVTest()
																						+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																						+ HivTestResultDate
																								.get(0)
																						+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																						+ patientIdsInfant);
																		List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																				.list();

																		if (HIVResultConceptForInfantTested
																				.size() != 0) {

																			if (HIVResultConceptForInfantTested
																					.get(0) == Integer
																					.parseInt(GlobalProperties
																							.gpGetPositiveAsResultToHIVTestConceptId())) {

																				patientIdsList
																						.add(patientIdsInfant);
																			}
																		}
																	}
																}
															}
														}
													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedPosAt6Weeks(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedPosAt6Weeks(String startDate,
			String endDate) throws ParseException {

		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPCREncounterId())
													+ " and (select cast(en.encounter_datetime as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided  = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery queryInfantTestedPCRPosAt6Weeks = session
														.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																+ "inner join obs ob on pg.patient_id = ob.person_id "
																+ "inner join person pe on pg.patient_id = pe.person_id "
																+ "inner join patient pa on pg.patient_id = pa.patient_id "
																+ "where ob.concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetResultForHIVTestConceptId())
																+ " and (cast(ob.obs_datetime as DATE)) >= '"
																+ startDate
																+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																+ endDate
																+ "' and ob.value_coded in ("
																+ GlobalProperties
																		.gpGetListOfAnswersToResultOfHIVTest()
																+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																+ endDate
																+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																+ "and pg.date_completed is null and pg.patient_id = "
																+ patientIdsInfant);

												List<Integer> patientIdsQueryInfantTestedPCRPosAt6Weeks = queryInfantTestedPCRPosAt6Weeks
														.list();

												if (patientIdsQueryInfantTestedPCRPosAt6Weeks
														.size() != 0) {

													SQLQuery queryHIVResultDateForInfantTested = session
															.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetResultForHIVTestConceptId())
																	+ " and o.value_coded in ("
																	+ GlobalProperties
																			.gpGetListOfAnswersToResultOfHIVTest()
																	+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																	+ patientIdsInfant);

													List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
															.list();

													if ((HIVResultDateForInfantTested
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (HIVResultDateForInfantTested
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														SQLQuery queryHIVResultConceptForInfantTested = session
																.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																		+ Integer
																				.parseInt(GlobalProperties
																						.gpGetResultForHIVTestConceptId())
																		+ " and o.value_coded in ("
																		+ GlobalProperties
																				.gpGetListOfAnswersToResultOfHIVTest()
																		+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																		+ HivTestResultDate
																				.get(0)
																		+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																		+ patientIdsInfant);
														List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																.list();

														if (HIVResultConceptForInfantTested
																.size() != 0) {

															if (HIVResultConceptForInfantTested
																	.get(0) == Integer
																	.parseInt(GlobalProperties
																			.gpGetPositiveAsResultToHIVTestConceptId())) {

																patientIdsList
																		.add(patientIdsInfant);
															}
														}
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPCREncounterId())
																	+ " and (select cast(en.encounter_datetime as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery queryInfantTestedPCRPosAt6Weeks = session
																		.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																				+ "inner join obs ob on pg.patient_id = ob.person_id "
																				+ "inner join person pe on pg.patient_id = pe.person_id "
																				+ "inner join patient pa on pg.patient_id = pa.patient_id "
																				+ "where ob.concept_id = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetResultForHIVTestConceptId())
																				+ " and (cast(ob.obs_datetime as DATE)) >= '"
																				+ startDate
																				+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																				+ endDate
																				+ "' and ob.value_coded in ("
																				+ GlobalProperties
																						.gpGetListOfAnswersToResultOfHIVTest()
																				+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																				+ endDate
																				+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																				+ "and pg.date_completed is null and pg.patient_id = "
																				+ patientIdsInfant);

																List<Integer> patientIdsQueryInfantTestedPCRPosAt6Weeks = queryInfantTestedPCRPosAt6Weeks
																		.list();

																if (patientIdsQueryInfantTestedPCRPosAt6Weeks
																		.size() != 0) {

																	SQLQuery queryHIVResultDateForInfantTested = session
																			.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																					+ Integer
																							.parseInt(GlobalProperties
																									.gpGetResultForHIVTestConceptId())
																					+ " and o.value_coded in ("
																					+ GlobalProperties
																							.gpGetListOfAnswersToResultOfHIVTest()
																					+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																					+ endDate
																					+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																					+ patientIdsInfant);

																	List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
																			.list();

																	if ((HIVResultDateForInfantTested
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (HIVResultDateForInfantTested
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		SQLQuery queryHIVResultConceptForInfantTested = session
																				.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																						+ Integer
																								.parseInt(GlobalProperties
																										.gpGetResultForHIVTestConceptId())
																						+ " and o.value_coded in ("
																						+ GlobalProperties
																								.gpGetListOfAnswersToResultOfHIVTest()
																						+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																						+ HivTestResultDate
																								.get(0)
																						+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																						+ patientIdsInfant);

																		List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																				.list();

																		if (HIVResultConceptForInfantTested
																				.size() != 0) {

																			if (HIVResultConceptForInfantTested
																					.get(0) == Integer
																					.parseInt(GlobalProperties
																							.gpGetPositiveAsResultToHIVTestConceptId())) {

																				patientIdsList
																						.add(patientIdsInfant);
																			}
																		}
																	}
																}
															}
														}
													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTestedPosAt9Months(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivPosMothersTestedPosAt9Months(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and en.encounter_type = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetSerologyAt9MonthId())
													+ " and (select cast(en.encounter_datetime as DATE)) <= '"
													+ endDate
													+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantExited = session
													.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and o.voided = 0 and o.person_id="
															+ patientIdsInfant);

											List<Integer> patientIds2InfantExited = queryInfantExited
													.list();

											if (patientIds2InfantExited.size() == 0) {

												SQLQuery queryInfantTestedPCRPosAt9Months = session
														.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																+ "inner join obs ob on pg.patient_id = ob.person_id "
																+ "inner join person pe on pg.patient_id = pe.person_id "
																+ "inner join patient pa on pg.patient_id = pa.patient_id "
																+ "where ob.concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetResultForHIVTestConceptId())
																+ " and (cast(ob.obs_datetime as DATE)) >= '"
																+ startDate
																+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																+ endDate
																+ "' and ob.value_coded in ("
																+ GlobalProperties
																		.gpGetListOfAnswersToResultOfHIVTest()
																+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																+ endDate
																+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																+ "and pg.date_completed is null and pg.patient_id = "
																+ patientIdsInfant);

												List<Integer> patientIdsQueryInfantTestedPCRPosAt9Months = queryInfantTestedPCRPosAt9Months
														.list();

												if (patientIdsQueryInfantTestedPCRPosAt9Months
														.size() != 0) {

													SQLQuery queryHIVResultDateForInfantTested = session
															.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetResultForHIVTestConceptId())
																	+ " and o.value_coded in ("
																	+ GlobalProperties
																			.gpGetListOfAnswersToResultOfHIVTest()
																	+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																	+ endDate
																	+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																	+ patientIdsInfant);

													List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
															.list();

													if ((HIVResultDateForInfantTested
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (HIVResultDateForInfantTested
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														SQLQuery queryHIVResultConceptForInfantTested = session
																.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																		+ Integer
																				.parseInt(GlobalProperties
																						.gpGetResultForHIVTestConceptId())
																		+ " and o.value_coded in ("
																		+ GlobalProperties
																				.gpGetListOfAnswersToResultOfHIVTest()
																		+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																		+ HivTestResultDate
																				.get(0)
																		+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																		+ patientIdsInfant);
														List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																.list();

														if (HIVResultConceptForInfantTested
																.size() != 0) {

															if (HIVResultConceptForInfantTested
																	.get(0) == Integer
																	.parseInt(GlobalProperties
																			.gpGetPositiveAsResultToHIVTestConceptId())) {

																patientIdsList
																		.add(patientIdsInfant);
															}
														}
													}
												}
											}
										}
									}
								}

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ "and pg.date_completed is null and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ " inner join person pe on rel.person_b = pe.person_id "
																	+ " inner join encounter en on rel.person_b = en.patient_id "
																	+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ " where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id ="
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())
																	+ " and en.encounter_type = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetSerologyAt9MonthId())
																	+ " and (select cast(en.encounter_datetime as DATE)) <= '"
																	+ endDate
																	+ "' and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																	+ patientId);

													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery queryInfantTestedPCRPosAt9Months = session
																		.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																				+ "inner join obs ob on pg.patient_id = ob.person_id "
																				+ "inner join person pe on pg.patient_id = pe.person_id "
																				+ "inner join patient pa on pg.patient_id = pa.patient_id "
																				+ "where ob.concept_id = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetResultForHIVTestConceptId())
																				+ " and (cast(ob.obs_datetime as DATE)) >= '"
																				+ startDate
																				+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																				+ endDate
																				+ "' and ob.value_coded in ("
																				+ GlobalProperties
																						.gpGetListOfAnswersToResultOfHIVTest()
																				+ ") and (cast(pg.date_enrolled as DATE)) <= '"
																				+ endDate
																				+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																				+ "and pg.date_completed is null and pg.patient_id = "
																				+ patientIdsInfant);

																List<Integer> patientIdsQueryInfantTestedPCRPosAt9Months = queryInfantTestedPCRPosAt9Months
																		.list();

																if (patientIdsQueryInfantTestedPCRPosAt9Months
																		.size() != 0) {

																	SQLQuery queryHIVResultDateForInfantTested = session
																			.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																					+ Integer
																							.parseInt(GlobalProperties
																									.gpGetResultForHIVTestConceptId())
																					+ " and o.value_coded in ("
																					+ GlobalProperties
																							.gpGetListOfAnswersToResultOfHIVTest()
																					+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
																					+ endDate
																					+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
																					+ patientIdsInfant);

																	List<Date> HIVResultDateForInfantTested = queryHIVResultDateForInfantTested
																			.list();

																	if ((HIVResultDateForInfantTested
																			.get(
																					0)
																			.getTime() >= newStartDate
																			.getTime())
																			&& (HIVResultDateForInfantTested
																					.get(
																							0)
																					.getTime() <= newEndDate
																					.getTime())) {

																		SQLQuery queryHIVResultConceptForInfantTested = session
																				.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
																						+ Integer
																								.parseInt(GlobalProperties
																										.gpGetResultForHIVTestConceptId())
																						+ " and o.value_coded in ("
																						+ GlobalProperties
																								.gpGetListOfAnswersToResultOfHIVTest()
																						+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
																						+ HivTestResultDate
																								.get(0)
																						+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
																						+ patientIdsInfant);
																		List<Integer> HIVResultConceptForInfantTested = queryHIVResultConceptForInfantTested
																				.list();

																		if (HIVResultConceptForInfantTested
																				.size() != 0) {

																			if (HIVResultConceptForInfantTested
																					.get(0) == Integer
																					.parseInt(GlobalProperties
																							.gpGetPositiveAsResultToHIVTestConceptId())) {

																				patientIdsList
																						.add(patientIdsInfant);
																			}
																		}
																	}
																}
															}
														}
													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#infantHivPosMothersTherapFood(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> infantHivPosMothersTherapFood(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newInfantHivPosMothersNvpAztAtBirth(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> newInfantHivPosMothersNvpAztAtBirth(String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#reportedDeadInfantHivPosMothers(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> reportedDeadInfantHivPosMothers(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery infantHIVPositiveInPMTCT = session
											.createSQLQuery("select distinct rel.person_b from relationship rel "
													+ " inner join person pe on rel.person_b = pe.person_id "
													+ " inner join encounter en on rel.person_b = en.patient_id "
													+ " inner join patient_program pg on rel.person_b = pg.patient_id "
													+ " where rel.person_a = "
													+ patientId
													+ " and pg.program_id ="
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetPMTCTProgramId())
													+ " and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
													+ patientId);

									List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
											.list();

									if (infantHIVPositive.size() != 0) {

										for (Integer patientIdsInfant : infantHIVPositive) {

											SQLQuery queryInfantDied = session
													.createSQLQuery("select distinct pg.patient_id from patient_program pg "
															+ "inner join obs ob on pg.patient_id = ob.person_id "
															+ "inner join person pe on pg.patient_id = pe.person_id "
															+ "inner join patient pa on pg.patient_id = pa.patient_id "
															+ "where ob.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetExitFromCareConceptId())
															+ " and (cast(ob.obs_datetime as DATE)) >= '"
															+ startDate
															+ "' and (cast(ob.obs_datetime as DATE)) <= '"
															+ endDate
															+ "' and ob.value_coded = "
															+ GlobalProperties
																	.gpGetExitFromCareDiedConceptId()
															+ " and (cast(pg.date_enrolled as DATE)) <= '"
															+ endDate
															+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
															+ " and pg.patient_id = "
															+ patientIdsInfant);

											List<Integer> patientIdsQueryInfantDied = queryInfantDied
													.list();

											if (patientIdsQueryInfantDied
													.size() != 0) {

												SQLQuery queryHIVResultDateForInfantDied = session
														.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetExitFromCareConceptId())
																+ " and o.value_coded = "
																+ GlobalProperties
																		.gpGetExitFromCareDiedConceptId()
																+ " and (select cast(max(o.obs_datetime)as DATE)) <= '"
																+ endDate
																+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided  = 0 and o.person_id="
																+ patientIdsInfant);

												List<Date> HIVResultDateForInfantDied = queryHIVResultDateForInfantDied
														.list();

												if (HIVResultDateForInfantDied
														.size() != 0) {

													if ((HIVResultDateForInfantDied
															.get(0).getTime() >= newStartDate
															.getTime())
															&& (HIVResultDateForInfantDied
																	.get(0)
																	.getTime() <= newEndDate
																	.getTime())) {

														patientIdsList
																.add(patientIdsInfant);
													}
												}
											}
										}
									}
								}
							}

							if (HivTestResultConcept.get(0) == Integer
									.parseInt(GlobalProperties
											.gpGetNegativeAsResultToHIVTestConceptId())) {

								SQLQuery queryTestingStatusOfPartner = session
										.createSQLQuery("select distinct pg.patient_id from patient_program pg "
												+ "inner join obs ob on pg.patient_id = ob.person_id "
												+ "inner join person pe on pg.patient_id = pe.person_id "
												+ "inner join patient pa on pg.patient_id = pa.patient_id "
												+ "where ob.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetTestingStatusOfPartnerConceptId())
												+ " and (cast(ob.obs_datetime as DATE)) <= '"
												+ endDate
												+ "' and ob.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (cast(pg.date_enrolled as DATE)) <= '"
												+ endDate
												+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
												+ "and pg.date_completed is null and pg.patient_id = "
												+ patientId);

								List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
										.list();

								if (patientIdsTestingStatusOfPartner.size() != 0) {

									SQLQuery queryHIVResultDateTestingStatusOfPartner = session
											.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
													+ endDate
													+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
													+ patientId);
									List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
											.list();

									if (HivTestResultDateHIVResultDateTestingStatusOfPartner
											.size() != 0) {

										SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
												.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
														+ HivTestResultDate
																.get(0)
														+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
														+ patientId);
										List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
												.list();

										if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
												.size() != 0) {

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.get(0) == Integer
													.parseInt(GlobalProperties
															.gpGetPositiveAsResultToHIVTestConceptId())) {

												SQLQuery infantHIVPositiveInPMTCT = session
														.createSQLQuery("select distinct rel.person_b from relationship rel "
																+ " inner join person pe on rel.person_b = pe.person_id "
																+ " inner join encounter en on rel.person_b = en.patient_id "
																+ " inner join patient_program pg on rel.person_b = pg.patient_id "
																+ " where rel.person_a = "
																+ patientId
																+ " and pg.program_id ="
																+ Integer
																		.parseInt(GlobalProperties
																				.gpGetPMTCTProgramId())
																+ " and rel.voided = 0 and pe.voided = 0 and en.voided = 0 and pg.voided = 0 and rel.person_a = "
																+ patientId);

												List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
														.list();

												if (infantHIVPositive.size() != 0) {

													for (Integer patientIdsInfant : infantHIVPositive) {

														SQLQuery queryInfantDied = session
																.createSQLQuery("select distinct pg.patient_id from patient_program pg "
																		+ "inner join obs ob on pg.patient_id = ob.person_id "
																		+ "inner join person pe on pg.patient_id = pe.person_id "
																		+ "inner join patient pa on pg.patient_id = pa.patient_id "
																		+ "where ob.concept_id = "
																		+ Integer
																				.parseInt(GlobalProperties
																						.gpGetExitFromCareConceptId())
																		+ " and (cast(ob.obs_datetime as DATE)) >= '"
																		+ startDate
																		+ "' and (cast(ob.obs_datetime as DATE)) <= '"
																		+ endDate
																		+ "' and ob.value_coded = "
																		+ GlobalProperties
																				.gpGetExitFromCareDiedConceptId()
																		+ " and (cast(pg.date_enrolled as DATE)) <= '"
																		+ endDate
																		+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
																		+ " and pg.patient_id = "
																		+ patientIdsInfant);

														List<Integer> patientIdsQueryInfantDied = queryInfantDied
																.list();

														if (patientIdsQueryInfantDied
																.size() != 0) {

															SQLQuery queryHIVResultDateForInfantDied = session
																	.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.value_coded = "
																			+ GlobalProperties
																					.gpGetExitFromCareDiedConceptId()
																			+ " and (select cast(max(o.obs_datetime)as DATE)) <= '"
																			+ endDate
																			+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.value and o.person_id="
																			+ patientIdsInfant);

															List<Date> HIVResultDateForInfantDied = queryHIVResultDateForInfantDied
																	.list();

															if ((HIVResultDateForInfantDied
																	.get(0)
																	.getTime() >= newStartDate
																	.getTime())
																	&& (HIVResultDateForInfantDied
																			.get(
																					0)
																			.getTime() <= newEndDate
																			.getTime())) {

																patientIdsList
																		.add(patientIdsInfant);

															}
														}
													}
												}
											}
										}

									}

								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosBreastFeeding(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosBreastFeeding(String startDate,
			String endDate) throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {
			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetMaternityEncounterId())
							+ " and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetDateOfConfinementConceptId())
							+ " and (cast(enc.encounter_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryEstimatedDateOfDelivery = session
							.createSQLQuery("select cast(encounter_datetime as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and (select cast(encounter_datetime as DATE)) is not null and voided = 0 and patient_id= "
									+ patientId);
					List<Date> dateOfDelivery = queryEstimatedDateOfDelivery
							.list();

					if (dateOfDelivery.size() != 0) {

						if ((dateOfDelivery.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfDelivery.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pg.patient_id from patient_program pg "
											+ "inner join obs ob on pg.patient_id = ob.person_id "
											+ "inner join person pe on pg.patient_id = pe.person_id "
											+ "inner join patient pa on pg.patient_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and (cast(ob.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and ob.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (cast(pg.date_enrolled as DATE)) <= '"
											+ endDate
											+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
											+ "and pg.date_completed is null and pg.patient_id = "
											+ patientId);

							List<Integer> patientIds4 = query3.list();

							if (patientIds4.size() != 0) {

								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
												+ endDate
												+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())) {

											SQLQuery queryHIVPosWomenBreastFeeding = session
													.createSQLQuery("select distinct pg.patient_id from patient_program pg "
															+ "inner join obs ob on pg.patient_id = ob.person_id "
															+ "inner join person pe on pg.patient_id = pe.person_id "
															+ "inner join patient pa on pg.patient_id = pa.patient_id "
															+ "where ob.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetInfantFeedingMethodConceptId())
															+ " and (cast(ob.obs_datetime as DATE)) >= '"
															+ startDate
															+ "' and (cast(ob.obs_datetime as DATE)) <= '"
															+ endDate
															+ "' and ob.value_coded IN ("
															+ GlobalProperties
																	.gpGetBreastedPredominatelyConceptIdConceptId()
															+ ") and (cast(pg.date_enrolled as DATE)) <= '"
															+ endDate
															+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
															+ "and pg.date_completed is null and pg.patient_id = "
															+ patientId);

											List<Integer> patientIdsOfHIVPosWomenBreastFeeding = queryHIVPosWomenBreastFeeding
													.list();

											if (patientIdsOfHIVPosWomenBreastFeeding
													.size() != 0)

											{

												patientIdsList.add(patientId);

											}
										}

									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosUsingFormula(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosUsingFormula(String startDate, String endDate)
			throws ParseException {
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where enc.encounter_type= "
							+ Integer.parseInt(GlobalProperties
									.gpGetMaternityEncounterId())
							+ " and ob.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetDateOfConfinementConceptId())
							+ " and (cast(enc.encounter_datetime as DATE)) >= '"
							+ startDate
							+ "' AND (cast(enc.encounter_datetime as DATE)) <= '"
							+ endDate
							+ "' and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
							+ " and enc.voided=0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {
				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryEstimatedDateOfDelivery = session
							.createSQLQuery("select cast(encounter_datetime as DATE) from encounter where encounter_type = "
									+ Integer.parseInt(GlobalProperties
											.gpGetMaternityEncounterId())
									+ " and (select cast(encounter_datetime as DATE)) is not null and voided = 0 and patient_id= "
									+ patientId);
					List<Date> dateOfDelivery = queryEstimatedDateOfDelivery
							.list();

					if (dateOfDelivery.size() != 0) {

						if ((dateOfDelivery.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfDelivery.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery query3 = session
									.createSQLQuery("select distinct pg.patient_id from patient_program pg "
											+ "inner join obs ob on pg.patient_id = ob.person_id "
											+ "inner join person pe on pg.patient_id = pe.person_id "
											+ "inner join patient pa on pg.patient_id = pa.patient_id "
											+ "where ob.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and (cast(ob.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and ob.value_coded IN ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (cast(pg.date_enrolled as DATE)) <= '"
											+ endDate
											+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
											+ "and pg.date_completed is null and pg.patient_id = "
											+ patientId);

							List<Integer> patientIds4 = query3.list();

							if (patientIds4.size() != 0) {

								SQLQuery queryHIVResultDate = session
										.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetResultForHIVTestConceptId())
												+ " and o.value_coded in ("
												+ GlobalProperties
														.gpGetListOfAnswersToResultOfHIVTest()
												+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
												+ endDate
												+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
												+ patientId);
								List<Date> HivTestResultDate = queryHIVResultDate
										.list();

								if (HivTestResultDate.size() != 0)

								{

									SQLQuery queryHIVResultConcept = session
											.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetResultForHIVTestConceptId())
													+ " and o.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
													+ HivTestResultDate.get(0)
													+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
													+ patientId);
									List<Integer> HivTestResultConcept = queryHIVResultConcept
											.list();

									if (HivTestResultConcept.size() != 0) {

										if (HivTestResultConcept.get(0) == Integer
												.parseInt(GlobalProperties
														.gpGetPositiveAsResultToHIVTestConceptId())) {

											SQLQuery queryHIVPosWomenBreastFeeding = session
													.createSQLQuery("select distinct pg.patient_id from patient_program pg "
															+ "inner join obs ob on pg.patient_id = ob.person_id "
															+ "inner join person pe on pg.patient_id = pe.person_id "
															+ "inner join patient pa on pg.patient_id = pa.patient_id "
															+ "where ob.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetInfantFeedingMethodConceptId())
															+ " and (cast(ob.obs_datetime as DATE)) >= '"
															+ startDate
															+ "' and (cast(ob.obs_datetime as DATE)) <= '"
															+ endDate
															+ "' and ob.value_coded IN ("
															+ GlobalProperties
																	.gpGetBreastedPredominatelyConceptIdConceptId()
															+ ") and (cast(pg.date_enrolled as DATE)) <= '"
															+ endDate
															+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
															+ "and pg.date_completed is null and pg.patient_id = "
															+ patientId);

											List<Integer> patientIdsOfHIVPosWomenBreastFeeding = queryHIVPosWomenBreastFeeding
													.list();

											if (patientIdsOfHIVPosWomenBreastFeeding
													.size() != 0)

											{

												patientIdsList.add(patientId);

											}
										}

									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	// ---------------------------------D. Family Planning Data
	// Elements--------------------------------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosExpectedFpAtFacility(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosExpectedFpAtFacility(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleWithHIVPosIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F' and  o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personExpectedAtFacilityIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs ob on p.person_id=ob.person_id where ob.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetEstimatedDateOfCOnfinementId())
								+ "' AND (cast(ob.value_datetime as DATE)) >= '"
								+ startDate
								+ "'"
								+ " AND (cast(ob.value_datetime as DATE)) <= '"
								+ endDate + "' ").list();
		List<Integer> personInFamilyPlanningIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs ob on p.person_id=ob.person_id where ob.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetMethodOfFamilyPlanningId())
								+ "' ").list();
		List<Integer> patientInEncounterIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id ")
				.list();

		for (Integer personFemaleWithHIVPosId : personFemaleWithHIVPosIds) {
			for (Integer personExpectedAtFacilityId : personExpectedAtFacilityIds) {
				if (personExpectedAtFacilityId.equals(personFemaleWithHIVPosId))
					for (Integer personInFamilyPlanningId : personInFamilyPlanningIds) {
						for (Integer patientInEncounterId : patientInEncounterIds) {

							if (personExpectedAtFacilityId
									.equals(patientInEncounterId)
									&& personExpectedAtFacilityId
											.equals(personFemaleWithHIVPosId)
									&& personExpectedAtFacilityId
											.equals(personInFamilyPlanningId)) {

								Person person = Context.getPersonService()
										.getPerson(personExpectedAtFacilityId);
								persons.add(person);
							}
						}
					}
			}
		}
		return persons;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosSeenInFp(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosSeenInFp(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleWithHIVPosIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F' and  o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPMTCTProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> patientInEncounterIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id ")
				.list();
		List<Integer> personInFamilyPlanningIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs ob on p.person_id=ob.person_id where ob.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetMethodOfFamilyPlanningId())
								+ "' AND (cast(ob.obs_datetime as DATE)) >= '"
								+ startDate
								+ "'"
								+ " AND (cast(ob.obs_datetime as DATE)) <= '"
								+ endDate + "' ").list();

		for (Integer personFemaleWithHIVPosId : personFemaleWithHIVPosIds) {
			for (int patientInProgramId : patientInProgramIds) {
				if (personFemaleWithHIVPosId.equals(patientInProgramId))

					for (Integer personInFamilyPlanningId : personInFamilyPlanningIds) {
						for (int patientInEncounterId : patientInEncounterIds) {

							if (personInFamilyPlanningId
									.equals(personFemaleWithHIVPosId)
									&& personInFamilyPlanningId
											.equals(patientInProgramId)
									&& personInFamilyPlanningId
											.equals(patientInEncounterId)) {
								Person person = Context.getPersonService()
										.getPerson(personInFamilyPlanningId);
								persons.add(person);
							}
						}
					}
			}
		}
		return persons;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosPartnersSeenInFp(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosPartnersSeenInFp(String startDate,
			String endDate) {

		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personFemaleWithHIVPosIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F' and  o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "' ").list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPMTCTProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personInFamilyPlanningIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs ob on p.person_id=ob.person_id where ob.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetMethodOfFamilyPlanningId())
								+ "' AND (cast(ob.obs_datetime as DATE)) >= '"
								+ startDate
								+ "'"
								+ " AND (cast(ob.obs_datetime as DATE)) <= '"
								+ endDate + "' ").list();
		List<Integer> partnerInFamilyPlanningIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetTestingStatusOfPartnerId())
								+ "' and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate + "' ").list();
		List<Integer> patientEncounterIds = session
				.createSQLQuery(
						"select distinct pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id")
				.list();

		for (Integer personFemaleWithHIVPosId : personFemaleWithHIVPosIds) {
			for (Integer personInProgramId : patientInProgramIds) {
				if (personInProgramId.equals(personFemaleWithHIVPosId))
					for (Integer personInFamilyPlanningId : personInFamilyPlanningIds) {
						for (Integer partnerInFamilyPlanningId : partnerInFamilyPlanningIds) {
							if (personInFamilyPlanningId
									.equals(partnerInFamilyPlanningId))
								for (Integer patientEncounterId : patientEncounterIds) {

									if (personInProgramId
											.equals(personFemaleWithHIVPosId)
											&& personInProgramId
													.equals(patientEncounterId)
											&& personInProgramId
													.equals(personInFamilyPlanningId)
											&& personInProgramId
													.equals(partnerInFamilyPlanningId)) {

										Person person = Context
												.getPersonService().getPerson(
														personInProgramId);
										persons.add(person);
									}
								}
						}
					}
			}
		}
		return persons;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosReceivingModernContraceptive(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosReceivingModernContraceptive(
			String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personFemaleWithHIVPosIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F' and  o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "' and p.voided=false").list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPMTCTProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate
								+ "' AND '"
								+ endDate
								+ "' AND pp.date_completed is null ").list();
		List<Integer> personInFamilyPlanningIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetMethodOfFamilyPlanningId())
								+ "' and o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetInjectableContraceptivesId())
								+ "' or o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetOralContraceptionId())
								+ "' or o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetCondomsId())
								+ "' AND (cast(o.obs_datetime as DATE)) >= '"
								+ startDate
								+ "'"
								+ " AND (cast(o.obs_datetime as DATE)) <= '"
								+ endDate + "' ").list();
		List<Integer> patientEncounterIds = session
				.createSQLQuery(
						"select distinct pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id")
				.list();

		for (Integer personFemaleWithHIVPosId : personFemaleWithHIVPosIds) {
			for (Integer patientInProgramId : patientInProgramIds)
				if (patientInProgramId.equals(personFemaleWithHIVPosId))
					for (Integer personInFamilyPlanningId : personInFamilyPlanningIds) {
						for (Integer patientEncounterId : patientEncounterIds) {

							if (patientInProgramId
									.equals(personFemaleWithHIVPosId)
									&& patientInProgramId
											.equals(patientEncounterId)
									&& patientInProgramId
											.equals(personInFamilyPlanningId)) {
								Person person = Context.getPersonService()
										.getPerson(patientInProgramId);
								persons.add(person);
							}
						}
					}
		}
		return persons;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#womenHivPosRefferedForFp(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> womenHivPosRefferedForFp(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personFemaleWithHIVPosIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F' and  o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "' and p.voided=false ").list();
		List<Integer> personReferredForFpIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetDispositionId())
								+ "' and o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetReferredForFamilyPlanningId())
								+ "' AND (cast(o.obs_datetime as DATE)) >= '"
								+ startDate
								+ "'"
								+ " AND (cast(o.obs_datetime as DATE)) <= '"
								+ endDate + "' ").list();
		List<Integer> patientEncounterIds = session
				.createSQLQuery(
						"select distinct pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id")
				.list();
		for (Integer personFemaleWithHIVPosId : personFemaleWithHIVPosIds) {
			for (Integer personReferredForFp : personReferredForFpIds) {
				if (personReferredForFp.equals(personFemaleWithHIVPosId))
					for (Integer patientEncounterId : patientEncounterIds)
						if (personReferredForFp
								.equals(personFemaleWithHIVPosId)
								&& personReferredForFp
										.equals(patientEncounterId)) {
							Person person = Context.getPersonService()
									.getPerson(personReferredForFp);
							persons.add(person);
						}
			}
		}
		return persons;
	}

	// ---------------------------------E. Submit VCT Data
	// Elements----------------------------------------------

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#couplesCounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> couplesCounseledTested(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personCoupledIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling=2 and p.voided=false and t.date_registration between '"
								+ startDate
								+ "' AND '"
								+ endDate
								+ "' AND p.gender='M' and p.voided = 0 and t.voided = 0 ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0 ")
				.list();

		for (Integer personCoupledId : personCoupledIds) {
			for (Integer personTestedId : personTested) {
				if (personTestedId.equals(personCoupledId)) {
					Person person = Context.getPersonService().getPerson(
							personTestedId);
					persons.add(person);
				}
			}
		}

		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#discordantCouples2(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> discordantCouples2(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> coupleCounseledTestedIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id inner join trac_sample_test ts on t.code_test=ts.sample_code where ts.voided=0 and t.vct_or_pit=0 and t.type_of_counseling=2 and p.voided=false and t.partner_id is not null ")
				.list();

		List<Integer> discordantCoupleMalePartnerIds = session
				.createSQLQuery(
						"select distinct t.client_id from trac_vct_client t inner join obs o on t.client_id-o.person_id where o.concept_id='2169' and o.value_coded='664' ")
				.list();
		List<Integer> discordantCoupleMaleInfectedIds = session
				.createSQLQuery(
						"Select distinct t.partner_id from trac_vct_client t inner join obs o on t.client_id=o.person_id where o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='703' ").list();

		// List<Integer>discordantCoupleFemalePartnerInfectedIds=session.createSQLQuery("select distinct t.client_id from trac_vct_client t inner join obs o on t.client_id-o.person_id where o.concept_id='2169' and o.value_coded='703' ").list();
		// List<Integer>discordantCoupleFemaleInfectedIds=session.createSQLQuery("Select distinct t.partner_id from trac_vct_client t inner join obs o on t.client_id=o.person_id where o.concept_id='2169' and o.value_coded='664' ").list();

		for (Integer coupleCounseledTestedId : coupleCounseledTestedIds) {
			for (Integer discordantCoupleMalePartnerId : discordantCoupleMalePartnerIds) {
				for (Integer discordantCoupleMaleInfectedId : discordantCoupleMaleInfectedIds) {
					if (coupleCounseledTestedId
							.equals(discordantCoupleMalePartnerId)
							&& coupleCounseledTestedId
									.equals(discordantCoupleMaleInfectedId))

					// for (Integer
					// discordantCoupleFemalePartnerInfectedId:discordantCoupleFemalePartnerInfectedIds){
					// for(Integer
					// discordantCoupleFemaleInfectedId:discordantCoupleFemaleInfectedIds)
					// {

					{

						Person person = Context.getPersonService().getPerson(
								coupleCounseledTestedId);
						persons.add(person);

					}
				}
			}
		}
		// }
		// }

		return persons;

		/*
		 * / SELECT distinct person_id from person p inner join trac_vct_client
		 * t on p.person_id=t.client_id inner join trac_sample_test ts on
		 * t.code_test=ts.sample_code where ts.voided=0 and t.vct_or_pit=0 and
		 * t.type_of_counseling=2 and p.voided=false and t.partner_id is not
		 * null
		 * 
		 * select distinct t.client_id from trac_vct_client t inner join obs o
		 * on t.client_id-o.person_id where o.concept_id='2169' and
		 * o.value_coded='664'Select distinct t.partner_id from trac_vct_client
		 * t inner join obs o on t.client_id=o.person_id where
		 * o.concept_id='2169' and o.value_coded='703'
		 * 
		 * select distinct t.client_id from trac_vct_client t inner join obs o
		 * on t.client_id-o.person_id where o.concept_id='2169' and
		 * o.value_coded='703'Select distinct t.partner_id from trac_vct_client
		 * t inner join obs o on t.client_id=o.person_id where
		 * o.concept_id='2169' and o.value_coded='664'
		 */

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleHivPosMoreThan25(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleHivPosMoreThan25(String startDate, String endDate) {

		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleHivPosUnder15to24(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleHivPosUnder15to24(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of hiv Positive female clients (age <15)
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleHivPosUnderFifteen(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleHivPosUnderFifteen(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleHivPosMoreThan25(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleHivPosMoreThan25(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleHivPosUnder15to24(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleHivPosUnder15to24(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of HIV Positive men (age<15)
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleHivPosUnderFifteen(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleHivPosUnderFifteen(String startDate, String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients(age 15-24) tested and received result
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemale15To24TestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemale15To24TestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTestedAndReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personFemaleId)) {
					for (Integer personTestedAndReceivedResultId : personTestedAndReceivedResultIds) {
						if (personTestedAndReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personTestedAndReceivedResultId
										.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedAndReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleFifteenTo24CounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleFifteenTo24CounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();
		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new Female clients (age 25+) counseled and tested for HIV
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleMore25CounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleMore25CounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.dead = FALSE AND pe.gender = 'F' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleMore25TestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleMore25TestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestsedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledAndTestsedId : personCounseledAndTestsedIds) {
				if (personCounseledAndTestsedId.equals(personFemaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestsedId)
								&& personReceivedResultId
										.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new Females clients (age <15 years) counselled and tested for
	 * HIV
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleUnderFifteenCounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleUnderFifteenCounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleUnderFifteenTestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newFemaleUnderFifteenTestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male clients(age 15-24) tested and received result
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMale15To24TestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMale15To24TestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='M'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTestedAndReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personTestedAndReceivedResultId : personTestedAndReceivedResultIds) {
						if (personTestedAndReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personTestedAndReceivedResultId
										.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedAndReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male clients (ages 15-24) counseled and tested for HIV
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleFifteenTo24CounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleFifteenTo24CounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='M'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male clients (ages 25 +)counseled and tested for HIV
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleMore25CounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> newMaleMore25CounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleMore25TestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> newMaleMore25TestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestsedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestsedId : personCounseledAndTestsedIds) {
				if (personCounseledAndTestsedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestsedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male clients (age <15) counseled and tested for HIV
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleUnderFifteenCounseledTested(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleUnderFifteenCounseledTested(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleUnderFifteenTestReceiveRes(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleUnderFifteenTestReceiveRes(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=0 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	// ---------------------------------F. Provider-Initiated Testing (PIT) Data
	// Elements------------------------------------

	/**
	 * Number of new female clients (age 15-24) counseled and tested for HIV
	 * through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#female15To24CounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> female15To24CounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female client (age 15-24) tested HIV positive through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#female15To24HivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> female15To24HivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female client (age 15-24) who received HIV results through
	 * PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#female15To24HivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> female15To24HivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='F'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTestedAndReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personFemaleId)) {
					for (Integer personTestedAndReceivedResultId : personTestedAndReceivedResultIds) {
						if (personTestedAndReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personTestedAndReceivedResultId
										.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedAndReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients (age >25) counseled and tested through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThan25CounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleMoreThan25CounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThan25HivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleMoreThan25HivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients(age 25+) who received HIV results through
	 * PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThan25HivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleMoreThan25HivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestsedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledAndTestsedId : personCounseledAndTestsedIds) {
				if (personCounseledAndTestsedId.equals(personFemaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestsedId)
								&& personReceivedResultId
										.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients(age <15) counseled and tested for HIV
	 * through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleUnderFifteenCounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleUnderFifteenCounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', p.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND p.voided = 0 and o.voided = 0 AND p.gender = 'F' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients (age <15) tested HIV positive through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleUnderFifteenHivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleUnderFifteenHivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new female clients(age <15) who received HIV results through
	 * PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleUnderFifteenHivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> femaleUnderFifteenHivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personFemaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'F' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0 ")
				.list();

		for (Integer personFemaleId : personFemaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personFemaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personFemaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#male15To24CounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> male15To24CounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='M'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male clients(age 15-24) tested HIV positive through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#male15To24HivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> male15To24HivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',pe.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#male15To24HivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> male15To24HivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.gender='M'and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " and DATEDIFF('"
								+ endDate
								+ "',p.birthdate) <= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFourYearsId())
								+ " and p.voided= 0 and o.voided = 0 and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 AND t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTestedAndReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personTestedAndReceivedResultId : personTestedAndReceivedResultIds) {
						if (personTestedAndReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personTestedAndReceivedResultId
										.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedAndReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new males clients (age 25+)counseled and tested for HIV through
	 * PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThan25CounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleMoreThan25CounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * Number of new male client(age 15-24)tested HIV positive through PIT
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThan25HivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleMoreThan25HivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThan25HivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleMoreThan25HivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) >= "
								+ Integer.parseInt(GlobalProperties
										.gpGetTwentyFiveYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestsedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestsedId : personCounseledAndTestsedIds) {
				if (personCounseledAndTestsedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestsedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleUnderFifteenCounseledThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleUnderFifteenCounseledThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', p.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND p.voided = 0 and o.voided = 0 AND p.gender = 'M' ")
				.list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct person_id from person p inner join trac_vct_client t on p.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and p.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleUnderFifteenHivPosThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleUnderFifteenHivPosThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseledAndTestedIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0  and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personReceivedResultIds = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledAndTestedId : personCounseledAndTestedIds) {
				if (personCounseledAndTestedId.equals(personMaleId)) {
					for (Integer personReceivedResultId : personReceivedResultIds) {
						if (personReceivedResultId
								.equals(personCounseledAndTestedId)
								&& personReceivedResultId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personReceivedResultId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleUnderFifteenHivResThroughPit(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> maleUnderFifteenHivResThroughPit(String startDate,
			String endDate) {
		List<Person> persons = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personMaleIds = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join obs o on pe.person_id=o.person_id WHERE DATEDIFF('"
								+ endDate
								+ "', pe.birthdate) < "
								+ Integer.parseInt(GlobalProperties
										.gpGetFifteenYearsId())
								+ " AND pe.voided = 0 and o.voided = 0 AND pe.gender = 'M' and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'  ").list();
		List<Integer> personCounseled = session
				.createSQLQuery(
						"SELECT distinct pe.person_id from person pe inner join trac_vct_client t on pe.person_id=t.client_id where t.vct_or_pit=1 and t.type_of_counseling is not null and pe.voided = 0 and t.voided = 0 and t.date_registration between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personTested = session
				.createSQLQuery(
						"SELECT distinct t.client_id from trac_vct_client t where t.code_test is not null and t.voided=0 ")
				.list();

		for (Integer personMaleId : personMaleIds) {
			for (Integer personCounseledId : personCounseled) {
				if (personCounseledId.equals(personMaleId)) {
					for (Integer personTestedId : personTested) {
						if (personTestedId.equals(personCounseledId)
								&& personTestedId.equals(personMaleId)) {

							Person person = Context.getPersonService()
									.getPerson(personTestedId);
							persons.add(person);
						}
					}
				}
			}
		}
		return persons;

	}

	// ---------------------------------G. PEP Data
	// Elements------------------------------------

	/**
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#
	 *      (java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivOccupExpo3MonthAfterPep(String startDate,
			String endDate) {
		List<Person> patients = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();

		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> personEnrolledInPepPRogramAtRiskOfOcupationExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personOnEncountersWithIds = session
				.createSQLQuery(
						"SELECT  DISTINCT enc.patient_id FROM encounter enc inner join obs ob on ob.person_id=enc.patient_id where ob.concept_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "'").list();
		List<Integer> personAtRiskExposedIds = session
				.createSQLQuery(
						"SELECT DISTINCT p.patient_id from patient p inner join obs o on p.patient_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetExposureToBloodOrBloodProductId())
								+ "' ").list();
		List<Integer> personOnArvDrugIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join orders ord on pat.patient_id=ord.patient_id where ord.concept_id IN "
								+ ConstantValues.LIST_OF_PROPHYLAXIS_DRUGS
								+ "  AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false AND ord.discontinued_reason is null")
				.list();

		for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
			for (Integer personEnrolledInPepPRogramAtRiskOfOcupationExposureId : personEnrolledInPepPRogramAtRiskOfOcupationExposureIds) {
				if (personEnrolledInPepId
						.equals(personEnrolledInPepPRogramAtRiskOfOcupationExposureId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personOnEncountersWithId : personOnEncountersWithIds) {
							if (patientInProgramId
									.equals(personOnEncountersWithId))
								for (Integer personAtRiskExposedId : personAtRiskExposedIds) {
									for (Integer personOnArvDrugId : personOnArvDrugIds) {
										if ((personEnrolledInPepId
												.equals(personEnrolledInPepPRogramAtRiskOfOcupationExposureId) && personEnrolledInPepId
												.equals(patientInProgramId))
												&& personEnrolledInPepId
														.equals(personOnEncountersWithId)
												&& personEnrolledInPepId
														.equals(personAtRiskExposedId)
												&& personEnrolledInPepId
														.equals(personOnArvDrugId)) {

											Person person = Context
													.getPersonService()
													.getPerson(
															personEnrolledInPepId);
											patients.add(person);
										}
									}
								}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivOccupationExposure(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivOccupationExposure(String startDate,
			String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfOcupationExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> patientInEncounterIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetExposureToBloodOrBloodProductId())
								+ "' ").list();

		for (Integer personEnrolledInPepPRogramAtRiskOfOcupationExposureId : personEnrolledInPepPRogramAtRiskOfOcupationExposureIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfOcupationExposureId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							for (Integer patientInEncounterId : patientInEncounterIds) {

								if ((personEnrolledInPepPRogramAtRiskOfOcupationExposureId
										.equals(personEnrolledInPepId)
										&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(patientInEncounterId) && personEnrolledInPepPRogramAtRiskOfOcupationExposureId
										.equals(patientInProgramId))
										&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(personAtRiskOnPEPId)) {

									Person person = Context
											.getPersonService()
											.getPerson(
													personEnrolledInPepPRogramAtRiskOfOcupationExposureId);
									patients.add(person);
								}
							}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * occupational exposure who received PEP
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivOccupationExposurePep(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivOccupationExposurePep(String startDate,
			String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfOcupationExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> personOnEncountersWithIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp inner join encounter enc on pp.patient_id=enc.patient_id")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT DISTINCT p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetExposureToBloodOrBloodProductId())
								+ "' ").list();
		List<Integer> personOnArvDrug = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join orders ord on pat.patient_id=ord.patient_id where ord.concept_id IN "
								+ ConstantValues.LIST_OF_PROPHYLAXIS_DRUGS
								+ "  AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false AND ord.discontinued_reason is null")
				.list();

		for (Integer personEnrolledInPepPRogramAtRiskOfOcupationExposureId : personEnrolledInPepPRogramAtRiskOfOcupationExposureIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfOcupationExposureId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							if (patientInProgramId.equals(personAtRiskOnPEPId))
								for (Integer personOnArvDrugId : personOnArvDrug) {
									for (Integer personOnEncountersWithId : personOnEncountersWithIds) {
										if ((personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(personEnrolledInPepId) && personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(patientInProgramId))
												&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
														.equals(personAtRiskOnPEPId)
												&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
														.equals(personOnArvDrugId)
												&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
														.equals(personOnEncountersWithId)) {

											Person person = Context
													.getPersonService()
													.getPerson(
															personEnrolledInPepPRogramAtRiskOfOcupationExposureId);
											patients.add(person);
										}
									}
								}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivOtherNoneOccupExpo3MonthAfterPep(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Person> newAtRiskHivOtherNoneOccupExpo3MonthAfterPep(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivOtherNoneOccupationExposure(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivOtherNoneOccupationExposure(
			String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();
		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfNonOcupationExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> patientInEncounterIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetSexualContactWithHivPositivePatient())
								+ "' ").list();

		for (Integer personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId : personEnrolledInPepPRogramAtRiskOfNonOcupationExposureIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							for (Integer patientInEncounterId : patientInEncounterIds) {

								if ((personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId
										.equals(personEnrolledInPepId)
										&& personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId
												.equals(patientInEncounterId) && personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId
										.equals(patientInProgramId))
										&& personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId
												.equals(personAtRiskOnPEPId)) {

									Person person = Context
											.getPersonService()
											.getPerson(
													personEnrolledInPepPRogramAtRiskOfNonOcupationExposureId);
									patients.add(person);
								}
							}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * Number of new clients at risk of HIV infection as a result of other
	 * non-occupational exposure who received PEP
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivOtherNoneOccupationExposurePep(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivOtherNoneOccupationExposurePep(
			String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> personOnEncountersWithIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp inner join encounter enc on pp.patient_id=enc.patient_id")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT DISTINCT p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetSexualContactWithHivPositivePatient())
								+ "' ").list();
		List<Integer> personOnArvDrug = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join orders ord on pat.patient_id=ord.patient_id where ord.concept_id IN "
								+ ConstantValues.LIST_OF_PROPHYLAXIS_DRUGS
								+ "  AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false AND ord.discontinued_reason is null")
				.list();

		for (Integer personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId : personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							if (patientInProgramId.equals(personAtRiskOnPEPId))
								for (Integer personOnArvDrugId : personOnArvDrug) {
									for (Integer personOnEncountersWithId : personOnEncountersWithIds) {
										if ((personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
												.equals(personEnrolledInPepId) && personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
												.equals(patientInProgramId))
												&& personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
														.equals(personAtRiskOnPEPId)
												&& personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
														.equals(personOnArvDrugId)
												&& personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId
														.equals(personOnEncountersWithId)) {

											Person person = Context
													.getPersonService()
													.getPerson(
															personEnrolledInPepPRogramAtRiskOfNonOcuppationalExposureId);
											patients.add(person);
										}
									}
								}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * Number of new clients at risk of HIV infection as a result of rape/
	 * sexual assault
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivRapeAssault(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivRapeAssault(String startDate, String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfOcupationExposureIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> patientInEncounterIds = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join encounter enc on pat.patient_id=enc.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT distinct p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetSexualAssaultId()) + "' ").list();

		for (Integer personEnrolledInPepPRogramAtRiskOfOcupationExposureId : personEnrolledInPepPRogramAtRiskOfOcupationExposureIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfOcupationExposureId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							for (Integer patientInEncounterId : patientInEncounterIds) {

								if ((personEnrolledInPepPRogramAtRiskOfOcupationExposureId
										.equals(personEnrolledInPepId)
										&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(patientInEncounterId) && personEnrolledInPepPRogramAtRiskOfOcupationExposureId
										.equals(patientInProgramId))
										&& personEnrolledInPepPRogramAtRiskOfOcupationExposureId
												.equals(personAtRiskOnPEPId)) {

									Person person = Context
											.getPersonService()
											.getPerson(
													personEnrolledInPepPRogramAtRiskOfOcupationExposureId);
									patients.add(person);
								}
							}
						}
					}
			}
		}
		return patients;

	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivRapeAssault3MonthAfterPep(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivRapeAssault3MonthAfterPep(String startDate,
			String endDate) {
		ArrayList<Person> persons = new ArrayList<Person>();
		/*
		 * Date myDate=null; SimpleDateFormat date=new
		 * SimpleDateFormat("yyyy-MM-dd"); try { myDate=date.parse(startDate);
		 * 
		 * Calendar cal=Calendar.getInstance(); if(myDate!=null)
		 * cal.setTime(myDate); cal.add(Calendar.MONTH, -3); Date
		 * beforeThreeMonthOfStartDate=cal.getTime();
		 */

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer.parseInt(GlobalProperties
										.gpGetResultForHIVTestConceptId())
								+ "' ").list();
		List<Integer> personOnArvDrug = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join orders ord on pat.patient_id=ord.patient_id where ord.concept_id = "
								+ ConstantValues.EFAVIRENZ
								+ "  AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false AND ord.discontinued_reason is null ")
				.list();

		for (Integer patientInProgramId : patientInProgramIds) {
			for (Integer personOnArvDrugId : personOnArvDrug) {
				if (patientInProgramId.equals(personOnArvDrugId)) {
					Person person = Context.getPersonService().getPerson(
							patientInProgramId);
					persons.add(person);
				}
			}
		}

		return persons;

	}

	/**
	 * Number of new clients at risk of HIV infection as a result of
	 * rapr/assault who received pep
	 * 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAtRiskHivRapeAssaultPep(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newAtRiskHivRapeAssaultPep(String startDate,
			String endDate) {
		ArrayList<Person> patients = new ArrayList<Person>();

		Session session = getSessionFactory2().getCurrentSession();
		List<Integer> personEnrolledInPepPRogramAtRiskOfRapeAssaultIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id ")
				.list();
		List<Integer> personEnrolledInPepIds = session
				.createSQLQuery(
						"SELECT DISTINCT pe.person_id from person pe inner join patient pat ON pat.patient_id = pe.person_id ")
				.list();
		List<Integer> personOnEncountersWithIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp inner join encounter enc on pp.patient_id=enc.patient_id")
				.list();
		List<Integer> patientInProgramIds = session
				.createSQLQuery(
						"SELECT DISTINCT pp.patient_id FROM patient_program pp INNER JOIN program pro ON pro.program_id = pp.program_id where pp.program_id= '"
								+ Integer.parseInt(GlobalProperties
										.gpGetPEPProgramId())
								+ "' and pp.date_enrolled between '"
								+ startDate + "' AND '" + endDate + "' ")
				.list();
		List<Integer> personAtRiskOnPEPIds = session
				.createSQLQuery(
						"SELECT DISTINCT p.person_id from person p inner join obs o on p.person_id=o.person_id where p.voided=false and o.concept_id='"
								+ Integer
										.parseInt(GlobalProperties
												.gpGetReasonpatientStartedArvsForProphylaxisId())
								+ "' and o.value_coded='"
								+ Integer.parseInt(GlobalProperties
										.gpGetSexualAssaultId()) + "' ").list();
		List<Integer> personOnArvDrug = session
				.createSQLQuery(
						"SELECT DISTINCT pat.patient_id from patient pat inner join orders ord on pat.patient_id=ord.patient_id where ord.concept_id IN "
								+ ConstantValues.LIST_OF_PROPHYLAXIS_DRUGS
								+ "  AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false AND ord.discontinued_reason is null")
				.list();

		for (Integer personEnrolledInPepPRogramAtRiskOfRapeAssaultId : personEnrolledInPepPRogramAtRiskOfRapeAssaultIds) {
			for (Integer personEnrolledInPepId : personEnrolledInPepIds) {
				if (personEnrolledInPepPRogramAtRiskOfRapeAssaultId
						.equals(personEnrolledInPepId))
					for (Integer patientInProgramId : patientInProgramIds) {
						for (Integer personAtRiskOnPEPId : personAtRiskOnPEPIds) {
							if (patientInProgramId.equals(personAtRiskOnPEPId))
								for (Integer personOnArvDrugId : personOnArvDrug) {
									for (Integer personOnEncountersWithId : personOnEncountersWithIds) {
										if ((personEnrolledInPepPRogramAtRiskOfRapeAssaultId
												.equals(personEnrolledInPepId) && personEnrolledInPepPRogramAtRiskOfRapeAssaultId
												.equals(patientInProgramId))
												&& personEnrolledInPepPRogramAtRiskOfRapeAssaultId
														.equals(personAtRiskOnPEPId)
												&& personEnrolledInPepPRogramAtRiskOfRapeAssaultId
														.equals(personOnArvDrugId)
												&& personEnrolledInPepPRogramAtRiskOfRapeAssaultId
														.equals(personOnEncountersWithId)) {

											Person person = Context
													.getPersonService()
													.getPerson(
															personEnrolledInPepPRogramAtRiskOfRapeAssaultId);
											patients.add(person);
										}
									}
								}
						}
					}
			}
		}
		return patients;

	}

	/* End Of PEP */

	/**
	 * @throws ParseException 
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#newMaleMoreThanFifteenInHivCare(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> newMaleMoreThanFifteenInHivCare(String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));
		


	try {
		
		Session session = getSessionFactory2().getCurrentSession();


			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and pg.voided = 0 and pe.voided = 0 "
							+ " and pa.voided = 0 and pe.gender = 'M' and pg.date_enrolled >= '"
							+ startDate 
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

					SQLQuery queryDateEnrolled = session
							.createSQLQuery("select cast(max(date_enrolled) as DATE) from patient_program where (select cast(max(date_enrolled) as DATE)) is not null and patient_id = "
									+ patientId);
					List<Date> dateEnrolled = queryDateEnrolled.list();

					if (dateEnrolled.get(0) != null) {

						if ((dateEnrolled.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateEnrolled.get(0).getTime() <= newEndDate
										.getTime()))

						{

							SQLQuery queryExited = session
									.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetExitFromCareConceptId())
											+ " and (cast(o.obs_datetime as DATE)) <= '"
											+ endDate
											+ "' and o.voided = 0 and o.person_id="
											+ patientId);

							List<Integer> patientIds3 = queryExited.list();

							if (patientIds3.size() == 0) {

								/*SQLQuery queryDate1 = session
										.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
												+ "(select(cast(max(encounter_datetime)as Date))) <= '"
												+ endDate
												+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
												+ patientId);

								List<Date> maxEnocunterDateTime = queryDate1
										.list();

								SQLQuery queryDate2 = session
										.createSQLQuery("select cast(max(value_datetime) as DATE ) "
												+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
												+ endDate
												+ "' and concept_id = "
												+ Integer
														.parseInt(GlobalProperties
																.gpGetReturnVisitDateConceptId())
												+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
												+ patientId);

								List<Date> maxReturnVisitDay = queryDate2
										.list();

								if (((maxReturnVisitDay.get(0)) != null)
										&& (maxEnocunterDateTime.get(0) != null)) {

									if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxEnocunterDateTime
											.get(0).getTime()) <= newEndDate
											.getTime())
											|| ((maxReturnVisitDay.get(0)
													.getTime()) >= threeMonthsBeforeEndDate
													.getTime() && (maxReturnVisitDay
													.get(0).getTime()) <= newEndDate
													.getTime())) {

										patientIdsList.add(patientId);

									}
								}

								else if (((maxReturnVisitDay.get(0)) == null)
										&& (maxEnocunterDateTime.get(0) != null)) {

									if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime()
											&& (maxEnocunterDateTime.get(0)
													.getTime()) <= newEndDate
													.getTime()) {

										patientIdsList.add(patientId);

									}
								} else if (((maxReturnVisitDay.get(0) != null))
										&& (maxReturnVisitDay.get(0).getTime() > newEndDate
												.getTime()))

								{*/
									patientIdsList.add(patientId);

								}
							}
						}
					}
			//}
			
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		
}

		catch (Exception e) {
			e.printStackTrace();
		}

			

		return patients;

	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#adultOnSecondLineReg(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> adultOnSecondLineReg(String startDate, String endDate)
			throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 15 "
							+ "  and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfSecondLineDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0  and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {
					// try {

					/*SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ endDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ endDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) <= newEndDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) <= newEndDate.getTime())) {

							patientIdsList.add(patientId);

						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
								.getTime()
								&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);

						}
					} else if (((maxReturnVisitDay.get(0) != null))
							&& (maxReturnVisitDay.get(0).getTime() > newEndDate
									.getTime()))

					{*/
						patientIdsList.add(patientId);

					}
				}
			//}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#arvAdultDiedThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvAdultDiedThisMonth(String startDate, String endDate)
			throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and ord.discontinued_date is not null and ord.discontinued_reason = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryDate = session
						.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and value_coded= "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareDiedConceptId())
								+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
								+ patientId);

				List<Date> dateOfDeath = queryDate.list();

				if (dateOfDeath.size() != 0)
					if ((dateOfDeath.get(0).getTime() >= newStartDate.getTime())
							&& (dateOfDeath.get(0).getTime() <= newEndDate
									.getTime())) {

						patientIdsList.add(patientId);
					}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#arvAdultFifteenInterruptTreatThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> arvAdultFifteenInterruptTreatThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		List<Integer> patientIdsList = new ArrayList<Integer>();

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 > 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and ord.discontinued = 1 and pg.date_enrolled <= '"
							+ endDate
							+ "' and ord.discontinued_date is not null and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2Date = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = query2Date.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDrugs = session
							.createSQLQuery("select count(*) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Integer> drugsPerPatient = queryDrugs.list();

					SQLQuery queryDrugsDiscontinued = session
							.createSQLQuery("select count(*) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfARVsDrugs()
									+ ") and ord.voided = 0 and ord.discontinued = 1 and ord.patient_id = "
									+ patientId);

					List<Integer> drugsDiscontinuedPerPatient = queryDrugsDiscontinued
							.list();

					if (drugsPerPatient.get(0) == drugsDiscontinuedPerPatient
							.get(0)) {

						SQLQuery queryDate = session
								.createSQLQuery("select cast(discontinued_date as DATE) from orders ord "
										+ "inner join drug_order do on ord.order_id = do.order_id "
										/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
										+ " where ord.concept_id IN ("
										+ GlobalProperties
												.gpGetListOfARVsDrugs()
										+ ") and (select cast(discontinued_date as DATE)) is not null and ord.voided = 0 and ord.patient_id = "
										+ patientId);

						List<Date> dateOfDiscontinuedDrugs = queryDate.list();

						boolean n = true;

						for (Date d : dateOfDiscontinuedDrugs) {

							if ((d.getTime() >= newStartDate.getTime())
									&& (d.getTime() <= newEndDate.getTime())) {
								;
							} else {
								n = false;
								break;
							}

						}

						if (n == true) {
							patientIdsList.add(patientId);
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PedsUnderFifteenEnrolledInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PedsUnderFifteenEnrolledInHiv(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*
							 * +
							 * "inner join drug d on do.drug_inventory_id = d.drug_id "
							 */
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*
								 * +
								 * "inner join drug d on do.drug_inventory_id = d.drug_id "
								 */
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);

							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#AdultMoreThanFifteenEnrolledInHiv(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> AdultMoreThanFifteenEnrolledInHiv(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*
							 * +
							 * "inner join drug d on do.drug_inventory_id = d.drug_id "
							 */
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 15 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*
								 * +
								 * "inner join drug d on do.drug_inventory_id = d.drug_id "
								 */
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);

							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInPreARVDiedThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInPreARVDiedThisMonth(String startDate,
			String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and ord.discontinued_date is not null and ord.discontinued_reason = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareDiedConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and value_coded= "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareDiedConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfDeath = queryDate.list();

					if (dateOfDeath.size() != 0)
						if ((dateOfDeath.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfDeath.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);

						}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInPreARVTLostToFollowUpThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInPreARVTLostToFollowUpThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) <= newEndDate
											.getTime())) {

								patientsNotLostToFollowUp.add(patientId);

							}

							else {
								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())) {

								patientsNotLostToFollowUp.add(patientId);

							}

							else {
								patientIdsList.add(patientId);

							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInPreARVTransferredInThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInPreARVTransferredInThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetTransferredInConceptId())
							+ " and o.value_coded = "
							+ Integer
									.parseInt(GlobalProperties
											.gpGetYesAsAnswerToTransferredInConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query2Date = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = query2Date.list();

					if (patientIds3.size() == 0) {

						SQLQuery queryDate = session
								.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetTransferredInConceptId())
										+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> dateTransferredIn = queryDate.list();

						if (dateTransferredIn.size() != 0)
							if ((dateTransferredIn.get(0).getTime() >= newStartDate
									.getTime())
									&& (dateTransferredIn.get(0).getTime() <= newEndDate
											.getTime())) {

								patientIdsList.add(patientId);
							}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInPreARVTransferredOutThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInPreARVTransferredOutThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join obs o on pg.patient_id = o.person_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and o.voided = 0 and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.date_enrolled <= '"
							+ endDate
							+ "' and o.concept_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromCareConceptId())
							+ " and o.value_coded = "
							+ Integer.parseInt(GlobalProperties
									.gpGetExitFromTransferredOutConceptId())
							+ " and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryDate = session
							.createSQLQuery("select cast(obs_datetime as DATE) from obs where concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and value_coded= "
									+ Integer
											.parseInt(GlobalProperties
													.gpGetExitFromTransferredOutConceptId())
									+ " and (select cast(obs_datetime as DATE)) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> dateOfTransferredOut = queryDate.list();

					if (dateOfTransferredOut.size() != 0)
						if ((dateOfTransferredOut.get(0).getTime() >= newStartDate
								.getTime())
								&& (dateOfTransferredOut.get(0).getTime() <= newEndDate
										.getTime())) {

							patientIdsList.add(patientId);
						}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInARVTLostToFollowUpNotLostThisMonthList(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInARVTLostToFollowUpNotLostThisMonthList(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Integer> patientIdsListNotLostToFollowUp = new ArrayList<Integer>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newStartDate = df.parse(startDate);

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeStartDate = df
				.parse(addDaysToDate(startDate, -3));

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfARVsDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) < '"
							+ startDate
							+ "' and pg.date_enrolled < '"
							+ startDate
							+ "' and pg.date_completed is null and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery queryExited = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) < '"
								+ startDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds3 = queryExited.list();

				if (patientIds3.size() == 0) {

					SQLQuery queryDate1 = session
							.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
									+ "(select(cast(max(encounter_datetime)as Date))) <= '"
									+ startDate
									+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
									+ patientId);

					List<Date> maxEnocunterDateTime = queryDate1.list();

					SQLQuery queryDate2 = session
							.createSQLQuery("select cast(max(value_datetime) as DATE ) "
									+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
									+ startDate
									+ "' and concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetReturnVisitDateConceptId())
									+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
									+ patientId);

					List<Date> maxReturnVisitDay = queryDate2.list();

					if (((maxReturnVisitDay.get(0)) != null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeStartDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) < newStartDate.getTime())
								|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeStartDate
										.getTime() && (maxReturnVisitDay.get(0)
										.getTime()) < newStartDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						}

						else {

							patientIdsListNotLostToFollowUp.add(patientId);
						}
					}

					else if (((maxReturnVisitDay.get(0)) == null)
							&& (maxEnocunterDateTime.get(0) != null)) {

						if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeStartDate
								.getTime() && (maxEnocunterDateTime.get(0)
								.getTime()) < newStartDate.getTime())) {

							patientsNotLostToFollowUp.add(patientId);

						} else {

							patientIdsListNotLostToFollowUp.add(patientId);

						}
					}
				}
			}

			for (Integer patientId : patientIdsListNotLostToFollowUp) {

				SQLQuery queryDate4 = session
						.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
								+ "(select(cast(max(encounter_datetime)as Date))) >= '"
								+ startDate
								+ "' and (select(cast(max(encounter_datetime)as Date))) <= '"
								+ endDate
								+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
								+ patientId);

				List<Date> maxEnocunterDateTimeBetweenStartAndEndDate = queryDate4
						.list();

				if (maxEnocunterDateTimeBetweenStartAndEndDate.size() != 0)

					if (maxEnocunterDateTimeBetweenStartAndEndDate.get(0) != null)

					{

						if (maxEnocunterDateTimeBetweenStartAndEndDate.get(0)
								.getTime() >= newStartDate.getTime()
								&& maxEnocunterDateTimeBetweenStartAndEndDate
										.get(0).getTime() <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);
						}
					}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#PatientsInPreARVTLostToFollowUpNotLostThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> PatientsInPreARVTLostToFollowUpNotLostThisMonth(
			String startDate, String endDate) throws ParseException {

		List<Integer> patientIdsListNotLostToFollowUp = new ArrayList<Integer>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newStartDate = df.parse(startDate);

		Date newEndDate = df.parse(endDate);

		Date threeMonthsBeforeStartDate = df
				.parse(addDaysToDate(startDate, -3));

		ArrayList<Integer> patientsNotLostToFollowUp = new ArrayList<Integer>();

		Session session = getSessionFactory2().getCurrentSession();

		try {

			SQLQuery query1 = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) < '"
							+ startDate
							+ "' and pg.date_enrolled < '"
							+ startDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIds1 = query1.list();

			for (Integer patientId : patientIds1) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) < '"
								+ startDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) < '"
									+ startDate
									+ "' and o.voided = 0 and o.person_id="
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ startDate
										+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ startDate
										+ "' and concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetReturnVisitDateConceptId())
										+ " and (select cast(max(value_datetime) as DATE )) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeStartDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) < newStartDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeStartDate
											.getTime() && (maxReturnVisitDay
											.get(0).getTime()) < newStartDate
											.getTime())) {

								patientsNotLostToFollowUp.add(patientId);

							}

							else {

								patientIdsListNotLostToFollowUp.add(patientId);
							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeStartDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) < newStartDate.getTime())) {

								patientsNotLostToFollowUp.add(patientId);

							} else {

								patientIdsListNotLostToFollowUp.add(patientId);

							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsListNotLostToFollowUp) {

				// SQLQuery queryARV = session
				// .createSQLQuery("select distinct pg.patient_id from patient_program pg "
				// + "inner join person pe on pg.patient_id = pe.person_id "
				// + "inner join patient pa on pg.patient_id = pa.patient_id "
				// + "inner join orders ord on pg.patient_id = ord.patient_id "
				// + "inner join drug_order do on ord.order_id = do.order_id "
				// + "inner join drug d on do.drug_inventory_id = d.drug_id "
				// + "where d.concept_id IN ("
				// + GlobalProperties.gpGetListOfARVsDrugs()
				// +
				// ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
				// + "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
				// + startDate
				// + "'and (cast(ord.start_date as DATE)) <= '"
				// + endDate
				// + "' and pg.date_enrolled <= '"
				// + endDate
				// + "' and pg.date_completed is null and pg.patient_id = "
				// + patientId
				// + " and pg.program_id =  "
				// + Integer.parseInt(GlobalProperties
				// .gpGetHIVProgramId()));
				//
				// List<Integer> patientIdsARV = queryARV.list();
				//
				// if (patientIdsARV.size() == 0) {

				SQLQuery queryDate4 = session
						.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
								+ "(select(cast(max(encounter_datetime)as Date))) >= '"
								+ startDate
								+ "' and (select(cast(max(encounter_datetime)as Date))) <= '"
								+ endDate
								+ "' and (select cast(max(encounter_datetime)as DATE)) is not null and voided = 0 and patient_id = "
								+ patientId);

				List<Date> maxEnocunterDateTimeBetweenStartAndEndDate = queryDate4
						.list();

				if (maxEnocunterDateTimeBetweenStartAndEndDate.get(0) != null) {
					if (maxEnocunterDateTimeBetweenStartAndEndDate.get(0)
							.getTime() >= newStartDate.getTime()
							&& maxEnocunterDateTimeBetweenStartAndEndDate
									.get(0).getTime() <= newEndDate.getTime()) {

						patientIdsList.add(patientId);
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	/**
	 * @throws ParseException
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#pregnantHivPosStartedCotrimoxazoleThisMonth(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> pregnantHivPosStartedCotrimoxazoleThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join obs ob on pg.patient_id = ob.person_id "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join encounter enc on pg.patient_id = enc.patient_id "
							+ "where pe.gender = 'f' and pg.program_id = "
							+ Integer.parseInt(GlobalProperties
									.gpGetPMTCTProgramId())
							+ " and (cast(pg.date_enrolled as DATE)) <= '"
							+ endDate
							+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 and enc.voided=0 and pg.date_completed is null ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryHIVResultDate = session
							.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and o.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
									+ endDate
									+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
									+ patientId);
					List<Date> HivTestResultDate = queryHIVResultDate.list();

					if (HivTestResultDate.size() != 0)

					{

						if ((HivTestResultDate.get(0).getTime() >= newStartDate
								.getTime())
								&& (HivTestResultDate.get(0).getTime() <= newEndDate
										.getTime())) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetPositiveAsResultToHIVTestConceptId())) {

									SQLQuery queryMinStartDate = session
											.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
													+ " inner join drug_order do on ord.order_id = do.order_id "
													+ " inner join drug d on do.drug_inventory_id = d.drug_id "
													+ " where d.concept_id IN ("
													+ GlobalProperties
															.gpGetListOfProphylaxisDrugs()
													+ ") "
													+ " and (select (cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
													+ patientId);

									List<Date> patientIdsMinStartDate = queryMinStartDate
											.list();

									if (patientIdsMinStartDate.size() != 0) {
										if ((patientIdsMinStartDate.get(0)
												.getTime() >= newStartDate
												.getTime())
												&& patientIdsMinStartDate
														.get(0).getTime() <= newEndDate
														.getTime()) {

											patientIdsList.add(patientId);
										}

									}
								}
							}
						}
					}
				}
			}
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#infantHivNegMothersInDiscordantCouplesEnrolledPmtctList(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> infantHivNegMothersInDiscordantCouplesEnrolledPmtctList(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<Person> patients = new ArrayList<Person>();

		List<Integer> patientIdsList = new ArrayList<Integer>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);

		Date newStartDate = df.parse(startDate);

		try {

			Session session = getSessionFactory2().getCurrentSession();
			SQLQuery query = session
					.createSQLQuery("select distinct rel.person_a from relationship rel "
							+ "inner join person pe on rel.person_a = pe.person_id "
							+ "where pe.gender = 'f' "
							+ " and rel.voided = 0 and pe.voided = 0 ");

			// Getting the size of the returned LIST which equals to the COUNTS
			// needed
			List<Integer> patientIds = query.list();

			for (Integer patientId : patientIds) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery query3 = session
							.createSQLQuery("select distinct pg.patient_id from patient_program pg "
									+ "inner join obs ob on pg.patient_id = ob.person_id "
									+ "inner join person pe on pg.patient_id = pe.person_id "
									+ "inner join patient pa on pg.patient_id = pa.patient_id "
									+ "where ob.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetResultForHIVTestConceptId())
									+ " and (cast(ob.obs_datetime as DATE)) <= '"
									+ endDate
									+ "' and ob.value_coded in ("
									+ GlobalProperties
											.gpGetListOfAnswersToResultOfHIVTest()
									+ ") and (cast(pg.date_enrolled as DATE)) <= '"
									+ endDate
									+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
									+ " and pg.patient_id = " + patientId);

					List<Integer> patientIds3 = query3.list();

					if (patientIds3.size() != 0) {
						SQLQuery queryHIVResultDate = session
								.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
										+ Integer
												.parseInt(GlobalProperties
														.gpGetResultForHIVTestConceptId())
										+ " and o.value_coded in ("
										+ GlobalProperties
												.gpGetListOfAnswersToResultOfHIVTest()
										+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
										+ endDate
										+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
										+ patientId);
						List<Date> HivTestResultDate = queryHIVResultDate
								.list();

						if (HivTestResultDate.size() != 0) {

							SQLQuery queryHIVResultConcept = session
									.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
											+ Integer
													.parseInt(GlobalProperties
															.gpGetResultForHIVTestConceptId())
											+ " and o.value_coded in ("
											+ GlobalProperties
													.gpGetListOfAnswersToResultOfHIVTest()
											+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
											+ HivTestResultDate.get(0)
											+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
											+ patientId);
							List<Integer> HivTestResultConcept = queryHIVResultConcept
									.list();

							if (HivTestResultConcept.size() != 0) {

								if (HivTestResultConcept.get(0) == Integer
										.parseInt(GlobalProperties
												.gpGetNegativeAsResultToHIVTestConceptId())) {

									SQLQuery queryTestingStatusOfPartner = session
											.createSQLQuery("select distinct pg.patient_id from patient_program pg "
													+ "inner join obs ob on pg.patient_id = ob.person_id "
													+ "inner join person pe on pg.patient_id = pe.person_id "
													+ "inner join patient pa on pg.patient_id = pa.patient_id "
													+ "where ob.concept_id = "
													+ Integer
															.parseInt(GlobalProperties
																	.gpGetTestingStatusOfPartnerConceptId())
													+ " and (cast(ob.obs_datetime as DATE)) <= '"
													+ endDate
													+ "' and ob.value_coded in ("
													+ GlobalProperties
															.gpGetListOfAnswersToResultOfHIVTest()
													+ ") and (cast(pg.date_enrolled as DATE)) <= '"
													+ endDate
													+ "' and pg.voided = 0 and ob.voided = 0 and pe.voided = 0 and pa.voided = 0 "
													+ " and pg.patient_id = "
													+ patientId);

									List<Integer> patientIdsTestingStatusOfPartner = queryTestingStatusOfPartner
											.list();

									if (patientIdsTestingStatusOfPartner.size() != 0) {

										SQLQuery queryHIVResultDateTestingStatusOfPartner = session
												.createSQLQuery("select cast(max(o.obs_datetime)as DATE) from obs o where o.concept_id = "
														+ Integer
																.parseInt(GlobalProperties
																		.gpGetTestingStatusOfPartnerConceptId())
														+ " and o.value_coded in ("
														+ GlobalProperties
																.gpGetListOfAnswersToResultOfHIVTest()
														+ ") and (select cast(max(o.obs_datetime)as DATE)) <= '"
														+ endDate
														+ "' and (select cast(max(o.obs_datetime)as DATE)) is not null and o.voided = 0 and o.person_id="
														+ patientId);
										List<Date> HivTestResultDateHIVResultDateTestingStatusOfPartner = queryHIVResultDateTestingStatusOfPartner
												.list();

										if (HivTestResultDateHIVResultDateTestingStatusOfPartner
												.size() != 0) {

											SQLQuery queryHIVResultConceptTestingStatusOfPartner = session
													.createSQLQuery("select o.value_coded from obs o where o.concept_id = "
															+ Integer
																	.parseInt(GlobalProperties
																			.gpGetTestingStatusOfPartnerConceptId())
															+ " and o.value_coded in ("
															+ GlobalProperties
																	.gpGetListOfAnswersToResultOfHIVTest()
															+ ") and (select cast(max(o.obs_datetime)as DATE)) = '"
															+ HivTestResultDate
																	.get(0)
															+ "' and o.value_coded is not null and o.voided = 0 and o.person_id= "
															+ patientId);
											List<Integer> HivTestResultConceptHIVResultConceptTestingStatusOfPartner = queryHIVResultConceptTestingStatusOfPartner
													.list();

											if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
													.size() != 0) {

												if (HivTestResultConceptHIVResultConceptTestingStatusOfPartner
														.get(0) == Integer
														.parseInt(GlobalProperties
																.gpGetPositiveAsResultToHIVTestConceptId())) {

													SQLQuery infantHIVPositiveInPMTCT = session
															.createSQLQuery("select distinct rel.person_b from relationship rel "
																	+ "inner join person pe on rel.person_b = pe.person_id "
																	+ "inner join patient_program pg on rel.person_b = pg.patient_id "
																	+ "where rel.person_a = "
																	+ patientId
																	+ " and pg.program_id = "
																	+ Integer
																			.parseInt(GlobalProperties
																					.gpGetPMTCTProgramId())

																	+ " and pg.voided = 0  and rel.voided = 0 and (cast(pg.date_enrolled as DATE)) >= '"
																	+ startDate
																	+ "' and (cast(pg.date_enrolled as DATE)) <= '"
																	+ endDate
																	+ "' ");
													List<Integer> infantHIVPositive = infantHIVPositiveInPMTCT
															.list();

													if (infantHIVPositive
															.size() != 0) {

														for (Integer patientIdsInfant : infantHIVPositive) {

															SQLQuery queryInfantExited = session
																	.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
																			+ Integer
																					.parseInt(GlobalProperties
																							.gpGetExitFromCareConceptId())
																			+ " and o.voided = 0 and o.person_id="
																			+ patientIdsInfant);

															List<Integer> patientIds2InfantExited = queryInfantExited
																	.list();

															if (patientIds2InfantExited
																	.size() == 0) {

																SQLQuery queryExposedInfantInPMTCT = session
																		.createSQLQuery("select (cast(pg.date_enrolled as DATE)) from patient_program pg"
																				+ " where pg.patient_id = "
																				+ patientIdsInfant
																				+ " and (select (cast(pg.date_enrolled as DATE))) is not null and pg.voided = 0 and pg.program_id = "
																				+ Integer
																						.parseInt(GlobalProperties
																								.gpGetPMTCTProgramId()));

																List<Date> exposedInfantInPMTCT = queryExposedInfantInPMTCT
																		.list();

																if ((exposedInfantInPMTCT
																		.get(0)
																		.getTime() >= newStartDate
																		.getTime())
																		&& (exposedInfantInPMTCT
																				.get(
																						0)
																				.getTime() <= newEndDate
																				.getTime())) {

																	patientIdsList
																			.add(patientIdsInfant);
																}
															}
														}
													}
												}
											}

										}

									}
								}
							}
						}
					}
				}
			}

			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patients;

	}

	public  List<Person> patientsOnCotrimoProphylaxisLessThan15YearsList (String startDate,
			String endDate) throws ParseException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newStartDate = df.parse(startDate);


		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery queryPatientsOnCotrimo = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 < 15 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIdsOnCotrimo = queryPatientsOnCotrimo.list();

			for (Integer patientId : patientIdsOnCotrimo) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds1 = query2.list();

				//for (Integer patientId : patientIds1) {
				
				if(patientIds1.size() == 0)
				{

					SQLQuery queryExited = session
					
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "'"
									+ " and o.voided = 0 and o.person_id= "
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select(cast(max(encounter_datetime)as DATE))) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetReturnVisitDateConceptId())
										+ " and (select(cast(max(value_datetime)as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay.get(0)
											.getTime()) <= newEndDate.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);

							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}
		
		
		for (Integer patientId : patientIdsList) {
			patients.add(Context.getPersonService().getPerson(patientId));
		}
		}
				
			catch (Exception e) {

				e.printStackTrace();
			}
			return patients;
	}
	
	public List<Person> patientsOnCotrimoProphylaxisGreatherThan15YearsList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException {

		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newStartDate = df.parse(startDate);


		try {

			Session session = getSessionFactory2().getCurrentSession();

			SQLQuery queryPatientsOnCotrimo = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "WHERE DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfProphylaxisDrugs()
							+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ "and pa.voided = 0 and pg.date_enrolled <= '"
							+ endDate
							+ "' and pg.program_id =  "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId()));

			List<Integer> patientIdsOnCotrimo = queryPatientsOnCotrimo.list();

			for (Integer patientId : patientIdsOnCotrimo) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct pg.patient_id from patient_program pg "
								+ "inner join person pe on pg.patient_id = pe.person_id "
								+ "inner join patient pa on pg.patient_id = pa.patient_id "
								+ "inner join orders ord on pg.patient_id = ord.patient_id "
								+ "inner join drug_order do on ord.order_id = do.order_id "
								/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
								+ "where ord.concept_id IN ("
								+ GlobalProperties.gpGetListOfARVsDrugs()
								+ ") and ord.discontinued = 0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
								+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
								+ endDate
								+ "'"
								+ " and pg.patient_id="
								+ patientId);

				List<Integer> patientIds1 = query2.list();

				//for (Integer patientId : patientIds1) {
				
				if(patientIds1.size()==0)
				{

					SQLQuery queryExited = session
							.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
									+ Integer.parseInt(GlobalProperties
											.gpGetExitFromCareConceptId())
									+ " and (cast(o.obs_datetime as DATE)) <= '"
									+ endDate
									+ "'"
									+ " and o.voided = 0 and o.person_id= "
									+ patientId);

					List<Integer> patientIds3 = queryExited.list();

					if (patientIds3.size() == 0) {

						/*SQLQuery queryDate1 = session
								.createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
										+ "(select(cast(max(encounter_datetime)as Date))) <= '"
										+ endDate
										+ "' and (select(cast(max(encounter_datetime)as DATE))) is not null and voided = 0 and patient_id = "
										+ patientId);

						List<Date> maxEnocunterDateTime = queryDate1.list();

						SQLQuery queryDate2 = session
								.createSQLQuery("select cast(max(value_datetime) as DATE ) "
										+ "from obs where (select(cast(max(value_datetime)as Date))) <= '"
										+ endDate
										+ "' and concept_id = "
										+ Integer.parseInt(GlobalProperties
												.gpGetReturnVisitDateConceptId())
										+ " and (select(cast(max(value_datetime)as DATE))) is not null and voided = 0 and person_id = "
										+ patientId);

						List<Date> maxReturnVisitDay = queryDate2.list();

						if (((maxReturnVisitDay.get(0)) != null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if (((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime() && (maxEnocunterDateTime.get(0)
									.getTime()) <= newEndDate.getTime())
									|| ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate
											.getTime() && (maxReturnVisitDay.get(0)
											.getTime()) <= newEndDate.getTime())) {

								patientIdsList.add(patientId);

							}
						}

						else if (((maxReturnVisitDay.get(0)) == null)
								&& (maxEnocunterDateTime.get(0) != null)) {

							if ((maxEnocunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate
									.getTime()
									&& (maxEnocunterDateTime.get(0).getTime()) <= newEndDate
											.getTime()) {

								patientIdsList.add(patientId);
							}
						} else if (((maxReturnVisitDay.get(0) != null))
								&& (maxReturnVisitDay.get(0).getTime() > newEndDate
										.getTime()))

						{*/
							patientIdsList.add(patientId);

						}
					}
				}
			//}
			
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}
				
			catch (Exception e) {

				e.printStackTrace();
			}
			return patients;
	}
	
	
	
	public List<Person> patientsPediatricNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException {
		
		
		
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery queryPatientsOnCotrimo = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 <= 13 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfSecondLineDrugs()
							+ ") "
							+ " and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ " and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIdsOnCotrimo = queryPatientsOnCotrimo.list();

			for (Integer patientId : patientIdsOnCotrimo) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfSecondLineDrugs()
									+ ") "
									+ " and (select(cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if (patientIdsMinStartDate.get(0) != null)

						if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
								.getTime())
								&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);
						}
				}
			}
			
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
		
	}
	
	
	public List<Person> patientsAdultNewOnSecondLineThisMonthList(String startDate,
			String endDate) throws ParseException, HibernateException,
			NumberFormatException {
		
		
		
		List<Integer> patientIdsList = new ArrayList<Integer>();

		ArrayList<Person> patients = new ArrayList<Person>();

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		Date newEndDate = df.parse(endDate);
		
		Date threeMonthsBeforeEndDate = df.parse(addDaysToDate(endDate, -3));

		Date newStartDate = df.parse(startDate);

		Session session = getSessionFactory2().getCurrentSession();

		try {
			SQLQuery queryPatientsOnCotrimo = session
					.createSQLQuery("select distinct pg.patient_id from patient_program pg "
							+ "inner join person pe on pg.patient_id = pe.person_id "
							+ "inner join patient pa on pg.patient_id = pa.patient_id "
							+ "inner join orders ord on pg.patient_id = ord.patient_id "
							+ "inner join drug_order do on ord.order_id = do.order_id "
							/*+ "inner join drug d on do.drug_inventory_id = d.drug_id "*/
							+ "where DATE_FORMAT(FROM_DAYS(TO_DAYS('"
							+ endDate
							+ "') - TO_DAYS(pe.birthdate)), '%Y')+0 >= 14 "
							+ " and ord.concept_id IN ("
							+ GlobalProperties.gpGetListOfSecondLineDrugs()
							+ ") "
							+ " and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
							+ " and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '"
							+ startDate
							+ "' and (cast(ord.start_date as DATE)) <= '"
							+ endDate
							+ "' and pg.program_id= "
							+ Integer.parseInt(GlobalProperties
									.gpGetHIVProgramId())
							+ " and pg.date_enrolled <= '" + endDate + "' ");

			List<Integer> patientIdsOnCotrimo = queryPatientsOnCotrimo.list();

			for (Integer patientId : patientIdsOnCotrimo) {

				SQLQuery query2 = session
						.createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ Integer.parseInt(GlobalProperties
										.gpGetExitFromCareConceptId())
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ endDate
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);

				List<Integer> patientIds2 = query2.list();

				if (patientIds2.size() == 0) {

					SQLQuery queryMinStartDate = session
							.createSQLQuery("select (cast(min(ord.start_date)as Date)) from orders ord "
									+ " inner join drug_order do on ord.order_id = do.order_id "
									/*+ " inner join drug d on do.drug_inventory_id = d.drug_id "*/
									+ " where ord.concept_id IN ("
									+ GlobalProperties.gpGetListOfSecondLineDrugs()
									+ ") "
									+ " and (select(cast(min(ord.start_date)as Date))) is not null and ord.voided = 0 and ord.patient_id = "
									+ patientId);

					List<Date> patientIdsMinStartDate = queryMinStartDate
							.list();

					if (patientIdsMinStartDate.get(0) != null)

						if ((patientIdsMinStartDate.get(0).getTime() >= newStartDate
								.getTime())
								&& patientIdsMinStartDate.get(0).getTime() <= newEndDate
										.getTime()) {

							patientIdsList.add(patientId);
						}
				}
			}
			
			for (Integer patientId : patientIdsList) {
				patients.add(Context.getPersonService().getPerson(patientId));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
		
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Person> NumberOfPregnantWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Person> NumberOfLactatingWomenWhoReceivedTherapeuticOrNutritionalSupplementationThisMonth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Person> NumberOfHIVPositivePregnantWomenIdentifiedAtMaternityWhoStartedTripleTherapyProphylaxis(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Person> NumberOfNewInfantsBornFromHIVPositiveMothersWhoReceivedARTProphylaxisAtBirth(
			String startDate, String endDate) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.openmrs.module.tracnetreporting.service.TracNetPatientService#numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Person> numberOfHIVPositivePregnantWomenWhoReceivedTripleTherapyAsProphylaxis(
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}


}