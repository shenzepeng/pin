package com.example.pin.handler.impl;

import com.example.pin.handler.PinHandler;
import com.example.pin.model.BusinessInfo;
import com.example.pin.model.QrCode;
import com.example.pin.model.SentArbitration;
import com.example.pin.model.SentToBusinessInfo;
import org.springframework.stereotype.Service;

/**
 * 要写注释呀
 */
@Service
public class PinHandlerImpl implements PinHandler {
    @Override
    public SentArbitration sentArbitration(String privateSecret, BusinessInfo businessInfo) {
        return null;
    }

    @Override
    public SentToBusinessInfo verArbitration(SentArbitration arbitration) {
        return null;
    }

    @Override
    public QrCode getInfo(String publicSecret, SentToBusinessInfo sentToBusinessInfo) {
        return null;
    }

    @Override
    public String getInfo(String publicKey, QrCode qrCode) {
        return null;
    }

    @Override
    public String getPin(String msg) {
        return null;
    }
}
