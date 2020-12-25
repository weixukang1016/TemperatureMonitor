package com.pvsoul.demo.service.impl;

import com.pvsoul.demo.entity.Test;
import com.pvsoul.demo.mapper.TestMapper;
import com.pvsoul.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public void saveData(String data) {
        String id = UUID.randomUUID().toString();
        Test test = new Test();
        test.setId(id);
        test.setData(data);
        test.setCreatetime(new Date());
        testMapper.insert(test);

    }
}
