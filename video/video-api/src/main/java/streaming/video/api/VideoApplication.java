package streaming.video.api;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService//(value = "my-service", ttl = 20, pingInterval = 15, environment = "test", version = "1.0.0", singleton = false)
@ApplicationPath("/v1")
public class VideoApplication extends Application {
}
