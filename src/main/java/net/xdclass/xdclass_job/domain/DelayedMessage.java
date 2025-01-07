package net.xdclass.xdclass_job.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delayed_messages") // 映射到表 delayed_messages
public class DelayedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    private Long id;

    @Column(name = "channel_type", nullable = false, length = 50) // 对应 channel_type 字段
    private String channelType;

    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT") // 对应 message_content 字段
    private String messageContent;

    @Column(name = "user_id", nullable = false) // 对应 user_id 字段
    private Long userId;

    @Column(name = "created_time", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP") // 对应 created_time 字段
    private LocalDateTime createdTime;

    @Column(name = "scheduled_time") // 对应 scheduled_time 字段
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING) // 对应 status 字段，枚举类型
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'PROCESSING', 'SENT', 'FAILED') DEFAULT 'PENDING'")
    private Status status;

    // 构造器、Getter 和 Setter 方法

    public DelayedMessage() {
        this.createdTime = LocalDateTime.now(); // 默认赋值当前时间
    }

    public DelayedMessage(String channelType, String messageContent, Long userId, LocalDateTime scheduledTime, Status status) {
        this.channelType = channelType;
        this.messageContent = messageContent;
        this.userId = userId;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.createdTime = LocalDateTime.now(); // 默认赋值当前时间
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // 枚举类，用于映射 status 字段
    public enum Status {
        PENDING,
        PROCESSING,
        SENT,
        FAILED
    }
}
