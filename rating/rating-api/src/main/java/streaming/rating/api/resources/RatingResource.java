package streaming.rating.api.resources;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import streaming.rating.api.configuration.RestProperties;
import streaming.rating.cdi.RatingsBean;
import streaming.rating.persistence.Rating;

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
@Path("ratings")
public class RatingResource {

    private Logger log = LogManager.getLogger(RatingResource.class.getName());

    @Inject
    private RatingsBean ratingsBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getRatings() {
        List<Rating> ratingList = ratingsBean.getRatings();
        return Response.ok(ratingList).build();
    }

    @GET
    @Path("/{rating_id}")
    public Response getRating(@PathParam("rating_id") String id) {
        Rating s = ratingsBean.getRating(id);

        if (id == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(s).build();
    }

    @GET
    @Path("/user/{user_id}")
    public Response getRatingForUser(@PathParam("user_id") String id) {
        List<Rating> s = ratingsBean.getRatingsForUser(id);
        return Response.ok(s).build();
    }
//
//    @GET
//    @Path("/video/{video_id}")
//    public Response getRatingForVideo(@PathParam("video_id") String id) {
//        List<Rating> s = ratingsBean.getRatingsForVideo(id);
//        return Response.ok(s).build();
//    }

    @Log
    @GET
    @Path("/video/{video_id}")
    public Response getRatingForVideo(@PathParam("video_id") String id, @QueryParam("agg")String aggType) {
        List<Rating> s = ratingsBean.getRatingsForVideo(id);

        log.info("QueryParam = "+aggType.toString()+ ".");
        if(aggType.equals("average"))
        {
            log.info("Average = "+aggType);
            double avg = ratingsBean.getAverage(s);
            return Response.ok(avg).build();
        }
        else if(aggType.equals("count"))
        {
            int count = s.size();
            return Response.ok(count).build();
        }
        return Response.ok(s).build();
    }

    @POST
    public Response rateVideo(Rating r) {
        r = ratingsBean.createRating(r);
        if (r == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (r.getId() != null) {
            return Response.status(Response.Status.OK).entity(r).build();
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
