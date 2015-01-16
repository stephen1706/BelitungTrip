package com.yulius.belitungtrip.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Trip")
public class Trip extends Model {
    @Column(name = "name")
    public String name;

    @Column(name = "time")
    public long time;

    public Trip(){
        super();
    }

    public Trip(String name, long time) {
        this.name = name;
        this.time = time;
    }

}
