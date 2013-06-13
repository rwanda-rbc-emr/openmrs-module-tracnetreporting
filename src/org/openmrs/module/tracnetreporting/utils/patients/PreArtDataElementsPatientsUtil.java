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
//import java.text.ParseException;
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
//public class PreArtDataElementsPatientsUtil {
//	
//	/**
//	 * Number of patients on Cotrimoxazole Prophylaxis this month
//	 * 
//	 * @throws ParseException
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsOnCotrimoProphylaxis(java.util.Date,
//	 *      java.util.Date)
//	 */
//	public static List<Patient> patientsOnCotrimoProphylaxis(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id " + "WHERE pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND ord.concept_id IN (" + ConstantValues.COTRIMOXAZOLE + ","
//		                + ConstantValues.DAPSONE + "," + ConstantValues.DIFLUCAN + ") "
//		                + "AND ord.voided = FALSE AND ord.void_reason IS NULL "
//		                + "AND ord.discontinued_date IS NULL AND ord.discontinued = FALSE");
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
//	 * Total number of new pediatric patients (age <18 months) enrolled in HIV care
//	 * 
//	 * @throws ParseException
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthsInHivCare(java.util.Date,
//	 *      java.util.Date)
//	 */
//	public static List<Patient> newPedsUnderEighteenMonthsInHivCare(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.EIGHTEEN_MONTH
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pp.date_enrolled between '"
//		                + startDate + "' AND '" + endDate + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Total number of female adult patients (age 15 or older) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> femaleMoreThanFifteenEverInHiv(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'F'");
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
//	 * Total number of female pediatric patients (age <15 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> femalePedsUnderFifteenEverInHiv(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'F'");
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
//	 * Total number of male adult patients (age 15 or older) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> maleMoreThanFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'M'");
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
//	 * Total number of male pediatric patients (age <15 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> malePedsUnderFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'M'");
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
//	 * Number of newly enrolled patients (age 15+ years) who started TB treatment this month?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledAdultsStartTbTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newEnrolledAdultsStartTbTreatThisMonth(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_TB_DRUGS
//		                + " " + "AND ord.voided = FALSE AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL" + " GROUP BY pp.patient_id");
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
//	 * Number of newly enrolled patients (age <15 years) who started TB treatment this month?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledPedsStartTbTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newEnrolledPedsStartTbTreatThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                                 String endDate) {
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
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_TB_DRUGS
//		                + " " + "AND ord.voided = FALSE AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL" + " GROUP BY pp.patient_id");
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
//	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleMoreThanFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newFemaleMoreThanFifteenInHivCare(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'F' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Total number of new female pediatric patients (age <15 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleUnderFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newFemaleUnderFifteenInHivCare(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'F' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Total number of new male adult patients (age 15+) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleMoreThanFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newMaleMoreThanFifteenInHivCare(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'M' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Total number of new male pediatric patients (age <15 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleUnderFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newMaleUnderFifteenInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'M' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Total number of new pediatric patients (age <5 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> newPedsUnderFiveInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIVE_YEARS + " AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND pp.date_enrolled between '" + startDate + "' AND '"
//		                + endDate + "' AND pp.voided = false AND pp.void_reason IS NULL");
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
//	 * Number of new patients screened for active TB at enrollment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsActiveTbAtEnrolThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> patientsActiveTbAtEnrolThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                             String endDate) {
//		
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		List<Integer> patientIds = session.createSQLQuery("query string").list();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Integer> patientIds = ExecuteHibernateQueryLanguageUtil.executeHqlPatients("SELECT DISTINCT pp.patient_id "
//		        + "FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		        + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		        + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		        + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id =" + ConstantValues.TB_SCREENING
//		        + " AND obs.obs_datetime <= pp.date_enrolled AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		        + " AND pp.voided = false AND pp.void_reason is null AND obs.voided = false AND obs.void_reason is null");
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
//	 * Number of patients screened TB Positive at enrollment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsTbPositiveAtEnrolThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> patientsTbPositiveAtEnrolThisMonth(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id ="
//		                + ConstantValues.TB_SCREENING + " AND obs.value_coded =" + ConstantValues.POSITIVE_ID
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND obs.obs_datetime <= pp.date_enrolled AND pp.voided = false "
//		                + "AND pp.void_reason is null AND obs.voided = false AND obs.void_reason is null");
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
//	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedUnderEighteenMonthsEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedUnderEighteenMonthsEverInHiv(SessionFactory sessionFactory, String startDate,
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.EIGHTEEN_MONTH
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID);
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
//	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static List<Patient> pedsUnderFiveEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIVE_YEARS + " AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID);
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
