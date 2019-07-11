package com.monkey.finder.find.service;

import com.monkey.finder.find.entity.Items;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IItemService {

    void insert(Items items);

    Items selectById(Long itemId);

    void delete(Long itemId);

    List<Map<String,Object>> getNearByLocation(Double longitude, Double latitude);

    String saveImg(MultipartFile file);

    void update(Items items);

    void offbaseDecr(Long itemId);

    ArrayList<Items> selectAll();
}
