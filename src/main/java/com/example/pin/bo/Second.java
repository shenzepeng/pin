package com.example.pin.bo;

import com.example.pin.constanst.Constant;
import com.example.pin.utils.RsaTool;
import lombok.Data;

import java.util.Map;

/**
 * IDA||IDM||Ekm{ IDA||IDM|| Eksa[   IDM||IDA||Ekm [PIN||COUNT||Data’]
 */
@Data
public class Second {
    private String IDM ;
    private String IDA;
    private String Ekm;
    public Second(){
        this.IDM= Constant.IDM;
        this.IDA=Constant.IDA;
    }
    public static void getSecond(){
        Map<String, Object> init = RsaTool.init();
        String privateKey = RsaTool.getPrivateKey(init);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(System.currentTimeMillis() + "", privateKey);
        Second second=new Second();
        second.setEkm(encryptByPrivateKey);
        System.out.println(second);
    }
}
