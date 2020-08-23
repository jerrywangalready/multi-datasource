package com.example.multidatasource.demo.mapper.mysql;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @typename: DemoMapper
 * @brief: demo的mapper层
 * @author: jerrywang
 * @date: 2019/11/5.
 * @version: 1.0.0
 * @since
 * 
 */
@Repository
public interface DemoMapper {

	List<Map<String, String>> getTest();

}
