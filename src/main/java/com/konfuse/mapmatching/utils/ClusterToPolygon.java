package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Konfuse
 * @Date: 2019/4/24 12:36
 */
public class ClusterToPolygon {
    public static final double LON_STEP = 0.003096000000000032;
    public static final double LAT_STEP = 0.001794000000000011;

    public static String absPath = "C:/Users/Konfuse/Desktop/data/";

    public static void main(String[] args) {
        String[] inputPath = {
               "morning",
               "noon",
               "afternoon",
               "evening",
               "night"
        };

        String outputPath = absPath + "GeoJsons/";

        File[] fileList;
        BufferedReader reader;
        String line;
        int num, len;
        double lon, lat, speed, sum;
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Geometry> geometryList = new ArrayList<>();
        List<Double> speedList = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates;
        MultiPoint multiPoint;
        Geometry a, b, element;
        try {
            //遍历每个时段，为每个时段生成一个geoJson
            for (String path : inputPath) {
                fileList = new File(absPath + "Cluster/" + path).listFiles();
                //遍历每一个散点文件，每一个文件是一个点云，输出多边形列表
                for (File file : fileList) {
                    sum = 0.0; num = 0;
                    reader = new BufferedReader(new FileReader(file));
                    //每一行是一个矩形的中心点，计算矩形的边界，加入点云的列表
                    while ((line = reader.readLine()) != null) {
                        lon = Double.parseDouble(line.split(",")[0]);
                        lat = Double.parseDouble(line.split(",")[1]);
                        speed = Double.parseDouble(line.split(",")[2]);
                        sum += speed; num ++;
                        coordinateList.add(new Coordinate(lon - LON_STEP/2, lat - LAT_STEP/2));
                        coordinateList.add(new Coordinate(lon - LON_STEP/2, lat + LAT_STEP/2));
                        coordinateList.add(new Coordinate(lon + LON_STEP/2, lat - LAT_STEP/2));
                        coordinateList.add(new Coordinate(lon + LON_STEP/2, lat + LAT_STEP/2));
                    }
                    //计算凸包多边形加入多边形列表，计算平均速度加入速度列表
                    coordinates = new Coordinate[coordinateList.size()];
                    coordinates = coordinateList.toArray(coordinates);
                    multiPoint = geometryFactory.createMultiPoint(coordinates);
                    coordinateList.clear();
                    speedList.add(sum / num);
                    geometryList.add(multiPoint.convexHull());
                }
                //去除多边形列表中多边形相交的区域
                len = geometryList.size();
                for (int i = 0; i < len - 1; i++) {
                    a = geometryList.get(i);
                    for (int j = i + 1; j < len; j++) {
                        b = geometryList.get(j);
                        if ("GeometryCollection".equals(b.getGeometryType())) {
                            System.out.println("Here");
                            List<Polygon> polygons = new ArrayList<>();
                            for (int k = 0; k < b.getNumGeometries(); k++) {
                                if ("Polygon".equals(b.getGeometryN(k).getGeometryType()))
                                    polygons.add((Polygon) b.getGeometryN(k));
                            }
                            b = geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
                            geometryList.set(j, b);
                        }
                        if (a.intersects(b)) {
                            if (a.contains(b) || b.contains(a)) continue;
                            geometryList.set(j, b.difference(a));
                        }
                    }
                }
                //去除多边形具有被包含关系的区域
                int[] flags = new int[geometryList.size()];
                for (int i = 0; i < geometryList.size(); i++) {
                    for (int j = 0; j < geometryList.size(); j++) {
                        if (i == j) continue;
                        if (geometryList.get(i).contains(geometryList.get(j))) flags[j] = 1;
                    }
                }
                for (int i = 0; i < flags.length; i++) {
                    if (flags[i] == 1) {
                        geometryList.remove(i);
                        speedList.remove(i);
                    }
                }
                //遍历多边形列表，输出geoJson
                System.out.println(path + "...");
                writeGeoJson(geometryList, speedList, outputPath + path + ".json");
                geometryList.clear(); speedList.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeGeoJson(List<Geometry> geometryList, List<Double> speedList, String path) {
        List<JSONObject> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        BufferedWriter writer = null;
        Geometry polygon;

        for (int i = 0; i < geometryList.size(); i++) {
            polygon = geometryList.get(i);
            if ("MultiPolygon".equals(polygon.getGeometryType())) {
                for (int j = 0; j < polygon.getNumGeometries(); j++) {
                    list.add(getGeoJson(polygon.getGeometryN(j).toText(), speedList.get(i)));
                }
            } else {
                list.add(getGeoJson(polygon.toText(), speedList.get(i)));
            }
        }
        jsonObject.put("type", "FeatureCollection");
        jsonObject.put("features", list);

        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonObject.toJSONString());
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

    public static List<List<List<Double>>> travelPolygonText(String text) {
        List<List<List<Double>>> boundary = new ArrayList<>();
        List<List<Double>> coordinates = new ArrayList<>();
        List<Double> coordinate;
        String[] points = text.substring(text.indexOf("((") + 2, text.indexOf("))")).split(", ");
        for (String point : points) {
            coordinate = new ArrayList<>();
            coordinate.add(Double.parseDouble(point.split(" ")[0]));
            coordinate.add(Double.parseDouble(point.split(" ")[1]));
            coordinates.add(coordinate);
        }
        boundary.add(coordinates);
        return boundary;
    }

    public static JSONObject getGeoJson(String polygonText, double speed) {
        JSONObject geoJson = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject geometry = new JSONObject();
        geometry.put("type", "Polygon");
        geometry.put("coordinates", travelPolygonText(polygonText));
        if (speed <= 20) {
            properties.put("speed", speed);
            properties.put("status", "Jam");
        } else if (speed <= 40) {
            properties.put("speed", speed);
            properties.put("status", "normal");
        }
        else {
            properties.put("speed", speed);
            properties.put("status", "free");
        }
        geoJson.put("type", "Feature");
        geoJson.put("properties", properties);
        geoJson.put("geometry", geometry);
        return geoJson;
    }
}
