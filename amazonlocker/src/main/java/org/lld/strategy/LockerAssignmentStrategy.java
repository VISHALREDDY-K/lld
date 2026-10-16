package org.lld.strategy;

import java.util.List;

import org.lld.models.Locker;
import org.lld.models.Package;

public interface LockerAssignmentStrategy {
    Locker assignLocker(Package pkg, List<Locker> lockers);
}
