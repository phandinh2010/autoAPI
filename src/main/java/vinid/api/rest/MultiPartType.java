package vinid.api.rest;

public enum MultiPartType {

    FILE("FILE"),
    TEXT("TEXT");

    private String type;

    private MultiPartType(String type) {
        this.type = type;
    }

    public static MultiPartType getType(String type) {
        switch (type) {
            case "FILE":
                return FILE;
            case "TEXT":
            default:
                return TEXT;
        }
    }
}
