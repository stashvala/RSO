package streaming.recommendation.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import io.netty.util.Timeout;
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
public class RecommendationsBean {

    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private Logger log = LogManager.getLogger(RecommendationsBean.class.getName());

    @Inject
    @DiscoverService(value = "video-service", environment = "dev")
    private Optional<String> basePathVideos;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

    }

    @Log
    public Video recommendOne(String userId) {

        List<Video> videos = getVideos(userId);

        if(videos == null)
            return  null;

        //Should be replaced with recommendation based on ratings
        Random r = new Random();
        int recommendedIdx = r.nextInt(videos.size());

        return videos.get(recommendedIdx);
    }

    public List<Video> recommendN(String userId, int n) {

        List<Video> videos = getVideos(userId);

        if(videos == null)
            return  null;

        //Should be replaced with recommendation based on ratings
        List<Video> rec = new ArrayList<Video>();
        Random r = new Random();
        while (n > 0 && videos.size() > 0){

            int recommendedIdx = r.nextInt(videos.size());
            rec.add(videos.get(recommendedIdx));
            videos.remove(recommendedIdx);

            n--;
        }

        return rec;
    }


    @Log
    public List<Video> getVideos(String userId) {
        //System.out.println("Basepath = " + basePath.get());
        log.info("Basepath = " + basePathVideos.get());
        if (!basePathVideos.isPresent()) {
            log.error("Video service not yet discovered...");
        }
        try {
            HttpGet request = new HttpGet(basePathVideos.get() + "/v1/videos");
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return getObjects(EntityUtils.toString(entity));
                }
            } else {
                String msg = "Remote server '" + basePathVideos + "' has responded with status " + status + ".";
                log.error(msg);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return new ArrayList<>();
    }


    private List<Video> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Video.class));
    }


}
