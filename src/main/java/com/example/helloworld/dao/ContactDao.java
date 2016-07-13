package com.example.helloworld.dao;


import org.hibernate.ScrollableResults;

public interface ContactDao {

    ScrollableResults getAllContacts();

}
