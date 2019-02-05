package com.neptuo.data.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private boolean isSystemValue;

    @Column(name = "system_value", updatable = false)
    public boolean getIsSystemValue() {
        return isSystemValue;
    }

    public void setIsSystemValue(boolean isSystemValue) {
        this.isSystemValue = isSystemValue;
    }

    @Transient
    public abstract Long getId();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity)) {
            return false;
        }

        AbstractEntity e = (AbstractEntity) obj;

        if (e.getId().longValue() == getId().longValue()) {
            return true;
        } else {
            return false;
        }
    }
}
