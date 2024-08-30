package com.elastic.demo.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class ElasticClientBean {
	@Value("${es.host}")
	private String ES_HOST ;
	@Value( "${es.protocal}" )
	private  String HTTP;



    @Bean
    public ElasticsearchClient elasticsearchClient (){
        RestClientBuilder b= RestClient.builder(new HttpHost(ES_HOST,9200,HTTP));
        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback =new HttpClientConfigImpl();
        b.setHttpClientConfigCallback(httpClientConfigCallback);
        RestClient rc=b.build();
        RestClientTransport restClientTransport = new RestClientTransport(rc, new JacksonJsonpMapper());
        return new ElasticsearchClient(restClientTransport);
    }
}
