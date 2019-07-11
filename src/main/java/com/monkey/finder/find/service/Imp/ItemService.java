package com.monkey.finder.find.service.Imp;

import com.monkey.finder.find.config.AppConfig;
import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.dao.ItemsMapper;
import com.monkey.finder.find.entity.Items;
import com.monkey.finder.find.entity.User;
import com.monkey.finder.find.service.IItemService;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.service.IUserService;
import com.monkey.finder.find.utils.ImageUtil;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ItemService implements IItemService {

    @Resource
    ItemsMapper itemsMapper;

    @Resource
    IRedisService redisService;

    @Resource
    IUserService userService;

    @Resource
    AppConfig appConfig;


    @Override
    public void insert(Items items) {
        if (items == null){
            log.debug("item is null");
        }
        itemsMapper.insert(items);
    }

    @Override
    public Items selectById(Long itemId) {
        if (itemId == null){
            return null;
        }
        return itemsMapper.selectById(itemId);
    }

    @Override
    public void delete(Long itemId) {
        if (itemId == null)
            return;
        Items items = selectById(itemId);
        if (items == null)
            return;
        items.setRecordState(0);
        itemsMapper.updateByPrimaryKey(items);
    }

    /**
     * 查询当前位置附近的失物信息
     * @param longitude
     * @param latitude
     * @return
     */
    @Override
    public List<Map<String,Object>> getNearByLocation(Double longitude, Double latitude){
        List<Map<String,Object>> lists = new ArrayList<>();
        Circle circle = new Circle(longitude,latitude, RedisGeoCommands.DistanceUnit.KILOMETERS.getMultiplier()*18000);
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> geoResults = redisService.gRadius(FindRedisConstants.REDIS_GEO_ITEMKEY,circle);
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> geoResult : geoResults) {
            Long item_id = Long.parseLong(geoResult.getContent().getName().toString());
            log.debug("item_id",item_id);
            double distance = geoResult.getDistance().getValue();
            log.debug("distance",distance);
            Items itemPo = selectById(item_id);
            log.debug("itemPo{}",itemPo);
            if(itemPo == null){
                continue;
            }
            Map<String,Object> map = getItemInfo(itemPo, distance);
            lists.add(map);
        }
        return lists;
    }

    @Override
    public String saveImg(MultipartFile file) {
        //图片存储的地址
        String img_target_path = appConfig.getUploadImagePath()+ "/" + ThreadLocalUtil.getUid() + "/img/";
//        String img_target_path = "/img" + ThreadLocalUtil.getUid() + "/img/";
        String path = null;
        try {
            path = ImageUtil.saveImage(file,img_target_path);
            log.debug("saveImagepath{}", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = path.replace("D://finder/", "");
        path = path.replace("/home/finder/finderProject", "");
        return path;
    }

    @Override
    public void update(Items items) {
        if (items == null)
            return;
        itemsMapper.updateByPrimaryKey(items);
    }

    private Map<String, Object> getItemInfo(Items itemPo, double distance) {
        Map<String,Object> map = new HashMap<>();
        String userNick = null;
        String userIcon = null;
        User userPo = userService.selectByUid(itemPo.getUserId());
        if (userPo != null){
            userIcon = userPo.getUserIcon();
            userNick = userPo.getUserNick();
        }
        map.put("userNick", userNick);
        map.put("userIcon", userIcon);
        map.put("itemInfo", itemPo.getItemInfo());
        map.put("lostTime", itemPo.getLostTime());
        map.put("lon", itemPo.getLon());
        map.put("lat", itemPo.getLat());
        map.put("itemId", itemPo.getItemId());
        map.put("imgPath", itemPo.getItemImg());
        //距离
        map.put("distance",distance);
        return map;
    }

    @Override
    public void offbaseDecr(Long itemId){
        if (itemId == null){
            log.debug("itemId is null");
            return;
        }
        itemsMapper.offbaseDecr(itemId);
    }


    //将offbase大于三的下线
    public void offbase(Long itemId){
        if (itemId == null){
            log.debug("itemId is null {}", itemId);
            return;
        }
        Items items = selectById(itemId);
        items.setRecordState(0);
        insert(items);
    }

    @Override
    public ArrayList<Items> selectAll() {
        return null;
    }
}
