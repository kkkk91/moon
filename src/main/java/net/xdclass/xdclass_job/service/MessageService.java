package net.xdclass.xdclass_job.service;

import net.xdclass.xdclass_job.domain.DelayedMessage;

import java.util.List;

public interface MessageService {


    /**
     * 保存消息，如果是晚上9点之后，则将消息计划发送时间设置为次日9点。
     *
     * @param channelType    消息渠道类型
     * @param messageContent 消息内容
     * @param userId         用户ID
     */
    void saveMessage(String channelType, String messageContent, Long userId);

    /**
     * 获取所有状态为 PENDING 并且计划发送时间已到的消息，更新状态为 PROCESSING。
     *
     * @return 待发送的消息列表
     */
    List<DelayedMessage> getPendingMessagesAndLock();

    /**
     * 发送消息，并根据发送结果更新状态。
     *
     * @param message 消息对象
     * @return 发送是否成功
     */
    boolean sendMessage(DelayedMessage message);

    /**
     * 更新消息状态为 SENT。
     *
     * @param messageId 消息ID
     */
    void markMessageAsSent(Long messageId);

    /**
     * 更新消息状态为 FAILED。
     *
     * @param messageId 消息ID
     */
    void markMessageAsFailed(Long messageId);
}
