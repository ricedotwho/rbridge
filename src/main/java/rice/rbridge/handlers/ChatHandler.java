package rice.rbridge.handlers;

import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import rice.rbridge.api.API;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static rice.rbridge.rBridge.config;

public class ChatHandler {
    Executor executor = Executors.newSingleThreadExecutor();
    Gson gson = new Gson();
    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        sendMessage(createMessage(event.message, event.username));
    }
    private String createMessage(String message, String name) {
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("message", message);
        return gson.toJson(data);
    }
    private String createURL(String url, String port, String path) {
        if(!(url.startsWith("http"))) { System.out.println("ERROR! Invalid URL!"); return url; }
        return url + (port == null ? "" : ":" + port) + path;
    }
    private void sendMessage(String jsonPayload) {
        if(!config.enabled) return;
        executor.execute(() -> {
            API api = new API();

            if(config.webhook && !config.webhookURL.isEmpty()) {
                api.simplePost(createURL(config.webhookURL, null, ""), jsonPayload);
            }
            else if(!config.webhook && !config.botURL.isEmpty()) {
                 api.simplePost(createURL(config.botURL, String.valueOf(config.botPort), "/sendmessage"), jsonPayload);
            }
        });
    }
}
