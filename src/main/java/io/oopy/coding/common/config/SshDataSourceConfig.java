package io.oopy.coding.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("local")
public class SshDataSourceConfig {
    private final SshTunnelingInitializer initializer;

    @Value("${DB_URL}")
    private String url;

    @Value("${DB_USER_NAME}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    @Bean(name="dataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        Integer forwardedPort = initializer.buildSshConnection();  // ssh 연결 및 터널링 설정
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver") // todo - env
                .build();
    }

}
