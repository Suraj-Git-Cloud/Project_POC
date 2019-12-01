package com.robobank.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.robobank.domain.CustomerStatement;
import com.robobank.domain.ReportData;
import com.robobank.service.CustomerStatementService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CustomerStatementController.class})
public class StatementControllerTest {
	
	@Autowired
	CustomerStatementController customerController;
	
	@MockBean
	CustomerStatementService customerStatementService;
	
	@MockBean
	ReportData reportData;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

	}
	
	@Test
	public void fileDataTest() throws IOException, Exception {
		
		when(customerStatementService.readFile("records.csv")).thenReturn(getList());
		when(customerStatementService.validateData(getList())).thenReturn(getReportList());
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/robobank/inputfile/records.csv").contentType(MediaType.APPLICATION_XML)).andReturn();
		Assert.assertEquals(mvcResult.getResponse().getStatus(),HttpStatus.NOT_ACCEPTABLE.value());
		verify(customerStatementService).readFile("records");
		
	}
	
	private List<CustomerStatement> getList() {
		return Arrays.asList(new CustomerStatement(112806, "A112806","Clothes from Peter de Vries", 10.2, -2, 10.8),
				new CustomerStatement(112806, "B112806", "Tickets for Erik Dekker", 11.2, -2, 10.8));
	}
	private List<String> getReportList() {
		return Arrays.asList("112806  Clothes from Peter de Vries","112806  Tickets for Erik Dekker");
	}
}
