package com.example.pin.model;

import lombok.Data;

import java.util.Date;

/**
 * 要写注释呀
 */
@Data
public class BusinessInfo {
    /**
     *商家的信息
     */
    private String IDM;
    /**
     *仲裁者A的信息
     */
    private String IDA;
    /**
     *商家自己记住的码
     */
    private String PIN;
    /**
     *商家的账号
     */
    private String COUNT;
    /**
     *      日期
     */
    private Date DATA;
}
