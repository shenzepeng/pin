package com.example.pin.handler.impl;

import com.example.pin.handler.PinHandler;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;
import com.example.pin.utils.JsonUtils;
import com.example.pin.utils.RSAUtils;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 要写注释呀
 */
@Service
public class PinHandlerImpl implements PinHandler {
    /**
     *商家M用自己的密钥把IDM，IDA,PIN,COUNT,DATA加密，作为签名，发送给仲裁者A
     * 其中IDM是商家的信息，IDA是仲裁者A的信息，PIN是商家自己记住的码，用来口头报予用户。COUNT是商家的账号，DATA是商品信息。
     * 即IDM ||IDA||Date||Ekm[ IDM||IDA||PIN||COUNT||Date]
     * @param privateMSecret
     * @param businessInfo
     */
    @SneakyThrows
    @Override
    public SentArbitration sentArbitration(String privateMSecret, BusinessInfo businessInfo) {
        String business = JsonUtils.objectToJson(businessInfo);
        RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateMSecret);
        String privateEncrypt = RSAUtils.privateEncrypt(business, rsaPrivateKey);
        SentArbitration sentArbitration=new SentArbitration();
        BeanUtils.copyProperties(businessInfo,sentArbitration);
        sentArbitration.setEkm(privateEncrypt);
        return sentArbitration;
    }
    /**
     *：仲裁者A把第一步接收到的Ekm[PIN||COUNT||Date]，
     * 加上IDM，IDA一起用A的私秘钥加密，形成二维码。
     * 即Eksa{ IDM||IDA||Ekm[PIN||COUNT||Date] }。（这里是指的是二维码）
     * 再配合IDA ，IDM和Date用商家M的密钥加密，发送给商家。
     * 即IDA||IDM||Ekm{ IDA||IDM|| Eksa[   IDM||IDA||Ekm [PIN||COUNT||Data’]  ]||Data }
     * @param arbitration
     */
    @SneakyThrows
    @Override
    public SentToBusinessInfo verArbitration(String privateAKey,String privateMKey,SentArbitration arbitration) {
        String toJson = JsonUtils.objectToJson(arbitration);
        RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateAKey);
        String privateEncrypt = RSAUtils.privateEncrypt(toJson, rsaPrivateKey);
        SentToBusinessInfo sentToBusinessInfo=new SentToBusinessInfo();
        BeanUtils.copyProperties(arbitration,sentToBusinessInfo);
        String json = JsonUtils.objectToJson(sentToBusinessInfo);
        RSAPrivateKey rsaPrivateMKey = RSAUtils.getPrivateKey(privateMKey);
        RSAUtils.privateEncrypt(json,rsaPrivateMKey);
        sentToBusinessInfo.setEkm(privateEncrypt);
        return sentToBusinessInfo;
    }
    /**
     * 第三步：商家M用自己的密钥解密第二步收到的消息，
     * 再用A的公开钥解密验证二维码内信息的正确性后，将二维码打印出来。
     * @param publicMSecret
     * @param sentToBusinessInfo
     * @return
     */
    @SneakyThrows
    @Override
    public QrCode getInfo(String publicMSecret, String publicASecret,SentToBusinessInfo sentToBusinessInfo) {
        RSAPublicKey publicMKey = RSAUtils.getPublicKey(publicMSecret);
        RSAPublicKey publicAKey = RSAUtils.getPublicKey(publicASecret);
        String sentToBusinessInfoEkm = sentToBusinessInfo.getEkm();

        return null;
    }
    /**
     * 第四步：客户C扫描二维码，
     * C用A的公开钥解密二维码获得M加密过的信息，即Ekm[PIN||COUNT||Date]。
     * @param publicKey
     * @param qrCode
     * @return
     */
    @Override
    public String getInfo(String publicKey, QrCode qrCode) {
        return null;
    }
    /**
     * 第五步：客户C把第四步得到的信息，
     * 加上IDC, IDM, IDA, Date, Money，
     * 一起用自己的密钥加密后，发送给仲裁者A。
     * 即IDC||IDM||IDA||Ekc[Ekm[PIN||COUNT||Data’]||IDC||IDM||IDA||Date||Money]
     *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
     * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
     */
    @Override
    public String getPin(String msg) {
        return null;
    }
}
