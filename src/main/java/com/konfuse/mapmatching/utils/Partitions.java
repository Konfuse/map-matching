package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.Bound;

import java.io.*;
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
        String path = "/home/konfuse/Desktop/dataAll.txt";
        String pathMorning = "morning.txt";
        String pathNoon = "noon.txt";
        String pathNight = "night.txt";
        BufferedReader reader;
        String line;
        String[] items;
        double lat, lng, speed;

        int count = 4;
        Bound bound = new Bound(LON_1, LON_2, LAT_1, LAT_2);
        Map<String, Bound> boundMap = getPartitions(count);
        Map<String, Double> sumMorning = new HashMap<>();
        Map<String, Long> numMorning = new HashMap<>();
        Map<String, Double> sumNoon = new HashMap<>();
        Map<String, Long> numNoon = new HashMap<>();
        Map<String, Double> sumNight = new HashMap<>();
        Map<String, Long> numNight = new HashMap<>();
        double stepLon = (LON_2 - LON_1) / count;
        double stepLat = (LAT_2 - LAT_1) / count;
        System.out.println(stepLon + "," + stepLat);
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
//                System.out.println(index + ":" + speed);

                if (time.compareTo("08") <= 0) {
                    double finalSpeed = speed;
                    sumMorning.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    numMorning.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                } else if (time.compareTo("16") <= 0) {
                    double finalSpeed = speed;
                    sumNoon.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    numNoon.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                } else {
                    double finalSpeed = speed;
                    sumNight.compute(index, (k, v) -> {
                        if (v == null) return finalSpeed;
                        return v + finalSpeed;
                    });
                    numNight.compute(index, (k, v) -> {
                        if (v == null) return 1L;
                        return v + 1;
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter writerMorning = null;
        BufferedWriter writerNoon = null;
        BufferedWriter writerNight = null;
        try {
            writerMorning = new BufferedWriter(new FileWriter(pathMorning));
            writerNoon = new BufferedWriter(new FileWriter(pathNoon));
            writerNight = new BufferedWriter(new FileWriter(pathNight));
            writerMorning.write("LON,LAT,SPEED");
            writerNoon.write("LON,LAT,SPEED");
            writerNight.write("LON,LAT,SPEED");
            for (String key : boundMap.keySet()) {
                if (sumMorning.containsKey(key)) {
                    writerMorning.newLine();
                    writerMorning.write(boundMap.get(key).center() + "," + sumMorning.get(key) / numMorning.get(key));
//                    writerMorning.write(boundMap.get(key).mbr() + ":" + sumMorning.get(key) / numMorning.get(key));
                }
                if (sumNoon.containsKey(key)) {
                    writerNoon.newLine();
                    writerNoon.write(boundMap.get(key).center() + "," + sumNoon.get(key) / numNoon.get(key));
//                    writerNoon.write(boundMap.get(key).mbr() + ":" + sumNoon.get(key) / numNoon.get(key));
                }
                if (sumNight.containsKey(key)) {
                    writerNight.newLine();
                    writerNight.write(boundMap.get(key).mbr() + ":" + sumNight.get(key) / numNight.get(key));
//                    writerNight.write(boundMap.get(key).mbr() + ":" + sumNight.get(key) / numNight.get(key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writerMorning.close();
                writerNoon.close();
                writerNight.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
