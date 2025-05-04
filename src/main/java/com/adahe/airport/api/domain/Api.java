package com.adahe.airport.api.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ada He
 * @TableName api
 */
@TableName(value ="api")
@Data
@Accessors(chain = true)
public class Api {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "url")
    private String url;

    /**
     * 
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 
     */
    @TableField(value = "request_header")
    private String requestHeader;

    /**
     * 
     */
    @TableField(value = "response_header")
    private String responseHeader;

    /**
     * 
     */
    @TableField(value = "is_enabled")
    private Integer enabled;

    /**
     * 
     */
    @TableField(value = "created_by")
    private Long createdBy;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;
}