package com.example.demoAuthen;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoAuthenApplicationTests {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DemoAuthenApplicationTests.class);
	@Test
	public void printHashPassword() {
		String plainTextPassword = "15520325";
		String hashPwd = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
		logger.info(hashPwd);
	}

}
