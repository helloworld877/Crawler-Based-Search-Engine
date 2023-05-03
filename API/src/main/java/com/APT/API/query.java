package com.APT.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class query {
    @GetMapping("/query")

    public ArrayList<HashMap<String, String>> query(@RequestParam(name = "q") String q) {
//        to make JSON Object
        ArrayList<HashMap<String, String>> result= new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "123");
        map.put("foo", "bar");
        map.put("aa", "bb");
        map.put("id","1");
        map.put("body", q);
        result.add(map);
        map = new HashMap<>();
        map.put("key", "345");
        map.put("foo", "bar");
        map.put("aa", "bb");
        map.put("id","2");
        map.put("body", q);
        result.add(map);


        return result;
    }

}
