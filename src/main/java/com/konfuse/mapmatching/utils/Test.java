package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.Bound;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.konfuse.mapmatching.utils.Partitions.getPartitions;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 11:51
 */
public class Test {
    public static final double LON_1 = 108.7811;
    public static final double LAT_1 = 34.1868;
    public static final double LON_2 = 109.0907;
    public static final double LAT_2 = 34.3662;

    public static void main(String[] args) {
        String[] dividePath = {
                "/home/konfuse/Desktop/data/OriginData/dataMorning.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataAfternoon.txt",
                "/home/konfuse/Desktop/data/OriginData/dataEvening.txt",
                "/home/konfuse/Desktop/data/OriginData/dataNight.txt"
        };
    }
}
