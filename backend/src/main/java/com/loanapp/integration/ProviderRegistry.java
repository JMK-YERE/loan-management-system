package com.loanapp.integration;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class ProviderRegistry {
 private final List<ProviderAdapter> adapters;
 public ProviderRegistry(List<ProviderAdapter> adapters){this.adapters=adapters;}
 public List<Map<String,Object>> health(){return adapters.stream().map(ProviderAdapter::health).toList();}
}
