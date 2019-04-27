package com.konfuse.mapmatching.utils;

import com.konfuse.mapmatching.domain.Bound;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Konfuse
 * @Date: 2019/4/27 10:35
 */
public class Process {
    public static final double LON_1 = 108.7811;
    public static final double LAT_1 = 34.1868;
    public static final double LON_2 = 109.0907;
    public static final double LAT_2 = 34.3662;

    public static String absPath = "C:/Users/Konfuse/Desktop/data/";

    public static void main(String[] args) {
        String path1 = absPath + "2011-06-01.txt";
        String path2 = absPath + "2011-06-02.txt";
        File file1 = new File(path1);
        File file2 = new File(path2);
        if (!file1.exists() || !file2.exists()) {
            System.out.println("file doesn't exits");
            return;
        }
        dealOrigin();
        divideOnTime();
        partition();
    }

    // Input: xx,id,time,lon,lat,speed,xx,xx in Desktop: yyyy-MM-dd.txt
    // Output: time,lon,lat,speed in OriginData: dataAll.txt
    public static void dealOrigin() {
        String[] pathRead = {
                absPath + "2011-06-01.txt",
                absPath + "2011-06-02.txt"
        };

        File dir = new File(absPath + "OriginData");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        String pathWrite = absPath + "OriginData/dataAll.txt";

        String line;
        String[] items;
        long count = 0;
        String lon, lat, speed, time;

        Bound bound = new Bound(LON_1, LON_2, LAT_1, LAT_2);
        BufferedReader reader;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathWrite));
            for (String path : pathRead) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
                while ((line = reader.readLine()) != null) {
                    count++;
                    items = line.split(",");
                    time = items[2].split(" ")[1];
                    lon = items[3];
                    lat = items[4];
                    speed = items[5];
                    System.out.println("process the %" + count + " data...");
                    if (! bound.contains(Double.parseDouble(lon), Double.parseDouble(lat)))
                        continue;
                    writer.write(time + "," + lon + "," + lat + "," + speed);
                    writer.newLine();
                }
            }
            System.out.println("Num: " + count);
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

    // Input: time,lon,lat,speed in OriginData: dataAll.txt
    // Output: lon,lat,speed in OriginData: data*.txt
    public static void divideOnTime() {
        String path = absPath + "OriginData/dataAll.txt";
        String[] dividePath = {
                absPath + "OriginData/dataMorning.txt",
                absPath + "OriginData/dataNoon.txt",
                absPath + "OriginData/dataAfternoon.txt",
                absPath + "OriginData/dataEvening.txt",
                absPath + "OriginData/dataNight.txt"
        };
        BufferedReader reader;
        List<BufferedWriter> list = new ArrayList<>();
        String time, line, lon, lat, speed;
        long count = 0L;
        try {
            reader = new BufferedReader(new FileReader(path));
            for (String s : dividePath) {
                list.add(new BufferedWriter(new FileWriter(s)));
            }
            while ((line = reader.readLine()) != null) {
                count++;
                System.out.println("process the %" + count + " data...");
                time = line.split(",")[0];
                lon = line.split(",")[1];
                lat = line.split(",")[2];
                speed = line.split(",")[3];
                if ("07:30".compareTo(time) <= 0 && "09:00".compareTo(time) >= 0) {
                    list.get(0).write(lon + "," + lat + "," + speed);
                    list.get(0).newLine();
                }
                else if ("11:00".compareTo(time) <= 0 && "13:00".compareTo(time) >= 0) {
                    list.get(1).write(lon + "," + lat + "," + speed);
                    list.get(1).newLine();
                }
                else if ("14:00".compareTo(time) <= 0 && "16:00".compareTo(time) >= 0) {
                    list.get(2).write(lon + "," + lat + "," + speed);
                    list.get(2).newLine();
                }
                else if ("17:30".compareTo(time) <= 0 && "20:00".compareTo(time) >= 0) {
                    list.get(3).write(lon + "," + lat + "," + speed);
                    list.get(3).newLine();
                }
                else if ("21:00".compareTo(time) <= 0 && "23:00".compareTo(time) >= 0) {
                    list.get(4).write(lon + "," + lat + "," + speed);
                    list.get(4).newLine();
                }
            }
            System.out.println(count + " in Total");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                for (BufferedWriter writer : list) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Input: lon,lat,speed in OriginData: data*.txt
    // Output: lon,lat,speed in PartitionCSV: *.csv (partition center position)
    public static void partition() {
        String[] inputPath = {
                absPath + "OriginData/dataMorning.txt",
                absPath + "OriginData/dataNoon.txt",
                absPath + "OriginData/dataAfternoon.txt",
                absPath + "OriginData/dataEvening.txt",
                absPath + "OriginData/dataNight.txt"
        };
        File dir = new File(absPath + "PartitionCSV");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        String[] outputPath = {
                absPath + "PartitionCSV/morning.csv",
                absPath + "PartitionCSV/noon.csv",
                absPath + "PartitionCSV/afternoon.csv",
                absPath + "PartitionCSV/evening.csv",
                absPath + "PartitionCSV/night.csv"
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
