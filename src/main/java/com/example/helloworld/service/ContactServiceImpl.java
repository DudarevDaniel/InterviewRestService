package com.example.helloworld.service;

import com.example.helloworld.dao.ContactDao;
import com.example.helloworld.dao.entity.Contact;
import com.example.helloworld.exceptions.FailedToParseResultException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {
    
    @Value("${contacts.number_of_objects_threshold}")
    private int numberOfObjectsThreshold;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactDao contactDao;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true, rollbackFor = FailedToParseResultException.class)
    public List<Contact> findByNamePattern(String nameFilter) throws FailedToParseResultException {
        Assert.isTrue(StringUtils.isNotEmpty(nameFilter), "Name Filter should be defined");
    
        return filterResultByName(contactDao.getAllContacts(), nameFilter);
    }
    
    private List<Contact> filterResultByName(ScrollableResults searchResult, String nameFilter) throws FailedToParseResultException {
        Pattern pattern = Pattern.compile(nameFilter);
        List<Contact> filteredContacts = new ArrayList<>();
        boolean hasNext = searchResult != null && searchResult.next();
        int resultsInContextSize = 0;
        try {
            while (hasNext) {
                Contact contact = parseRow(searchResult);
                resultsInContextSize++;
                if (!matchesPattern(pattern, contact.getName())) {
                    filteredContacts.add(contact);
                }
                if (resultsInContextSize >= numberOfObjectsThreshold) {
                    entityManager.clear();
                    resultsInContextSize = 0;
                }
                hasNext = searchResult.next();
            }
        } catch (Exception ex) {
            logger.error("Failed to parse the result", ex);
            throw new FailedToParseResultException();
        } finally {
            if (searchResult != null) {
                searchResult.close();
            }
        }
        return filteredContacts;
    }
    
    private boolean matchesPattern(Pattern pattern, String contactName) {
        return pattern.matcher(contactName).matches();
    }
    
    private Contact parseRow(ScrollableResults searchResult) {
        return (Contact) searchResult.get()[0];
    }
    
}
