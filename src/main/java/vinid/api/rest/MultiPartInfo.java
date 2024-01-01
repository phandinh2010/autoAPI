package vinid.api.rest;

public class MultiPartInfo {

    private String key;
    private String value;
    private MultiPartType type;

    MultiPartInfo() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MultiPartType getType() {
        return this.type;
    }

    public void setType(MultiPartType type) {
        this.type = type;
    }
}
