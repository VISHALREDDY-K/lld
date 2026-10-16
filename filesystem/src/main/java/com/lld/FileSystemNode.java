package com.lld;

import java.time.Instant;

public abstract class FileSystemNode {
    public String name;
    public Instant createdDate;

    public FileSystemNode(String name){
        this.name = name;
        this.createdDate = Instant.now();
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setName(String name) {
        this.name = name;
    }
}
