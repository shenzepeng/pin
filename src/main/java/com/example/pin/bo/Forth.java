package com.example.pin.bo;

import com.example.pin.utils.RsaTool;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * 要写注释呀
 */
@Data
public class Forth {
    private String Ekm;
    @SneakyThrows
    public static void getForth(){
        Map<String, Object> init = RsaTool.init();
        String privateKey = RsaTool.getPrivateKey(init);
        Thread.sleep(100L);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(System.currentTimeMillis() + "", privateKey);
        Forth forth=new Forth();
        forth.setEkm(encryptByPrivateKey);
        System.out.println(forth);
    }
}
