package com.konfuse.mapmatching.utils;

import com.alibaba.fastjson.JSONObject;
import com.konfuse.mapmatching.domain.Bound;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Konfuse
 * @Date: 2019/4/18 11:51
 */
public class Test {
    public static void main(String[] args) {
        Bound bound = new Bound(Partitions.LON_1, Partitions.LON_2, Partitions.LAT_1, Partitions.LAT_2);
//        System.out.println(Arrays.deepToString(bound.coordinates()));
        JSONObject geo = new JSONObject();
        geo.put("demo", bound.coordinates());
        System.out.println(geo.toJSONString());
    }
}
