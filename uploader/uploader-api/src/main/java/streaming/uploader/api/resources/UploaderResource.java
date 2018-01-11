package streaming.uploader.api.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.sun.org.apache.regexp.internal.RE;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metered;

import org.eclipse.microprofile.metrics.annotation.Metric;
import streaming.uploader.cdi.UploaderBean;
import streaming.uploader.persistence.Uploader;
import streaming.uploader.cdi.UploaderBean;
import streaming.uploader.api.configuration.RestProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;

@Log
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("uploaders")
public class UploaderResource {

    private Logger log = LogManager.getLogger(UploaderResource.class.getName());

    @Inject
    private UploaderBean uploadersBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    @Metric(name = "simple_counter")
    private Counter counter;

    @GET
    @Metered
    public Response getUploaders() {
        counter.inc(1);
        List<Uploader> uploaderList = uploadersBean.getUploaders();
        return Response.ok(uploaderList).build();
    }

    @GET
    @Path("/counter")
    public Response getCounter(){
        return Response.status(Response.Status.OK).entity(counter).build();
    }

    @GET
    @Path("/{Uploader_id}")
    public Response getUploader(@PathParam("Uploader_id") String uploader_id) {
        log.info("Querying uploader " + uploader_id);
        Uploader uploader = uploadersBean.getUploader(uploader_id);
        if (uploader == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(uploader).build();
    }

    @DELETE
    @Path("/{uploader_id}")
    public Response deleteUploader(@PathParam("uploader_id") String uploader_id) {
        boolean deleted = uploadersBean.deleteUploader(uploader_id);

        if (deleted) return Response.status(Response.Status.GONE).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //Checks service health
    @POST
    @Path("/healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        log.info("Setting health to " + healthy);
        return Response.ok().build();
    }

    //Updates uploader at id
    @PUT
    @Path("/{Uploader_id}")
    public Response putUploader(@PathParam("Uploader_id") String uploader_id, Uploader uploader) {
        uploader = uploadersBean.putUploader(uploader_id, uploader);
        if (uploader == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (uploader.getId() != null) {
            return Response.status(Response.Status.OK).entity(uploader).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    //Creates new uploader
    @POST
    public Response putUploader(Uploader uploader) {
        uploader = uploadersBean.createUploader(uploader);
        if (uploader == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (uploader.getId() != null) {
            return Response.status(Response.Status.OK).entity(uploader).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    //Deletes video only if uploader owns it.
    @DELETE
    @Path("{Uploader_id}/videos/{video_id}")
    public Response deleteVideo(@PathParam("Uploader_id") String uploader_id,
                                @PathParam("video_id") String video_id) {
        boolean deleted = false;
        try {
            deleted = uploadersBean.deleteVideo(uploader_id, video_id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (deleted) return Response.status(Response.Status.GONE).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //Return filtered uploaders
    @GET
    @Path("/filtered")
    public Response getUploadersFiltered() {
        List<Uploader> uploaders;

        uploaders = uploadersBean.getUploadersFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(uploaders).build();
    }

}
