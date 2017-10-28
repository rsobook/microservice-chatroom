package si.fri.rsobook.rest;

import javax.enterprise.context.RequestScoped;
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

    @GET
    public Response getList() {
        return Response.ok(chatRooms).build();
    }

    @POST
    @Path("{room}")
    public Response postRoom(@PathParam("room") String room) {

        chatRooms.add(room);

        return Response.ok().build();
    }

    @DELETE
    @Path("{room}")
    public Response deleteRoom(@PathParam("room") String room) {

        chatRooms.remove(room);

        return Response.ok().build();
    }
}
