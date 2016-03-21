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
package org.openmrs.module.tracnetreporting;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.openmrs.api.context.Context;

/**
 *
 */
public class GlobalProperties {
	

	
	public static String gpGetListOfARVsDrugs(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfARVsDrugs");
		 }
	
	public static List<Integer> gpGetListOfARVsDrugsAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfARVsDrugs(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	public static String gpGetListOfFirstLineDrugs(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfFirstLineDrugs");
		 }
	
	public static List<Integer> gpGetListOfFirstLineDrugsAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfFirstLineDrugs(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	public static String gpGetListOfSecondLineDrugs(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfSecondLineDrugs");
		 }
	
	public static List<Integer> gpGetListOfSecondLineDrugsAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfSecondLineDrugs(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	
	public static String gpGetListOfTBDrugs(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfTBDrugs");
		 }
	
	public static List<Integer> gpGetListOfTBDrugsAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfTBDrugs(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }

	
	public static String gpGetListOfAnswersToResultOfHIVTest(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfAnswersToResultOfHIVTest");
		 }
	
	public static List<Integer> gpGetListOfAnswersToResultOfHIVTestAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfAnswersToResultOfHIVTest(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	public static String gpGetListOfAnswersToRapidPlasminReagent(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfAnswersToRapidPlasminReagent");
		 }
	
	public static List<Integer> gpGetListOfAnswersToRapidPlasminReagentAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfAnswersToRapidPlasminReagent(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	
	public static String gpGetreactiveAsfAnswerToRapidPlasminReagentConceptIdConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.reactiveAsfAnswerToRapidPlasminReagentConceptId");
		 }
	
	public static String gpGetExitFromCareConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.exitFromCareConceptId");
		 }
	
	public static String gpGetExitFromCareDiedConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.exitFromCareDiedConceptId");
		 }
	
	public static String gpGetExitFromTransferredOutConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.exitFromCareTransferredOutConceptId");
		 }
	
	public static String gpGetTransferredInConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.transferredInConceptId");
		 }
	
