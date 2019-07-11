package com.monkey.finder.find.controller;

import com.monkey.finder.find.config.AppConfig;
import com.monkey.finder.find.constants.FindRedisConstants;
import com.monkey.finder.find.entity.Items;
import com.monkey.finder.find.service.IItemService;
import com.monkey.finder.find.service.IRedisService;
import com.monkey.finder.find.status.FinderResultStateEnum;
import com.monkey.finder.find.status.ResultStatus;
import com.monkey.finder.find.utils.IOUtil;
import com.monkey.finder.find.utils.PageUtil;
import com.monkey.finder.find.utils.ThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Api(value = "核心操作controller")
@RestController
@RequestMapping("/findcore")
public class CoreController {

    @Resource
    IRedisService redisService;

    @Resource
    IItemService itemService;

    @Resource
    AppConfig appConfig;

    private List<Map<String,Object>> listActivity;

    //发布拾取物品
    @ApiOperation(value = "发布物品", notes = "")
    @ApiImplicitParam(name = "item", value = "物品信息", dataType = "Items", required = true,paramType = "from")
    @RequestMapping(value = "/createItem", method = RequestMethod.POST)
    public ResultStatus createItem(@RequestBody Items item){
        if (item == null){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        item.setUserId(ThreadLocalUtil.getUid());
        item.setRecordState(1);
        itemService.insert(item);
        log.debug("item :{}", item);
        //放置到GEO中
        redisService.gAdd(FindRedisConstants.REDIS_GEO_ITEMKEY, new Point(Double.parseDouble(item.getLon()), Double.parseDouble(item.getLat())), item.getItemId());
        HashMap<String,Long> map = new HashMap<>();
        map.put("itemId", item.getItemId());
        return FinderResultStateEnum.OK.toResultStatus().setInfo(map);
    }

    //删除已发布的物品
    @ApiOperation(value = "删除物品", notes = "")
    @ApiImplicitParam(name = "itemId", value = "物品id", dataType = "Long", required = true, paramType = "path")
    @RequestMapping(value = "/delItem", method = RequestMethod.DELETE)
    public ResultStatus delItem(@RequestParam Long itemId){
        if (itemId == null){
         return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        Items items = itemService.selectById(itemId);
        if (items != null){
            if (!items.getUserId().equals(ThreadLocalUtil.getUid()))
                return FinderResultStateEnum.PERMISSION_DENIED.toResultStatus();
            //删除该条信息
            itemService.delete(itemId);
            //将该信息的位置信息从redis中删除
            redisService.gDel(FindRedisConstants.REDIS_GEO_ITEMKEY,itemId);
        }
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //获取附近的失物信息
    @ApiOperation(value = "查看附近活动", notes = "根据用户当前定位查看附近的活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude",value = "经度", dataType = "Double", required = true,paramType = "query"),
            @ApiImplicitParam(name = "latitude",value = "纬度", dataType = "Double", required = true,paramType = "query"),
            @ApiImplicitParam(name = "nowPage",value = "第几页",dataType = "int",required = true,paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "一页显示多少条",dataType = "int",required = true,paramType = "query"),
    })
    @RequestMapping(value = "/getActivityByLocation", method = RequestMethod.GET)
    public ResultStatus getActivityByLocation(@RequestParam(required = true) Double longitude,
                                              @RequestParam(required = true) Double latitude,
                                              @RequestParam(required = true) int nowPage,
                                              @RequestParam(required = true) int pageSize){
        if(longitude == null || latitude == null){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        if(nowPage == 0 || pageSize == 0){
            nowPage = 1;
            pageSize = 30;
        }
        List<Map<String,Object>> list = itemService.getNearByLocation(longitude,latitude);
        //todo 数据优化
        listActivity = list;
        if(list == null || list.isEmpty()){
            return FinderResultStateEnum.NOT_FOUND_ERROR.toResultStatus().setInfo("没有相关搜索数据");
        }
        PageUtil<Map<String,Object>> pageUtil = new PageUtil(list,nowPage,pageSize);
        list = pageUtil.getPagedList();
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Long a = ((Date)o1.get("lostTime")).getTime();
                Long b = ((Date)o2.get("lostTime")).getTime();
                double a_distance = (double)o1.get("distance");
                double b_distance = (double)o1.get("distance");
                //TODO 包装类型
                if(a_distance > b_distance){
                    return -1;
                    //TODO 包装类型
                }else if(a_distance == b_distance){
                    if(a.compareTo(b) > 0){
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
        });
        Map<String,Object> map = new HashMap<>();
        map.put("activitys",list);
        map.put("nowPage",nowPage);
        map.put("tatalPage",pageUtil.getTotalPage());
        return FinderResultStateEnum.OK.toResultStatus().setInfo(map);
    }

    //上传物品图片
    @ApiOperation(value = "上传物品图片", notes = "物品图片上传")
    @ApiImplicitParam(name = "itemId", value = "物品id", dataType = "Long", required = true, paramType = "query")
    @RequestMapping(value = "/uplodeItemImg", method = RequestMethod.POST)
    public ResultStatus uplodeItemImg(@RequestParam Long itemId,
                                      @RequestParam("file") MultipartFile file){
        if (itemId == null || file == null) {
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        String path = itemService.saveImg(file);
        if (StringUtils.isEmpty(path)){
            log.debug("上传图片失败");
            return FinderResultStateEnum.UPLODEICONIMAGE_FAIL_ERROR.toResultStatus();
        }
        log.debug("icon path:{}",path);
        Items items = itemService.selectById(itemId);
        items.setItemImg(path);
        itemService.update(items);
        return FinderResultStateEnum.OK.toResultStatus();
    }

    //todo 根据id查看物品消息
    @ApiOperation(value = "根据id查物品信息", notes = "根据id查物品信息")
    @ApiImplicitParam(name = "itemId", value = "物品id", dataType = "Long", required = true, paramType = "query")
    @RequestMapping(value = "/getItemInfo", method = RequestMethod.GET)
    public ResultStatus getItemInfo(@RequestParam Long itemId){
        if (itemId == null)
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        Items items = itemService.selectById(itemId);
        if (items == null){
            return FinderResultStateEnum.NOT_FOUND_ERROR.toResultStatus();
        }
        return FinderResultStateEnum.OK.toResultStatus().setInfo(items);
    }

    @ApiOperation(value = "生成带参二维码",notes = "")
    @ApiImplicitParam(name = "link",value = "String",required = true,paramType = "query")
    @RequestMapping(value = "/createQRCode",method = RequestMethod.POST)
    public ResultStatus createQRCode(@RequestParam String link){
        if(link == null){
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        }
        String base64Img = IOUtil.creatQRcodeTobase64(link.toString(),200,200,"png");
        log.debug("path:{}",base64Img);
        return FinderResultStateEnum.OK.toResultStatus().setInfo(base64Img);
    }


    //todo 根据id设置消息的下线基数
    @ApiOperation(value = "根据id调整下线基数", notes = "根据id查物品的下线基数")
    @ApiImplicitParam(name = "itemId", value = "物品id", dataType = "Long", required = true, paramType = "query")
    @RequestMapping(value = "/setItemOffbase", method = RequestMethod.GET)
    public ResultStatus setItemOffbase(@RequestParam Long itemId){
        if (itemId == null)
            return FinderResultStateEnum.MISSING_PARAMETER_ERROR.toResultStatus();
        Items items = itemService.selectById(itemId);
        if (items == null){
            return FinderResultStateEnum.NOT_FOUND_ERROR.toResultStatus();
        }
        //将该idoffbase+1
        itemService.offbaseDecr(itemId);
        return FinderResultStateEnum.OK.toResultStatus();
    }

}
