package com.pwcprac.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pwcprac.entity.RegisteredApp;

public interface RegisteredAppRepository extends MongoRepository<RegisteredApp, String>{
	RegisteredApp findOneByAppId(String appId);
}
