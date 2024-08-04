package com.jdragon.apex.client;

import com.jdragon.apex.config.ProxyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apexStatusClient", url = "https://apexlegendsstatus.com", configuration = ProxyFeignConfig.class)
public interface ApexStatusClient {

    @GetMapping("/profile/{platform}/{uid}")
    String profile(@PathVariable("platform") String platform,
                   @PathVariable("uid") String uid);

    @GetMapping("/core/interface")
    String coreInterface(@RequestParam("token") String token,
                         @RequestParam("platform") String platform,
                         @RequestParam("uid") String uid,
                         @RequestHeader("Cookie") String cookie);

    @GetMapping("/core/interface")
    String coreInterfaceByName(@RequestParam("token") String token,
                         @RequestParam("platform") String platform,
                         @RequestParam("player") String player,
                         @RequestHeader("Cookie") String cookie);
}
