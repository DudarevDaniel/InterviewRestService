package com.example.helloworld.service;

import org.hibernate.ScrollableResults;

public interface ContactService {

    ScrollableResults findByNamePattern(String nameFilter);

}
