package org.be.airqualitymonitoring.service;


import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.twilioConfig.TwilioConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Service
@Transactional
public class SmsServiceImpl implements SMSService {
	
	   private final TwilioConfig twilioConfig = new TwilioConfig();;	

		@Override
		public void sendSMS(User user,String message) {
			if(isValidPhoneNumber(user.getPhoneNumber())){
				PhoneNumber receiver=new PhoneNumber(user.getPhoneNumber());
				PhoneNumber sender=new PhoneNumber(twilioConfig.getTwillioNumber());
							MessageCreator creater=Message.creator(receiver,sender,message);
							creater.create();
			}
			else {
				throw new IllegalArgumentException(user.getPhoneNumber() +" is not a valid phone number");
			}
			
			
		}
		
		
		// checks whether a phone number matches a valid pattern
		boolean isValidPhoneNumber(String phoneNumber) {
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

			com.google.i18n.phonenumbers.Phonenumber.PhoneNumber parsedPhoneNumber = null;
			try {
				parsedPhoneNumber = phoneUtil.parse(phoneNumber, "US");
			} catch (NumberParseException e) {
				e.printStackTrace();
			}

			return phoneUtil.isValidNumber(parsedPhoneNumber);
		}

}
