package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ClientConfig.class)
class ClientConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @DisplayName("RestTemplate Bean이 스프링 컨텍스트에 정상 등록되는지 테스트")
    void restTemplateBeanIsLoaded() {
        assertThat(restTemplate).isNotNull();
    }
}
