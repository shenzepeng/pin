package com.example.pin.bo;

import com.example.pin.PinApplication;
import com.example.pin.constanst.Constant;
import lombok.Data;

/**
 * 要写注释呀
 */
@Data
public class First {
    private String IDM ;
    private String IDA;
    private long date;
    public First(){
        this.IDM= Constant.IDM;
        this.IDA=Constant.IDA;
        this.date=System.currentTimeMillis();
    }
}
