package rice.rbridge.handlers;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class ServerCommandSender implements ICommandSender {

    @Override
    public String getCommandSenderName() {
        return "Server";
    }

    @Override
    public IChatComponent func_145748_c_() {
        return null;
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {
        MinecraftServer.getServer().logInfo(p_145747_1_.getUnformattedText());
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true; // Server console can use any command
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(0, 64, 0); // Default to server spawn
    }

    @Override
    public World getEntityWorld() {
        return MinecraftServer.getServer().worldServers[0]; // Default to overworld
    }
}
