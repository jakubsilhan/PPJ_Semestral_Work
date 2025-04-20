package semestralwork.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import semestralwork.Provisioning.Provisioner;

@Configuration
public class PersistenceConfiguration {

    @Profile({"devel","test"})
    @Bean(initMethod = "doProvision")
    public Provisioner provisioner() {
        return new Provisioner();
    }
}
