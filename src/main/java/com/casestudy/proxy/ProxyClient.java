package com.casestudy.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service",url = "${auth.service.url}")
public interface ProxyClient {
    @GetMapping("/auth/get-roles/{username}")
    String getRolesById(@PathVariable String username);

    @DeleteMapping("/auth/delete/id/{userId}")
    void deleteUser(@PathVariable Long userId);
}
