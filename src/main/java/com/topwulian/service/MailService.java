package com.topwulian.service;

import java.util.List;

import com.topwulian.model.Mail;

public interface MailService {

	void save(Mail mail, List<String> toUser);
}
