package com.example.helloworld.controller;

import com.example.helloworld.dao.entity.Contact;
import com.example.helloworld.exceptions.FailedToParseResultException;
import com.example.helloworld.service.ContactService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@RestController
@RequestMapping("/hello/contacts")
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactService contactService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> getContactsByNameFilter(@RequestParam("nameFilter") String nameFilter) {
        if (!isValidNameFilter(nameFilter)) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Contact> searchResult = new ArrayList<>();
        try {
            searchResult = contactService.findByNamePattern(nameFilter);
        } catch (FailedToParseResultException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        if (searchResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity<List<Contact>>(searchResult, HttpStatus.OK);
    }

    private boolean isValidNameFilter(String nameFilter) {
        if (StringUtils.isEmpty(nameFilter)) {
            return false;
        }
        try {
            Pattern.compile(nameFilter);
        } catch (PatternSyntaxException e) {
            logger.error("Incorrect name filter: " + nameFilter, e);
            return false;
        }
        return true;
    }

}
