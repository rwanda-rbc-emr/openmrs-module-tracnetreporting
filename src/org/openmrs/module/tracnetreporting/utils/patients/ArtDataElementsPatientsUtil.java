///**
// * The contents of this file are subject to the OpenMRS Public License
// * Version 1.0 (the "License"); you may not use this file except in
// * compliance with the License. You may obtain a copy of the License at
// * http://license.openmrs.org
// *
// * Software distributed under the License is distributed on an "AS IS"
// * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
// * License for the specific language governing rights and limitations
// * under the License.
// *
// * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
// */
//package org.openmrs.module.tracnetreporting.utils.patients;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.openmrs.Patient;
//import org.openmrs.api.context.Context;
//import org.openmrs.module.tracnetreporting.service.ConstantValues;
//import org.openmrs.module.tracnetreporting.service.ExecuteHibernateQueryLanguageUtil;
//
///**
// *
// */
//public class ArtDataElementsPatientsUtil {
//	
//	/**
//	 * Total number of adult patients who are on First Line Regimen
//	 * 
//	 * @param sessionFactory
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	public static List<Patient> adultOnFirstLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patients_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_FIRST_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = FALSE AND pp.void_reason IS NULL");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of adult patients who are on Second Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultOnSecondLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> adultOnSecondLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_SECOND_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have died this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultDiedThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvAdultDiedThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id = "
//		                + ConstantValues.REASON_FOR_EXITING_CARE + " AND obs.value_coded = " + ConstantValues.PATIENT_DIED
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have had their treatment interrupted this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultFifteenInterruptTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvAdultFifteenInterruptTreatThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                   String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.discontinued_date BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.discontinued = true AND ord.voided = FALSE AND ord.void_reason is null "
//		                + "group by pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) lost to followup (>3 months)
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultLostFollowupMoreThreeMonths(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvAdultLostFollowupMoreThreeMonths(SessionFactory sessionFactory, String startDate,
//	                                                                String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN encounter enc ON enc.patient_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.encounter_id = enc.encounter_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = FALSE AND pp.void_reason IS NULL AND enc.voided = false "
//		                + "AND enc.void_reason IS NULL AND enc.encounter_type IN (2,4) AND ord.concept_id IN"
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = FALSE AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND (SELECT MAX(enc.encounter_datetime)) < DATE_SUB('"
//		                + startDate + "', INTERVAL 3 MONTH) AND obs.concept_id = " + ConstantValues.NEXT_SCHEDULED_VISIT
//		                + " AND (SELECT MAX(obs.value_datetime)) < DATE_SUB('" + startDate
//		                + "', INTERVAL 3 MONTH) GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have been transferred in this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferreInThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvAdultTransferreInThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE obs.concept_id = "
//		                + ConstantValues.TRANSFERRED_IN + " AND obs.value_coded = " + ConstantValues.YES
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have been transferred out this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferredOutThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvAdultTransferredOutThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                            String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE obs.concept_id = "
//		                + ConstantValues.REASON_FOR_EXITING_CARE + " AND obs.value_coded = "
//		                + ConstantValues.TRANSFERRED_OUT
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have died this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsDiedThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvPedsDiedThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id = "
//		                + ConstantValues.REASON_FOR_EXITING_CARE + " AND obs.value_coded = " + ConstantValues.PATIENT_DIED
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have had their treatment interrupted this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsFifteenInterruptTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvPedsFifteenInterruptTreatThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                  String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false "
//		                + "AND pp.void_reason is null AND ord.discontinued_date BETWEEN '" + startDate + "' AND '" + endDate
//		                + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.discontinued = true AND ord.voided = FALSE AND ord.void_reason is null "
//		                + "group by pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) lost to followup (>3 months)
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsLostFollowupMoreThreeMonths(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvPedsLostFollowupMoreThreeMonths(SessionFactory sessionFactory, String startDate,
//	                                                               String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN encounter enc ON enc.patient_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.encounter_id = enc.encounter_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = FALSE AND pp.void_reason IS NULL AND enc.voided = false "
//		                + "AND enc.void_reason IS NULL AND enc.encounter_type IN (2,4) AND ord.concept_id IN"
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = FALSE AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND (SELECT MAX(enc.encounter_datetime)) < DATE_SUB('"
//		                + startDate + "', INTERVAL 3 MONTH) AND obs.concept_id = " + ConstantValues.NEXT_SCHEDULED_VISIT
//		                + " AND (SELECT MAX(obs.value_datetime)) < DATE_SUB('" + startDate
//		                + "', INTERVAL 3 MONTH) GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have been transferred in this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredInThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvPedsTransferredInThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE obs.concept_id = "
//		                + ConstantValues.TRANSFERRED_IN + " AND obs.value_coded = " + ConstantValues.YES
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have been transferred out this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredOutThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> arvPedsTransferredOutThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                           String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE obs.concept_id = "
//		                + ConstantValues.REASON_FOR_EXITING_CARE + " AND obs.value_coded = "
//		                + ConstantValues.TRANSFERRED_OUT
//		                + " AND obs.voided = false AND obs.void_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of female adult patients (age 15 or older) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> femaleMoreThanFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate,
//	                                                              String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'F' "
//		                + "AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of female patients on treatment 12 months after initiation of ARVs
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleOnTreatTwelveAfterInitArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> femaleOnTreatTwelveAfterInitArv(SessionFactory sessionFactory, String startDate,
//	                                                            String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "WHERE per.gender = 'F' AND pp.voided = false AND pp.void_reason is null AND DATEDIFF('"
//		                + startDate + "',(select MIN(ord.start_date))) >= " + ConstantValues.ONE_YEAR
//		                + " AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null group by pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of female pediatric patients (age <15 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> femalePedsUnderFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate,
//	                                                               String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'F' AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of male adult patients (age 15 or older) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> maleMoreThanFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate,
//	                                                            String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'M' "
//		                + "AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of male patients on treatment 12 months after initiation of ARVs
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleOnTreatTwelveAfterInitArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> maleOnTreatTwelveAfterInitArv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "WHERE per.gender = 'M' AND pp.voided = false AND pp.void_reason is null AND DATEDIFF('"
//		                + startDate + "',(select MIN(ord.start_date))) >= " + ConstantValues.ONE_YEAR
//		                + " AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of male pediatric patients (age <15 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> malePedsUnderFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate,
//	                                                             String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'M' AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage undefined this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultUndefinedWhoStageThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newAdultUndefinedWhoStageThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                               String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id " + "WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded =" + ConstantValues.UNKNOWN
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 4 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageFourThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newAdultWhoStageFourThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_FOUR_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 1 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageOneThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newAdultWhoStageOneThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_ONE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 3 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageThreeThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newAdultWhoStageThreeThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                           String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_THREE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 2 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageTwoThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newAdultWhoStageTwoThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_TWO_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new female adult patients (age 15+) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleAdultStartiArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newFemaleAdultStartiArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                             String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'F' "
//		                + "AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new female pediatric patients (age <15 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemalePedsUnderFifteenStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newFemalePedsUnderFifteenStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                       String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'F' AND (select MIN(start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' and ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null "
//		                + "AND pp.voided = false AND pp.void_reason is null GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new male adult patients (age 15+) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleAdultStartiArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newMaleAdultStartiArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                           String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'M' "
//		                + "AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new male pediatric patients (age <15 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMalePedsUnderFifteenStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newMalePedsUnderFifteenStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                     String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'M' AND (select MIN(start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' and ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null "
//		                + "AND pp.voided = false AND pp.void_reason is null group by pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients whose WHO Stage is undefined this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUndefinedWhoStageThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsUndefinedWhoStageThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                              String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id " + "WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded =" + ConstantValues.UNKNOWN
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients (<18 months) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsUnderEighteenMonthStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                       String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.EIGHTEEN_MONTH + " AND (select MIN(start_date)) BETWEEN '" + startDate + "' AND '"
//		                + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND pp.voided = false "
//		                + "AND pp.void_reason is null GROUP BY pp.patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients (age <5 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsUnderFiveStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                              String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIVE_YEARS + " AND (select MIN(start_date)) BETWEEN '" + startDate + "' AND '"
//		                + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND pp.voided = false "
//		                + "AND pp.void_reason is null GROUP BY patient_id");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO stage 4 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageFourThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsWhoStageFourThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_FOUR_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 1 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageOneThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsWhoStageOneThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_ONE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 3 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageThreeThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsWhoStageThreeThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_THREE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 2 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageTwoThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsWhoStageTwoThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_TWO_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of pediatric patients who are on First Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnFirstLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedsOnFirstLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_FIRST_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = FALSE AND pp.void_reason IS NULL");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of pediatric patients who are on Second Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnSecondLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedsOnSecondLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id " + "WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_SECOND_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <18 months) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderEighteenMonthsCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedsUnderEighteenMonthsCurrentOnArv(SessionFactory sessionFactory, String startDate,
//	                                                                String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.EIGHTEEN_MONTH + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <5 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedsUnderFiveCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlPatients("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIVE_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		for (Integer patientId : patientIds) {
//			Patient patient = Context.getPatientService().getPatient(patientId);
//			if (!patient.getVoided()) {
//				patients.add(patient);
//			}
//		}
//		
//		return patients;
//	}
//}
