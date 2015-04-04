package com.yulius.belitungtrip.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Poi")
public class Poi extends Model {
    @Column(name = "id")
    public String id;
    @Column(name = "poiName")
    public String poiName;
}
