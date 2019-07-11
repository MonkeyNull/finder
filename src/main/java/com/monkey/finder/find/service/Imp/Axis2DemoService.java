package com.monkey.finder.find.service.Imp;

public class Axis2DemoService {
    public String getGreeting(String name)
    {
        return "您好 " + name;
    }
    public void update(String data)
    {
        System.out.println("<" + data + ">已经更新");
    }
}
