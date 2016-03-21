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
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.openmrs.module.tracnetreporting.service.ConstantValues;
//import org.openmrs.module.tracnetreporting.service.ExecuteHibernateQueryLanguageUtil;
//
///**
// *
// */
//public class ArtDataElementsIndicatorsUtil {
//	
//	/**
//	 * Total number of adult patients who are on First Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultOnFirstLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int adultOnFirstLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_FIRST_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = FALSE AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of adult patients who are on Second Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#adultOnSecondLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int adultOnSecondLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_SECOND_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have died this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultDiedThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvAdultDiedThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have had their treatment interrupted this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultFifteenInterruptTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvAdultFifteenInterruptTreatThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND pp.voided = false AND pp.void_reason is null AND ord.discontinued_date BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.discontinued = true AND ord.voided = FALSE AND ord.void_reason is null "
//		                + "group by pp.patient_id");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) lost to followup (>3 months)
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultLostFollowupMoreThreeMonths(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvAdultLostFollowupMoreThreeMonths(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have been transferred in this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferreInThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvAdultTransferreInThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age 15+) who have been transferred out this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvAdultTransferredOutThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvAdultTransferredOutThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have died this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsDiedThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvPedsDiedThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have had their treatment interrupted this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsFifteenInterruptTreatThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvPedsFifteenInterruptTreatThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false "
//		                + "AND pp.void_reason is null AND ord.discontinued_date BETWEEN '" + startDate + "' AND '" + endDate
//		                + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " AND ord.discontinued = true AND ord.voided = FALSE AND ord.void_reason is null "
//		                + "group by pp.patient_id");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) lost to followup (>3 months)
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsLostFollowupMoreThreeMonths(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvPedsLostFollowupMoreThreeMonths(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have been transferred in this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredInThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvPedsTransferredInThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of ARV patients (age <15) who have been transferred out this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#arvPedsTransferredOutThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int arvPedsTransferredOutThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of female adult patients (age 15 or older) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleMoreThanFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int femaleMoreThanFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'F' "
//		                + "AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of female patients on treatment 12 months after initiation of ARVs
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femaleOnTreatTwelveAfterInitArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int femaleOnTreatTwelveAfterInitArv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of female pediatric patients (age <15 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#femalePedsUnderFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int femalePedsUnderFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'F' AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of male adult patients (age 15 or older) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleMoreThanFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int maleMoreThanFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) >= "
//		                + ConstantValues.FIFTEEN_YEARS + " AND per.gender = 'M' "
//		                + "AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of male patients on treatment 12 months after initiation of ARVs
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#maleOnTreatTwelveAfterInitArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int maleOnTreatTwelveAfterInitArv(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of male pediatric patients (age <15 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#malePedsUnderFifteenCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int malePedsUnderFifteenCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'M' AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage undefined this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultUndefinedWhoStageThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newAdultUndefinedWhoStageThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id " + "WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded =" + ConstantValues.UNKNOWN
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 4 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageFourThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newAdultWhoStageFourThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_FOUR_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 1 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageOneThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newAdultWhoStageOneThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_ONE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 3 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageThreeThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newAdultWhoStageThreeThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_THREE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new adult patients who are WHO stage 2 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newAdultWhoStageTwoThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newAdultWhoStageTwoThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_TWO_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)>="
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new female adult patients (age 15+) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemaleAdultStartiArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newFemaleAdultStartiArvThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'F' "
//		                + "AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null GROUP BY pp.patient_id");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new female pediatric patients (age <15 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newFemalePedsUnderFifteenStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newFemalePedsUnderFifteenStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                             String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new male adult patients (age 15+) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMaleAdultStartiArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newMaleAdultStartiArvThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id "
//		                + "WHERE DATEDIFF(CURDATE(), per.birthdate) >= " + ConstantValues.FIFTEEN_YEARS
//		                + " AND per.gender = 'M' "
//		                + "AND pp.voided = false AND pp.void_reason is null AND (select MIN(ord.start_date)) BETWEEN '"
//		                + startDate + "' AND '" + endDate + "' AND ord.concept_id IN " + ConstantValues.LIST_OF_ARV_DRUGS
//		                + " " + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null GROUP BY pp.patient_id");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new male pediatric patients (age <15 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newMalePedsUnderFifteenStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newMalePedsUnderFifteenStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                           String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients whose WHO Stage is undefined this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUndefinedWhoStageThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsUndefinedWhoStageThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id " + "WHERE obs.concept_id IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded =" + ConstantValues.UNKNOWN
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients (<18 months) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderEighteenMonthStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsUnderEighteenMonthStartArvThisMonth(SessionFactory sessionFactory, String startDate,
//	                                                             String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients (age <5 years) starting ARV treatment this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsUnderFiveStartArvThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsUnderFiveStartArvThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
//		
//		int indicator = 0;
//		
//		//		Session session = sessionFactory.getCurrentSession();
//		//		indicator records = session.createSQLQuery("query string").list().size();
//		
//		//Getting the size of the returned LIST which equals to the COUNTS needed
//		List<Object[]> records = ExecuteHibernateQueryLanguageUtil
//		        .executeHqlIndicators("SELECT pp.patient_id FROM patient_program pp "
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
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO stage 4 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageFourThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsWhoStageFourThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_FOUR_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 1 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageOneThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsWhoStageOneThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_ONE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 3 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageThreeThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsWhoStageThreeThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_THREE_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Number of new pediatric patients who are WHO Stage 2 this month
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#newPedsWhoStageTwoThisMonth(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int newPedsWhoStageTwoThisMonth(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN obs ON obs.person_id = per.person_id WHERE obs.concept_id " + "IN ("
//		                + ConstantValues.WHO_STAGE + "," + ConstantValues.CURRENT_WHO_HIV_STAGE + ","
//		                + ConstantValues.WHO_STAGE_AT_TRANSFER_IN + ") AND obs.value_coded ="
//		                + ConstantValues.WHO_STAGE_TWO_ADULT
//		                + " AND obs.voided = false AND obs.void_reason is null AND pro.concept_id ="
//		                + ConstantValues.HIV_PROGRAM_ID + " AND DATEDIFF(CURDATE(), per.birthdate)<"
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients who are on First Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnFirstLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedsOnFirstLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_FIRST_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason IS NULL AND ord.discontinued = FALSE "
//		                + "AND ord.discontinued_reason IS NULL AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = FALSE AND pp.void_reason IS NULL");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients who are on Second Line Regimen
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsOnSecondLineReg(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedsOnSecondLineReg(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id " + "WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_SECOND_LINE + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIFTEEN_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <18 months) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderEighteenMonthsCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedsUnderEighteenMonthsCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.EIGHTEEN_MONTH + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//	/**
//	 * Total number of pediatric patients (age <5 years) who are currently on ARV treatment
//	 * 
//	 * @see org.openmrs.module.tracnetreporting.service.TracNetIndicatorService#pedsUnderFiveCurrentOnArv(java.lang.String,
//	 *      java.lang.String)
//	 */
//	public static int pedsUnderFiveCurrentOnArv(SessionFactory sessionFactory, String startDate, String endDate) {
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
//		                + "INNER JOIN orders ord ON ord.patient_id = pat.patient_id "
//		                + "INNER JOIN program pro ON pro.program_id = pp.program_id "
//		                + "INNER JOIN person per ON per.person_id = pat.patient_id WHERE ord.concept_id IN "
//		                + ConstantValues.LIST_OF_ARV_DRUGS + " "
//		                + "AND ord.voided = false AND ord.void_reason is null AND ord.discontinued = false "
//		                + "AND ord.discontinued_reason is null AND DATEDIFF(CURDATE(), per.birthdate) < "
//		                + ConstantValues.FIVE_YEARS + " AND pp.voided = false AND pp.void_reason is null");
//		
//		indicator = records.size();
//		
//		return indicator;
//	}
//	
//}
