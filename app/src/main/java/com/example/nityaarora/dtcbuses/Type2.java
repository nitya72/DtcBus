package com.example.nityaarora.dtcbuses;

import java.util.HashMap;
import java.util.List;

public class Type2 {
    List<List<HashMap<String,String>>> routes;
    String html_walk;
    int duration_walk;
    int num_stops;
    String html_transit;
    int duration_trans;
    String bus;
    String fare;
    Type2(List<List<HashMap<String,String>>> routes,String html_walk,int duration_walk,int num_stops, String html_transit,int duration_trans,String bus,String fare){
        this.routes=routes;
        this.html_walk=html_walk;
        this.duration_walk=duration_walk;
        this.num_stops=num_stops;
        this.html_transit=html_transit;
        this.duration_trans=duration_trans;
        this.bus=bus;
        this.fare=fare;
    }
}
