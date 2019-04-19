package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.Bound;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 20:51
 */
public class Partitions {
    public static final double LON_1 = 108.7811;
    public static final double LAT_1 = 34.1868;
    public static final double LON_2 = 109.0907;
    public static final double LAT_2 = 34.3662;

    public static void main(String[] args) {
        String path = "dataAll.txt";
        BufferedReader reader;
        String line;
        String[] items;
        double lat, lng, speed;

        int count = 4;
        Bound bound = new Bound(LON_1, LON_2, LAT_1, LAT_2);
        Map<String, Bound> boundMap = getPartitions(count);
        Map<String, Double> morningSum = new HashMap<>();
        Map<String, Long> morningNum = new HashMap<>();
        Map<String, Double> noonSum = new HashMap<>();
        Map<String, Long> noonNum = new HashMap<>();
        Map<String, Double> nightSum = new HashMap<>();
        Map<String, Long> nightNum = new HashMap<>();
        double stepLon = (LON_2 - LON_1) / count;
        double stepLat = (LAT_2 - LAT_1) / count;
        String index, time;
        try {
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                items = line.split(",");
                time = items[1].split(":")[0];
                lng = Double.parseDouble(items[2]);
                lat = Double.parseDouble(items[3]);
                speed = Double.parseDouble(items[4]);
                if (! bound.contains(lng, lat))
                    continue;
                index = (int)((lng - LON_1) / stepLon) + "-" + (int)((lat - LAT_1) / stepLat);

                if (time.compareTo("08") <= 0) {
                    double finalSpeed = speed;
                    morningSum.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    morningNum.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                } else if (time.compareTo("16") <= 0) {
                    double finalSpeed = speed;
                    noonSum.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    noonNum.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                } else {
                    double finalSpeed = speed;
                    nightSum.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    nightNum.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject morningJson = new JSONObject();
        JSONObject noonJson = new JSONObject();
        JSONObject nightJson = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject geometry = new JSONObject();
        for (String key : boundMap.keySet()) {

        }
    }

    public static Map<String, Bound> getPartitions(int count) {
        if (count <= 1)
            return null;
        Map<String, Bound> boundMap = new HashMap<>();
        double stepLon = (LON_2 - LON_1) / count;
        double stepLat = (LAT_2 - LAT_1) / count;
        double startLon, startLat;

        for (int iLat = 0; iLat < count; iLat++) {
            for (int iLon = 0; iLon < count; iLon++) {
                startLat = LAT_1 + iLat * stepLat;
                startLon = LON_1 + iLon * stepLon;
                boundMap.put(iLon + "-" + iLat, new Bound(startLon, startLon + stepLon, startLat, startLat + stepLat));
            }
        }

        return boundMap;
    }
}
