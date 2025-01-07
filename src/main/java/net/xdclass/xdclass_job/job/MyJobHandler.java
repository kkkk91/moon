package net.xdclass.xdclass_job.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import net.xdclass.xdclass_job.domain.DelayedMessage;
import net.xdclass.xdclass_job.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyJobHandler {
//        private Logger log = LoggerFactory.getLogger(MyJobHandler.class);
//
//    @XxlJob(value = "austinJob", init = "init", destroy = "destroy")
//    public void execute() {
//        String param = XxlJobHelper.getJobParam();
//        System.out.println("param=" + param);
//
//        log.info("定时任务 execute 任务触发成功:" + LocalDateTime.now());
//        XxlJobHelper.log("这个是执行日志:param=" + param);
//        XxlJobHelper.handleFail("自定义错误，任务执行失败");
//        XxlJobHelper.handleSuccess("任务执行成功");
//        //return ReturnT.SUCCESS;
//    }
//
//    @XxlJob(value = "shardingJobHandler")
//    public void shardingJobHandler() {
//        //当前的执行器编号
//        int shardIndex = XxlJobHelper.getShardIndex();
//        //总的分片数，就是执行器的集群数量
//        int shardTotal = XxlJobHelper.getShardTotal();
//        log.info("分片总数:{},当前分片数{}", shardTotal, shardIndex);
//
//        List<Integer> allUserIds = getAllUserIds();
//        allUserIds.forEach(obj->{//遍历用户列表中的每一个id，如果id和分片总数取余等于当前执行器编号，则进行处理
//
//            if(obj % shardTotal== shardIndex){
//                log.info("第{}片，命中分片开始处理用户id={}",shardIndex,obj);
//            }
//        });
//    }
//
//
//
//    private List<Integer> getAllUserIds() {
//        List<Integer> ids = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            ids.add(i);
//        }
//        return ids;
//    }
//
//
//    private void init() {
//        log.info("init 方法调用成功");
//    }
//
//    private void destroy() {
//        log.info("destroy 方法调用成功");
//    }


//------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------


    private Logger log = LoggerFactory.getLogger(MyJobHandler.class);

    @Autowired
    private MessageService messageService;

    @XxlJob(value = "austinJob")
    public void execute() {
        log.info("定时任务开始执行...");

        // 获取所有待发送的消息并锁定
        List<DelayedMessage> messages = messageService.getPendingMessagesAndLock();
        // 打印每条消息的详细信息
        for (DelayedMessage message : messages) {
            log.info("消息详情 - ID: {}, 渠道类型: {}, 内容: {}, 用户ID: {}, 状态: {}, 创建时间: {}, 计划发送时间: {}",
                    message.getId(), message.getChannelType(), message.getMessageContent(),
                    message.getUserId(), message.getStatus(), message.getCreatedTime(),
                    message.getScheduledTime());
        }

        // 遍历消息逐一处理
        for (DelayedMessage message : messages) {
            log.info("开始处理消息ID: {}", message.getId());

            // 发送消息
            boolean success = messageService.sendMessage(message);

            // 根据发送结果更新状态
            if (success) {
                messageService.markMessageAsSent(message.getId());
                log.info("消息ID: {} 发送成功", message.getId());
            } else {
                messageService.markMessageAsFailed(message.getId());
                log.error("消息ID: {} 发送失败", message.getId());
            }
        }

        XxlJobHelper.handleSuccess("任务执行成功");
    }

}