	public static String gpGetWeightConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.weightConceptId");
		 }
	
	public static String gpGetHeightConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.heightConceptId");
		 }
	
	public static String gpGetYesAsAnswerToTransferredInConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.yesAsAnswerToTransferredInConceptId");
		 }
	
	public static String gpGetResultForHIVTestConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.resultForHIVTestConceptId");
		 }
	
	public static String gpGetPositiveAsResultToHIVTestConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.positiveConceptId");
		 }
	
	public static String gpGetNegativeAsResultToHIVTestConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.negativeConceptId");
		 }
	
	public static String gpGetIndeterminateAsResultToHIVTestConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.indeterminateConceptId");
		 }
	
	public static String gpGetDateResultOfHIVTestReceivedConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.dateResultOfHIVTestReceived");
		 }
	
	public static String gpGetCD4CountConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.cd4CountConceptId");
		 }
	
	public static String gpGetTBScreeningConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.tbScreeningConceptId");
		 }
	
	public static String gpGetWhoStageConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageConceptId");
		 }
	
	public static String gpGetCurrentWhoHIVStageConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.currentWhoHIVStageConceptId");
		 }
	
	public static String gpGetWhoStageAtTransferInConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageAtTransferInConceptId");
		 }
	
	public static String gpGetWhoStageOneAdultConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageOneAdultConceptId");
		 }
	
	public static String gpGetWhoStageTwoAdultConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageTwoAdultConceptId");
		 }
	
	public static String gpGetWhoStageThreeAdultConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageThreeAdultConceptId");
		 }
	
	public static String gpGetWhoStageFourAdultConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageFourAdultConceptId");
		 }
	
	public static String gpGetWhoStageOnePedsConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageOnePedsConceptId");
		 }
	
	public static String gpGetWhoStageTwoPedsConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageTwoPedsConceptId");
		 }
	public static String gpGetWhoStageThreePedsConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageThreePedsConceptId");
		 }
	public static String gpGetWhoStageFourPedsConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.whoStageFourPedsConceptId");
		 }
	public static String gpGetUnknownStageConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.unknownWhoStageConceptId");
		 }
	
	
	public static String gpGetHIVProgramId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.hivProgramId");
		 }
	
	public static String gpGetPMTCTProgramId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.pmtctProgramId");
		 }
	
	public static String gpGetPCREncounterId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.pcrEncounterId");
		 }
	
	public static String gpGetSerologyAt9MonthId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.serologyTestAt9MonthId");
		 }
	
	public static String gpGetSerologyAt18MonthId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.serologyTestAt18MonthId");
		 }
	
	public static String gpGetCPNEncounterId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.cpnEncounterId");
		 }
	
	public static String gpGetMaternityEncounterId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.maternityEncounterId");
		 }
	
	public static String gpGetMotherFollowUpEncounterId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.motherFollowUpEncounterId");
		 }
	
	public static String gpGetRapidPlasminReagentConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.rapidPlasminReagentConceptId");
		 }
	
	public static String gpGetReturnVisitDateConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.returnVisitDateConceptId");
		 }
	
	public static String gpGetBreastedExclusivelyConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.breastedExclusivelyConceptId");
		 }
	
	public static String gpGetInfantFeedingMethodConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.infantFeedingMethodConceptId");
		 }
	
	public static String gpGetBreastedPredominatelyConceptIdConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.breastedPredominatelyConceptId");
		 }
	
	public static String gpGetUsingFormulaConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.usingFormulaConceptId");
		 }
	
	public static String gpGetMixedFeedingConceptIdConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.mixedFeedingConceptId");
		 }
	
	public static String gpGetTestingStatusOfPartnerConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.testingStatusOfPartnerConceptId");
		 }
	
	public static String gpGetEstimatedDateOfConfinementConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.estimatedDateOfConfinement");
		 }
	
	public static String gpGetDateOfConfinementConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.dateOfConfinement");
		 }
	
	public static String gpGetHIVTestInDeliveryRoomConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.HIVTestInDeliveryRoom");
		 }
	
	public static String gpGetOpportunisticInfectionsConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.OpportunisticInfectionsConceptId");
		 }
	
	public static String gpGetOpportunisticInfectionSTIConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.OpportunisticInfectionSTIConceptId");
		 }
	
	public static String gpGetEndDateOfOpportunisticInfectionSTIConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.EndDateOfOpportunisticInfectionSTIConceptId");
		 }
	
	public static String gpGetOpportunisticInfectionTBConceptId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.OpportunisticInfectionTBConceptId");
		 }
	
	
	


	/*Family planning Data Elements*/
	

	public static String listOfAnswerStatusOfPArtnerId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfAnswerStatusOfPArtnerId");
		 }
	public static List<Integer> gpGetlistOfAnswerStatusOfPArtnerAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(listOfAnswerStatusOfPArtnerId(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	 public static String gpGetMethodOfFamilyPlanningId(){
			return Context.getAdministrationService().getGlobalProperty("tracnetreporting.methodOfFamilyPlanningId");
			 }
	 
	 public static String gpGetEstimatedDateOfCOnfinementId(){
			return Context.getAdministrationService().getGlobalProperty("tracnetreporting.estimatedDateOfCOnfinementId");
			 }
	 
	 public static String gpGetMarriedOrLivingWithPartnerId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.marriedOrLivingWithPartnerId");
		 }
	 
	 public static String gpGetCivilStatusId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.civilStatusId");
		 }
	 
	 public static String gpGetLivingWithPartnerId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.livingWithPartnerId");
		 }
	 
	 public static String gpGetInjectableContraceptivesId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.injectableContraceptivesId");
		 }
	 
	 public static String gpGetOralContraceptionId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.oralContraceptionId");
		 }
	 
	 public static String gpGetCondomsId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.condomsId");
		 }
	 public static String gpGetTestingStatusOfPartnerId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.testingStatusOfPartnerId");
		 }
	 public static String gpGetDispositionId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.dispositionId");
		 }
	 public static String gpGetReferredForFamilyPlanningId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.referredForFamilyPlanningId");
		 }
	
   /*VCT and PIT data element*/ 

	public static String gpGetFifteenYearsId(){
	   return Context.getAdministrationService().getGlobalProperty("tracnetreporting.fifteenYearsId");
			}

	public static String gpGetTwentyFourYearsId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.twentyFourYearsId");
		 }
	

	public static String gpGetTwentyFiveYearsId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.twentyFiveYearsId");
		 }
	
		
	 
	/*PEP data Element*/
	
	public static String gpGetListOfProphylaxisDrugs(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.listOfProphylaxisDrugs");
		 }
	
	
	public static List<Integer> gpGetListOfProphylaxisDrugsAsIntegers(){
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  StringTokenizer tokenizer = new StringTokenizer(gpGetListOfProphylaxisDrugs(),",");
		  while (tokenizer.hasMoreTokens()) {
		   Integer id = Integer.parseInt(tokenizer.nextToken());
		         list.add(id);
		        }
		  return list;
		 }
	
	
	public static String gpGetPEPProgramId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.pepProgramId");
		 }
	
	public static String gpGetReasonpatientStartedArvsForProphylaxisId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.reasonpatientStartedArvsForProphylaxisId");
		 }
	
	public static String gpGetExposureToBloodOrBloodProductId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.exposureToBloodOrBloodProductId");
		 }
	
	public static String gpGetSexualAssaultId(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.sexualAssaultId");
		 }
	
	public static String gpGetSexualContactWithHivPositivePatient(){
		  return Context.getAdministrationService().getGlobalProperty("tracnetreporting.sexualContactWithHivPositivePatient");
		 }
	
   
	

}
