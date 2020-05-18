package com.example.pin.bo;

import com.example.pin.utils.DesUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.UUID;

/**
 * 要写注释呀
 */
@Data
public class Sixth {
    private String addPinToC;
    @SneakyThrows
    public static void getSixth(){
        Sixth sixth=new Sixth();
        sixth.setAddPinToC(DesUtil.encrypt(UUID.randomUUID().toString(), System.currentTimeMillis()+""));
        System.out.println(sixth);
    }
}
