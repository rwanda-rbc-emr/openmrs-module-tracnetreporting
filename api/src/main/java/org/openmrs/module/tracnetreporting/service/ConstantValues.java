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

/**
 * Constants used in Patients Alerts Module. This file should contain all
 * Concepts IDs needed to match all useful conditions in order to determine
 * alerts to be displayed when a patient matches those conditions.
 */
public class ConstantValues {

	// ------------- AGE CATEGORIES ------------------

	public static final int EIGHTEEN_MONTH = 2;

	public static final int FIVE_YEARS = 5;

	public static final int FIFTEEN_YEARS = 15;

	public static final int ONE_YEAR = 365;
	
    //public static final int FIFTEEN_YEARS = 5475;
	
	public static final int TWENTY_FOUR_YEARS=8760;
	
	public static final int TWENTY_FIVE_YEARS=9125;

	// ------------- OTHERS CONCEPTS -----------------

	public static final int PROPHYLAXIS_STARTED = 2740;

	//public static final int HIV_PROGRAM_ID = 1482;
	
	public static final int HIV_PROGRAM_ID = 2;
	
	//public static final int ADULT_HIV_PROGRAM_ID = 3;
	
	//public static final int PEDS_HIV_PROGRAM_ID = 10;

	public static final int TUBERCULOSIS = 58;

	public static final int COTRIMOXAZOLE = 916;

	public static final int DAPSONE = 92;

	public static final int DIFLUCAN = 747;

	public static final int TB_TREATEMENT = 2690;

	public static final int SEVERE_MALNUTRITION = 1313;

	public static final int TB_SCREENING = 2136;

	public static final int POSITIVE_ID = 703;
	
	public static final int NEGATIVE_ID = 664;

	public static final int REASON_FOR_EXITING_CARE = 1811;

	public static final int PATIENT_DIED = 1742;

	public static final int TRANSFERRED_OUT = 1744;

	public static final int TRANSFERRED_IN = 2536;

	public static final int YES = 1065;

	public static final int NEXT_SCHEDULED_VISIT = 5096;
	
	public static final int RESULT_OF_HIV_TEST = 2169;
	
	public static final int DATE_RESULT_OF_HIV_TEST_RECEIVED = 6251;
	
	public static final String LIST_OF_ANSWERS_TO_RESULT_OF_HIV_TEST = " (664, 703) " ;
	
	public static final String LIST_OF_ANSWERS_TO_RAPID_PLASMIN_REAGENT = "(1228,1229)";
	
	public static final int RAPID_PLASMIN_REAGENT = 1478;
	
	public static final int REACTIVE = 1228;
	
	public static final int NON_REACTIVE = 1229;
	
	public static final int HIV_TEST_IN_DELIVERY_ROOM = 6247;
	
	public static final int Breastfeeding = 5526;
	
	public static final int InfantFormula = 5254;
	
	public static final int PCRTest = 7;
	
	public static final int SerologyAt9Month = 8;
	
	public static final int SerologyAt18Month = 9;
	
	public static final int ReasonForExitingCareDied = 1742;

	// ------------- ARVs DRUGS -----------------

	public static final int ANTIRETROVIRAL_DRUGS = 1085;

	public static final int ABACAVIR = 814;

	public static final int DIDANOSINE = 796;

	public static final int EFAVIRENZ = 633;

	public static final int LAMIVUDINE = 628;

	public static final int LOPINAVIR_AND_RITONAVIR = 794;

	public static final int NELFINAVIR = 635;

	public static final int NEVIRAPINE = 631;

	public static final int OTHER_ANTIRETROVIRAL_DRUG = 5424;

	public static final int STAVUDINE = 625;

	public static final int STAVUDINE_LAMIVUDINE_AND_NEVIRAPINE = 792;

	public static final int TENOFOVIR = 802;

	public static final int UNKNOWN_ANTIRETROVIRAL_DRUG = 5811;

	public static final int ZIDOVUDINE = 797;

	public static final int ZIDOVUDINE_AND_LAMIVUDINE = 630;

	public static final int STAVUDINE_AND_LAMIVUDINE = 2833;

	public static final int STAVUDINE_LAMIVUDINE_AND_ABACAVIR = 2203;

	public static final int STAVUDINE_LAMIVUDINE_AND_EFAVIRENZ = 1613;

	public static final String LIST_OF_ARV_DRUGS = "(633,814,796,628,749,794,795,635,631,5424,625,792,802,5811,797,630,2833,2203,1613)";

