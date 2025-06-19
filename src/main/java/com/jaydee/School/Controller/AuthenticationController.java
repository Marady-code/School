package com.jaydee.School.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.RefreshTokenRequest;
import com.jaydee.School.DTO.RegisterRequest;
import com.jaydee.School.config.security.AuthenticationRequest;
import com.jaydee.School.config.security.AuthenticationResponse;
import com.jaydee.School.config.security.AuthenticationResponseNoToken;
import com.jaydee.School.config.security.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseNoToken> login(@Valid @RequestBody AuthenticationRequest request) {
		AuthenticationResponse response = authenticationService.authenticate(request);
		return ResponseEntity.ok()
				.header("Authorization", response.getType() + " " + response.getToken())
				.header("Access-Control-Expose-Headers", "Authorization")
				.body(AuthenticationResponseNoToken.fromAuthenticationResponse(response));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponseNoToken> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		AuthenticationResponse response = authenticationService.refreshToken(request);
		return ResponseEntity.ok()
				.header("Authorization", response.getType() + " " + response.getToken())
				.header("Access-Control-Expose-Headers", "Authorization")
				.body(AuthenticationResponseNoToken.fromAuthenticationResponse(response));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
		authenticationService.logout(token);
		return ResponseEntity.ok().build();
	}
}