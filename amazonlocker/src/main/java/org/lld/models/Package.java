package org.lld.models;

import org.lld.enums.LockerSize;
import org.lld.enums.PackageStatus;

public class Package {
    private final String id;
    private final String orderId;
    private final PackageStatus status;
    private final LockerSize size;

    public Package(String id, String orderId, PackageStatus status, LockerSize size) {
        if(id==null || id.isEmpty()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty");
        }
        if(orderId==null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public LockerSize getSize() {
        return size;
    }
}
