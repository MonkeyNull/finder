package com.monkey.finder.find.service;

import com.monkey.finder.find.entity.Demo;

public interface IDemoService {


    void insert(Demo demo);

    Demo select(int id);
}
