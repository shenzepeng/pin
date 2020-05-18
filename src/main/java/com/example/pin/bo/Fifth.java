package com.example.pin.bo;

import com.example.pin.constanst.Constant;
import com.example.pin.utils.RsaTool;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * IDC||IDM||IDA||Ekc
 */
@Data
public class Fifth {
    private String IDC;
    private String IDM;
    private String IDA;
    private String Ekc;
    public Fifth(){
        this.IDM= Constant.IDM;
        this.IDA=Constant.IDA;
        this.IDC=Constant.IDC;
    }
    @SneakyThrows
    public static void getFifth(){
        Map<String, Object> init = RsaTool.init();
        String privateKey = RsaTool.getPrivateKey(init);
        Thread.sleep(10L);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(System.currentTimeMillis() + "", privateKey);
        Fifth fifth=new Fifth();
        fifth.setEkc(encryptByPrivateKey);
        System.out.println(fifth);
    }
}
