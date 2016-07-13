package com.example.helloworld.dao;

import com.example.helloworld.InterviewApplicationTests;
import org.hibernate.ScrollableResults;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = {"classpath:cleanUp.sql", "classpath:test-data.sql"})
public class ContactDaoTest extends InterviewApplicationTests {

    @Autowired
    private ContactDao contactDao;

    @Test
    public void testGetAllContacts() throws Exception {
        ScrollableResults contacts = contactDao.getAllContacts();

        Assert.assertNotNull(contacts);
        int resultSize = 0;
        while (contacts.next()) {
            contacts.get();
            resultSize++;
        }
        Assert.assertEquals(5, resultSize);
    }

}
