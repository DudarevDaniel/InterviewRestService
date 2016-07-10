package com.example.helloworld.dao;

import com.example.helloworld.InterviewApplicationTests;
import com.example.helloworld.dao.entity.Contact;
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
    public void testFindByNamePattern() throws Exception {
        String namePattern = "^A.*$";
        ScrollableResults contacts = contactDao.findByNamePattern(namePattern);

        Assert.assertNotNull(contacts);

        int resultSize = 0;
        while (contacts.next()) {
            Object row = contacts.get()[0];
            Contact contact = (Contact) row;
            resultSize++;
            Assert.assertFalse(contact.getName().startsWith("A"));
        }
        Assert.assertEquals(3, resultSize);
    }

}
