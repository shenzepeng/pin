package com.example.pin.model;

import lombok.Data;

import java.util.Date;

/**
 * 发送给仲裁者
 */
@Data
public class SentArbitration {
    /**
     * IDM是商家的信息
     */
    private String IDM;
    /**
     * ，IDA是仲裁者A的信息
     */
    private String IDA;
    private Date date;
    /**
     * Ekm
     */
    private String Ekm;
}
