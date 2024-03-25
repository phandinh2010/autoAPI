package com.api.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import groovy.json.JsonException;
import org.apache.commons.io.FilenameUtils;

import java.io.*;

public class JsonUtils {

    private static Gson gson = new Gson();
    private static final TypeAdapter<JsonElement> strictGsonObjectAdapter = (new Gson()).getAdapter(JsonElement.class);

    private JsonUtils() {
    }

    public static String fromObjectToJsonString(Object object) throws JsonException {
        try {
            return gson.toJson(object);
        } catch (Exception var2) {
            throw new JsonException();
        }
    }

    public static String fromJsonFileToString(String filePath) throws JsonException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            JsonElement jsonElement = (JsonElement)gson.fromJson(bufferedReader, JsonElement.class);
            return (new GsonBuilder()).serializeNulls().setPrettyPrinting().create().toJson(jsonElement);
        } catch (Exception var3) {
            throw new JsonException("File path is incorrect or content is not json valid format");
        }
    }

    public static <T> void fromObjectToJsonFile(T object, String filePath) throws IOException {
        Writer writer = new FileWriter(filePath);
        gson.toJson(object, writer);
        writer.flush();
        writer.close();
    }

    public static <T> T fromJsonStringToObject(String jsonString, Class<T> T) {
        return gson.fromJson(jsonString, T);
    }

    public static <T> T fromJsonFileToObject(String jsonFile, Class<T> T) throws FileNotFoundException {
        return gson.fromJson(new FileReader(jsonFile), T);
    }

    public static <T> T fromJsonStringToObject(String jsonString, String jsonPath, Class<T> T) throws JsonException {
        JsonObject jObject = (JsonObject)gson.fromJson(jsonString, JsonObject.class);
        return getObject(jObject, jsonPath, T);
    }

    public static <T> T fromJsonFileToObject(String jsonFile, String jsonPath, Class<T> T) throws FileNotFoundException, JsonException {
        JsonObject jObject = (JsonObject)gson.fromJson(new FileReader(jsonFile), JsonObject.class);
        return getObject(jObject, jsonPath, T);
    }

    private static <T> T getObject(JsonElement je, String jsonPath, Class<T> t) throws JsonException {
        String[] pathChildren = jsonPath.split("\\.");
        String[] var6 = pathChildren;
        int var7 = pathChildren.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String k = var6[var8];
            if (je == null) {
                throw new JsonException("Unable to find json node with provided jsonpath " + jsonPath);
            }

            if (!je.isJsonObject()) {
                throw new JsonException("Current node must be JsonObject. " + je.getAsString());
            }

            String key = getNode(k);
            Integer idx = getIndex(k);
            if (idx == -1) {
                je = je.getAsJsonObject().get(key);
            } else {
                je = je.getAsJsonObject().get(key).getAsJsonArray().get(idx);
            }
        }

        return gson.fromJson(je, t);
    }

    private static Integer getIndex(String node) {
        return node.contains("[") && node.contains("]") ? Integer.parseInt(node.substring(node.indexOf(91) + 1, node.indexOf(93))) : -1;
    }

    private static String getNode(String node) {
        return node.contains("[") ? node.substring(0, node.indexOf(91)) : node;
    }

    public static boolean isJsonStringValid(String text) {
        try {
            parseStrict(new StringReader(text));
            return true;
        } catch (JsonSyntaxException var2) {
            return false;
        }
    }

    public static boolean isJsonFileValid(String filePath) throws JsonException, FileNotFoundException {
        if (!FilenameUtils.getExtension(filePath).equals("json")) {
            throw new JsonException("Input file should be json");
        } else {
            try {
                parseStrict(new FileReader(filePath));
                return true;
            } catch (JsonSyntaxException var2) {
                return false;
            }
        }
    }

    private static JsonElement parseStrict(Reader jsonReader) {
        try {
            JsonReader reader = new JsonReader(jsonReader);

            JsonElement var3;
            try {
                JsonElement result = (JsonElement)strictGsonObjectAdapter.read(reader);
                reader.hasNext();
                var3 = result;
            } catch (Throwable var5) {
                try {
                    reader.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            reader.close();
            return var3;
        } catch (IOException var6) {
            throw new JsonSyntaxException(var6);
        }
    }
}
