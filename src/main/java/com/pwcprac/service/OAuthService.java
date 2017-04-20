package com.pwcprac.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.pwcprac.dao.RegisteredAppRepository;
import com.pwcprac.dao.UserRepository;
import com.pwcprac.dto.AuthResult;
import com.pwcprac.entity.RegisteredApp;
import com.pwcprac.entity.User;
import com.pwcprac.util.AuthCache;
import com.pwcprac.util.URLGenerator;
/**
 * UserService (Auth Service)
 * port:8181
 * @author axia021
 *
 */

@RestController
@RequestMapping(value = "/oauth2")
public class OAuthService {
	@Autowired
	private UserRepository udao;
	@Autowired
	private RegisteredAppRepository adao;
	@Autowired
	private AuthCache authCache;
	@Autowired
	private URLGenerator uRLGenerator;
	
	@GetMapping(value = "/{app_id}/request_token_url",produces = "application/json; charset=utf-8")
	public AuthResult requestTokenUrl(@PathVariable("app_id")String appId,@RequestParam("app_key")String appKey,@RequestParam("callback_url")String callbackURL,HttpServletResponse response){
		AuthResult result = new AuthResult();
		RegisteredApp app = adao.findOneByAppId(appId.trim());
		if(app==null){
			result.setResult(false);
			result.setDetail("the app has not been registed yet.");
			return result;
		}
		if(!appKey.equals(app.getAppKey())){
			result.setResult(false);
			result.setDetail("wrong app_key");
			return result;
		}
		//TODO: define UrlVerification
		if(!callbackURL.equals(app.getCallbackUrl())){
			result.setResult(false);
			result.setDetail("illegal callbackUrl");
			return result;
		}
		uRLGenerator.setCallbackURL(callbackURL);
		uRLGenerator.setApp(app);
		String loginUrl = uRLGenerator.generateURL();
		authCache.getAuthCache().put(uRLGenerator.getLoginUrlCode(), uRLGenerator);
		result.setResult(true);
		result.setUrl(loginUrl);
		return result;
	}
	
	//TODO return accessToken
	@PostMapping(value="/user_authorization_url")
	public String userAuthorizationUrl(@RequestParam("user_authorization_url_code")String userAuthorizationUrlCode,@RequestParam("login_url_code")String loginUrlCode){
		AuthResult authResult = new AuthResult();
		Integer loginUrlCodeInt = Integer.parseInt(loginUrlCode);
		URLGenerator uRLGenerator = authCache.getAuthCache().get(loginUrlCodeInt);
		if(uRLGenerator==null){
			return "time out. rejected";
		}
		if(uRLGenerator.getCode()==Integer.parseInt(userAuthorizationUrlCode))
			return "accessToken";
		return "reject";
	}
	
	//@GetMapping(value="/login",produces = "application/json; charset=utf-8")
	@PostMapping(value="/login",produces = "application/json; charset=utf-8")
	public AuthResult login(@RequestBody User user,@RequestParam("urlcode")String urlcode,HttpServletResponse res){
		AuthResult result = new AuthResult();
		Map<Integer, URLGenerator> cache = authCache.getAuthCache();	
		Integer code = Integer.parseInt(urlcode);
		if(!cache.containsKey(code)){
			result.setResult(false);
			result.setDetail("illegal url");
			return result;
		}
		User mongoUser = udao.findOneByAccount(user.getAccount());
		if(mongoUser==null){
			result.setResult(false);
			result.setDetail("user not exsist");
			return result;
		}
		if(!mongoUser.getPassword().equals(user.getPassword())){
			result.setResult(false);
			result.setDetail("wrong password");
			return result;
		}

		URLGenerator urlGenerator = cache.get(code);
		String callbackUrlwithCode = urlGenerator.getCallbackURL()+"?code="+urlGenerator.generateCode();
		//set timeouts and going to delete cache
		authCache.getAuthCache().remove(urlGenerator.getLoginUrlCode());
		authCache.addEntryWithTimeOuts(urlGenerator.getLoginUrlCode(), urlGenerator);
		
		result.setResult(true);
		result.setUrl(callbackUrlwithCode);
		return result;
	}
}
