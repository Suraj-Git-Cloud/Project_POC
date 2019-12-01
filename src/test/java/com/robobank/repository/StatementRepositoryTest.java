package com.robobank.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import com.robobank.domain.CustomerStatement;
import com.robobank.utils.StatmentUtils;

@RunWith(MockitoJUnitRunner.class)
public class StatementRepositoryTest {

	@InjectMocks
	StatementRepository statementRepo;
	
	@Mock
	StatmentUtils statementUtils;
	
	@Value("${dir.path}")
	String path;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void readCSVFileTestWhenListIsNotNull() throws IOException {
		String fileName = "records.csv";
		Path statementsfilePath = Paths.get(path+fileName);
		try {
			Mockito.when(statementUtils.initializeDataFromFile(statementsfilePath)).thenReturn(getList());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Assert.assertNotNull(statementRepo.readFileCSV(fileName));
		
	}
	
	@Test
	public void readXMLileTestWhenListIsNotNull() throws Exception {
		String fileName = "records.xml";
		Path statementsfilePath = Paths.get(path+fileName);
		Assert.assertNotNull(statementRepo.readFileXML(fileName));
		
	}
	
	@Test
	public void isValidDataTest() {
		statementRepo.validateData(getList());
		Assert.assertNotNull(statementRepo.validateData(getList()));
		
	}
	
	private List<CustomerStatement> getList() {
		return Arrays.asList(new CustomerStatement(111, null, null, 10.2, -2, 10.8),
				new CustomerStatement(112, null, null, 11.2, -2, 10.8));
	}
}
