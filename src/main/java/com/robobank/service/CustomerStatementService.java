package com.robobank.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.robobank.domain.CustomerStatement;
import com.robobank.domain.ReportData;

public interface CustomerStatementService {	
	
	List<CustomerStatement> readFile(String fileName) throws IOException, Exception;
	List<String> validateData(List<CustomerStatement> records);
}
