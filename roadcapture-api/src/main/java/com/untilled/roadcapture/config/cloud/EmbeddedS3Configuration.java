package com.untilled.roadcapture.config.cloud;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.untilled.roadcapture.util.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

//@Slf4j
//@Profile({"local"})
//@Configuration
public class EmbeddedS3Configuration {

    /*@Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.mock.port}")
    private int port;

    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder()
                .withPort(port) //해당 포트에 프로세스 생성
                .withInMemoryBackend() //인메모리에서 활성화
                .build();
    }

    @PostConstruct
    public void startS3Mock() throws IOException { //임베디드 S3 서버 가동
        port = ProcessUtils.isRunningPort(port) ? ProcessUtils.findAvailableRandomPort() : port;
        this.s3Mock().start();
        log.info("인메모리 S3 Mock 서버가 시작됩니다. port: {}", port);
    }

    @PreDestroy
    public void destroyS3Mock() {
        this.s3Mock().shutdown();
        log.info("인메모리 S3 Mock 서버가 종료됩니다. port: {}", port);
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3Client() {

        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(getUri(), region);
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        client.createBucket(bucket);

        log.info("bucket created!!!");

        return client;
    }

    private String getUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .build()
                .toUriString();
    }*/

}
