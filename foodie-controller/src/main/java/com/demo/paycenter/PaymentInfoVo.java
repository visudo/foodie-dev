package com.demo.paycenter;

import lombok.Data;

@Data
public class PaymentInfoVo {
    private String amount;
    private String merchantOrderId;
    private String merchantUserId;
    private String qrCodeUrl;
}
