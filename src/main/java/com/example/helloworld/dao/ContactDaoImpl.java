package com.example.helloworld.dao;

import com.example.helloworld.dao.entity.Contact;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ContactDaoImpl implements ContactDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ScrollableResults findByNamePattern(String nameFilter) {
        Session session = entityManager.unwrap(Session.class);
        return session.getNamedQuery(Contact.FIND_BY_NAME_PATTERN_QUERY)
                .setString("pattern", nameFilter)
                .setReadOnly(true)
                .setCacheable(false)
                .scroll(ScrollMode.FORWARD_ONLY);
    }
}
