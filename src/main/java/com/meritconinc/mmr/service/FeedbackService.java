package com.meritconinc.mmr.service;

import java.util.List;

import com.meritconinc.mmr.model.aboutus.FeedbackVO;
import com.meritconinc.mmr.model.common.KeyValueVO;

public interface FeedbackService {
	
	public void insertFeedback(FeedbackVO feedbackVO);
	
	public List<KeyValueVO> getFeedbackTypes(String locale);
	
}