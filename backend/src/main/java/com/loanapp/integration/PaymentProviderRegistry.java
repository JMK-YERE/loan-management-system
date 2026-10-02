package com.loanapp.integration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PaymentProviderRegistry {
    private final Environment env;
    public PaymentProviderRegistry(Environment env){this.env=env;}

    public List<Map<String,Object>> status(){
        return List.of(
            status("MPESA","app.payment.mpesa.enabled","app.payment.mpesa.base-url"),
            status("AIRTEL_MONEY","app.payment.airtel.enabled","app.payment.airtel.base-url"),
            status("MIXX_BY_YAS","app.payment.mixx.enabled","app.payment.mixx.base-url"),
            status("HALOPESA","app.payment.halopesa.enabled","app.payment.halopesa.base-url"),
            status("TANQR","app.payment.tanqr.enabled","app.payment.tanqr.base-url"),
            status("TIPS","app.payment.tips.enabled","app.payment.tips.base-url"),
            status("TISS","app.payment.tiss.enabled","app.payment.tiss.base-url"),
            status("BANK_TRANSFER","app.payment.bank.enabled","app.payment.bank.base-url")
        );
    }

    private Map<String,Object> status(String provider,String enabledKey,String urlKey){
        boolean enabled=env.getProperty(enabledKey,Boolean.class,false);
        String url=env.getProperty(urlKey,"");
        return Map.of("provider",provider,"enabled",enabled,"configured",enabled&&!url.isBlank(),"baseUrlConfigured",!url.isBlank());
    }
}