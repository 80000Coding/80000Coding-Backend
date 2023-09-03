package io.oopy.coding.common.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "ssh")
@Validated
@Slf4j
public class SshTunnelingInitializer {

    @Value("${ssh.host}")
    private String host;
    @Value("${ssh.user}")
    private String user;
    @Value("${ssh.port}")
    private int sshPort;
    @Value("${ssh.password}")
    private String password;
    @Value("${ssh.database-port}")
    private int databasePort;

    private Session session;

    @PreDestroy
    public void closeSSH() {
        if (session != null && session.isConnected())
            session.disconnect();
    }

    public Integer buildSshConnection() {
        Integer forwardedPort = null;

        try {
            log.debug("{}@{}:{}:{} with privateKey",user, host, sshPort, databasePort);
            log.info("start ssh tunneling..");
            JSch jSch = new JSch();
            log.info("creating ssh session");
            session = jSch.getSession(user, host, sshPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();  // ssh 연결
            log.info("success connecting ssh connection ");

            // 로컬pc의 남는 포트 하나와 원격 접속한 pc의 db포트 연결
            log.info("start forwarding");
            forwardedPort = session.setPortForwardingL(33306, "localhost", databasePort);
            log.info("successfully connected to database");
        } catch (JSchException e){
            this.closeSSH();
            e.printStackTrace();
            log.error("fail to make ssh tunneling : {}", e.getMessage());
        }
        return forwardedPort;
    }
}
