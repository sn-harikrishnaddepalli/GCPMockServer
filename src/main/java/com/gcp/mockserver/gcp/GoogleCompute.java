package com.gcp.mockserver.gcp;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.gcp.mockserver.utils.gcp.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.common.collect.Lists;

public class GoogleCompute {

	public static Compute createComputeService() throws IOException, GeneralSecurityException {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		GoogleCredential credential = GoogleCredential.fromStream(
				new FileInputStream(Constants.CREDENTIALS_PATH.URL + Constants.CREDENTIALS_PATH.FILE_NAME))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

		if (credential.createScopedRequired()) {
			credential =
					credential.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
		}

		return new Compute.Builder(httpTransport, jsonFactory, credential)
				.setApplicationName("Google-ComputeSample/0.1")
				.build();
	}

}
