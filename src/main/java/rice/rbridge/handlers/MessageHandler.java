package rice.rbridge.handlers;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import rice.rbridge.utils.ChatUtils;

import static rice.rbridge.Info.PREFIX;

public class MessageHandler {
    static MinecraftServer server = MinecraftServer.getServer();
    static ServerCommandSender serverSender = new ServerCommandSender();
    public static void handleMessage(JsonObject obj) {
        if(!obj.has("target") || obj.get("target").getAsString().equals("all")) {
            ChatUtils.sendMessageToAllPlayers(formatMssage(obj.get("name").getAsString(), obj.get("message").getAsString(), false));
            return;
        }
        ChatUtils.sendMessageToPlayerByName(formatMssage(obj.get("name").getAsString(), obj.get("message").getAsString(), true), obj.get("target").getAsString());
    }
    private static String formatMssage(String name, String message, boolean whisper) {
        if (whisper) { return  EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC + name + " whispers to you: " + message; }
        return PREFIX + "<" + name + "> " + message;
    }
    public static JsonObject toJson(String jsonStr) {
        Gson g = new Gson();
        return g.fromJson(jsonStr, JsonObject.class);
    }
    private JsonObject toJson(StringBuilder jsonStr) {
        Gson g = new Gson();
        return g.fromJson(jsonStr.toString(), JsonObject.class);
    }

    public static void handleCommand(JsonObject json) {
        if(!json.has("command")) { System.out.println("Recieved command POST with no command! (" + json.getAsString() + ")"); }
        String command = json.get("command").getAsString();
        server.getCommandManager().executeCommand(serverSender, command);
    }
}
