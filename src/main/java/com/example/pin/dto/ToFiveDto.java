package com.example.pin.dto;

import lombok.Data;

/**
 * 要写注释呀
 */
@Data
public class ToFiveDto {
    /**
     *商家的信息
     */
    private String IDM;
    /**
     *仲裁者A的信息
     */
    private String IDA;

    private String IDC;

    private String msg;
}
