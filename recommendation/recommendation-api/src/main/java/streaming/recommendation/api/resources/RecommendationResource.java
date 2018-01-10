package streaming.recommendation.api.resources;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
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

@Log
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("recommendations")
public class RecommendationResource {

    private Logger log = LogManager.getLogger(RecommendationResource.class.getName());

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




}
