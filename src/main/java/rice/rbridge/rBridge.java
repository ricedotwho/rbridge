package rice.rbridge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import rice.rbridge.api.HttpServer;
import rice.rbridge.commands.BridgeCommand;
import rice.rbridge.config.Config;
import rice.rbridge.config.ConfigManager;
import rice.rbridge.handlers.ChatHandler;

import java.io.IOException;
@SideOnly(Side.SERVER)
@Mod(modid = Info.MOD_ID, version = Info.VERSION, acceptableRemoteVersions = "*" )
public class rBridge {
    public static Config config; // directly editable bcs im lazy
    public static HttpServer httpServer;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatHandler());

        try {
            config = ConfigManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new BridgeCommand());
        startServer();
        if(config.webhookURL.isEmpty() && config.webhook) {
            System.out.println("WARNING! webhookURL is empty! The mod WILL NOT WORK!");
        }
        else if(config.botURL.isEmpty() && !config.webhook) {
            System.out.println("WARNING! botURL is empty! The mod WILL NOT WORK!");
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        stopServer();
    }

    public static void startServer() {
        if(config.port.toString().length() != 4) {
            System.out.println("Port is invalid! (" + config.port + ")");
            return;
        }
        try {
            httpServer = new HttpServer(config.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void stopServer() {
        if (httpServer != null) {
            httpServer.stop();
            System.out.println("NanoHTTPD server stopped.");
        }
    }
}
