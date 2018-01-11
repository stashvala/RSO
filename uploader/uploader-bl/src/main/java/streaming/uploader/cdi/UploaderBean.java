package streaming.uploader.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import streaming.uploader.persistence.Uploader;
import streaming.video.persistence.Video;
import streaming.uploader.cdi.configuration.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;

@RequestScoped
@Log
public class UploaderBean {

    private Logger log = LogManager.getLogger(UploaderBean.class.getName());

    @PersistenceContext(unitName = "uploaders-jpa")
    private EntityManager em;

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

    @Inject
    private RestProperties restProperties;

    @Inject
    @DiscoverService(value = "video-service", environment = "dev")
    private Optional<String> basePath;

    @Inject
    private UploaderBean uploadersBean;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

        // basePath = "http://localhost:8081/v1/";
    }


    public List<Uploader> getUploaders() {
        Query query = em.createNamedQuery("Uploader.getAll", Uploader.class);
        List<Uploader> uploaders = query.getResultList();

        return uploaders;
    }

    @Log
    public Uploader getUploader(String uploaderId) {
        //System.out.println("bean getUploader: " + uploaderId);
        log.info("bean getUploader: " + uploaderId);
        Uploader uploader = em.find(Uploader.class, uploaderId);

        if (uploader == null) {
            throw new NotFoundException();
        }

        List<Video> videos = uploadersBean.getVideos(uploaderId);
        uploader.setVideos(videos);

        return uploader;
    }

    @Log
    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getVideosFallback")
    @Timeout
    public List<Video> getVideos(String userId) {
        //System.out.println("Basepath = " + basePath.get());
        log.info("Basepath = " + basePath.get());
        if (basePath.isPresent()) {
            try {
                HttpGet request = new HttpGet(basePath.get() + "/v1/videos?where=userId:EQ:" + userId);
                HttpResponse response = httpClient.execute(request);

                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        return getObjects(EntityUtils.toString(entity));
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

        return new ArrayList<>();
    }

    @Log
    @Timeout
    public Video getVideo(String uploaderId, String videoId) {
        log.info("Basepath = " + basePath.get());
        if (basePath.isPresent()) {
            try {
                HttpGet request = new HttpGet(basePath.get() + "/v1/videos?where=uploaderId:EQ:" + uploaderId+" videoID:EQ:" + videoId);
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

    public List<Video> getVideosFallback(String uploaderId) {

        List<Video> videos = new ArrayList<>();

        Video video = new Video();

        video.setTitle("N/A");
        video.setViews(-1);
        video.setDuration(0.0);

        videos.add(video);

        return videos;
    }

    private List<Video> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Video.class));
    }


    public Uploader createUploader(Uploader uploader) {
        try {
            beginTx();
            em.persist(uploader);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return uploader;
    }

    public Uploader putUploader(String uploaderId, Uploader uploader) {

        Uploader u = em.find(Uploader.class, uploaderId);

        if (u == null) {
            return null;
        }

        try {
            beginTx();
            uploader.setId(u.getId());
            uploader = em.merge(uploader);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return uploader;
    }

    public boolean deleteUploader(String uploaderId) {

        Uploader uploader = em.find(Uploader.class, uploaderId);

        if (uploader != null) {
            try {
                beginTx();
                em.remove(uploader);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    public boolean deleteVideo(String uploaderId, String videoId) throws IOException {

        log.info("Deleting video for uploader");
        if(!basePath.isPresent())
            return false;

        Video v = getVideo(uploaderId, videoId);

        if (v == null)
            return false;
        String urlString = basePath.get() + "v1/videos/"+videoId;

        try{
        URL url = new URL(urlString);
        HttpURLConnection httpCon = (HttpURLConnection)
        url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty(
                "Content-Type", "application/json" );
        httpCon.setRequestMethod("DELETE");
        httpCon.connect();
        }catch (Exception e){
            log.error("Error deleting video "+e.getMessage());
            return false;
        }

        return true;
    }

    public List<Uploader> getUploadersFilter(UriInfo uri_info) {
        QueryParameters queryParameters = QueryParameters.query(uri_info.getRequestUri().getQuery()).defaultOffset(0).build();
        List<Uploader> uploaders = JPAUtils.queryEntities(em, Uploader.class, queryParameters);
        return uploaders;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
