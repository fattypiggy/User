package com.pwcprac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.pwcprac.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = PwcpracUserApplication.class)
public class PwcpracUserApplicationTests {
	//@Autowired UserRepository repository;
	@Autowired MongoClient m;
    @Test
    public void readsFirstPageCorrectly() {
    	User user = new User();
		user.setId("1");
		user.setName("anydd");
		user.setPassword("fdsa");
		MongoTemplate mt = new MongoTemplate(m, "local");
		mt.save(user);
    }

}
