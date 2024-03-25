package com.api.rest;

import com.api.exceptions.JsonCreationException;
import com.api.exceptions.JsonElementNotFoundException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class RestBody {

    private JsonElement jsonElement;

    public RestBody() {
        this.jsonElement = new JsonObject();
    }

    public RestBody(JsonObject jsonElement) {
        this.jsonElement = jsonElement;
    }

    public RestBody(String body) {
        this.jsonElement = (JsonElement) (new GsonBuilder()).serializeNulls().create().fromJson(body, JsonElement.class);
    }

    public RestBody(Object body) {
        this.jsonElement = (new GsonBuilder()).serializeNulls().create().toJsonTree(body);
    }

    public String print() {
        return (new GsonBuilder()).serializeNulls().create().toJson(this.jsonElement);
    }

    public String prettyPrint() {
        return (new GsonBuilder()).serializeNulls().setPrettyPrinting().create().toJson(this.jsonElement);
    }

    public void add(String key, Object value) {
        if (this.jsonElement.isJsonObject()) {
            this.add(this.jsonElement.getAsJsonObject(), key, value);
        } else if (this.jsonElement.isJsonPrimitive() || this.jsonElement.isJsonNull()) {
            throw new JsonCreationException("Invalid JsonElement");
        }

    }

    public void addMap(String parentKey, String key, Object value) {
        if (this.jsonElement.isJsonObject()) {
            this.addMap(this.jsonElement.getAsJsonObject(), parentKey, key, value);
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public void addArrayValue(String key, Object value) {
        this.addArrayValue(this.jsonElement.getAsJsonObject(), key, value);
    }

    public void addValueToArray(String key, Object value) {
        this.addArrayValue(this.jsonElement.getAsJsonObject(), key, value);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void addArrayMap(String parentKey, String key, Object value) {
        this.addArrayMap(this.jsonElement.getAsJsonObject(), parentKey, key, value);
    }

    public void addMapToArray(String parentKey, String key, Object value) {
        this.addArrayMap(this.jsonElement.getAsJsonObject(), parentKey, key, value);
    }

    public void deleteMapInArray(String key, int index) {
        this.deleteArrayMap(this.jsonElement.getAsJsonObject(), key, index);
    }

    public void delete(String key) {
        this.delete(this.jsonElement.getAsJsonObject(), key);
    }

    public void deleteValueArrayValue(String key, int index) {
        this.deleteArrayValue(this.jsonElement.getAsJsonObject(), key, index);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void createJson(String parentKey, String key, Object value) {
        if (!parentKey.contains(".") && parentKey.contains("[") && parentKey.contains("]")) {
            this.addMapToArray(parentKey, key, value);
        } else if (!parentKey.contains(".") && !parentKey.contains("[") && !parentKey.contains("]")) {
            this.addMap(parentKey, key, value);
        } else {
            String[] path = parentKey.split("\\.");
            String firstNode = this.getNode(path[0]);
            int index = this.getIndex(path[0]);
            this.jsonElement.getAsJsonObject().add(firstNode, this.createJsonElement(this.jsonElement.getAsJsonObject().get(firstNode), index, path, 1, key, value));
        }

    }

    public void addMapWithJsonPath(String parentKey, String key, Object value) {
        if (!parentKey.contains(".") && parentKey.contains("[") && parentKey.contains("]")) {
            this.addMapToArray(parentKey, key, value);
        } else if (!parentKey.contains(".") && !parentKey.contains("[") && !parentKey.contains("]")) {
            this.addMap(parentKey, key, value);
        } else {
            String[] path = parentKey.split("\\.");
            String firstNode = this.getNode(path[0]);
            int index = this.getIndex(path[0]);
            this.jsonElement.getAsJsonObject().add(firstNode, this.createJsonElement(this.jsonElement.getAsJsonObject().get(firstNode), index, path, 1, key, value));
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public void createJson(String parentKey, Object value) {
        if (!parentKey.contains(".") && parentKey.contains("[") && parentKey.contains("]")) {
            this.addValueToArray(parentKey, value);
        } else {
            String[] path = parentKey.split("\\.");
            String node = this.getNode(path[0]);
            int nodeIndex = this.getIndex(path[0]);
            this.jsonElement.getAsJsonObject().add(node, this.createJsonElement(this.jsonElement.getAsJsonObject().get(node), nodeIndex, path, 1, value));
        }

    }

    public void addValueWithJsonPath(String parentKey, Object value) {
        if (!parentKey.contains(".") && parentKey.contains("[") && parentKey.contains("]")) {
            this.addValueToArray(parentKey, value);
        } else {
            String[] path = parentKey.split("\\.");
            String node = this.getNode(path[0]);
            int nodeIndex = this.getIndex(path[0]);
            this.jsonElement.getAsJsonObject().add(node, this.createJsonElement(this.jsonElement.getAsJsonObject().get(node), nodeIndex, path, 1, value));
        }

    }

    private JsonElement createJsonElement(JsonElement jeCurNode, int curNodeIndex, String[] path, int curLevel, Object value) {
        if (jeCurNode == null) {
            if (curNodeIndex == -1) {
                jeCurNode = new JsonObject();
            } else {
                jeCurNode = new JsonArray();
            }
        }

        int length = path.length;
        String nextNode;
        int nextNodeIndex;
        JsonElement jeChild;
        JsonElement jeChild2;
        if (((JsonElement) jeCurNode).isJsonObject()) {
            JsonObject joCurNode = ((JsonElement) jeCurNode).getAsJsonObject();
            if (curLevel == length) {
                throw new JsonElementNotFoundException("The last node should be an array.");
            } else {
                nextNode = this.getNode(path[curLevel]);
                nextNodeIndex = this.getIndex(path[curLevel]);
                ++curLevel;
                jeChild = joCurNode.get(nextNode);
                jeChild2 = this.createJsonElement(jeChild, nextNodeIndex, path, curLevel, value);
                joCurNode.add(nextNode, jeChild2);
                return joCurNode;
            }
        } else if (!((JsonElement) jeCurNode).isJsonArray()) {
            return null;
        } else {
            JsonArray jaCurNode = ((JsonElement) jeCurNode).getAsJsonArray();
            if (curLevel == length) {
                this.addPrimitive(jaCurNode, value);
            } else {
                nextNode = this.getNode(path[curLevel]);
                nextNodeIndex = this.getIndex(path[curLevel]);
                ++curLevel;
                if (jaCurNode.size() != 0 && jaCurNode.size() > curNodeIndex) {
                    if (jaCurNode.size() != 0 && jaCurNode.size() > curNodeIndex) {
                        jeChild = jaCurNode.get(curNodeIndex);
                        JsonObject s;
                        if (jeChild.isJsonArray()) {
                            s = new JsonObject();
                            jeChild2 = jeChild.getAsJsonArray().get(nextNodeIndex);
                            this.addJsonElement(s, nextNode, this.createJsonElement(jeChild2, nextNodeIndex, path, curLevel, value));
                            jaCurNode.add(s);
                        } else if (jeChild.isJsonObject()) {
                            jeChild2 = jeChild.getAsJsonObject().get(nextNode);
                            s = this.addJsonElement(jeChild.getAsJsonObject(), nextNode, this.createJsonElement(jeChild2, nextNodeIndex, path, curLevel, value));
                            s.getAsJsonObject();
                        }
                    }
                } else {
                    JsonObject joChildNode;
                    if (nextNodeIndex == -1) {
                        joChildNode = new JsonObject();
                        JsonObject joChildNode2 = new JsonObject();
                        jaCurNode.add(this.add(joChildNode, nextNode, this.createJsonElement(joChildNode2, nextNodeIndex, path, curLevel, value)));
                    } else {
                        joChildNode = new JsonObject();
                        JsonArray jaChildNode = new JsonArray();
                        joChildNode.add(nextNode, this.createJsonElement(jaChildNode, nextNodeIndex, path, curLevel, value));
                        jaCurNode.add(joChildNode);
                    }
                }
            }

            return jaCurNode;
        }
    }

    private JsonElement createJsonElement(JsonElement jsonElement, int curNodeIndex, String[] path, int curIndex, String key, Object value) {
        if (jsonElement == null) {
            if (curNodeIndex == -1) {
                jsonElement = new JsonObject();
            } else {
                jsonElement = new JsonArray();
            }
        }

        if (((JsonElement) jsonElement).isJsonObject()) {
            if (curIndex == path.length) {
                return this.addProperty(((JsonElement) jsonElement).getAsJsonObject(), key, value);
            } else {
                String node = this.getNode(path[curIndex]);
                int curChildNodeIndex = this.getIndex(path[curIndex]);
                ++curIndex;
                ((JsonElement) jsonElement).getAsJsonObject().add(node, this.createJsonElement(((JsonElement) jsonElement).getAsJsonObject().get(node), curChildNodeIndex, path, curIndex, key, value));
                return ((JsonElement) jsonElement).getAsJsonObject();
            }
        } else {
            if (((JsonElement) jsonElement).isJsonArray()) {
                JsonArray ja = ((JsonElement) jsonElement).getAsJsonArray();
                if (curIndex != path.length) {
                    String childNode = this.getNode(path[curIndex]);
                    int childNodeIndex = this.getIndex(path[curIndex]);
                    ++curIndex;
                    if (ja.size() != 0 && ja.size() > curNodeIndex) {
                        if (ja.size() != 0 && curNodeIndex < ja.size()) {
                            JsonElement jeChild = ja.get(curNodeIndex);
                            JsonObject s;
                            JsonElement jeChild2;
                            if (jeChild.isJsonArray()) {
                                s = new JsonObject();
                                jeChild2 = jeChild.getAsJsonArray().get(childNodeIndex);
                                this.addJsonElement(s, childNode, this.createJsonElement(jeChild2, childNodeIndex, path, curIndex, key, value));
                                ja.add(s);
                            } else if (jeChild.isJsonObject()) {
                                jeChild2 = jeChild.getAsJsonObject().get(childNode);
                                s = this.addJsonElement(jeChild.getAsJsonObject(), childNode, this.createJsonElement(jeChild2, childNodeIndex, path, curIndex, key, value));
                                s.getAsJsonObject();
                            }
                        }
                    } else {
                        JsonObject joChildNode;
                        if (childNodeIndex == -1) {
                            joChildNode = new JsonObject();
                            JsonObject joChildNode2 = new JsonObject();
                            ja.add(this.add(joChildNode, childNode, this.createJsonElement(joChildNode2, childNodeIndex, path, curIndex, key, value)));
                        } else {
                            joChildNode = new JsonObject();
                            JsonArray jaChildNode = new JsonArray();
                            joChildNode.add(childNode, this.createJsonElement(jaChildNode, childNodeIndex, path, curIndex, key, value));
                            ja.add(joChildNode);
                        }
                    }

                    return ja;
                }

                JsonObject j = new JsonObject();
                this.addProperty(j, key, value);
                if (ja.size() == 0 || ja.size() <= curNodeIndex) {
                    ja.add(j);
                    return ja;
                }

                if (ja.get(curNodeIndex).isJsonObject()) {
                    this.addProperty(ja.get(curNodeIndex).getAsJsonObject(), key, value);
                    return ja;
                }

                if (ja.get(curNodeIndex).isJsonArray()) {
                    ja.get(curNodeIndex).getAsJsonArray().add(j);
                    return ja.get(curNodeIndex).getAsJsonArray();
                }
            }

            return null;
        }
    }

    private Integer getIndex(String node) {
        return node.contains("[") && node.contains("]") ? Integer.parseInt(node.substring(node.indexOf(91) + 1, node.indexOf(93))) : -1;
    }

    private String getNode(String node) {
        return node.contains("[") ? node.substring(0, node.indexOf(91)) : node;
    }

    private void deleteArrayValue(JsonObject jsonObject, String parentKey, Integer index) {
        JsonElement je = jsonObject.get(parentKey);
        if (je != null && je.isJsonArray()) {
            je.getAsJsonArray().remove(index);
        }

    }

    private void delete(JsonObject jsonObject, String key) {
        if (jsonObject.get(key) != null) {
            jsonObject.remove(key);
        }

    }

    private void deleteArrayMap(JsonObject jsonObject, String parentKey, Integer index) {
        JsonElement je = jsonObject.get(parentKey);
        if (je != null && je.isJsonArray()) {
            JsonElement jeChild = je.getAsJsonArray().get(index);
            if (jeChild != null) {
                jeChild.getAsJsonArray().remove(index);
            }
        }

    }

    private JsonObject addProperty(JsonObject jo, String key, Object value) {
        if (value instanceof Number) {
            jo.addProperty(key, (Number) value);
        } else if (value instanceof String) {
            jo.addProperty(key, (String) value);
        } else if (value instanceof Character) {
            jo.addProperty(key, (Character) value);
        } else if (value instanceof Boolean) {
            jo.addProperty(key, (Boolean) value);
        } else if (value instanceof JsonElement) {
            jo.add(key, (JsonElement) value);
        }

        return jo;
    }

    private JsonObject addJsonElement(JsonObject jo, String key, JsonElement value) {
        jo.add(key, value);
        return jo;
    }

    private void addPrimitive(JsonArray ja, Object value) {
        if (value instanceof Number) {
            ja.add(new JsonPrimitive((Number) value));
        } else if (value instanceof String) {
            ja.add(new JsonPrimitive((String) value));
        } else if (value instanceof Character) {
            ja.add(new JsonPrimitive((Character) value));
        } else if (value instanceof Boolean) {
            ja.add(new JsonPrimitive((Boolean) value));
        }

    }

    private JsonElement add(JsonObject jsonObject, String key, Object value) {
        if (key.contains(".")) {
        }

        return this.addProperty(jsonObject, key, value);
    }

    private void addMap(JsonObject jsonObject, String parentKey, String key, Object value) {
        if (key.contains(".")) {
            throw new JsonCreationException("Key '" + key + "' should not contain \".\"");
        } else {
            JsonObject jo = jsonObject.getAsJsonObject(parentKey);
            if (jo == null) {
                jo = new JsonObject();
            }

            this.addProperty(jo, key, value);
            this.addJsonElement(jsonObject, parentKey, jo);
        }
    }

    private void addArrayValue(JsonObject jsonObject, String parentKey, Object value) {
        if (parentKey.contains(".")) {
            throw new JsonCreationException("Parent key should not contain \".\" ");
        } else {
            JsonArray ja = jsonObject.getAsJsonArray(parentKey);
            if (ja == null) {
                ja = new JsonArray();
            }

            this.addPrimitive(ja, value);
            this.addJsonElement(jsonObject, parentKey, ja);
        }
    }

    private void addArrayMap(JsonObject jsonObject, String parentKey, String key, Object value) {
        if (parentKey.contains(".")) {
            throw new JsonCreationException("Parent key should not contain \".\"");
        } else {
            int index = 0;
            if (parentKey.contains("[") && parentKey.contains("]")) {
                index = Integer.parseInt(parentKey.substring(parentKey.indexOf(91) + 1, parentKey.indexOf(93)));
                parentKey = parentKey.substring(0, parentKey.indexOf(91));
            }

            if (index < 0) {
                throw new JsonCreationException("Index must be equals or greater than 0.");
            } else {
                JsonObject jo = null;
                JsonArray ja = jsonObject.getAsJsonArray(parentKey);
                if (ja == null) {
                    ja = new JsonArray();
                }

                if (ja.size() < index) {
                    throw new JsonCreationException("Index should be equals or less than array's size.");
                } else {
                    if (ja.size() != 0 && index < ja.size()) {
                        jo = ja.get(index).getAsJsonObject();
                    }

                    if (jo == null) {
                        jo = new JsonObject();
                    }

                    this.addProperty(jo, key, value);
                    if (ja.size() != 0 && index <= ja.size() - 1) {
                        if (index < ja.size()) {
                            ja.set(index, jo);
                        }
                    } else {
                        ja.add(jo);
                    }

                    this.addJsonElement(jsonObject, parentKey, ja);
                }
            }
        }
    }
}
