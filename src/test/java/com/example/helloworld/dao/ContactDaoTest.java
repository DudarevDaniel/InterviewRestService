package com.example.helloworld.dao;

import com.example.helloworld.InterviewApplicationTests;
import com.example.helloworld.dao.entity.Contact;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = {"classpath:cleanUp.sql", "classpath:test-data.sql"})
public class ContactDaoTest extends InterviewApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Test
    public void testPersistenceContext() {
        Session session = entityManager.unwrap(Session.class);
        List<Contact> contactList = session.createCriteria(Contact.class)
                .add(Restrictions.eq("name", "Aaron"))
                .list();
        Assert.assertTrue(contactList.size() == 1);
        Assert.assertTrue(session.contains(contactList.get(0)));
    }

}
