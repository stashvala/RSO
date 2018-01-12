package streaming.comment.api.resources;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import streaming.comment.api.configuration.RestProperties;
import streaming.comment.cdi.CommentsBean;
import streaming.comment.persistence.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Log
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("comments")
public class CommentResource {

    private Logger log = LogManager.getLogger(CommentResource.class.getName());

    @Inject
    private CommentsBean commentsBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getComments() {
        List<Comment> commentList = commentsBean.getComments();
        return Response.ok(commentList).build();
    }

    @GET
    @Path("/{comment_id}")
    public Response getComment(@PathParam("comment_id") String id) {
        Comment c = commentsBean.getComment(id);

        if (id == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(c).build();
    }

    @GET
    @Path("/user/{user_id}")
    public Response getCommentForUser(@PathParam("user_id") String id) {
        List<Comment> c = commentsBean.getCommentsForUser(id);
        if(c == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(c).build();
    }
    @Log
    @GET
    @Path("/video/{video_id}")
    public Response getCommentForVideo(@PathParam("video_id") String id) {
        List<Comment> c = commentsBean.getCommentsForVideo(id);

        if(c == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(c).build();
    }

    @POST
    public Response commentVideo(Comment r) {
        r = commentsBean.createComment(r);
        if (r == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if (r.getId() != null) {
            return Response.status(Response.Status.OK).entity(r).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @PUT
    @Path("/{Comment_id}")
    public Response putComment(@PathParam("Comment_id") String comment_id, Comment comment) {
        comment = commentsBean.putComment(comment_id, comment);
        if (comment == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (comment.getId() != null) {
            return Response.status(Response.Status.OK).entity(comment).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @POST
    @Path("/healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        log.info("Setting health to " + healthy);
        return Response.ok().build();
    }
}
