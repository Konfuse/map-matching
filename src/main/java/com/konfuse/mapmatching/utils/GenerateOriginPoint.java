package com.konfuse.mapmatching.utils;

import com.konfuse.mapmatching.domain.Bound;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 10:31
 */
public class GenerateOriginPoint {
    // Input: xx,id,time,lon,lat,speed,xx,xx in Desktop: yyyy-MM-dd.txt
    // Output: time,lon,lat,speed in OriginData: dataAll.txt
    public static void main(String[] args) {
        String[] pathRead = {"/home/konfuse/Desktop/2011-06-01.txt", "/home/konfuse/Desktop/2011-06-02.txt"};
        String pathWrite = "/home/konfuse/Desktop/data/OriginData/dataAll.txt";

        String line;
        String[] items;
        long count = 0;
        String lon, lat, speed, time;

        Bound bound = new Bound(Partitions.LON_1, Partitions.LON_2, Partitions.LAT_1, Partitions.LAT_2);
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
//                System.out.println(id.substring(0, 1).equals("陕"));
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
}
