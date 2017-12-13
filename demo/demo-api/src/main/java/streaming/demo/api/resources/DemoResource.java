package streaming.demo.api.resources;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("demo")
public class DemoResource {

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/info")
    public Response getDemo() {
        JsonObjectBuilder out_json = Json.createObjectBuilder();
        JsonArrayBuilder names = Json.createArrayBuilder();
        names.add("sh4851");
        names.add("mj5726");
        out_json.add("clani",names.build());
        out_json.add("opis_projekta","Nas projekt implementira aplikacijo za pretocne vsebine.");

        JsonArrayBuilder ms_url = Json.createArrayBuilder();
        ms_url.add("localhost:8080/v1/users/");
        ms_url.add("localhost:8082/v1/videos/");
        out_json.add("mikrostoritve",ms_url.build());

        JsonArrayBuilder github_url = Json.createArrayBuilder();
        github_url.add("https://github.com/stashvala/RSO");
        github_url.add("https://github.com/stashvala/RSO");
        out_json.add("github",github_url.build());

        JsonArrayBuilder travis_url = Json.createArrayBuilder();
        travis_url.add("https://travis-ci.org/stashvala/RSO");
        travis_url.add("https://travis-ci.org/stashvala/RSO");
        out_json.add("travis",travis_url.build());

        JsonArrayBuilder dockerhub_url = Json.createArrayBuilder();
        dockerhub_url.add("https://hub.docker.com/r/stashvala/streaming_app/");
        dockerhub_url.add("https://hub.docker.com/r/stashvala/streaming_app/");
        out_json.add("dockerhub",dockerhub_url.build());

        return Response.status(Response.Status.OK).entity(out_json.build().toString()).build();
    }

}
