package streaming.user.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import streaming.user.persistence.User;
import streaming.video.persistence.Video;
import streaming.user.cdi.configuration.RestProperties;

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
import java.util.*;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;

@RequestScoped
@Log
public class UsersBean {

    private Logger log = LogManager.getLogger(UsersBean.class.getName());

    @PersistenceContext(unitName = "users-jpa")
    private EntityManager em;

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

    @Inject
    private RestProperties restProperties;

    @Inject
    @DiscoverService(value = "video-service", environment = "dev")
    private Optional<String> basePath;

    @Inject
    private UsersBean usersBean;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

        // basePath = "http://localhost:8081/v1/";
    }


    public List<User> getUsers() {
        Query query = em.createNamedQuery("User.getAll", User.class);

        List<User> users = query.getResultList();

        for(User u : users){
            List<Video> videos = usersBean.getVideos(u.getId());
            u.setVideos(videos);
        }

        return users;
    }

    @Log
    public User getUser(String userId) {
        //System.out.println("bean getUser: " + userId);
        log.info("bean getUser: " + userId);
        User user = em.find(User.class, userId);

        if (user == null) {
            throw new NotFoundException();
        }

        List<Video> videos = usersBean.getVideos(userId);
        user.setVideos(videos);

        return user;
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

    public List<Video> getVideosFallback(String userId) {

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

    public User createUser(User user) {
        try {
            beginTx();
            em.persist(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return user;
    }

    public User putUser(String userId, User user) {

        User u = em.find(User.class, userId);

        if (u == null) {
            return null;
        }

        try {
            beginTx();
            user.setId(u.getId());
            user = em.merge(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return user;
    }

    public boolean deleteUser(String userId) {

        User user = em.find(User.class, userId);

        if (user != null) {
            try {
                beginTx();
                em.remove(user);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    public List<User> getUsersFilter(UriInfo uri_info) {
        QueryParameters queryParameters = QueryParameters.query(uri_info.getRequestUri().getQuery()).defaultOffset(0).build();
        List<User> users = JPAUtils.queryEntities(em, User.class, queryParameters);
        return users;
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
