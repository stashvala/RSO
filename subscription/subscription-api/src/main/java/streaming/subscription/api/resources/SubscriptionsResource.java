package streaming.subscription.api.resources;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import streaming.subscription.api.configuration.RestProperties;
import streaming.subscription.cdi.SubscriptionsBean;
import streaming.subscription.persistence.Subscription;

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
@Path("subscriptions")
public class SubscriptionsResource {

    private Logger log = LogManager.getLogger(SubscriptionsResource.class.getName());

    @Inject
    private SubscriptionsBean subsBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getSubscriptions() {
        List<Subscription> subsList = subsBean.getSubscriptions();
        return Response.ok(subsList).build();
    }

    @GET
    @Path("/{subs_id}")
    public Response getSubscription(@PathParam("subs_id") String subs_id) {
        Subscription s = subsBean.getSubscription(subs_id);

        if (subs_id == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(s).build();
    }

    @GET
    @Path("/user/{user_id}")
    public Response getSubscriptionForUser(@PathParam("user_id") String subs_id) {
        Subscription s = subsBean.getSubscriptionsForUser(subs_id);
        return Response.ok(s).build();
    }

    @POST
    @Path("/has_valid")
    public Response getIsValidForUser(String user_id) {
        Boolean isValid = subsBean.hasUserValidSubscription(user_id);
        return Response.ok(isValid).build();
    }
}
