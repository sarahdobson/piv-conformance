package gov.gsa.pivconformance.card.client;

import gov.gsa.pivconformance.tlv.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Encapsulates a Key History data object  as defined by SP800-73-4 Part 2 Appendix A Table 19
 *
 */
public class KeyHistoryObject extends PIVDataObject {
    // slf4j will thunk this through to an appropriately configured logging library
    private static final Logger s_logger = LoggerFactory.getLogger(KeyHistoryObject.class);
    private int m_keysWithOnCardCerts = 0;
    private int m_keysWithOffCardCerts = 0;
    private byte[] m_offCardCertUrl;

    /**
     *
     * Returns Integer containing keysWithOnCardCerts value
     *
     * @return Integer containing keysWithOnCardCerts value
     */
    public int getKeysWithOnCardCerts() {
        return m_keysWithOnCardCerts;
    }

    /**
     *
     * Sets the keysWithOnCardCerts value
     *
     * @param keysWithOnCardCerts Integer containing keysWithOnCardCerts value
     */
    public void setKeysWithOnCardCerts(int keysWithOnCardCerts) {
        m_keysWithOnCardCerts = keysWithOnCardCerts;
    }

    /**
     *
     * Returns Integer containing keysWithOffCardCerts value
     *
     * @return Integer containing keysWithOffCardCerts value
     */
    public int getKeysWithOffCardCerts() {
        return m_keysWithOffCardCerts;
    }

    /**
     *
     * Sets the keysWithOffCardCerts value
     *
     * @param keysWithOffCardCerts Integer containing keysWithOffCardCerts value
     */
    public void setKeysWithOffCardCerts(int keysWithOffCardCerts) {
        m_keysWithOffCardCerts = keysWithOffCardCerts;
    }

    /**
     *
     * Returns byte array containing offCardCertUrl value
     *
     * @return Byte array containing offCardCertUrl value
     */
    public byte[] getOffCardCertUrl() {
        return m_offCardCertUrl;
    }


    /**
     *
     * Sets the offCardCertUrl value
     *
     * @param offCardCertUrl Byte array containing offCardCertUrl value
     */
    public void setOffCardCertUrl(byte[] offCardCertUrl) {
        m_offCardCertUrl = offCardCertUrl;
    }

    /**
     *
     * Decode function that decodes Key History Object retrieved from the card and populates various class fields.
     *
     * @return True if decode was successful, false otherwise
     */
    public boolean decode() {
        byte[] rawBytes = this.getBytes();
        BerTlvParser tlvp = new BerTlvParser(new CCTTlvLogger(this.getClass()));
        BerTlvs outer = tlvp.parse(rawBytes);
        List<BerTlv> outerTlvs = outer.getList();
        if(outerTlvs.size() == 1 && outerTlvs.get(0).isTag(new BerTag(0x53))) {
            byte[] tlvBuf = outerTlvs.get(0).getBytesValue();
            outer = tlvp.parse(tlvBuf);
        }
        for(BerTlv tlv : outer.getList()) {
            byte[] tag = tlv.getTag().bytes;
            if(Arrays.equals(tag, TagConstants.KEYS_WITH_ON_CARD_CERTS)) {
                m_keysWithOnCardCerts = tlv.getIntValue();
            } else if(Arrays.equals(tag, TagConstants.KEYS_WITH_OFF_CARD_CERTS)) {
                m_keysWithOffCardCerts = tlv.getIntValue();
            } else if(Arrays.equals(tag, TagConstants.OFF_CARD_CERT_URL)) {
                m_offCardCertUrl = tlv.getBytesValue();
            }else if(!Arrays.equals(tag, TagConstants.ERROR_DETECTION_CODE_TAG) && tlv.getBytesValue().length != 0) {
                s_logger.warn("Unexpected tag: {} with value: {}", Hex.encodeHexString(tlv.getTag().bytes), Hex.encodeHexString(tlv.getBytesValue()));
            }
            s_logger.debug("found tag: {}", Hex.encodeHexString(tag));
        }

        if (m_keysWithOnCardCerts == 0 || m_keysWithOffCardCerts == 0)
            return false;

        return true;
    }
}
