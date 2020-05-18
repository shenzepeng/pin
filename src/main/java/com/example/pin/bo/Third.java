package com.example.pin.bo;

import com.example.pin.constanst.Constant;
import com.example.pin.utils.RsaTool;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * 要写注释呀
 */
@Data
public class Third {
    private String IDM;
    private String IDA;
    /**
     * 签名
     */
    private String Eksa;
    public Third(){
        this.IDM= Constant.IDM;
        this.IDA=Constant.IDA;
    }

    @SneakyThrows
    public static void getThird(){
        Map<String, Object> init = RsaTool.init();
        String privateKey = RsaTool.getPrivateKey(init);
        Thread.sleep(100L);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(System.currentTimeMillis() + "", privateKey);
        Third third=new Third();
        third.setEksa(encryptByPrivateKey);
        System.out.println(third);
    }
}
