package com.asu.hsi.engine;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.asu.hsi.properties.Constants;
import com.asu.hsi.properties.SessionVar;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleSession {

	public synchronized static boolean isUserValid(String token) {
		boolean val = false;
		
		if (token == null) {
			return val;
		}
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance()).setAudience(Arrays.asList(Constants.CLIENT_ID))
						// If you retrieved the token on Android using the Play
						// Services 8.3 API or newer, set
						// the issuer to "https://accounts.google.com".
						// Otherwise, set the issuer to
						// "accounts.google.com". If you need to verify tokens
						// from multiple sources, build
						// a GoogleIdTokenVerifier for each issuer and try them
						// both.
						.setIssuer("accounts.google.com").build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			String email = payload.getEmail();
			
			if (SessionVar.validEmails.contains(email.toLowerCase())){
				val = true;
			}
		}

		return val;
	}

	public static String[] getUserEmail(String token) {
		String[] val = {"", ""};

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance()).setAudience(Arrays.asList(Constants.CLIENT_ID))
						// If you retrieved the token on Android using the Play
						// Services 8.3 API or newer, set
						// the issuer to "https://accounts.google.com".
						// Otherwise, set the issuer to
						// "accounts.google.com". If you need to verify tokens
						// from multiple sources, build
						// a GoogleIdTokenVerifier for each issuer and try them
						// both.
						.setIssuer("accounts.google.com").build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (idToken != null) {
			Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");

			// Use or store profile information
			// ...
			val[0] = email;
			val[1] = givenName;

		} else {
			System.out.println("Invalid ID token.");
		}

		return val;
	}

}
