package com.example.pin.init;

import com.example.pin.bo.*;
import com.example.pin.utils.BinarySaver;
import lombok.SneakyThrows;

import java.net.URL;

/**
 * 要写注释呀
 */
public class Caculate {
    @SneakyThrows
    public static void init(){
        System.out.println("init");
        First.getFirstString();
        Second.getSecond();
        Third.getThird();
        Forth.getForth();
        Fifth.getFifth();
        Sixth.getSixth();
        BinarySaver.saveBinary(new URL(BinarySaver.url));
        System.out.println("end");
    }
}
