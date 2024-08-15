package ru.flamexander.transfer.service.core.backend.factories;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import ru.flamexander.transfer.service.core.backend.configurations.RestProperties;

public class RestClientFactory {
    public RestClient createRestClient(RestProperties properties){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(properties.getWriteTimeout().toSeconds()))
                .setResponseTimeout(Timeout.ofSeconds(properties.getReadTimeout().toSeconds()))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .baseUrl(properties.getUrl())
                .build();
    }
}
