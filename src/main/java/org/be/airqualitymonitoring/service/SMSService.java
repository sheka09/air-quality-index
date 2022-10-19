package org.be.airqualitymonitoring.service;

import org.be.airqualitymonitoring.entity.User;

public interface SMSService {

	public void sendSMS(User user, String message);

	}
