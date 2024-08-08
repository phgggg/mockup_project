package com.itsol.mockup.repository.impl;

import com.itsol.mockup.repository.ProjectRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseRepo {
    protected static final Logger logger = LogManager.getLogger(UsersRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ProjectRepository projectRepository;
}
