package streaming.video.cdi;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import streaming.video.persistence.Video;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
public class VideosBean {

    @PersistenceContext(unitName = "videos-jpa")
    private EntityManager em;

    public List<Video> getVideos() {

        Query query = em.createNamedQuery("Video.getAll", Video.class);

        return query.getResultList();

    }

    public Video getVideo(String videoId) {

        Video video = em.find(Video.class, videoId);

        if (video == null) {
            throw new NotFoundException();
        }

        return video;
    }

    public Video createVideo(Video video) {

        try {
            beginTx();
            em.persist(video);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return video;
    }

    public Video putVideo(String videoId, Video video) {

        Video v = em.find(Video.class, videoId);

        if (v == null) {
            return null;
        }

        try {
            beginTx();
            video.setId(v.getId());
            video = em.merge(video);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return video;
    }

    public boolean deleteVideo(String videoId) {

        Video video = em.find(Video.class, videoId);

        if (video != null) {
            try {
                beginTx();
                em.remove(video);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    public List<Video> getVideosFilter(UriInfo uri_info) {
        QueryParameters queryParameters = QueryParameters.query(uri_info.getRequestUri().getQuery()).defaultOffset(0).build();
        List<Video> videos = JPAUtils.queryEntities(em, Video.class, queryParameters);
        return videos;
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
