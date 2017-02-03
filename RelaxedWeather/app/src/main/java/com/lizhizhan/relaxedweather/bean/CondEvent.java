package com.lizhizhan.relaxedweather.bean;

/**
 * Created by lizhizhan on 2017/1/26.
 */

public class CondEvent {
    Boolean cond;

    public CondEvent(Boolean change) {
        this.cond = change;
    }

    public Boolean getCond() {
        return cond;
    }

    public void setCond(Boolean cond) {
        this.cond = cond;
    }
}
