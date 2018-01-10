package streaming.rating.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import streaming.rating.api.configuration.RestProperties;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class RatingServiceHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(RatingServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(RatingServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}