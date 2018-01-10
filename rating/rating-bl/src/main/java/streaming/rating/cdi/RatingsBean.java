package streaming.rating.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import streaming.rating.persistence.Rating;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;

@RequestScoped
@Log
public class RatingsBean {

    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private Logger log = LogManager.getLogger(RatingsBean.class.getName());

    @PersistenceContext(unitName = "ratings-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

    }

    @Log
    public List<Rating> getRatings() {
        Query query = em.createNamedQuery("rating.getAll", Rating.class);
        return query.getResultList();

    }

    public Rating createRating(Rating r) {
        try {
            beginTx();
            em.persist(r);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return r;
    }


    public Rating updateRating(String ratingId, Rating r) {

        Rating rating = em.find(Rating.class, ratingId);

        if (rating == null) {
            return null;
        }

        try {
            beginTx();
            r.setId(rating.getId());
            r = em.merge(r);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return r;
    }

    @Log
    public Rating getRating(String ratingId) {
        log.debug("Finding rating " + ratingId);
        Rating s = em.find(Rating.class, ratingId);
        return s;
    }

    @Log
    public List<Rating> getRatingsForUser(String userId) {
        log.debug("Finding rating for user" + userId);
        Query q = em.createQuery("SELECT s from rating  s where  s.userId = :user")
                .setParameter("user", userId);
        return q.getResultList();
    }

    public double getAverage(List<Rating> ratings){

        double avg = 0;
        for (Rating r:ratings)
        {
            avg += r.getRating();
        }

        return avg / ratings.size();
    }

    public int getCount(List<Rating> ratings){

        return ratings.size();
    }

    @Log
    public List<Rating> getRatingsForVideo(String videoId) {
        log.debug("Finding rating for video" + videoId);
        Query q = em.createQuery("SELECT s from rating  s where  s.videoId = :user")
                .setParameter("user", videoId);
        return q.getResultList();
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
