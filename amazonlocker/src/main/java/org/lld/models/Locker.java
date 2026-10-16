package org.lld.models;

import org.lld.enums.LockerSize;
import org.lld.enums.LockerStatus;

public class Locker {
    private String id;
    private Package assignedPackage;
    private LockerStatus status;
    private LockerSize size;

    public Locker(String id, LockerSize size) {
        this.id = id;
        this.size = size;
        this.status = LockerStatus.AVAILABLE;
    }

    public LockerSize getSize() {
        return size;
    }

    public boolean isAvailable() {
        return status == LockerStatus.AVAILABLE;
    }
}
