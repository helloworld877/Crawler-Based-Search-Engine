package com.APT.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class query {
    @GetMapping("/query")

    public Map<String, String> query(@RequestParam(name = "q") String q) {
//        to make JSON Object
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        map.put("q", q);

        return map;
    }

}