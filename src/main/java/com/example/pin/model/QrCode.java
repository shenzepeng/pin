package com.example.pin.model;

import lombok.Data;

/**
 * 要写注释呀
 */
@Data
public class QrCode {
    private String IDM;
    private String IDA;
    /**
     * 签名
     */
    private String Eksa;
}
