package com.loanapp.integration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class ConfigurationProviderAdapter implements ProviderAdapter {
 private final Environment env;
 public ConfigurationProviderAdapter(Environment env){this.env=env;}
 public ProviderType type(){return ProviderType.ACCOUNTING;}
 public boolean configured(){return env.getProperty("app.accounting.enabled",Boolean.class,false);}
 public Map<String,Object> health(){return Map.of("provider",type().name(),"configured",configured(),"mode",configured()?"READY":"NOT_CONFIGURED");}
}
