package com.dgpad.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

	@Autowired 
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "ALI", password = "ALI_PASSWORD", authorities = {"Admin"})
	public void testUpdateOrderStatus() throws Exception {
		Integer orderId = 3;
		String phase = "NEW";
		String requestURL = "/order_followup/change/" + orderId + "/" + phase;

		mockMvc.perform(post(requestURL).with(csrf()))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@WithMockUser(username = "ALI", password = "ALI_PASSWORD", authorities = {"Admin"})
	public void testGetAnalysesSalesLastWeek() throws Exception {
		String URL = "/Analyses/sales-analytics/by-date/last_7Days";
		mockMvc.perform((get(URL))).andExpect(status().isOk()).andDo(print());

	}
	@Test
	@WithMockUser(username = "ALI", password = "ALI_PASSWORD", authorities = {"Admin"})
	public void testGetAnalysesSalesLastTowQuarters() throws Exception {
		String URL = "/Analyses/sales-analytics/by-date/15Days";
		mockMvc.perform((get(URL))).andExpect(status().isOk()).andDo(print());

	}
	@Test
	@WithMockUser(username = "ALI", password = "ALI_PASSWORD", authorities = {"Admin"})
	public void getAnalysesUsingCategory() throws Exception {
		String URL = "/Analyses/sales-analytics/category/15Days";
		mockMvc.perform((get(URL))).andExpect(status().isOk()).andDo(print());
	}
	@Test
	@WithMockUser(username = "ALI", password = "ALI_PASSWORD", authorities = {"Admin"})
	public void getAnalysesUsingProduct() throws Exception {
		String URL = "/Analyses/sales-analytics/product/15Days";
		mockMvc.perform((get(URL))).andExpect(status().isOk()).andDo(print());
	}
}
