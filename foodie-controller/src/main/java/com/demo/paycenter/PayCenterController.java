package com.demo.paycenter;

import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.vo.MerchantOrdersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟支付中心
 */
@RestController
public class PayCenterController {

    @Autowired
    QRService qrService;

    @Autowired
    RestTemplate restTemplate;

    private static int count = 0;

    /**
     * 获取二维码
     * @param merchantOrderId
     * @param merchantUserId
     * @return
     * @throws IOException
     */
    @PostMapping("/foodie-payment/payment/getWXPayQRCode")
    public DemoJsonResult qrCode(String merchantOrderId,String merchantUserId) throws IOException {
        PaymentInfoVo paymentInfoVo = new PaymentInfoVo();
        paymentInfoVo.setAmount("1");
        paymentInfoVo.setMerchantOrderId(merchantOrderId);
        paymentInfoVo.setMerchantUserId(merchantUserId);
        // 模拟使用
        paymentInfoVo.setQrCodeUrl("http://127.0.0.1:9999/erweima.jpg");
        return new DemoJsonResult(paymentInfoVo);
    }

    /**
     * 模拟向商户提交订单接口
     * @param servletResponse
     * @return
     * @throws IOException
     */
    @PostMapping("/foodie-payment/payment/createMerchantOrder")
    public DemoJsonResult createMerchantOrder(@RequestBody MerchantOrdersVO merchantOrdersVO) throws JSONException, InterruptedException {
        // 支付中心回调客户，支付成功接口
        Thread.sleep(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> map = new HashMap<>();
        map.put("merchantOrderId", merchantOrdersVO.getMerchantOrderId());
        HttpEntity<Map> entity=new HttpEntity(map,headers);
        ResponseEntity<DemoJsonResult> responseEntity =
                restTemplate.postForEntity("http://127.0.0.1:8088/orders/notifyMerchantOrderPaid", entity, DemoJsonResult.class);

        return DemoJsonResult.ok();
    }


    /**
     * 模拟服务端提供查询支付结果
     * @param orderId
     * @return
     */
    @PostMapping("/orders/getPaidOrderInfo")
    public DemoJsonResult getPayInfo(String orderId){
        count++;
        if(count % 3 == 0) {
            Map<String,Object> result = new HashMap<>();
            result.put("orderStatus",20);
            return DemoJsonResult.ok(result);
        }
        return DemoJsonResult.errorMsg("not ok");
    }
}

