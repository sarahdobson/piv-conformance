package gov.gsa.pivconformance.card.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gsa.pivconformance.card.client.APDUConstants;

public class ChainingAPDUTransmitter {
	
	private CardChannel m_channel = null;
    private static final Logger s_logger = LoggerFactory.getLogger(ChainingAPDUTransmitter.class);
    private static final Logger s_apduLogger = LoggerFactory.getLogger("gov.gsa.pivconformance.apdu");
	private static final Object logMessage = null;
	
	public ChainingAPDUTransmitter(CardChannel c) {
		m_channel = c;
	}
	
	protected RequestAPDUWrapper fixLengthExpected(RequestAPDUWrapper request, int correctLE) {
		int cla = request.getCla();
		int ins = request.getIns();
		int p1 = request.getP1();
		int p2 = request.getP2();
		byte[] data = request.getData();
		if (data == null) {
			return new RequestAPDUWrapper(cla, ins, p1, p2, correctLE);
		} else {
			return new RequestAPDUWrapper(cla, ins, p1, p2, data, correctLE);
		}
	}
	
	ResponseAPDUWrapper nativeTransmit(RequestAPDUWrapper request) throws CardException, CardClientException {
		CommandAPDU cmd = new CommandAPDU(request.getBytes());
    	ResponseAPDU rsp = null;
    	try {
    		
			String apduTrace;
			// Mask PIN
	    	if (cmd.getINS() == APDUConstants.VERIFY) {
	    		byte[] maskedPin = cmd.getBytes();
	    		for (int i = 5, end = i + cmd.getNc(); i < end; i++) {
	    			maskedPin[i] = (byte) 0xAA;
	    		}
	    		apduTrace = String.format("Sending Command APDU %s", Hex.encodeHexString(maskedPin).replaceAll("..(?=.)", "$0 "));
	    	}
	    	else {
	    		apduTrace = String.format("Sending Command APDU %s", Hex.encodeHexString((cmd.getBytes())).replaceAll("..(?=.)", "$0 "));
	    	}
    		s_apduLogger.debug(apduTrace);
    		
			rsp = m_channel.transmit(cmd);

		} catch (CardException e) {
			s_logger.error("Caught CardException {} transmitting APDU.", e.getMessage(), e);
			throw e;
		}
		return new ResponseAPDUWrapper(rsp.getBytes());
	}
	
	protected ResponseAPDUWrapper basicTransmit(RequestAPDUWrapper request)
			throws CardClientException, CardException {
		RequestAPDUWrapper encodedRequest = encodeRequest(request);
		ResponseAPDUWrapper encodedResponse = nativeTransmit(
				encodedRequest);
		return decodeResponse(encodedResponse);
	}

	protected ResponseAPDUWrapper decodeResponse(ResponseAPDUWrapper response)
			throws CardException {
		return response;
	}

	protected RequestAPDUWrapper encodeRequest(RequestAPDUWrapper request)
			throws CardException {
		return request;
	}

	public ResponseAPDUWrapper transmit(RequestAPDUWrapper request) throws CardClientException, CardException {
		ResponseAPDUWrapper response = this.basicTransmit(request);
		if (response.getSw1() == 0x6C) {
			// wrong LengthExpected field: happens e.g. on ReinerSCT e-com in
			// combination with Starcos3.0 cards
			int le = response.getSw2();
			RequestAPDUWrapper fixedRequest = fixLengthExpected(request, le);
			response = this.basicTransmit(fixedRequest);
		}
		if (response.getSw1() == 0x61) {
			s_logger.debug("Using GET RESPONSE to retrieve large object");
			ByteArrayOutputStream dataBaos = new ByteArrayOutputStream();
			try {
				dataBaos.write(response.getData());
			} catch (IOException e) {
				s_logger.error("Failed to write data to byte array.", e);
				throw new CardClientException("Failed to write data to byte array.", e);
			}
			do {
				// "GET RESPONSE" command
				RequestAPDUWrapper fixedRequest = new RequestAPDUWrapper(0, 0xC0, 0, 0,
						response.getSw2());
				response = this.basicTransmit(fixedRequest);
			} while(response.getSw1() == 0x61);

			ResponseAPDUWrapper fixedResponse = new ResponseAPDUWrapper(dataBaos.toByteArray(), response.getSw1(), response.getSw2());
			s_logger.debug("Returning status {}{} following GET RESPONSE", response.getSw1(), response.getSw2());
		}
		if (request.isChainedRequest() && request.getNextRequest() != null) {
			response = transmit(request.getNextRequest());
		}
		return response;
	}
}
