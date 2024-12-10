package rice.rbridge.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fi.iki.elonen.NanoHTTPD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rice.rbridge.handlers.MessageHandler.*;
import static rice.rbridge.rBridge.config;

public class HttpServer extends NanoHTTPD {
    Gson gson = new Gson();

    public HttpServer(int port) throws IOException {
        super(port);
        start(SOCKET_READ_TIMEOUT, false);
        System.out.println("NanoHTTPD server started on port: " + port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();

        if (Method.POST.equals(method)) {
            if("/message".equals(uri)) {
                try {
                    Map<String, String> postData = new HashMap<>();
                    session.parseBody(postData);

                    String messageContent = postData.get("postData");

                    JsonObject jsonCommand = toJson(messageContent);

                    if(!jsonCommand.has("Api-Key") || !jsonCommand.get("Api-Key").getAsString().equals(config.apiKey)) {
                        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", "{\"error\":\"Invalid or missing Api-Key\"}");
                    }

                    if(config.logRequests) System.out.println("Received POST message: " + messageContent);

                    handleMessage(jsonCommand);

                    return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"status\":\"Message received\"}");
                } catch (IOException | ResponseException e) {
                    e.printStackTrace();
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"error\":\"Failed to parse request body\"}");
                }
            }

            if("/command".equals(uri)) {
                try {
                    Map<String, String> postData = new HashMap<>();
                    session.parseBody(postData);

                    String commandContent = postData.get("postData");

                    if(config.logRequests) System.out.println("Received POST command: " + commandContent);

                    JsonObject jsonCommand = toJson(commandContent);

                    if(!jsonCommand.has("Api-Key") || !jsonCommand.get("Api-Key").getAsString().equals(config.apiKey)) {
                        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", "{\"error\":\"Invalid or missing Api-Key\"}");
                    }

                    handleCommand(jsonCommand);

                    return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"status\":\"Message received\"}");
                } catch (IOException | ResponseException e) {
                    e.printStackTrace();
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"error\":\"Failed to parse request body\"}");
                }
            }
        }

        // Handle GET requests or other endpoints as usual
        if ("/status".equals(uri)) {
            return newFixedLengthResponse("{\"status\":True}");
        }

        if ("/online".equals(uri)) {
            MinecraftServer server = MinecraftServer.getServer();

            if (server == null || server.isSinglePlayer()) return newFixedLengthResponse("{\"success\":False}") ;

            List<String> data = new ArrayList<>();

            for (Object obj : server.getConfigurationManager().playerEntityList) {
                if(!(obj instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer) obj;
                data.add(player.getDisplayName());
            }
            return newFixedLengthResponse(gson.toJson(data));
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", "{\"error\":\"Not found\"}");
    }
}
