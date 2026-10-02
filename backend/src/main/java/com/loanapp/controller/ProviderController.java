package com.loanapp.controller;

import com.loanapp.integration.PaymentProviderRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integrations/providers")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','CEO_DIRECTOR','DIRECTOR')")
public class ProviderController {
    private final PaymentProviderRegistry registry;
    public ProviderController(PaymentProviderRegistry registry){this.registry=registry;}

    @GetMapping("/payments")
    public List<Map<String,Object>> payments(){return registry.status();}
}