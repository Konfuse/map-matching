package com.konfuse.mapmatching.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Konfuse
 * @Date: 19-4-21 下午9:37
 */
public class DivideDataOnTime {
    // Input: time,lon,lat,speed in OriginData: dataAll.txt
    // Output: lon,lat,speed in OriginData: data*.txt
    public static void main(String[] args) {
        String path = "/home/konfuse/Desktop/data/OriginData/dataAll.txt";
        String[] dividePath = {
                "/home/konfuse/Desktop/data/OriginData/dataMorning.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataAfternoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataEvening.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNight.txt"
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
}
