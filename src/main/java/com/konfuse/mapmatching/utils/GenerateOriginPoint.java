package com.konfuse.mapmatching.utils;

import com.konfuse.mapmatching.domain.Bound;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 10:31
 */
public class GenerateOriginPoint {
    public static void main(String[] args) {
        String pathRead = "C:/Users/Konfuse/Desktop/2011-06-19.txt";
        String pathWrite = "dataAll.txt";

        String line;
        String[] items;
        long count = 0;
        String lon, lat, speed, time, id = null;

        Bound bound = new Bound(Partitions.LON_1, Partitions.LON_2, Partitions.LAT_1, Partitions.LAT_2);
        BufferedReader reader;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathRead), "GBK"));
            writer = new BufferedWriter(new FileWriter(pathWrite));
            while ((line = reader.readLine()) != null) {
                count++;
                items = line.split(",");
                id = items[1];
                time = items[2].split(" ")[1];
                lon = items[3];
                lat = items[4];
                speed = items[5];
//                System.out.println(id.substring(0, 1).equals("陕"));
                if (! bound.contains(Double.parseDouble(lon), Double.parseDouble(lat)))
                    continue;
                writer.write(id + "," + time + "," + lon + "," + lat + "," + speed);
                writer.newLine();
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
