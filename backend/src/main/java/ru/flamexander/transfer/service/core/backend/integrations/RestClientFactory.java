package ru.flamexander.transfer.service.core.backend.integrations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class RestClientFactory {

    @Value("${integrations.limits-info-service.read-timeout}")
    private int readTimeout;

    @Value("${integrations.limits-info-service.write-timeout}")
    private int writeTimeout;

    @Bean
    public WebClient restClient() {
        TcpClient tcpClient = TcpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, writeTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(readTimeout))
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(writeTimeout)));

        HttpClient httpClient = HttpClient.from(tcpClient);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}