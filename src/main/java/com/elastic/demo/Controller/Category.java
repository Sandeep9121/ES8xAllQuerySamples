package com.elastic.demo.Controller;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Category {
    private String key;
    private int docCount;

    // Constructor, getters, and setters

    // Example constructor
    public Category(String key, int docCount) {
        this.key = key;
        this.docCount = docCount;
    }

    // Example getters and setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }

    // Method to convert Category object to JsonObject
    public JsonObject toJsonObject() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("key", this.key);
        builder.add("doc_count", this.docCount);
        return builder.build();
    }
}