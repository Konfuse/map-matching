package com.konfuse.mapmatching.controller;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.MapPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 10:16
 */
@RestController
public class MapPointController {
    @RequestMapping("/points")
    public Iterable<MapPoint> list() {
        Set<MapPoint> mapPoints = new HashSet<>();

        String path = "data100.txt";
        String line;
        String[] items;
        double lat, lng, speed;
        int id = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                id ++;
                items = line.split(",");
                lat = Double.parseDouble(items[1]);
                lng = Double.parseDouble(items[0]);
                speed = Double.parseDouble(items[2]);
                mapPoints.add(new MapPoint("some point", speed, lat, lng));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapPoints;
    }

    @RequestMapping("/circle")
    public String myList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lat", 34.2617);
        jsonObject.put("lng", 108.9427);
        return jsonObject.toJSONString();
    }
}
