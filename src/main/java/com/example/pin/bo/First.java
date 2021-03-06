package com.example.pin.bo;

import com.example.pin.PinApplication;
import com.example.pin.constanst.Constant;
import com.example.pin.utils.RsaTool;
import lombok.Data;

import java.util.Map;

/**
 * 要写注释呀
 */
@Data
public class First {
    private String IDM ;
    private String IDA;
    private long date;
    private String Ekm;
    public First(){
        this.IDM= Constant.IDM;
        this.IDA=Constant.IDA;
        this.date=System.currentTimeMillis();
    }
    public static void getFirstString(){
        Map<String, Object> init = RsaTool.init();
        String privateKey = RsaTool.getPrivateKey(init);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(System.currentTimeMillis() + "", privateKey);
        First first=new First();
        first.setEkm(encryptByPrivateKey);
        System.out.println(first);
    }
}
