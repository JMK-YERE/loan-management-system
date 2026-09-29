package com.loanapp.service;

import com.loanapp.model.Payment; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import java.net.URI; import java.net.http.*; import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.util.*;

@Service
public class MobileMoneyGatewayService{
 @Value("${app.payments.provider:DISABLED}") private String provider;
 @Value("${app.payments.base-url:}") private String baseUrl;
 @Value("${app.payments.api-key:}") private String apiKey;
 @Value("${app.payments.api-secret:}") private String apiSecret;
 @Value("${app.payments.merchant-id:}") private String merchantId;
 public Map<String,Object> initiate(Payment p,String phone){
  if(baseUrl.isBlank()||apiKey.isBlank()) return Map.of("status","NOT_CONFIGURED","provider",provider,"message","Payment provider credentials hazijawekwa Render.");
  try{
   String body="{"merchantId":""+esc(merchantId)+"","amount":"+p.getAmount()+","phone":""+esc(phone)+"","reference":""+esc(p.getTransactionId())+"","provider":""+esc(p.getPaymentMethod().name())+"","currency":"TZS"}";
   String signature=sha256(body+apiSecret);
   HttpRequest req=HttpRequest.newBuilder(URI.create(baseUrl)).header("Content-Type","application/json").header("X-API-Key",apiKey).header("X-Signature",signature).POST(HttpRequest.BodyPublishers.ofString(body)).build();
   HttpResponse<String> res=HttpClient.newHttpClient().send(req,HttpResponse.BodyHandlers.ofString());
   return Map.of("status",res.statusCode()<300?"INITIATED":"FAILED","httpStatus",res.statusCode(),"provider",provider,"response",res.body());
  }catch(Exception e){return Map.of("status","ERROR","provider",provider,"message","Gateway request failed");}
 }
 public boolean configured(){return !baseUrl.isBlank()&&!apiKey.isBlank()&&!apiSecret.isBlank();}
 private String sha256(String s)throws Exception{byte[] d=MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));StringBuilder b=new StringBuilder();for(byte x:d)b.append(String.format("%02x",x));return b.toString();}
 private String esc(String s){return s==null?"":s.replace("\\","\\\\").replace(""","\\"");}
}
