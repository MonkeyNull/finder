package com.monkey.finder.find.task;

import com.monkey.finder.find.entity.Items;
import com.monkey.finder.find.service.Imp.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Monkey on 2018/10/29.
 */
@Slf4j
@Component
public class ActivityTask {
    /**
     "0/5 * *  * * ?"   每5秒触发
     "0 0 12 * * ?"    每天中午十二点触发
     "0 15 10 ? * *"    每天早上10：15触发
     "0 15 10 * * ?"    每天早上10：15触发
     "0 15 10 * * ? *"    每天早上10：15触发
     "0 15 10 * * ? 2005"    2005年的每天早上10：15触发
     "0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发
     "0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发
     "0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
     "0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发
     "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
     "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
     */

    @Resource
    ItemService itemService;


    /**
     * 每晚23.59下线当日到期的活动
     */
    @Scheduled(cron="0 59 23 ? * *")
    public void offActivity(){

        ArrayList<Items> list = itemService.selectAll();

        for (Items item: list) {
            if (item.getOffBase() >= 3)
                itemService.offbase(item.getItemId());
        }
    }


}
