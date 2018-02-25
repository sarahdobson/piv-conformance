package gov.gsa.pivconformance.card.client;

public class APDUConstants {
    public static final byte COMMAND = 0x00;
    public static final byte COMMAND_CC = 0x10;
    public static final byte SELECT = (byte)0xa4;
    public static final byte[] PIV_APPID = { (byte)0xa0, 0x00, 0x00, 0x03, 0x08, 0x00, 0x00, 0x10, 0x00, 0x01, 0x00 };

    public static final int SUCCESSFUL_EXEC = 0x9000;

    public static final int APP_NOT_FOUND = 0x6A82;


    public static final String CARD_CAPABILITY_CONTAINER_OID = "2.16.840.1.101.3.7.1.219.0";
    public static final byte[] CARD_CAPABILITY_CONTAINER_TAG =  {0x5F, (byte)0xC1, 0x07};

    public static final String CARD_HOLDER_UNIQUE_IDENTIFIER_OID = "2.16.840.1.101.3.7.2.48.0";
    public static final byte[] CARD_HOLDER_UNIQUE_IDENTIFIER_TAG = {0x5F, (byte)0xC1, 0x02};

    public static final String X509_CERTIFICATE_FOR_PIV_AUTHENTICATION_OID = "2.16.840.1.101.3.7.2.1.1";
    public static final byte[] X509_CERTIFICATE_FOR_PIV_AUTHENTICATION_TAG = {0x5F, (byte)0xC1, 0x05};

    public static final String CARDHOLDER_FINGERPRINTS_OID = "2.16.840.1.101.3.7.2.96.16";
    public static final byte[] CARDHOLDER_FINGERPRINTS_TAG = {0x5F, (byte)0xC1, 0x03};


    public static final String SECURITY_OBJECT_OID = "2.16.840.1.101.3.7.2.144.0";
    public static final byte[] SECURITY_OBJECT_TAG = {0x5F, (byte)0xC1, 0x06};

    public static final String CARDHOLDER_FACIAL_IMAGE_OID = "2.16.840.1.101.3.7.2.96.48";
    public static final byte[] CARDHOLDER_FACIAL_IMAGE_TAG = {0x5F, (byte)0xC1, 0x08};

    public static final String X509_CERTIFICATE_FOR_CARD_AUTHENTICATION_OID = "2.16.840.1.101.3.7.2.5.0";
    public static final byte[] X509_CERTIFICATE_FOR_CARD_AUTHENTICATION_TAG = {0x5F, (byte)0xC1, 0x01};

    public static final String X509_CERTIFICATE_FOR_DIGITAL_SIGNATURE_OID = "2.16.840.1.101.3.7.2.1.0";
    public static final byte[] X509_CERTIFICATE_FOR_DIGITAL_SIGNATURE_TAG = {0x5F, (byte)0xC1, 0x0A};


    public static final String X509_CERTIFICATE_FOR_KEY_MANAGEMENT_OID = "2.16.840.1.101.3.7.2.1.2";
    public static final byte[] X509_CERTIFICATE_FOR_KEY_MANAGEMENT_TAG = {0x5F, (byte)0xC1, 0x0B};


    public static final String PRINTED_INFORMATION_OID = "2.16.840.1.101.3.7.2.48.1";
    public static final byte[] PRINTED_INFORMATION_TAG = {0x5F, (byte)0xC1, 0x09};


    public static final String DISCOVERY_OBJECT_OID = "2.16.840.1.101.3.7.2.96.80";
    public static final byte[] DISCOVERY_OBJECT_TAG = {0x7E};


    public static final String KEY_HISTORY_OBJECT_OID = "2.16.840.1.101.3.7.2.96.96";
    public static final byte[] KEY_HISTORY_OBJECT_TAG = {0x5F, (byte)0xC1, 0x0C};


    public static final String CARDHOLDER_IRIS_IMAGES_OID = "2.16.840.1.101.3.7.2.16.21";
    public static final byte[] CARDHOLDER_IRIS_IMAGES_TAG = {0x5F, (byte)0xC1, 0x21};


    public static final String BIOMETRIC_INFORMATION_TEMPLATES_GROUP_TEMPLATE_OID = "2.16.840.1.101.3.7.2.16.22";
    public static final byte[] BIOMETRIC_INFORMATION_TEMPLATES_GROUP_TEMPLATE_TAG = {0x7F, 0x61};


    public static final String SECURE_MESSAGING_CERTIFICATE_SIGNER_OID = "2.16.840.1.101.3.7.2.16.23";
    public static final byte[] SECURE_MESSAGING_CERTIFICATE_SIGNER_TAG = {0x5F, (byte)0xC1, 0x22};


    public static final String PAIRING_CODE_REFERENCE_DATA_CONTAINER_OID = "2.16.840.1.101.3.7.2.16.24";
    public static final byte[] PAIRING_CODE_REFERENCE_DATA_CONTAINER_TAG = {0x5F, (byte)0xC1, 0x23};

}