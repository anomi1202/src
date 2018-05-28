package JsonHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

abstract class AbstractJsonHandler {
    private Logger logger = LoggerFactory.getLogger(AutoCloseable.class);
    protected Path PATH_FILE_JSON;

    AbstractJsonHandler(Path PATH_FILE_JSON) {
        this.PATH_FILE_JSON = PATH_FILE_JSON;
    }

    protected void setValueToTag(Path PATH_FILE_JSON, String tagName, String newValue) {
        try (BufferedReader fileReader = Files.newBufferedReader(PATH_FILE_JSON)) {
            Gson jsonDoc = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = jsonDoc.fromJson(fileReader, JsonElement.class).getAsJsonObject();

            setValueToTag(tagName, newValue, jsonObject);
            try (BufferedWriter fileWriter = Files.newBufferedWriter(PATH_FILE_JSON)) {
                String json = jsonDoc.toJson(jsonObject).replace("\\u003d", "=").replace("  ", "\t");
                fileWriter.write(json);
                fileWriter.flush();
            }
        } catch (IOException e) {
            logger.error("FAILED", e);
        }
    }

    private void setValueToTag(String tagName, String newValue, JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> pair : jsonObject.entrySet()) {
            JsonElement l_jsonElement = pair.getValue();

            if (jsonObject.has(tagName) && !jsonObject.get(tagName).getAsString().equals(newValue)) {
                jsonObject.addProperty(tagName, newValue);
                break;
            } else if (!l_jsonElement.isJsonPrimitive()) {
                setValueToTag(tagName, newValue, l_jsonElement.getAsJsonObject());
            }
        }
    }

    public String getValueFromTag(String tagName) throws IOException {
        String tagValue = "";
        try (BufferedReader fileReader = Files.newBufferedReader(PATH_FILE_JSON)) {
            Gson jsonDoc = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = jsonDoc.fromJson(fileReader, JsonElement.class).getAsJsonObject();
            tagValue = getValueFromTag(tagName, jsonObject);
        } catch (IOException e) {
            logger.error("FAILED", e);
            throw new IOException(e.getMessage());
        }

        return tagValue;
    }

    private String getValueFromTag(String tagName, JsonObject jsonObject){
        String tagValue = "";
        for (Map.Entry<String, JsonElement> pair : jsonObject.entrySet()) {
            JsonElement l_jsonElement = pair.getValue();

            if (jsonObject.has(tagName)) {
                tagValue = jsonObject.get(tagName).getAsString();
                break;
            } else if (!l_jsonElement.isJsonPrimitive()){
                tagValue = getValueFromTag(tagName, l_jsonElement.getAsJsonObject());
            }
        }

        return tagValue;
    }

}
