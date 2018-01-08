package streaming.user.api.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.sun.org.apache.regexp.internal.RE;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;

import streaming.user.persistence.User;
import streaming.user.cdi.UsersBean;
import streaming.user.api.configuration.RestProperties;

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
@Path("users")
public class UsersResource {

    private Logger log = LogManager.getLogger(UsersResource.class.getName());

    @Inject
    private UsersBean usersBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Metered
    public Response getUsers() {
        List<User> userList = usersBean.getUsers();
        return Response.ok(userList).build();
    }

    @GET
    @Path("/{User_id}")
    public Response getUser(@PathParam("User_id") String user_id) {
        log.info("Querying user " + user_id);
        User user = usersBean.getUser(user_id);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        log.info("Setting health to " + healthy);
        return Response.ok().build();
    }

    @PUT
    @Path("/{User_id}")
    public Response putUser(@PathParam("User_id") String user_id, User user) {
        user = usersBean.putUser(user_id, user);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (user.getId() != null) {
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{User_id}")
    public Response deleteUser(@PathParam("User_id") String user_id) {
        boolean deleted = usersBean.deleteUser(user_id);

        if (deleted) return Response.status(Response.Status.GONE).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //REST ENDPOINT
    @GET
    @Path("/filtered")
    public Response getUsersFiltered() {
        List<User> users;

        users = usersBean.getUsersFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(users).build();
    }

}
