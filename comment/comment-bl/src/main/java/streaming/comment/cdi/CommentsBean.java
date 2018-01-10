package streaming.comment.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import streaming.comment.persistence.Comment;

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
public class CommentsBean {

    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private Logger log = LogManager.getLogger(CommentsBean.class.getName());

    @PersistenceContext(unitName = "comments-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

    }

    @Log
    public List<Comment> getComments() {
        Query query = em.createNamedQuery("comment.getAll", Comment.class);
        return query.getResultList();

    }

    public Comment createComment(Comment r) {
        try {
            beginTx();
            em.persist(r);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return r;
    }

    public Comment putComment(String userId, Comment user) {

        Comment u = em.find(Comment.class, userId);

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


    public Comment updateComment(String commentId, Comment r) {

        Comment comment = em.find(Comment.class, commentId);

        if (comment == null) {
            return null;
        }

        try {
            beginTx();
            r.setId(comment.getId());
            r = em.merge(r);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return r;
    }

    @Log
    public Comment getComment(String commentId) {
        log.debug("Finding comment " + commentId);
        Comment s = em.find(Comment.class, commentId);
        return s;
    }

    @Log
    public List<Comment> getCommentsForUser(String userId) {
        log.debug("Finding comment for user" + userId);
        Query q = em.createQuery("SELECT s from comment  s where  s.userId = :user")
                .setParameter("user", userId);
        return q.getResultList();
    }


    @Log
    public List<Comment> getCommentsForVideo(String videoId) {
        log.debug("Finding comment for video" + videoId);
        Query q = em.createQuery("SELECT s from comment  s where  s.videoId = :user")
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
