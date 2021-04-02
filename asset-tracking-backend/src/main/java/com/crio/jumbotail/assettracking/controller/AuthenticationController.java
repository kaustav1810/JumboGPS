package com.crio.jumbotail.assettracking.controller;

import com.crio.jumbotail.assettracking.exceptions.JwtAuthException;
import com.crio.jumbotail.assettracking.service.CustomUserDetailsService;
import com.crio.jumbotail.assettracking.utils.JwtUtil;
import com.crio.jumbotail.assettracking.exchanges.AuthRequest;
import com.crio.jumbotail.assettracking.exchanges.AuthResponse;
import com.crio.jumbotail.assettracking.exchanges.CreateUserRequest;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			throw new JwtAuthException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new JwtAuthException("INVALID_CREDENTIALS", e);
		}

		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping(value = "/register")
	public ResponseEntity<Void> saveUser(@RequestBody CreateUserRequest user) {
		userDetailsService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<AuthResponse> refreshtoken(HttpServletRequest request) {
		// From the HttpRequest get the claims
		DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok().body(new AuthResponse(token));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		if (claims == null) {
			throw new JwtAuthException("Token Not Expired");
		}
		Map<String, Object> expectedMap = new HashMap<>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

}