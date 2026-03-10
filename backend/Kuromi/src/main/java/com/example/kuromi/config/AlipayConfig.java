package com.example.kuromi.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @Value("${alipay.gatewayUrl}")
    private String gatewayUrl;

    @Bean
    public Config alipaySdkConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = gatewayUrl.replace("https://", "").replace("/gateway.do", "");
        config.signType = "RSA2";

        config.appId = appId;
        config.merchantPrivateKey = privateKey;
        config.alipayPublicKey = alipayPublicKey;

        // 可选，设置异步通知地址（沙箱可省略）
        // config.notifyUrl = "http://你的域名/api/order/alipayNotify";

        Factory.setOptions(config);
        System.out.println("支付宝沙箱SDK初始化成功！");

        return config;
    }
}
