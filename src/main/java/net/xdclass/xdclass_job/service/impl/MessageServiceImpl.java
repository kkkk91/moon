package net.xdclass.xdclass_job.service.impl;

import net.xdclass.xdclass_job.domain.DelayedMessage;
import net.xdclass.xdclass_job.domain.DelayedMessage.Status;
import net.xdclass.xdclass_job.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EntityManager entityManager;

    /**
     * 保存消息逻辑
     */
    @Override
    @Transactional
    public void saveMessage(String channelType, String messageContent, Long userId) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 判断是否为晚上九点到早上九点之间
        LocalTime start = LocalTime.of(21, 0); // 晚上九点
        LocalTime end = LocalTime.of(9, 0);   // 早上九点
        LocalTime currentTime = now.toLocalTime();

        if (currentTime.isAfter(start) || currentTime.isBefore(end)) {
            // 当前时间在晚上九点到早上九点之间，需要存入数据库
            DelayedMessage delayedMessage = new DelayedMessage();
            delayedMessage.setChannelType(channelType);
            delayedMessage.setMessageContent(messageContent);
            delayedMessage.setUserId(userId);
            delayedMessage.setCreatedTime(now);
            delayedMessage.setScheduledTime(now.plusHours(12)); // 计划发送时间设为 12 小时后
            delayedMessage.setStatus(Status.PENDING);

            entityManager.persist(delayedMessage); // 保存到数据库
            System.out.println("消息存入延迟表：" + delayedMessage);
        } else {
            // 当前时间不在晚上九点到早上九点之间，可以直接发送
            System.out.println("消息立即发送：" + messageContent);
            // 调用发送消息逻辑
            sendMessageDirectly(channelType, messageContent, userId);
        }
    }

    // 立即发送消息的方法
    private void sendMessageDirectly(String channelType, String messageContent, Long userId) {
        // 在这里实现消息发送逻辑，例如调用外部 API
        System.out.println("发送消息: 渠道类型=" + channelType + ", 内容=" + messageContent + ", 用户ID=" + userId);
    }

    /**
     * 获取所有状态为 PENDING 且计划发送时间已到的消息，更新状态为 PROCESSING
     */
    @Override
    @Transactional
    public List<DelayedMessage> getPendingMessagesAndLock() {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM delayed_messages WHERE status = :status AND scheduled_time <= :currentTime FOR UPDATE",
                DelayedMessage.class
        );
        query.setParameter("status", Status.PENDING.name());
        query.setParameter("currentTime", LocalDateTime.now());

        @SuppressWarnings("unchecked")
        List<DelayedMessage> messages = query.getResultList();

        // 更新状态为 PROCESSING
        for (DelayedMessage message : messages) {
            message.setStatus(Status.PROCESSING);
        }

        // 将状态更新同步到数据库
        entityManager.flush();

        return messages;
    }
    /**
     * 发送消息
     */
    @Override
    @Transactional
    public boolean sendMessage(DelayedMessage message) {
        try {
            System.out.printf("Sending message to user %d: %s%n", message.getUserId(), message.getMessageContent());
            return true; // 假设发送成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 假设发送失败
        }
    }

    /**
     * 将发送成功的消息状态改为 SENT
     */
    @Override
    @Transactional
    public void markMessageAsSent(Long messageId) {
        DelayedMessage message = entityManager.find(DelayedMessage.class, messageId);
        if (message != null) {
            message.setStatus(Status.SENT);
        }
    }

    /**
     * 将发送失败的消息状态改为 FAILED
     */
    @Override
    @Transactional
    public void markMessageAsFailed(Long messageId) {
        DelayedMessage message = entityManager.find(DelayedMessage.class, messageId);
        if (message != null) {
            message.setStatus(Status.FAILED);
        }
    }
}
