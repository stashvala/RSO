package streaming.player.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import streaming.video.persistence.Video;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.*;

@RequestScoped
@Log
public class PlayersBean {

    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private Logger log = LogManager.getLogger(PlayersBean.class.getName());

    @Inject
    @DiscoverService(value = "video-service", environment = "dev")
    private Optional<String> basePath;


    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

    }

    public Video getVideo(String videoId) {
        log.info("Basepath = " + basePath.get());
        if (basePath.isPresent()) {
            try {
                HttpGet request = new HttpGet(basePath.get() + "/v1/videos/"+ videoId);
                HttpResponse response = httpClient.execute(request);

                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        String json = EntityUtils.toString(entity);
                        Video v = new ObjectMapper().readValue(json, Video.class);
                        return  v;
                    }
                } else {
                    String msg = "Remote server '" + basePath + "' has responded with status " + status + ".";
                    log.error(msg);
                    throw new InternalServerErrorException(msg);
                }

            } catch (IOException e) {
                String msg = e.getClass().getName() + " occured: " + e.getMessage()
                        + ". BasePath " + basePath.get() + " was used.";
                log.error(msg);
                throw new InternalServerErrorException(msg);
            }
        } else {
            //System.out.println("Video service not yet discovered...");
            log.error("Video service not yet discovered...");
        }

        return null;
    }



}
