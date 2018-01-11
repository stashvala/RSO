package streaming.player.api.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import streaming.player.cdi.PlayersBean;
import streaming.video.persistence.Video;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@Log
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("player")
public class PlayerResource {

    private Logger log = LogManager.getLogger(PlayerResource.class.getName());

    @Inject
    private PlayersBean playBean;

    @Context
    protected UriInfo uriInfo;


    @GET
    @Path("/{video_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("video_id") String id) {

        Video v = playBean.getVideo(id);

        if(v == null)
            return Response.status(Response.Status.NO_CONTENT).build();
//        Video v = new Video();
//        v.setDuration(102.5);
//        v.setTitle("test");


        Player p = new Player();
        p.setTitle(v.getTitle());
        p.setDuration(v.getDuration());

        Random r = new Random();
        p.setVolume(r.nextDouble()*10);
        p.setSubtitleLang("ENG");

        double elapsed = Instant.now().getEpochSecond() % v.getDuration();

        p.setElapsed(elapsed);

        return Response.ok(p).build();
    }

}

class Player
{
    private String title;

    private double duration;

    private String subtitleLang;

    private double volume;

    private double elapsed;

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getSubtitleLang() {
        return subtitleLang;
    }

    public void setSubtitleLang(String subtitleLang) {
        this.subtitleLang = subtitleLang;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getElapsed() {
        return elapsed;
    }

    public void setElapsed(double elapsed) {
        this.elapsed = elapsed;
    }
}
