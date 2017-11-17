package si.fri.rsobook.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.rsobook.config.ChatRoomApiConfigProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class ConfigHealthCheck implements HealthCheck {

    @Inject
    private ChatRoomApiConfigProperties chatRoomApiConfigProperties;

    @Override
    public HealthCheckResponse call() {

        if(chatRoomApiConfigProperties.getMaxRooms() == null){
            return HealthCheckResponse.named(ConfigHealthCheck.class.getSimpleName()).down().build();
        }

        return HealthCheckResponse.named(ConfigHealthCheck.class.getSimpleName()).up().build();
    }

}
