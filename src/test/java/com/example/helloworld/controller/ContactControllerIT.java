package com.example.helloworld.controller;


import com.example.helloworld.InterviewApplicationTests;
import com.example.helloworld.dao.entity.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = {"classpath:cleanUp.sql", "classpath:test-data.sql"})
public class ContactControllerIT extends InterviewApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetContactsByNameFilter() throws Exception {
        String namePattern = "^A.*$";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hello/contacts")
                .param("nameFilter", namePattern)
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Contact> contacts = objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Contact.class));

        Assert.assertEquals(3, contacts.size());
        boolean hasUnfilteredContacts = contacts.stream().anyMatch(contact -> contact.getName().startsWith("A"));
        Assert.assertFalse(hasUnfilteredContacts);
    }

    @Test
    public void testGetContactsByNameFilterWithInvalidNameFilter() throws Exception {
        String namePattern = "***";
        mockMvc.perform(MockMvcRequestBuilders.get("/hello/contacts")
                .param("nameFilter", namePattern)
        )
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetContactsByNameFilterWithEmptyResult() throws Exception {
        String namePattern = "***";
        mockMvc.perform(MockMvcRequestBuilders.get("/hello/contacts")
                .param("nameFilter", namePattern)
        )
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

}
