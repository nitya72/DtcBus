package com.example.nityaarora.dtcbuses;

import java.util.HashMap;
import java.util.List;

public class DataType {
    int fare;
    int walk;
    List<List<HashMap<String,String>>> routes;

    DataType(int fare,int walk,List<List<HashMap<String,String>>> routes)
    {
        this.fare=fare;
        this.walk=walk;
        this.routes=routes;
    }
}
