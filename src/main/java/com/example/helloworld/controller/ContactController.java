package com.example.helloworld.controller;

import com.example.helloworld.dao.entity.Contact;
import com.example.helloworld.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollableResults;
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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@RestController
@RequestMapping("/hello/contacts")
public class ContactController {

    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String COMMA_SEPARATOR = ", ";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactService contactService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getContactsByNameFilter(@RequestParam("nameFilter") String nameFilter) {
        if (!isValidNameFilter(nameFilter)) {
            return ResponseEntity.badRequest().body(null);
        }
        ScrollableResults searchResult = contactService.findByNamePattern(nameFilter);
        return buildJsonFromScrollableResult(searchResult);
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
    
    private ResponseEntity<String> buildJsonFromScrollableResult(ScrollableResults searchResult) {
        StringBuilder resultJsonBuilder = new StringBuilder(ARRAY_START);
        boolean hasNext = searchResult != null && searchResult.next();
        ObjectMapper objectMapper = new ObjectMapper();
        int resultSize = 0;
        try {
            while (hasNext) {
                resultJsonBuilder.append(objectMapper.writeValueAsString(parseRow(searchResult)));
                resultSize++;
                hasNext = searchResult.next();
                if (hasNext) {
                    resultJsonBuilder.append(COMMA_SEPARATOR);
                }
            }
        } catch (Exception ex) {
            logger.error("Failed to parse the result", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            if (searchResult != null) {
                searchResult.close();
            }
        }
        String result = resultJsonBuilder.append(ARRAY_END).toString();
        if (resultSize == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
        }
        return ResponseEntity.ok(result);
    }

    private Contact parseRow(ScrollableResults searchResult) {
        Object row = searchResult.get()[0];
        return (Contact) row;
    }

}
