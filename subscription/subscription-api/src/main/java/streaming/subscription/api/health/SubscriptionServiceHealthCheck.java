package streaming.subscription.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import streaming.subscription.api.configuration.RestProperties;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class SubscriptionServiceHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(SubscriptionServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(SubscriptionServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}