	public static final String LIST_OF_FIRST_LINE = "(633,814,796,628,635,631,5424,625,792,802,5811,797,630,2833,2203,1613)";

	public static final int RITONAVIR = 795;

	public static final int INDINAVIR = 749;

	public static final int KALETRA = 794;

	public static final String LIST_OF_SECOND_LINE = "(795,749,794)";
	
	public static final String LIST_OF_PROPHYLAXIS_DRUGS = "(916,747,92)";

	// ---------------- WHO STAGES ------------------------

	public static final int WHO_STAGE = 1480;

	public static final int CURRENT_WHO_HIV_STAGE = 5356;

	public static final int WHO_STAGE_AT_TRANSFER_IN = 2527;

	public static final int UNKNOWN = 1067;

	public static final int WHO_STAGE_ONE_ADULT = 1204;

	public static final int WHO_STAGE_TWO_ADULT = 1205;

	public static final int WHO_STAGE_THREE_ADULT = 1206;

	public static final int WHO_STAGE_FOUR_ADULT = 1207;

	public static final int WHO_STAGE_ONE_PEDS = 1220;

	public static final int WHO_STAGE_TWO_PEDS = 1221;

	public static final int WHO_STAGE_THREE_PEDS = 1222;

	public static final int WHO_STAGE_FOUR_PEDS = 1223;
	
	public static final String LIST_OF_ANSWERS_TO_ADULT_WHO_STAGE = " (1204, 1205, 1206) " ;

	// ----------------------------------------------------
	public static final int METHOD_OF_FAMILY_PLANNING_ID = 374;

	public static final int NONE = 1107;

	public static final int NOT_APPLICABLE = 1175;

	public static final int NORPLANT = 1718;

	public static final int TUBAL_LIGATION = 1719;

	public static final int ABSTINENCE = 1720;

	public static final int VASECTOMY = 1721;

	public static final int CONDOMS = 190;

	public static final int STERILIZATION = 2712;

	public static final int INTRAUTERINE_DEVICE = 5275;

	public static final int FEMALE_STERILIZATION = 5276;

	public static final int NATURAL_FAMILY_PLANNING = 5277;

	public static final int DIAPHRAGM = 5278;

	public static final int INJECTABLE_CONTRACEPTIVES = 5279;

	public static final int OTHER_NON_CODED = 5622;

	public static final int ORAL_CONTRACEPTION = 780;

	public static final int MEDROXYPROGESTERONE_ACETATE = 907;

	// ------------------------------------------------------

	public static final int VISIT_DATE_ID = 5096;

	public static final int CD4_COUNT_CONCEPT_ID = 5497;

	public static final int PMTCT_PROGRAM_ID = 1647;

	public static final int TB_PROGRAM_ID = 1648;

	public static final int MDR_TB_PROGRAM_ID = 2842;

	public static final int TB_CONCEPT_ID = 58;

	public static final int CHILD_OF_HIV_PATIENT_IN_CLINIC_ID = 1153;

	public static final int MARRIED_OR_LIVING_WITH_PARTNER_ID = 1055;

	public static final int OUTPATIENT_DIAGNOSIS_ID = 3065;

	public static final int SEXUALLY_TRANSMITTED_INFECTION = 174;
	
	public static final int ESTIMATED_DATE_OF_CONFINEMENT = 5596;

	// ---------------------------------------------------------

	public static final int TESTING_STATUS_OF_PARTNER_ID = 3082;

	public static final int[] LIST_OF_ANSWERS_STATUS_OF_PARTNER = { 1067, 3083,
			2416, 2224 };

	public static final int WAITING_FOR_TEST_RESULTS = 2224;

	public static final int DID_NOT_ANSWER = 2416;

	public static final int NO_TEST = 3083;

	// ---------------------------------------------------------

	public static final int NUMBER_OF_BIRTHS_ID = 1053;

	public static final int NUMBER_OF_PREGNANCIES_ID = 5624;

	public static final int STI_ID = 174;

	public static final int TB_TREATMENT_ID = 2690;

	public static final int SIMPLE_TUBERCULOSIS_TEST_RESULT = 3495;

	public static final int[] LIST_OF_ANSWERS_TO_TB_TREATMENT = { 2126, 2691,
			2692, 5622 };

	public static final int[] LIST_OF_ANSWERS_TO_TB_TEST = { 1067, 664, 703 };

	public static final int UNKNOWN_ID = 1067;

	public static final int CIVIL_STATUS_ID = 1054;

