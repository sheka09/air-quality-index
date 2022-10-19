package org.be.airqualitymonitoring.twilioConfig;

import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class TwilioConfig {
	private String  accountSid="*******************";
	private String authToken="******************";
	private String  twillioNumber="*************";
	
	

}
