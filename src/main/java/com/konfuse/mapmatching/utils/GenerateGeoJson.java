package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.Bound;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Konfuse
 * @Date: 19-4-19 下午8:36
 */
public class GenerateGeoJson {
    public static void main(String[] args) {
        String path = "morning.txt";
        String pathWrite = "geo.json";
        BufferedReader reader;
        BufferedWriter writer = null;
        JSONObject geoJson;
        JSONObject properties;
        JSONObject geometry;
        Bound bound;
        String line, coordinates;
        List<JSONObject> list = new ArrayList<>();
        double speed;
        try {
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                geoJson = new JSONObject();
                properties = new JSONObject();
                geometry = new JSONObject();

                coordinates = line.split(":")[0];
                bound = new Bound(
                        Double.parseDouble(coordinates.split(",")[0]),
                        Double.parseDouble(coordinates.split(",")[2]),
                        Double.parseDouble(coordinates.split(",")[1]),
                        Double.parseDouble(coordinates.split(",")[3])
                );
                geometry.put("type", "Polygon");
                geometry.put("coordinates", bound.coordinates());

                speed = Double.parseDouble(line.split(":")[1]);
                if (speed <= 20) properties.put("speed", "red");
                else if (speed <= 40) properties.put("speed", "yellow");
                else properties.put("color", "green");

                geoJson.put("type", "Feature");
                geoJson.put("properties", properties);
                geoJson.put("geometry", geometry);
                list.add(geoJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer = new BufferedWriter(new FileWriter(pathWrite));
            writer.write(list.toString());
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
