package com.pvsoul.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pvsoul.demo.entity.Test;

public interface TestMapper extends BaseMapper<Test> {
    int deleteByPrimaryKey(String id);

    int insert(Test record);

    int insertSelective(Test record);

    Test selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Test record);

    int updateByPrimaryKey(Test record);
}