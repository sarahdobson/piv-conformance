package gov.gsa.conformancelib.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import gov.gsa.conformancelib.configuration.CardSettingsSingleton;
import gov.gsa.conformancelib.configuration.CardSettingsSingleton.LOGIN_STATUS;
import gov.gsa.conformancelib.utilities.CardUtils;
import gov.gsa.pivconformance.card.client.APDUConstants;
import gov.gsa.pivconformance.card.client.AbstractPIVApplication;
import gov.gsa.pivconformance.card.client.CardCapabilityContainer;
import gov.gsa.pivconformance.card.client.SecurityObject;
import gov.gsa.pivconformance.card.client.CardHandle;
import gov.gsa.pivconformance.card.client.CardHolderUniqueIdentifier;
import gov.gsa.pivconformance.card.client.CardholderBiometricData;
import gov.gsa.pivconformance.card.client.DiscoveryObject;
import gov.gsa.pivconformance.card.client.MiddlewareStatus;
import gov.gsa.pivconformance.card.client.PIVDataObject;
import gov.gsa.pivconformance.card.client.PIVDataObjectFactory;
import gov.gsa.pivconformance.card.client.PrintedInformation;
import gov.gsa.pivconformance.tlv.BerTag;
import gov.gsa.pivconformance.tlv.TagConstants;

public class SP800_73_4SecurityObjectTests {

	//Security Object blob no larger than 1008 bytes
	@DisplayName("SP800-73-4.33 test")
	@ParameterizedTest(name = "{index} => oid = {0}")
	@MethodSource("sp800_73_4_SecurityObjectTestProvider")
	void sp800_73_4_Test_33(String oid, TestReporter reporter) {
		assertNotNull(oid);
		CardSettingsSingleton css = CardSettingsSingleton.getInstance();
		assertNotNull(css);
		if (css.getLastLoginStatus() == LOGIN_STATUS.LOGIN_FAIL) {
			ConformanceTestException e = new ConformanceTestException(
					"Login has already been attempted and failed. Not trying again.");
			fail(e);
		}
		try {
			
			CardUtils.setUpPivAppHandleInSingleton();
			CardUtils.authenticateInSingleton(false);
		} catch (ConformanceTestException e) {
			fail(e);
		}

		// Get card handle and PIV handle
		CardHandle ch = css.getCardHandle();
		AbstractPIVApplication piv = css.getPivHandle();

		// Created an object corresponding to the OID value
		PIVDataObject o = PIVDataObjectFactory.createDataObjectForOid(oid);
		assertNotNull(o);

		// Get data from the card corresponding to the OID value
		MiddlewareStatus result = piv.pivGetData(ch, oid, o);
		assertTrue(result == MiddlewareStatus.PIV_OK);

		byte[] bertlv = o.getBytes();
		assertNotNull(bertlv);

		//Confirm Security object blob is not larger than 120
		assertTrue(bertlv.length <= 1008);
	}

