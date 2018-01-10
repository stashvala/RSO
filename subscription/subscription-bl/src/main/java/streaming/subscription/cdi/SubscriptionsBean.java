package streaming.subscription.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import streaming.subscription.persistence.Subscription;

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
public class SubscriptionsBean {

    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private Logger log = LogManager.getLogger(SubscriptionsBean.class.getName());

    @PersistenceContext(unitName = "subscriptions-jpa")
    private EntityManager em;

    @Inject
    private SubscriptionsBean subscriptionsBean;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

    }

    @Log
    public List<Subscription> getSubscriptions() {
        Query query = em.createNamedQuery("subscription.getAll", Subscription.class);
        return query.getResultList();

    }

    @Log
    public Subscription getSubscription(String subscriptionId) {
        log.debug("Finding subscription " + subscriptionId);
        Subscription s = em.find(Subscription.class, subscriptionId);
        return s;
    }

    @Log
    public Subscription getSubscriptionsForUser(String userId) {
        log.debug("Finding subscription for user" + userId);
        Query q = em.createQuery("SELECT s from subscription  s where  s.userId = :user")
                .setParameter("user", userId);
        return (Subscription) q.getSingleResult();
    }

    public List<Subscription> getUsersFilter(UriInfo uri_info) {
        QueryParameters queryParameters = QueryParameters.query(uri_info.getRequestUri().getQuery()).defaultOffset(0).build();
        List<Subscription> users = JPAUtils.queryEntities(em, Subscription.class, queryParameters);
        return users;
    }

    public Boolean hasUserValidSubscription(String userId){
        Subscription s = getSubscriptionsForUser(userId);
        log.debug("Subscription expiration: "+ s.getValidUntil());

        Date today = new Date();
        today.setHours(0);

        return s.getValidFrom().before(today) && s.getValidUntil().after(today);
    }

}