	public static final int MARRIED_ID = 5555;

	public static final int LIVING_WITH_PARTNER_ID = 1060;

	// ---------------------- TB Drugs ----------------------------------

	public static final int TB_TREATMENT_DRUGS = 1159;

	public static final int TUBERCULOSIS_DRUGS = 3014;

	public static final int ISONIAZID = 656;

	public static final int RIFAMPICIN = 767;

	public static final int ETHAMBUTOL = 745;

	public static final int PYRAZINAMIDE = 5829;

	public static final int STREPTOMYCIN = 438;

	public static final int CAPREOMYCIN = 1411;

	public static final int KANAMYCIN = 1417;

	public static final int AMIKACIN = 1406;

	public static final int CIPROFLOXACIN = 740;

	public static final int OFLOXACIN = 1418;

	public static final int LEVOFLOXACIN = 755;

	public static final int MOXIFLOXACIN = 955;

	public static final int ETHIONAMIDE = 1414;

	public static final int CYCLOSERINE = 1413;

	public static final int P_AMINOSALICYLIC_ACID = 1419;

	public static final int PROTHIONAMIDE = 1415;

	public static final int THIOACETAZONE = 1633;

	public static final int AMOXICILLIN_AND_CLAVULANIC_ACID = 450;

	public static final int CLARITHROMYCIN = 2459;

	public static final int RIFABUTINE = 2460;

	public static final String LIST_OF_TB_DRUGS = "(656, 767, 745, 5829, 438, 1411, 1417, "
			+ "1406, 740, 1418, 755, 955, 1414,1413, 1419, 1415, 1633, 450, 2459, 2460)";

	// --------------- Lab concept ids ----------------//

	public static final int INTERNATIONAL_NORMALIZED_RATION = 3796;

	public static final int HEMATOCRIT = 1015;

	public static final int HEMOGLOBIN = 21;

	public static final int MEAN_CORPUSCULAR_VOLUME = 851;

	public static final int MEAN_CORPUSCULAR_HEMOGLOBIN = 1018;

	public static final int RED_BLOOD_CELLS = 679;

	public static final int RED_CELL_DISTRIBUTION_WIDTH = 1016;

	public static final int ERYTHROCYTE_SEDIMENTATION_RATE = 855;

	public static final int WHITE_BLOOD_CELL_COUNT = 678;

	public static final int BASOPHILIS = 1025;

	public static final int EOSINOPHILS = 1026;

	public static final int LYMPHOCYTES = 1021;

	public static final int MONOCYTES = 1023;

	public static final int SERUM_ALBUMIN = 848;

	public static final int ALKALINE_PHOSPATASE = 785;

	public static final int ALANINE_AMINOTRANSFERASE = 654;

	public static final int ASPARTATE_AMINOTRANSFERASE = 653;

	public static final int SERUM_AMYLASE = 3054;

	public static final int BICARBONATE = 1626;

	public static final int SERUM_CALCIUM = 3056;

	public static final int SERUM_CHLORIDE = 1134;

	public static final int CHOLESTEROL = 1006;

	public static final int LOW_DENSITY_LIPOPROTEIN = 1008;

	public static final int HIGH_DENSITY_LIPOPROTEIN = 1007;

	public static final int CREATINE_KINASE = 1011;

	public static final int LACTATE_DEHYDROGENASE = 1014;

	public static final int LIPASE = 1013;

	public static final int SERUM_MAGNESIUM = 3058;

	public static final int OXYGEN_SATURATION = 5092;

	public static final int TOTAL_PROTEIN = 717;

	public static final int SERUM_SODIUM = 1132;

	public static final int THYROID_STIMULATING_HORMONE = 1624;

	public static final int TRIGLYCERIDES = 1009;

	public static final int UREA_NITROGEN = 857;

	public static final int URIC_ACID = 3055;

	public static final int GLUCOSE = 887;
	
	
	 // --------------- Family planning concept ids ----------------//
	    
		public static final int DISPOSITION =3798;
		
		public static final int REFERRED_FOR_FAMILY_PLANNING = 6734;

		public static final int METHOD_OF_ENROLLMENT = 1650;

		public static final int VCT_PROGRAM_ID = 1649;
		
		//public static final int 
		
		//-------------PEP concept ids----------------------------//
		
		public static final int PEP_PROGRAM_ID=822;
		
		public static final int HIV_INFECTED=1169;
		
		public static final int SEXUAL_ASSAULT=165;
		
	   

}
