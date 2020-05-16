package com.example.pin.handler.impl;

import com.example.pin.handler.PinHandler;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;
import com.example.pin.utils.DesUtil;
import com.example.pin.utils.JsonUtils;
import com.example.pin.utils.RSAUtils;
import com.example.pin.utils.RsaTool;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.crypto.Des;

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
     * @param businessInfo
     */
    @SneakyThrows
    @Override
    public SentArbitration sentArbitration(String msk, BusinessInfo businessInfo) {
        System.out.println("第一步"+msk+"businessInfo   "+businessInfo);
        String business = JsonUtils.objectToJson(businessInfo);
        String ekm = RsaTool.encryptByPrivateKey(business, msk);
        SentArbitration sentArbitration=new SentArbitration();
        BeanUtils.copyProperties(businessInfo,sentArbitration);
        sentArbitration.setEkm(ekm);
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
    public SentToBusinessInfo verArbitration(String aKey,String mKey,SentArbitration arbitration) {
        System.out.println("第一步"+aKey+"arbitration   "+arbitration);
        String toJson = JsonUtils.objectToJson(arbitration);
        SentToBusinessInfo sentToBusinessInfo=new SentToBusinessInfo();

        sentToBusinessInfo.setEkm(decrypt);
        BeanUtils.copyProperties(arbitration,sentToBusinessInfo);
        String json = JsonUtils.objectToJson(sentToBusinessInfo);
        String s = DesUtil.encrypt(json, mKey);
        SentToBusinessInfo sent=new SentToBusinessInfo();
        BeanUtils.copyProperties(sentToBusinessInfo,sent);
        sent.setEkm(s);
        return sentToBusinessInfo;
    }
    /**
     * 第三步：商家M用自己的密钥解密第二步收到的消息，
     * 再用A的公开钥解密验证二维码内信息的正确性后，将二维码打印出来。
     * @param
     * @param sentToBusinessInfo
     * @return
     */
    @SneakyThrows
    @Override
    public QrCode getInfo(String mSecret, String aSecret,SentToBusinessInfo sentToBusinessInfo) {
        String sentToBusinessInfoEkm = sentToBusinessInfo.getEkm();
        String decrypt = DesUtil.decrypt(aSecret, sentToBusinessInfoEkm);
        QrCode qrCode=new QrCode();
        BeanUtils.copyProperties(sentToBusinessInfo,qrCode);
        qrCode.setEksa(decrypt);
        return qrCode;
    }
    /**
     * 第四步：客户C扫描二维码，
     * C用A的钥解密二维码获得M加密过的信息，即Ekm[PIN||COUNT||Date]。
     * @param publicKey
     * @param qrCode
     * @return
     */
    @SneakyThrows
    @Override
    public String getInfo(String publicKey, QrCode qrCode) {
        String eksa = qrCode.getEksa();
        String decrypt = DesUtil.decrypt(eksa, eksa);
        return decrypt;
    }
    /**
     * 第五步：客户C把第四步得到的信息，
     * 加上IDC, IDM, IDA, Date, Money，
     * 一起用自己的密钥加密后，发送给仲裁者A。
     * 即IDC||IDM||IDA||Ekc[Ekm[PIN||COUNT||Data’]||IDC||IDM||IDA||Date||Money]
     *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
     * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
     */
    @SneakyThrows
    @Override
    public String getPin(String msg,String keyC,String keyA)
    {
        String encrypt = DesUtil.encrypt(msg, keyC);
        String decrypt = DesUtil.decrypt(encrypt, keyA);
        return decrypt;
    }
    @SneakyThrows
    @Override
    public String getMsg(String msg){
        BusinessInfo businessInfo = JsonUtils.jsonToPojo(msg, BusinessInfo.class);
        String decrypt = DesUtil.decrypt(msg, businessInfo.getPIN());
        return decrypt;
    }
}
