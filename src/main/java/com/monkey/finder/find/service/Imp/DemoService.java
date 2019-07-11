package com.monkey.finder.find.service.Imp;

import com.monkey.finder.find.dao.DemoMapper;
import com.monkey.finder.find.entity.Demo;
import com.monkey.finder.find.service.IDemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.OnClose;

@Service
public class DemoService implements IDemoService {

    @Resource
    DemoMapper demoMapper;

    @Override
    public void insert(Demo demo) {
        demoMapper.insert(demo);
    }

    @Override
    public Demo select(int id){
        return demoMapper.selectByPrimaryKey(id);
    }
}
