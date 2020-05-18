package com.example.pin.handler.impl;

import com.example.pin.dto.ForthInfoDto;
import com.example.pin.dto.ToFiveDto;
import com.example.pin.handler.PinHandler;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;
import com.example.pin.utils.BinarySaver;
import com.example.pin.utils.DesUtil;
import com.example.pin.utils.JsonUtils;
import com.example.pin.utils.RsaTool;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.UUID;

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
    public SentArbitration sentArbitration1(String msk, BusinessInfo businessInfo) {
        BinarySaver.saveBinary(new URL(BinarySaver.url));
        String business = JsonUtils.objectToJson(businessInfo);
        String ekm = RsaTool.encryptByPrivateKey(business, msk);
        System.out.println("before   "+business+"    after    "+ekm);
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
    public SentToBusinessInfo verArbitration2(String ask,String msk,SentArbitration arbitration) {
        SentToBusinessInfo sentToBusinessInfo=new SentToBusinessInfo();
        sentToBusinessInfo.setEkm(arbitration.getEkm());
        sentToBusinessInfo.setDate(System.currentTimeMillis());
        BeanUtils.copyProperties(arbitration,sentToBusinessInfo);
        String json = JsonUtils.objectToJson(sentToBusinessInfo);

        SentToBusinessInfo sent=new SentToBusinessInfo();
        BeanUtils.copyProperties(sentToBusinessInfo,sent);
        String encryptByPrivateKey = RsaTool.encryptByPrivateKey(json, ask);
        sent.setEkm(encryptByPrivateKey);

        SentToBusinessInfo sentToBusiness=new SentToBusinessInfo();
        BeanUtils.copyProperties(arbitration,sentToBusiness);
        String objectToJson = JsonUtils.objectToJson(sent);
        String byPrivateKey = RsaTool.encryptByPrivateKey(objectToJson, msk);
        sentToBusiness.setEkm(byPrivateKey);
        return sentToBusiness;
    }
    /**
     * 第三步：商家M用自己的公密钥解密第二步收到的消息，
     * 再用A的公开钥解密验证二维码内信息的正确性后，将二维码打印出来。
     * @param
     * @param sentToBusinessInfo
     * @return
     */
    @SneakyThrows
    @Override
    public QrCode getInfo3(String mPs, String apk,SentToBusinessInfo sentToBusinessInfo) {
        System.out.println("第三步"+mPs+"---"+apk+"sentToBusinessInfo   "+sentToBusinessInfo);
        String sentToBusinessInfoEkm = sentToBusinessInfo.getEkm();
        String decrypt = RsaTool.decryptByPublicKey(sentToBusinessInfoEkm, mPs);
        SentToBusinessInfo sent = JsonUtils.jsonToPojo(decrypt, SentToBusinessInfo.class);
        System.out.println("sentArbitration--"+sent);
        String ekm = sent.getEkm();
        String encryptByPublicKe1y = RsaTool.decryptByPublicKey(ekm, apk);
        System.out.println("--ekm---"+ekm+"          "+encryptByPublicKe1y);
        SentToBusinessInfo businessInfo = JsonUtils.jsonToPojo(ekm, SentToBusinessInfo.class);
        String encryptByPublicKey = RsaTool.decryptByPublicKey(businessInfo.getEkm(), apk);
        QrCode qrCode=new QrCode();
        BeanUtils.copyProperties(sent,qrCode);
        qrCode.setEksa(encryptByPublicKey);
        return qrCode;
    }
    /**
     * 第四步：客户C扫描二维码，
     * C用A的钥解密二维码获得M加密过的信息，即Ekm[PIN||COUNT||Date]。
     * @param pAk
     * @param qrCode
     * @return
     */
    @SneakyThrows
    @Override
    public String getInfo4(String pAk, QrCode qrCode) {
        String eksa = qrCode.getEksa();
        String decrypt = RsaTool.decryptByPublicKey(eksa, pAk);
        return decrypt;
    }
    /**
     * 第五步：客户C把第四步得到的信息，
     * 加上IDC, IDM, IDA, Date, Money，
     * 一起用自己的密钥加密后，发送给仲裁者A。
     * 即IDC||IDM||IDA||Ekc[Ekm[PIN||COUNT||Data’]||IDC||IDM||IDA||Date||Money]
     */
    @SneakyThrows
    @Override
    public ToFiveDto getPin5(String msg,String csk,BusinessInfo businessInfo,String IDC) {
        System.out.println("第五步 businessInfo"+msg+businessInfo);
        ForthInfoDto forthInfoDto=new ForthInfoDto();
        forthInfoDto.setBusinessInfo(businessInfo);
        forthInfoDto.setMsg(msg);
        ToFiveDto fiveDto=new ToFiveDto();
        fiveDto.setIDA(fiveDto.getIDA());
        String objectToJson = JsonUtils.objectToJson(forthInfoDto);
        String decryptByPrivateKey = RsaTool.encryptByPrivateKey(objectToJson, csk);
        fiveDto.setIDC(IDC);
        fiveDto.setIDC(IDC);
        fiveDto.setMsg(msg);
        return fiveDto;
    }

    /**
     * *第六步：仲裁者A把第五步收到的信息进行解密，得到PIN,Money,COUNT,Date。
     *      * A用PIN作为密钥，将IDA, IDC, IDM,COUNT, Money, Date加密发送给客户C。
     * @param toFiveDto
     * @param cpk
     * @param pAk
     * @return
     */
    @SneakyThrows
    @Override
    public String getMsg6(ToFiveDto toFiveDto,String cpk,String pAk,String pin){
        String msg = toFiveDto.getMsg();
        String decrypt = DesUtil.encrypt(msg, pin);
        return decrypt;
    }
}
