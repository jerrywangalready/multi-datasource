package com.example.multidatasource.demo.service;

import com.example.multidatasource.demo.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    DemoMapper demoMapper;

    @Override
    public List<Map<String, String>> getStudent() {
        return demoMapper.getTest();
    }
}
