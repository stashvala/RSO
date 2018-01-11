package streaming.uploader.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import streaming.uploader.api.configuration.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class UploaderServiceHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(UploaderServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(UploaderServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}