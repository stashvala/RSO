package streaming.video.api.resources;

import com.sun.org.apache.regexp.internal.RE;

import streaming.video.persistence.Video;
import streaming.video.cdi.VideosBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("videos")
public class VideosResource {

    @Inject
    private VideosBean videosBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getVideos() {
        List<Video> videoList = videosBean.getVideos();
        return Response.ok(videoList).build();
    }

    @GET
    @Path("/{Video_id}")
    public Response getVideo(@PathParam("Video_id") String video_id) {
        Video video = videosBean.getVideo(video_id);
        if (video == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(video).build();
    }

    @PUT
    @Path("/{Video_id}")
    public Response putVideo(@PathParam("Video_id") String video_id, Video video) {
        video = videosBean.putVideo(video_id, video);
        if (video == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (video.getId() != null) {
            return Response.status(Response.Status.OK).entity(video).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{Video_id}")
    public Response deleteVideo(@PathParam("Video_id") String video_id) {
        boolean deleted = videosBean.deleteVideo(video_id);

        if (deleted) return Response.status(Response.Status.GONE).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //REST ENDPOINT
    @GET
    @Path("/filtered")
    public Response getVideosFiltered() {
        List<Video> videos;

        videos = videosBean.getVideosFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(videos).build();
    }

}
