package ru.vsu.scoringservice.config;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class TunneledHikariDataSource extends HikariDataSource implements InitializingBean {

    @Value("${ssh.tunnel.create}")
    private Boolean createTunnel;
    @Value("${ssh.tunnel.host}")
    private String tunnelHost;
    @Value("${ssh.tunnel.port}")
    private Integer tunnelPort;
    @Value("${ssh.tunnel.username}")
    private String tunnelUsername;
    @Value("${ssh.tunnel.privateKeyFile}")
    private String tunnelPrivateKeyFile;

    public TunneledHikariDataSource(HikariConfig configuration) {
        super(configuration);
    }

    @Override
    public void afterPropertiesSet() {
        if (createTunnel) {
            try {
                log.info("Start connect to ssh");
                JSch jSch = new JSch();
                jSch.addIdentity(tunnelPrivateKeyFile);

                Session session = jSch.getSession(tunnelUsername, tunnelHost, tunnelPort);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("session");
                channel.connect();
                if (channel.isConnected()) {
                    log.info("Connecting to ssh");
                }
            } catch (JSchException e) {
                log.error("Connection to ssh failed: {}", e.getMessage());
            }
        }
    }
}
