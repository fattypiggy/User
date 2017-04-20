package com.pwcprac.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pwcprac.dao.RegisteredAppRepository;
import com.pwcprac.entity.RegisteredApp;


@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableMongoRepositories("com.pwcprac.dao")
public class UserServiceTest {
	@Autowired
	private RegisteredAppRepository adao;
	@Test
	public void test() {
//		RegisteredApp app = new RegisteredApp();
//		app.setAppId("timkInk");
//		app.setAppKey("1234");
//		app.setCallbackUrl("http://localhost:7788");
//		app.setId("1");
//		adao.save(app);
		RegisteredApp app = adao.findOneByAppId("timkInk");
		System.out.println("APPKEY"+app.getAppKey());
	}
}
