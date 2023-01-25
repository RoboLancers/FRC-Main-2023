package frc.robot.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.Waypoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bananasamirite.robotmotionprofile.ParametricSpline.fromWaypoints;

public class SplineUtils {
    public static ParametricSpline splineFromFile(File file) throws IOException {
        JsonNode node = new ObjectMapper().readTree(file);
        JsonNode waypoints = node.get("waypoints");
        List<Waypoint> parsedWaypoints = new ArrayList<>();
        for (JsonNode waypoint : waypoints) {
            parsedWaypoints.add(new Waypoint(
                    waypoint.get("x").asDouble(),
                    waypoint.get("y").asDouble(),
                    waypoint.get("angle").asDouble(),
                    waypoint.get("weight").asDouble(),
                    waypoint.get("time").asDouble()
            ));
        }
        return fromWaypoints(parsedWaypoints);
    }
}
