package com.example.multidatasource.demo.service;

import com.example.multidatasource.demo.mapper.hive.HiveDemoMapper;
import com.example.multidatasource.demo.mapper.mysql.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private HiveDemoMapper hiveDemoMapper;

    @Override
    public List<Map<String, String>> getStudent() {
        System.out.println(hiveDemoMapper.getTest());
        return demoMapper.getTest();
    }
}
