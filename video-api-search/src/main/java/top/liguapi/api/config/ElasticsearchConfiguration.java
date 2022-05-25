package top.liguapi.api.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/25 21:20
 */
@Configuration
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

    @Override
    @Bean("restHighLevelClient")
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration build = ClientConfiguration.builder().connectedTo(host + ":" + port).build();
        return RestClients.create(build).rest();
    }
}
