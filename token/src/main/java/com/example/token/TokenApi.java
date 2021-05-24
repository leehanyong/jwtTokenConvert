package com.example.token;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import manage.AuthenticationResponse;
import manage.Jwt;
import manage.JwtUtil;

@RestController
public class TokenApi {

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";
	}

	/**
	 * 토큰생성요청
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws exceptionUtill, Exception
	 * @throws Exception
	 */
	@RequestMapping(value = "/jwtToken")
	public ResponseEntity<?> authenticate() throws Exception {

		Jwt token = this.jwtUtil.makeJwt();
		System.out.println("token : " + token);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
}
