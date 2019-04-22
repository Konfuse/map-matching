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
        String[] inputPath = {
                "/home/konfuse/Desktop/data/OriginData/dataMorning.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataAfternoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataEvening.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNight.txt"
        };
        String[] outputPath = {
                "/home/konfuse/Desktop/data/PartitionCSV/morning.csv",
                "/home/konfuse/Desktop/data/PartitionCSV/noon.csv",
                "/home/konfuse/Desktop/data/PartitionCSV/afternoon.csv",
                "/home/konfuse/Desktop/data/PartitionCSV/evening.csv",
                "/home/konfuse/Desktop/data/PartitionCSV/night.csv"
        };

        if (inputPath.length != outputPath.length) {
            System.out.println("wrong path config...");
            return;
        }

        int count = 100;
        double stepLon = (LON_2 - LON_1) / count;
        double stepLat = (LAT_2 - LAT_1) / count;

        Map<String, Bound> boundMap = getPartitions(count);
        for (int i = 0; i < inputPath.length; i++) {
            System.out.println("process %" + inputPath[i].split("/")[6] + " file...");
            dataPartition(count, boundMap, inputPath[i], outputPath[i]);
        }

        System.out.println("Lon step: " + stepLon + ", Lon step: " + stepLat);
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

    public static void dataPartition(int count, Map<String, Bound> boundMap, String dataPath, String outPath) {
        BufferedReader reader;
        String line, index;
        String[] items;
        double lat, lng, speed;
        long total = 0L;

        Map<String, Double> sum = new HashMap<>();
        Map<String, Long> num = new HashMap<>();

        double stepLon = (LON_2 - LON_1) / count;
        double stepLat = (LAT_2 - LAT_1) / count;
        int indexLon, indexLat;
        try {
            reader = new BufferedReader(new FileReader(dataPath));
            while ((line = reader.readLine()) != null) {
                items = line.split(",");
                lng = Double.parseDouble(items[0]);
                lat = Double.parseDouble(items[1]);
                speed = Double.parseDouble(items[2]);
                indexLon = (int)((lng - LON_1) / stepLon) == count ? (int)((lng - LON_1) / stepLon) - 1 : (int)((lng - LON_1) / stepLon);
                indexLat = (int)((lat - LAT_1) / stepLat) == count ? (int)((lat - LAT_1) / stepLat) - 1 : (int)((lat - LAT_1) / stepLat);
                index = indexLon + "-" + indexLat;
//                System.out.println(index + ":" + speed);
                double finalSpeed = speed;
                sum.compute(index, (k, v) -> {
                    if (v == null) return finalSpeed;
                    return v + finalSpeed;
                });
                num.compute(index, (k, v) -> {
                    if (v == null) return 1L;
                    return v + 1;
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outPath));
            writer.write("LON,LAT,SPEED");
            for (String key : sum.keySet()) {
                total++;
                writer.newLine();
                writer.write(boundMap.get(key).center() + "," + sum.get(key) / num.get(key));
            }
            System.out.println(total + " partitions built.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
