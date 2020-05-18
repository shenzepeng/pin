package com.example.pin.constanst;

import com.example.pin.handler.PinHandler;
import com.example.pin.utils.RsaTool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * 要写注释呀
 */
@Data
@Component
public class Constant {
    //商家密钥
    public static String mSk;
    public static String mPk;
    //仲裁者A
    public static String aSk;
    public static String aPk;
    //用户
    public static String uSk;
    public static String uPk;
    public static String IDA = "IDA";
    public static String IDM = "IDM";
    public static String IDC = "IDC";
    public static Double money = 100d;
    public final static String PIN = UUID.randomUUID().toString();

    static {
        Map<String, Object> init = RsaTool.init();
        mSk = RsaTool.getPrivateKey(init);
        mPk = RsaTool.getPublicKey(init);

        Map<String, Object> aInit = RsaTool.init();
        aSk = RsaTool.getPrivateKey(aInit);
        aPk = RsaTool.getPublicKey(aInit);

        Map<String, Object> uInit = RsaTool.init();
        uSk = RsaTool.getPrivateKey(uInit);
        uPk = RsaTool.getPublicKey(uInit);
    }

}
