package rice.rbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import rice.rbridge.Info;
import rice.rbridge.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;
import static rice.rbridge.rBridge.*;
import static rice.rbridge.config.ConfigManager.writeConfig;
import static rice.rbridge.utils.ChatUtils.*;

public class BridgeCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "b";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> commandAliases = new ArrayList<>();
        commandAliases.add("rbridge");
        commandAliases.add("bridge");
        return commandAliases;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "b <setting> <value>?";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();
        switch (args.length) {
            case 1:
                options.add("help");
                options.add("toggle");
                options.add("webhook");
                options.add("logrequests");
                options.add("online");
                options.add("setapikey");
                break;
        }
        return options;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] raw_args) {
        String[] args = Arrays.stream(raw_args)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        float f;
        switch(args.length) {
            default:
                sendMessageUnknown(args, sender);
                return;
            case 0:
                sendMessagePrefix(Info.MOD_NAME + " made by rice.who. Version: " + Info.VERSION, sender);
                return;
            case 1:
                switch(args[0]) {
                    default:
                        sendMessageUnknown(args[0], sender);
                        return;
                    case "help":
                        // me when /n doesnt work :sob:
                        sendMessagePrefix(GREEN + Info.MOD_NAME + " Help:", sender);
                        sendMessage("/b toggle" + GRAY + " Toggles the mod", sender);
                        sendMessage("/b webhook" + GRAY + " Toggles webhook use, will use the botURL if this is disabled", sender);
                        sendMessage("/b webhook <URL>" + GRAY + " Sets the webhook URL", sender);
                        sendMessage("/b boturl <URL>" + GRAY + " Sets the bot URL", sender);
                        break;
                    case "toggle":
                        config.enabled = !config.enabled;
                        if(config.enabled) { startServer(); } else { stopServer(); }
                        writeConfig(config);
                        sendMessagePrefix(config.enabled ? GREEN + Info.MOD_NAME + " enabled" : RED + Info.MOD_NAME + " disabled", sender);
                        break;
                    case "webhook":
                        config.webhook = !config.webhook;
                        writeConfig(config);
                        sendMessagePrefix("Webhook " + (config.webhook ? GREEN + "enabled" : RED + "disabled"), sender);
                        break;
                    case "logrequests":
                        config.logRequests = !config.logRequests;
                        writeConfig(config);
                        sendMessagePrefix("Request logging " + (config.logRequests ? GREEN + "enabled" : RED + "disabled"), sender);
                        break;
                    case "online":
                        int count = 0;
                        StringBuilder sb = new StringBuilder();
                        MinecraftServer server = MinecraftServer.getServer();
                        if (server == null || server.isSinglePlayer()) return;

                        for (Object obj : server.getConfigurationManager().playerEntityList) {
                            if(!(obj instanceof EntityPlayer)) continue;
                            count++;
                            EntityPlayer player = (EntityPlayer) obj;
                            sb.append(player.getDisplayName()).append("\n");
                        }

                        sendMessagePrefix("Online: " + count + " Players: " + sb, sender);
                        break;
                }
                return;
            case 2:
                switch(args[0]) {
                    default:
                        sendMessageUnknown(args[0], sender);
                        return;
                    case "webhook":
                        config.webhookURL = args[1];
                        writeConfig(config);
                        sendMessagePrefix(GREEN + "Set webhook URL to: " + WHITE + args[1], sender);
                        return;
                    case "boturl":
                        config.botURL = args[1];
                        writeConfig(config);
                        sendMessagePrefix(GREEN + "Set boturl URL to: " + WHITE + args[1], sender);
                        return;
                    case "port":
                        if(!Utils.isInt(args[1])) { sendMessageError("Port must be an integer!", sender); }
                        if(args[1].length() != 4) { sendMessageError("Port must be 4 numbers! 8080 is Default", sender); }
                        config.port = Integer.parseInt(args[1]);
                        writeConfig(config);
                        sendMessagePrefix(GREEN + "Set port URL to: " + WHITE + args[1], sender);
                        return;
                    case "botport":
                        if(!Utils.isInt(args[1])) { sendMessageError("Port must be an integer!", sender); }
                        if(args[1].length() != 4) { sendMessageError("Port must be 4 numbers! 8080 is Default", sender); }
                        config.botPort = Integer.parseInt(args[1]);
                        writeConfig(config);
                        sendMessagePrefix(GREEN + "Set botport URL to: " + WHITE + args[1], sender);
                        return;
                    case "setapikey":
                        config.apiKey = args[1];
                        writeConfig(config);
                        sendMessagePrefix(GREEN + "Set Api-Key to: " + WHITE + args[1], sender);
                        return;
                }
        }
    }
}