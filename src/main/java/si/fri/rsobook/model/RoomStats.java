package si.fri.rsobook.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import si.fri.rsobook.core.restComponenets.utility.JSONObjectMapper;


public class RoomStats {

    public String name;

    public int userCount;

    public RoomStats(String name, int userCount) {
        this.name = name;
        this.userCount = userCount;
    }

    public int incUsers() {
        userCount += 1;
        return userCount;
    }

    public int decUsers() {
        userCount -= 1;
        return userCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String toJson() {
        try {
            return JSONObjectMapper.buildDefault().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
