package org.lld.strategy;

import java.util.List;

import org.lld.enums.LockerSize;
import org.lld.exceptions.NoLockerAvailableException;
import org.lld.models.Locker;
import org.lld.models.Package;

public class SmallestLockerStrategy implements LockerAssignmentStrategy {
    @Override
    public Locker assignLocker(Package pkg, List<Locker> lockers) {
        
        for(int i=pkg.getSize().ordinal();i<LockerSize.values().length;i++) {
            LockerSize size = LockerSize.values()[i];
            for(Locker locker : lockers) {
                if(locker.getSize() == size && locker.isAvailable()) {
                    return locker;
                }
            }
        }
        throw new NoLockerAvailableException("No locker available for package " + pkg.getId());
    }
}
