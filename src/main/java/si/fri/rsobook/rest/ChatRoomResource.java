package si.fri.rsobook.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;
import si.fri.rsobook.config.ChatRoomApiConfigProperties;
import si.fri.rsobook.core.restComponenets.utility.JSONObjectMapper;
import si.fri.rsobook.model.RoomStats;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentHashMap;

@Path("ChatRoom")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatRoomResource {

    //private static final Set<String> chatRooms = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    private static final ConcurrentHashMap<String, RoomStats> rooms = new ConcurrentHashMap<String, RoomStats>();

    private final Logger log = LogManager.getLogger(ChatRoomResource.class.getName());

    @Inject
    private ChatRoomApiConfigProperties chatRoomApiConfigProperties;

    @Inject
    @Metric(name = "users_connected")
    private Counter usersConnected;

    @Inject
    @Metric(name = "users_disconnected")
    private Counter usersDisconnected;


    @GET
    public Response getList() {
        try {
            String list = JSONObjectMapper.buildDefault().writeValueAsString(rooms.elements());
            return Response.ok(list).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }

    @Log
    @POST
    @Path("{room}/joined")
    public Response userJoined(@PathParam("room") String room) {
        usersConnected.inc();
        if (!rooms.containsKey(room)) {
            // create new room
            if (rooms.size() >= chatRoomApiConfigProperties.getMaxRooms()) {
                // too many rooms
                log.info("user denied connecting to " + room);
                return Response.status(Response.Status.BAD_REQUEST).entity("Too many rooms").build();
            }
            rooms.put(room, new RoomStats(room, 0));
        }
        RoomStats rs = rooms.get(room);
        rs.incUsers();
        log.info("user connected to " + rs.toJson());
        return Response.ok().build();
    }

    @Log
    @POST
    @Path("{room}/disconnected")
    public Response userDisconnected(@PathParam("room") String room) {
        usersDisconnected.inc();
        RoomStats rs = rooms.get(room);
        if (rs != null) {
            rs.decUsers();
            log.info("user disconnected from " + rs.toJson());
            if (rs.getUserCount() <= 0) {
                log.info("deleting room " + rs.getName());
                rooms.remove(room);
            }
        }

        return Response.ok().build();
    }


//    @Log
//    @POST
//    @Path("{room}")
//    public Response postRoom(@PathParam("room") String room) {
//
//        if(chatRooms.size() <= chatRoomApiConfigProperties.getMaxRooms()) {
//            chatRooms.add(room);
//            return Response.ok().build();
//        } else {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//    }
//
//    @Log
//    @DELETE
//    @Path("{room}")
//    public Response deleteRoom(@PathParam("room") String room) {
//
//        chatRooms.remove(room);
//
//        return Response.ok().build();
//    }
}
