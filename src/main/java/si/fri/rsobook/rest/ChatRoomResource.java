package si.fri.rsobook.rest;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;
import si.fri.rsobook.config.ChatRoomApiConfigProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Path("ChatRoom")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatRoomResource {

    private static final Set<String> chatRooms = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Inject
    private ChatRoomApiConfigProperties chatRoomApiConfigProperties;

    @Inject
    @Metric(name = "chatRoom_returned")
    private Counter chatRoomReturnedCounter;


    @GET
    public Response getList() {
        chatRoomReturnedCounter.inc(chatRooms.size());
        return Response.ok(chatRooms).build();
    }

    @Log
    @POST
    @Path("{room}")
    public Response postRoom(@PathParam("room") String room) {

        if(chatRooms.size() <= chatRoomApiConfigProperties.getMaxRooms()) {
            chatRooms.add(room);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Log
    @DELETE
    @Path("{room}")
    public Response deleteRoom(@PathParam("room") String room) {

        chatRooms.remove(room);

        return Response.ok().build();
    }
}
