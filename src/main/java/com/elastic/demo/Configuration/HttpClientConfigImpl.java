package com.elastic.demo.Configuration;

import java.io.File;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class HttpClientConfigImpl implements RestClientBuilder.HttpClientConfigCallback{
	 
	 @Override
	    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
	        try {
	            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	            UsernamePasswordCredentials passwordCredentials = new UsernamePasswordCredentials("elastic", "password@123");
	            credentialsProvider.setCredentials(AuthScope.ANY, passwordCredentials);
	            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
	            File trustStoreLocationFile =ResourceUtils.getFile("classpath:static/truststore.p12");
	            SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(trustStoreLocationFile, "password".toCharArray());
	            SSLContext sslContext = sslContextBuilder.build();
	            httpAsyncClientBuilder.setSSLContext(sslContext); 

	        } catch (Exception e) {
	             e.printStackTrace();
	        }
	        return httpAsyncClientBuilder;
	    }

}
