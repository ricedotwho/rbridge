package rice.rbridge.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import static net.minecraft.util.EnumChatFormatting.RED;
import static rice.rbridge.Info.PREFIX;

public class ChatUtils {
    public static void sendMessageUnknown(String[] args, ICommandSender sender) {
        if(sender == null) return;
        StringBuilder sb = new StringBuilder();
        for(String s : args) {
            sb.append(" ").append(s);
        }
        sendMessage(RED + "Unknown Setting (Args:" + sb + ")", sender);
    }
    public static void sendMessageUnknown(String arg, ICommandSender sender) {
        sendMessageUnknown(new String[]{arg}, sender);
    }
    public static void sendMessage(String message, ICommandSender sender) {
        sendMessage(new ChatComponentText(message), sender);
    }
    public static void sendMessage(ChatComponentText chatComponentText, ICommandSender sender) {
        if(sender == null) return;
        sender.addChatMessage(chatComponentText);
    }
    public static void sendMessage(String message, EntityPlayer player) {
        sendMessage(new ChatComponentText(message), player);
    }
    public static void sendMessage(ChatComponentText chatComponentText, EntityPlayer player) {
        if(player == null) return;
        player.addChatMessage(chatComponentText);
    }
    public static void sendMessagePrefix(String message, ICommandSender sender) {
        sendMessage(PREFIX + message, sender);
    }
    public static void sendMessageError(String message, ICommandSender sender) {
        sendMessagePrefix(RED + message, sender);
    }
    public static void sendMessageToAllPlayers(String message) {
        if(message == null) return;
        MinecraftServer server = MinecraftServer.getServer();

        if (server == null || server.isSinglePlayer()) return;

        for (Object obj : server.getConfigurationManager().playerEntityList) {
            if(!(obj instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) obj;
            sendMessage(message, player);
        }
    }
    public static void sendMessageToPlayerByName(String message, String target) {
        if(target == null || target.equals("null")) { sendMessageToAllPlayers(message); return; }
        MinecraftServer server = MinecraftServer.getServer();

        if (server == null || server.isSinglePlayer()) return;

        for (Object obj : server.getConfigurationManager().playerEntityList) {
            if(!(obj instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) obj;
            System.out.println("Target: " + target + " player: " + player.getDisplayName());
            if(!target.equalsIgnoreCase(player.getDisplayName())) continue;
            sendMessage(message, player);
            return;
        }
    }
}