	//Tags 0xBA, 0xBB, 0XFE are present in that order
	@DisplayName("SP800-73-4.34 test")
    @ParameterizedTest(name = "{index} => oid = {0}")
    @MethodSource("sp800_73_4_SecurityObjectTestProvider")
    void sp800_73_4_Test_34(String oid, TestReporter reporter) {
        assertNotNull(oid);
        CardSettingsSingleton css = CardSettingsSingleton.getInstance();
        assertNotNull(css);
        if(css.getLastLoginStatus() == LOGIN_STATUS.LOGIN_FAIL) {
        	ConformanceTestException e  = new ConformanceTestException("Login has already been attempted and failed. Not trying again.");
        	fail(e);
        }
        try {
			
			CardUtils.setUpPivAppHandleInSingleton();
			CardUtils.authenticateInSingleton(false);
		} catch (ConformanceTestException e) {
			fail(e);
		}
        
        //Get card handle and PIV handle
        CardHandle ch = css.getCardHandle();
        AbstractPIVApplication piv = css.getPivHandle();
        
        //Created an object corresponding to the OID value
        PIVDataObject o = PIVDataObjectFactory.createDataObjectForOid(oid);
        assertNotNull(o);
    	
        //Get data from the card corresponding to the OID value
        MiddlewareStatus result = piv.pivGetData(ch, oid, o);
        assertTrue(result == MiddlewareStatus.PIV_OK);
        	
        
        boolean decoded = o.decode();
		assertTrue(decoded);
		
		//Get tag list
		List<BerTag> tagList = ((SecurityObject) o).getTagList();
		
		BerTag berMappingTag = new BerTag(TagConstants.MAPPING_OF_DG_TO_CONTAINER_ID_TAG);
		BerTag berSecurityObjectTag = new BerTag(TagConstants.SECURITY_OBJECT_TAG);
		BerTag berEDCTag = new BerTag(TagConstants.ERROR_DETECTION_CODE_TAG);
		
		//Confirm tags 0xBA, 0xBB, 0XFE are present
		assertTrue(tagList.contains(berMappingTag));
		assertTrue(tagList.contains(berSecurityObjectTag));
		assertTrue(tagList.contains(berEDCTag));
		
		int orgMappingTagIndex = tagList.indexOf(berMappingTag);
		
		//Confirm tags 0xBA, 0xBB, 0XFE are in right order
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex).bytes,TagConstants.MAPPING_OF_DG_TO_CONTAINER_ID_TAG));
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex+1).bytes,TagConstants.SECURITY_OBJECT_TAG));
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex+2).bytes,TagConstants.ERROR_DETECTION_CODE_TAG));
    }
	
	//No tags other than (0xBA, 0xBB, 0xFE) are present
	@DisplayName("SP800-73-4.35 test")
    @ParameterizedTest(name = "{index} => oid = {0}")
    @MethodSource("sp800_73_4_SecurityObjectTestProvider")
    void sp800_73_4_Test_35(String oid, TestReporter reporter) {
        assertNotNull(oid);
        CardSettingsSingleton css = CardSettingsSingleton.getInstance();
        assertNotNull(css);
        if(css.getLastLoginStatus() == LOGIN_STATUS.LOGIN_FAIL) {
        	ConformanceTestException e  = new ConformanceTestException("Login has already been attempted and failed. Not trying again.");
        	fail(e);
        }
        try {
			
			CardUtils.setUpPivAppHandleInSingleton();
			CardUtils.authenticateInSingleton(false);
		} catch (ConformanceTestException e) {
			fail(e);
		}
        
        //Get card handle and PIV handle
        CardHandle ch = css.getCardHandle();
        AbstractPIVApplication piv = css.getPivHandle();
        
        //Created an object corresponding to the OID value
        PIVDataObject o = PIVDataObjectFactory.createDataObjectForOid(oid);
        assertNotNull(o);
    	
        //Get data from the card corresponding to the OID value
        MiddlewareStatus result = piv.pivGetData(ch, oid, o);
        assertTrue(result == MiddlewareStatus.PIV_OK);
        	
        
        boolean decoded = o.decode();
		assertTrue(decoded);
				
		//Get tag list
		List<BerTag> tagList = ((SecurityObject) o).getTagList();
		
		BerTag berMappingTag = new BerTag(TagConstants.MAPPING_OF_DG_TO_CONTAINER_ID_TAG);
		BerTag berSecurityObjectTag = new BerTag(TagConstants.SECURITY_OBJECT_TAG);
		BerTag berEDCTag = new BerTag(TagConstants.ERROR_DETECTION_CODE_TAG);
		
		//Confirm tags 0x01, 0x02, 0x05, 0x06 are present
		assertTrue(tagList.contains(berMappingTag));
		assertTrue(tagList.contains(berSecurityObjectTag));
		assertTrue(tagList.contains(berEDCTag));
		
		
		//Confirm only 3 tags are present
		assertTrue(tagList.size() == 3);
		
		int orgMappingTagIndex = tagList.indexOf(berMappingTag);
		
		//Confirm tags 0x01, 0x02, 0x05, 0x06 are in right order
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex).bytes,TagConstants.MAPPING_OF_DG_TO_CONTAINER_ID_TAG));
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex+1).bytes,TagConstants.SECURITY_OBJECT_TAG));
		assertTrue(Arrays.equals(tagList.get(orgMappingTagIndex+2).bytes,TagConstants.ERROR_DETECTION_CODE_TAG));
    }
	
	//Parse data at tag 0xBA and for each data container found ensure that performing a select returns status words 0x90, 0x00
	@DisplayName("SP800-73-4.36 test")
    @ParameterizedTest(name = "{index} => oid = {0}")
    @MethodSource("sp800_73_4_SecurityObjectTestProvider")
    void sp800_73_4_Test_36(String oid, TestReporter reporter) {
        assertNotNull(oid);
        CardSettingsSingleton css = CardSettingsSingleton.getInstance();
        assertNotNull(css);
        if(css.getLastLoginStatus() == LOGIN_STATUS.LOGIN_FAIL) {
        	ConformanceTestException e  = new ConformanceTestException("Login has already been attempted and failed. Not trying again.");
        	fail(e);
        }
        try {
			
			CardUtils.setUpPivAppHandleInSingleton();
			CardUtils.authenticateInSingleton(false);
		} catch (ConformanceTestException e) {
			fail(e);
		}
        
        //Get card handle and PIV handle
        CardHandle ch = css.getCardHandle();
        AbstractPIVApplication piv = css.getPivHandle();
        
        //Created an object corresponding to the OID value
        PIVDataObject o = PIVDataObjectFactory.createDataObjectForOid(oid);
        assertNotNull(o);
    	
        //Get data from the card corresponding to the OID value
        MiddlewareStatus result = piv.pivGetData(ch, oid, o);
        assertTrue(result == MiddlewareStatus.PIV_OK);
        	
        
        boolean decoded = o.decode();
		assertTrue(decoded);
		
		HashMap<Integer, String> idList = ((SecurityObject) o).getContainerIDList();
		
		assertTrue(idList.size() > 0);
		
		for (HashMap.Entry<Integer,String> entry : idList.entrySet())  {
		
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 

            PIVDataObject tmpObj = PIVDataObjectFactory.createDataObjectForOid(entry.getValue());
            assertNotNull(tmpObj);
            
            result = piv.pivGetData(ch, entry.getValue(), tmpObj);
            assertTrue(result == MiddlewareStatus.PIV_OK);
		}

    }
	
	//For each container listed in the security object, calculate the hash of the data within that container and confirm that the actual 
	//hashes match those written to the security object
	@DisplayName("SP800-73-4.37 test")
    @ParameterizedTest(name = "{index} => oid = {0}")
    @MethodSource("sp800_73_4_SecurityObjectTestProvider")
    void sp800_73_4_Test_37(String oid, TestReporter reporter) {
        assertNotNull(oid);
        CardSettingsSingleton css = CardSettingsSingleton.getInstance();
        assertNotNull(css);
        if(css.getLastLoginStatus() == LOGIN_STATUS.LOGIN_FAIL) {
        	ConformanceTestException e  = new ConformanceTestException("Login has already been attempted and failed. Not trying again.");
        	fail(e);
        }
        try {
        	//css.setApplicationPin("123456");
			CardUtils.setUpPivAppHandleInSingleton();
			CardUtils.authenticateInSingleton(false);
		} catch (ConformanceTestException e) {
			fail(e);
		}
        
        //Get card handle and PIV handle
        CardHandle ch = css.getCardHandle();
        AbstractPIVApplication piv = css.getPivHandle();
        
        //Created an object corresponding to the OID value
        PIVDataObject o = PIVDataObjectFactory.createDataObjectForOid(oid);
        assertNotNull(o);
    	
        //Get data from the card corresponding to the OID value
        MiddlewareStatus result = piv.pivGetData(ch, oid, o);
        assertTrue(result == MiddlewareStatus.PIV_OK);
        	
        
        boolean decoded = o.decode();
		assertTrue(decoded);
		
		HashMap<String, byte[]> soDataElements = new  HashMap<String, byte[]>();
		
		HashMap<Integer, String> idList = ((SecurityObject) o).getContainerIDList();
		
		assertTrue(idList.size() > 0);
		
		for (HashMap.Entry<Integer,String> entry : idList.entrySet())  {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
            PIVDataObject dataObject = PIVDataObjectFactory.createDataObjectForOid(entry.getValue());
            result = piv.pivGetData(ch, entry.getValue(), dataObject);
            if(result != MiddlewareStatus.PIV_OK) continue;
            
            decoded = dataObject.decode();
    		assertTrue(decoded);
            
            if(entry.getValue().equals(APDUConstants.CARD_CAPABILITY_CONTAINER_OID)) {
        		soDataElements.put(APDUConstants.CARD_CAPABILITY_CONTAINER_OID, ((CardCapabilityContainer) dataObject).getSignedContent());
            } else if(entry.getValue().equals(APDUConstants.CARD_HOLDER_UNIQUE_IDENTIFIER_OID)) {
        		soDataElements.put(APDUConstants.CARD_HOLDER_UNIQUE_IDENTIFIER_OID, ((CardHolderUniqueIdentifier) dataObject).getChuidContainer());            	
            } else if(entry.getValue().equals(APDUConstants.CARDHOLDER_FINGERPRINTS_OID)) {
        		soDataElements.put(APDUConstants.CARDHOLDER_FINGERPRINTS_OID, ((CardholderBiometricData) dataObject).getCceffContainer());           	
            } else if(entry.getValue().equals(APDUConstants.CARDHOLDER_FACIAL_IMAGE_OID)) {
        		soDataElements.put(APDUConstants.CARDHOLDER_FACIAL_IMAGE_OID, ((CardholderBiometricData) dataObject).getCceffContainer());        	
            } else if(entry.getValue().equals(APDUConstants.PRINTED_INFORMATION_OID)) {
        		soDataElements.put(APDUConstants.PRINTED_INFORMATION_OID, ((PrintedInformation) dataObject).getSignedContent());       	
            } else if(entry.getValue().equals(APDUConstants.DISCOVERY_OBJECT_OID)) {
        		soDataElements.put(APDUConstants.DISCOVERY_OBJECT_OID, ((DiscoveryObject) dataObject).getSignedContent());       	
            } else if(entry.getValue().equals(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID)) {
        		soDataElements.put(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID, ((CardholderBiometricData) dataObject).getCceffContainer());        	
            } else if(entry.getValue().equals(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID)) {
        		soDataElements.put(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID, ((CardholderBiometricData) dataObject).getCceffContainer());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_1_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_1_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_2_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_2_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_3_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_3_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_4_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_4_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_5_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_5_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_6_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_6_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_7_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_7_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_8_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_8_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_9_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_9_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_10_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_10_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_11_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_11_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_12_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_12_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_13_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_13_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_14_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_14_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_15_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_15_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_16_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_16_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_17_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_17_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_18_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_18_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_19_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_19_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_20_OID)) {
                soDataElements.put(APDUConstants.RETIRED_X_509_CERTIFICATE_FOR_KEY_MANAGEMENT_20_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID)) {
                soDataElements.put(APDUConstants.CARDHOLDER_IRIS_IMAGES_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.BIOMETRIC_INFORMATION_TEMPLATES_GROUP_TEMPLATE_OID)) {
                soDataElements.put(APDUConstants.BIOMETRIC_INFORMATION_TEMPLATES_GROUP_TEMPLATE_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.SECURE_MESSAGING_CERTIFICATE_SIGNER_OID)) {
                soDataElements.put(APDUConstants.SECURE_MESSAGING_CERTIFICATE_SIGNER_OID, ((PIVDataObject) dataObject).getBytes());
            } else if(entry.getValue().equals(APDUConstants.PAIRING_CODE_REFERENCE_DATA_CONTAINER_OID)) {
                soDataElements.put(APDUConstants.PAIRING_CODE_REFERENCE_DATA_CONTAINER_OID, ((PIVDataObject) dataObject).getBytes());             
            }  else {
            	fail("Unrecongnized container OID (" + oid + ")");
            }
		}

		((SecurityObject) o).setMapOfDataElements(soDataElements);
		
		//Confirm that message digest from signed attributes bag matches the digest over Fingerprint biometric data (excluding contents of digital signature field) 
		boolean verified = ((SecurityObject) o).verifyHashes();
		assertTrue(verified);
    }
	
	private static Stream<Arguments> sp800_73_4_SecurityObjectTestProvider() {

		return Stream.of(Arguments.of(APDUConstants.SECURITY_OBJECT_OID));

	}
}
