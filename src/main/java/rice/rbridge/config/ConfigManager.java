package rice.rbridge.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import rice.rbridge.Info;
import rice.rbridge.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class ConfigManager {
    private static final Gson gson = new Gson();
    public static final String configPath = "config/" + Info.MOD_ID + "/config.json";
    private static final File configFile = new File(configPath);

    public static Config init() throws IOException {
        if(!checkConfigDir()) {
            return defaultConfig();
        }
        return gson.fromJson(new InputStreamReader(Files.newInputStream(configFile.toPath())), new TypeToken<Config>(){}.getType());
    }
    public static void writeConfig(Config config) {
        FileUtils.writeJson(config, configFile);
    }
    private static boolean checkConfigDir() {
        if(configFile.exists()) return true;
        File parentDir = configFile.getParentFile();
        if(parentDir != null && !parentDir.exists()) {
            if(!parentDir.mkdirs()) {
                System.out.println("Failed to create file: " + parentDir.getName());
                return false;
            }
            System.out.println("Created file: " + parentDir.getName());
        }
        try {
            if(configFile.createNewFile()) {
                System.out.println("Created file: " + configFile.getName());
                FileUtils.writeJson(defaultConfig(), configFile);
                return true; // Allow it to read the default config
            } else {
                System.out.println("File already exists!: " + configFile.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    private static Config defaultConfig() {
        return new Config(
                true,
                true,
                false,
                false,
                "",
                "",
                5000,
                8080,
                ""
        );
    }
}
