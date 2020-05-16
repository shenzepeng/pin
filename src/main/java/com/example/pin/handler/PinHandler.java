package com.example.pin.handler;

import com.example.pin.dto.ForthInfoDto;
import com.example.pin.dto.ToFiveDto;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;

/**
 * 要写注释呀
 */
public interface PinHandler {
    /**
     *商家M用自己的密钥把IDM，IDA,PIN,COUNT,DATA加密，作为签名，发送给仲裁者A
     * 其中IDM是商家的信息，IDA是仲裁者A的信息，PIN是商家自己记住的码，用来口头报予用户。COUNT是商家的账号，DATA是商品信息。
     * 即IDM ||IDA||Date||Ekm[ IDM||IDA||PIN||COUNT||Date]
     * @param msk
     * @param businessInfo
     */
    SentArbitration sentArbitration1(String msk, BusinessInfo businessInfo);

    /**
     *：仲裁者A把第一步接收到的Ekm[PIN||COUNT||Date]，加上IDM，IDA一起用A的私秘钥加密，形成二维码。
     * 即Eksa{ IDM||IDA||Ekm[PIN||COUNT||Date] }。（这里是指的是二维码）
     * 再配合IDA ，IDM和Date用商家M的密钥加密，发送给商家。
     * 即IDA||IDM||Ekm{ IDA||IDM|| Eksa[   IDM||IDA||Ekm [PIN||COUNT||Data’]  ]||Data }
     * @param arbitration
     */
    SentToBusinessInfo verArbitration2(String ask,String msk,SentArbitration arbitration);

    /**
     * 第三步：商家M用自己的密钥解密第二步收到的消息，
     * 再用A的公开钥解密验证二维码内信息的正确性后，将二维码打印出来。
     * @param publicMSecret
     * @param sentToBusinessInfo
     * @return
     */
    QrCode getInfo3(String publicMSecret,String apk,SentToBusinessInfo sentToBusinessInfo);

    /**
     * 第四步：客户C扫描二维码，
     * C用A的公开钥解密二维码获得M加密过的信息，即Ekm[PIN||COUNT||Date]。
     * @param publicKey
     * @param qrCode
     * @return
     */
    String getInfo4(String publicKey,QrCode qrCode);
    /**
     * 第五步：客户C把第四步得到的信息，
     * 加上IDC, IDM, IDA, Date, Money，
     * 一起用自己的密钥加密后，发送给仲裁者A。
     * 即IDC||IDM||IDA||Ekc[Ekm[PIN||COUNT||Data’]||IDC||IDM||IDA||Date||Money]
     *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
     * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
     */
    ToFiveDto getPin5(String msg, String keyC, BusinessInfo businessInfo, String IDC);

    /**
     *  *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
     *      * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
     * @param msg
     * @param
     * @return
     */
     String getMsg6(ToFiveDto toFiveDto,String upk,String apk,String pin);
}
