package rice.rbridge.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    static Gson gson = new Gson();
    static Gson pgson = new GsonBuilder().setPrettyPrinting().create();
    public static void writeJson(Object obj, File file) {
        writeJson(obj, file, true);
    }
    public static void writeJson(Object obj, File file, boolean pretty) {
        try {
            FileWriter myWriter = new FileWriter(file);
            if(pretty) { pgson.toJson(obj, myWriter); }
            else { gson.toJson(obj, myWriter); }
            myWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
