package com.example.pin.constanst;

import com.example.pin.handler.PinHandler;
import com.example.pin.utils.RsaTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

/**
 * 要写注释呀
 */
public class Constant {
    //商家密钥
    private static String mSk;
    private static String mPk;
    //仲裁者A
    private static String aSk;
    private static String aPk;
    //用户
    private static String uSk;
    private static String uPk;
    public static String IDA = "IDA";
    public static String IDM = "IDM";
    public static String IDC = "IDC";
    public static Double money = 100d;
    private final static String PIN = UUID.randomUUID().toString();
    @Autowired
    private PinHandler pinHandler;

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
