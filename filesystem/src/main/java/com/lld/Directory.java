package com.lld;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Directory extends FileSystemNode{
    Map<String, FileSystemNode> children;

    public Directory(String name) {
        super(name);
        this.children = new ConcurrentHashMap<>();
    }

    public void addChild(FileSystemNode child) {
        children.put(child.name, child);
    }

    public FileSystemNode getChild(String name) {
        return children.get(name);
    }

    public Map<String, FileSystemNode> getChildren() {
        return children;
    }

    public void removeChild(String name) {
        children.remove(name);
    }

}
