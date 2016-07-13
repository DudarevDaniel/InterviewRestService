package com.example.helloworld.service;

import com.example.helloworld.dao.entity.Contact;
import com.example.helloworld.exceptions.FailedToParseResultException;

import java.util.List;

public interface ContactService {

    List<Contact> findByNamePattern(String nameFilter) throws FailedToParseResultException;

}
