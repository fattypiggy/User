package com.pwcprac.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.mongodb.operation.CreateUserOperation;
import com.pwcprac.dao.UserRepository;
import com.pwcprac.dto.ResultData;
import com.pwcprac.entity.User;
import com.thoughtworks.xstream.core.util.Base64Encoder;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(value = "/user")
public class UserService{
	@Autowired
	private UserRepository udao;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private static final String SALT = "BNJKJ:O&(*4b23kjdas~%$?Klp;kh(";

	@PostMapping(value = "/verification", produces = "application/json; charset=utf-8")
	public ResultData<User> verify(@RequestBody User user) {
		ResultData<User> resultData = new ResultData<User>();
		User mongoUser = udao.findOneByAccount(user.getAccount());
		if (mongoUser == null) {
			resultData.setResult(false);
			resultData.setMsg("User not exist");
			return resultData;
		}
		if (!mongoUser.getPassword().equals(getMd5(user.getPassword()))) {
			resultData.setResult(false);
			resultData.setMsg("Wrong password");
			return resultData;
		}
		resultData.setResult(true);
		resultData.setData(mongoUser);
		return resultData;
	}

	@PostMapping(value = "/saving", produces = "application/json; charset=utf-8")
	public ResultData<String> saveUser(@RequestBody User user) {
		ResultData<String> resultData = new ResultData<String>();
		String md5Password = getMd5(user.getPassword());

		if (md5Password != null) {
			user.setPassword(md5Password);
		} else {
			resultData.setMsg("failed");
		}
		if(validateAccount(user.getAccount())){
			if (udao.save(user) != null) {
				resultData.setMsg("success");
			} else {
				resultData.setMsg("failed");
			}
		}else{
			resultData.setMsg("failed");
		}
		return resultData;
	}

	private String getMd5(String old) {
		try {
			String string = old + SALT;
			MessageDigest md = MessageDigest.getInstance("MD5");
			Base64Encoder base64Encoder = new Base64Encoder();
			return base64Encoder.encode(md.digest(string.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Boolean validateAccount(String account){
		User mongoUser = udao.findOneByAccount(account);
		if(mongoUser == null){
			return true;
		}
		return false;
	}
	@GetMapping(value = "/getAll",produces = "application/json; charset=utf-8")
	public List<User> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
	        @RequestParam(value = "size", defaultValue = "10") Integer size){
		Sort sort = new Sort(Direction.DESC, "account");
		Pageable pageable = new PageRequest(page, size, sort);
		Page<User> users = udao.findAll(pageable);	
		return users.getContent();
	}
	
	@PostMapping(value = "/delete", produces = "application/json; charset=utf-8")
	public ResultData<String> delete(@RequestBody User user){
		ResultData<String> resultData = new ResultData<String>();
		User mongoUser = udao.findOneByAccount(user.getAccount());
		if(mongoUser==null){
			resultData.setResult(false);
			resultData.setMsg("account not exsist");
			return resultData;
		}
		udao.delete(mongoUser);
		resultData.setResult(true);
		return resultData;
	}

	@GetMapping(value = "/{account}/verification",produces = "application/json; charset=utf-8")
	public ResultData<String> verifyExist(@PathVariable("account")String account){
		List<User> users = udao.findByAccount(account);
		ResultData<String> resultData = new ResultData<>();
		if(users.size()>0){
			resultData.setMsg("invalid");
		}else{
			resultData.setMsg("valid");
		}
		return resultData;
	}
	
}
