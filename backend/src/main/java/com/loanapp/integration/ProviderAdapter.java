package com.loanapp.integration;
import java.util.Map;
public interface ProviderAdapter {
 ProviderType type();
 boolean configured();
 Map<String,Object> health();
}
