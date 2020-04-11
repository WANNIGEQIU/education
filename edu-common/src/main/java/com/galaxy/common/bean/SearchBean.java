package com.galaxy.common.bean;

import lombok.Data;

@Data
public class SearchBean {

    /**
     * 页数
     */
    private Long page;

    /**
     * 页数
     */
    private Long limit;

    /**
     * 排序
     */
    private String order;

    /**
     * 排序的字段
     */
    private String key;

    private String beginTime;

    private String endTime;
}
