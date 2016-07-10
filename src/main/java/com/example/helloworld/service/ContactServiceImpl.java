package com.example.helloworld.service;

import com.example.helloworld.dao.ContactDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Override
    public ScrollableResults findByNamePattern(String nameFilter) {
        Assert.isTrue(StringUtils.isNotEmpty(nameFilter), "Name Filter should be defined");
        return contactDao.findByNamePattern(nameFilter);
    }
}
