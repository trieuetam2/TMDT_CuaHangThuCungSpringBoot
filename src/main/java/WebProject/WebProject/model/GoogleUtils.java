package WebProject.WebProject.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


//import entity.AccountGoogle;

@Service
public class GoogleUtils {

	@Autowired
	private Environment env;
	public static String GOOGLE_CLIENT_ID = "540833837549-eof6l5jn50qm1r4j72i1cnorecik8rt0.apps.googleusercontent.com";
	public static String GOOGLE_CLIENT_SECRET = "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";
	public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/login-google";
	public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
	public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
	public static String GOOGLE_GRANT_TYPE = "authorization_code";
	
//	public String getToken(final String code) throws ClientProtocolException, IOException {
//		String link = env.getProperty();
//
//		String response = Request.Post(link)
//				.bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
//						.add("client_secret", env.getProperty("google.app.secret"))
//						.add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
//						.add("grant_type", "authorization_code").build())
//				.execute().returnContent().asString();
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode node = mapper.readTree(response).get("access_token");
//		return node.textValue();
//	}

//	public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
//		String link = env.getProperty("google.link.get.user_info") + accessToken;
//		String response = Request.Get(link).execute().returnContent().asString();
//		ObjectMapper mapper = new ObjectMapper();
//		GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
//		System.out.println(googlePojo);
//		return googlePojo;
//	}
	
	
	
	//=================================
	public String getToken(String code) throws ClientProtocolException, IOException {
		String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
				.bodyForm(Form.form().add("client_id", GOOGLE_CLIENT_ID)
						.add("client_secret", GOOGLE_CLIENT_SECRET)
						.add("redirect_uri", GOOGLE_REDIRECT_URI).add("code", code)
						.add("grant_type", GOOGLE_GRANT_TYPE).build())
				.execute().returnContent().asString();
		System.out.println();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response).get("access_token");
		return node.textValue();
	}

	public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
		String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
		String response = Request.Get(link).execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
		return googlePojo;
	}
}
