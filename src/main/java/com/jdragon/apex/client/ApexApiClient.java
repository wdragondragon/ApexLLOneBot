package com.jdragon.apex.client;

import com.jdragon.apex.config.ApexApiFeignConfig;
import com.jdragon.apex.config.ProxyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apexApiClient", url = "https://api.mozambiquehe.re", configuration = {ProxyFeignConfig.class, ApexApiFeignConfig.class})
public interface ApexApiClient {

    @GetMapping("/bridge")
    String bridge(@RequestParam("platform") String platform, @RequestParam("player") String player);

}
