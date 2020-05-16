package com.example.pin;


import com.example.pin.dto.ToFiveDto;
import com.example.pin.handler.PinHandler;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;
import com.example.pin.utils.RsaTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PinApplicationTests {
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

    @Test
    public void contextLoads() {
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setCOUNT("100");
        businessInfo.setDATA(new Date());
        businessInfo.setIDA(IDA);
        businessInfo.setIDM(IDM);
        businessInfo.setMoney(money);
        businessInfo.setPIN(PIN);
        /**
         *商家M用自己的密钥把IDM，IDA,PIN,COUNT,DATA加密，作为签名，发送给仲裁者A
         * 其中IDM是商家的信息，IDA是仲裁者A的信息，PIN是商家自己记住的码，用来口头报予用户。COUNT是商家的账号，DATA是商品信息。
         * 即IDM ||IDA||Date||Ekm[ IDM||IDA||PIN||COUNT||Date]
         * @param msk
         * @param businessInfo
         */
        SentArbitration sentArbitration = pinHandler.sentArbitration1(mSk, businessInfo);

        /**
         *：仲裁者A把第一步接收到的Ekm[PIN||COUNT||Date]，加上IDM，IDA一起用A的私秘钥加密，形成二维码。
         * 即Eksa{ IDM||IDA||Ekm[PIN||COUNT||Date] }。（这里是指的是二维码）
         * 再配合IDA ，IDM和Date用商家M的密钥加密，发送给商家。
         * 即IDA||IDM||Ekm{ IDA||IDM|| Eksa[   IDM||IDA||Ekm [PIN||COUNT||Data’]  ]||Data }
         * @param arbitration
         */
        SentToBusinessInfo sentToBusinessInfo = pinHandler.verArbitration2(aSk, mSk, sentArbitration);
        /**
         * 第三步：商家M用自己的密钥解密第二步收到的消息，
         * 再用A的公开钥解密验证二维码内信息的正确性后，将二维码打印出来。
         * @param publicMSecret
         * @param sentToBusinessInfo
         * @return
         */
        QrCode qrCode = pinHandler.getInfo3(mPk, aPk, sentToBusinessInfo);
        /**
         * 第四步：客户C扫描二维码，
         * C用A的公开钥解密二维码获得M加密过的信息，即Ekm[PIN||COUNT||Date]。
         * @param publicKey
         * @param qrCode
         * @return
         */
        String msg = pinHandler.getInfo4(aPk, qrCode);
    /**
     * 第五步：客户C把第四步得到的信息，
     * 加上IDC, IDM, IDA, Date, Money，
     * 一起用自己的密钥加密后，发送给仲裁者A。
     * 即IDC||IDM||IDA||Ekc[Ekm[PIN||COUNT||Data’]||IDC||IDM||IDA||Date||Money]
     */
        ToFiveDto pin = pinHandler.getPin5(msg, mSk, businessInfo, IDC);
        /**
         *  *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
         *      * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
         * @param msg
         * @param
         * @return
         */
        pinHandler.getMsg6(pin, uPk, aPk,PIN);
    }

}
