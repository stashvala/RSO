package streaming.user.cdi.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "external-dependencies.video-service.enabled",watch = true)
    private boolean videoServiceEnabled;

    public boolean isVideoServiceEnabled(){
        return videoServiceEnabled;
    }
    public void setVideoServiceEnabled(boolean videoServiceEnabled){
        this.videoServiceEnabled = videoServiceEnabled;
    }
}