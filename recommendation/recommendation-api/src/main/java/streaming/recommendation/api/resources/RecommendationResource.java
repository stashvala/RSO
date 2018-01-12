package streaming.recommendation.api.resources;

import streaming.recommendation.cdi.RecommendationsBean;
import streaming.video.persistence.Video;

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
@Path("recommendations")
public class RecommendationResource {


    @Inject
    private RecommendationsBean recBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/{user_id}")
    public Response getRecommendation(@PathParam("user_id") String id) {
        Video v = recBean.recommendOne(id);

        if(v == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(v).build();
    }

    @GET
    @Path("/{user_id}/{n}")
    public Response getRecommendations(@PathParam("user_id") String id,@PathParam("n") int n) {
        List<Video> v = recBean.recommendN(id, n);

        if(v == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(v).build();
    }




}
