package com.loanapp.service;

import com.loanapp.model.User; import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import java.net.URI; import java.net.http.*; import java.util.Map;

@Service
public class KycService{
 private final UserRepository users;
 @Value("${app.nida.verification-url:}") private String url;
 @Value("${app.nida.api-key:}") private String apiKey;
 public KycService(UserRepository users){this.users=users;}
 public Map<String,Object> verify(Long userId){
  User u=users.findById(userId).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  if(url.isBlank()||apiKey.isBlank()) return Map.of("status","NOT_CONFIGURED","message","NIDA stakeholder verification credentials hazijawekwa");
  try{
   String json="{\"nin\":\""+escape(u.getNidaNumber())+"\"}";
   HttpRequest req=HttpRequest.newBuilder(URI.create(url)).header("Authorization","Bearer "+apiKey).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
   HttpResponse<String> res=HttpClient.newHttpClient().send(req,HttpResponse.BodyHandlers.ofString());
   return Map.of("status",res.statusCode()<300?"VERIFIED":"FAILED","httpStatus",res.statusCode(),"response",res.body());
  }catch(Exception e){return Map.of("status","ERROR","message","NIDA verification request failed");}
 }
 private String escape(String s){return s==null?"":s.replace("\\","\\\\").replace("\"","\\\"");}
}
