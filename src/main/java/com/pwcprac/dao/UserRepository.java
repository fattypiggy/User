package com.pwcprac.dao;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.pwcprac.entity.User;

public interface UserRepository extends MongoRepository<User, String>{
	User findOneById(String id);
	User findOneByAccount(String account);
	List<User> findByAccount(String account);
}
