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
//package org.openmrs.module.tracnetreporting.utils.indicators;
//
//import java.text.ParseException;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.openmrs.module.tracnetreporting.service.ConstantValues;
//import org.openmrs.module.tracnetreporting.service.ExecuteHibernateQueryLanguageUtil;
//
///**
// *
// */
//public class PreArtDataElementsIndicatorsUtil {
//	
//	/**
//	 * Number of patients on Cotrimoxazole Prophylaxis this month
//	 * 
//	 * @throws ParseException
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsOnCotrimoProphylaxis(java.util.Date,
//	 *      java.util.Date)
//	 */
//	public static int patientsOnCotrimoProphylaxis(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id " + "WHERE pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND ord.concept_id IN (" + ConstantValues.COTRIMOXAZOLE + ","
//		                + ConstantValues.DAPSONE + "," + ConstantValues.DIFLUCAN + ") "
//		                + "AND ord.voided = FALSE AND ord.void_reason IS NULL "
//		                + "AND ord.discontinued_date IS NULL AND ord.discontinued = FALSE");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new pediatric patients (age <18 months) enrolled in HIV care
//	 * 
//	 * @throws ParseException
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthsInHivCare(java.util.Date,
//	 *      java.util.Date)
//	 */
//	public static int newPedsUnderEighteenMonthsInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.EIGHTEEN_MONTH
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pp.date_enrolled between '"
//		                + startDate + "' AND '" + endDate + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of female adult patients (age 15 or older) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int femaleMoreThanFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'F'");
//		
//		indicator = records.size();
//		
//		//log.info("--------------- INDICATOR ------------- << " + indicator + " >> ");
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of female pediatric patients (age <15 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int femalePedsUnderFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id WHERE DATEDIFF(CURDATE(), pe.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pe.gender = 'F'");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of male adult patients (age 15 or older) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int maleMoreThanFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'M'");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of male pediatric patients (age <15 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int malePedsUnderFifteenEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID + " AND pe.gender = 'M'");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of newly enrolled patients (age 15+ years) who started TB treatment this month?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledAdultsStartTbTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newEnrolledAdultsStartTbTreatThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of newly enrolled patients (age <15 years) who started TB treatment this month?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newEnrolledPedsStartTbTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newEnrolledPedsStartTbTreatThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new female adult patients (age 15+) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleMoreThanFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newFemaleMoreThanFifteenInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'F' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new female pediatric patients (age <15 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleUnderFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newFemaleUnderFifteenInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'F' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new male adult patients (age 15+) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleMoreThanFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newMaleMoreThanFifteenInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'M' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new male pediatric patients (age <15 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleUnderFifteenInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newMaleUnderFifteenInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pe.gender = 'M' AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND pp.date_enrolled between '" + startDate + "' AND '" + endDate
//		                + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of new pediatric patients (age <5 years) enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveInHivCare(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsUnderFiveInHivCare(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIVE_YEARS + " AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND pp.date_enrolled between '" + startDate + "' AND '"
//		                + endDate + "' AND pp.voided = false AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new patients screened for active TB at enrollment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsActiveTbAtEnrolThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int patientsActiveTbAtEnrolThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil.executeHqlIndicators("SELECT DISTINCT pp.patient_id "
//		        + "FROM patient_program pp INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		        + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		        + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		        + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id =" + ConstantValues.TB_SCREENING
//		        + " AND obs.obs_datetime <= pp.date_enrolled AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		        + " AND pp.voided = false AND pp.void_reason is null AND obs.voided = false AND obs.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of patients screened TB Positive at enrollment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#patientsTbPositiveAtEnrolThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int patientsTbPositiveAtEnrolThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id ="
//		                + ConstantValues.TB_SCREENING + " AND obs.value_coded =" + ConstantValues.POSITIVE_ID
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID
//		                + " AND obs.obs_datetime <= pp.date_enrolled AND pp.voided = false "
//		                + "AND pp.void_reason is null AND obs.voided = false AND obs.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <18 months) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedUnderEighteenMonthsEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedUnderEighteenMonthsEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.EIGHTEEN_MONTH
//		                + " AND pro.concept_id =" + ConstantValues.HIV_PROGRAM_ID);
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <5 years) ever enrolled in HIV care?
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveEverInHiv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedsUnderFiveEverInHiv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT DISTINCT pp.patient_id FROM patient_program pp "
//		                + "INNER JOIN patient pat ON pat.patient_id = pp.patient_id "
//		                + "INNER JOIN person pe ON pat.patient_id = pe.person_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "WHERE DATEDIFF(CURDATE(), pe.birthdate) < " + ConstantValues.FIVE_YEARS + " AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID);
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//}
