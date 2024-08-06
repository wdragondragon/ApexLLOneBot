package com.jdragon.apex.client;

import com.jdragon.apex.config.ApexApiFeignConfig;
import com.jdragon.apex.config.ProxyFeignConfig;
import com.jdragon.apex.entity.vo.apexapi.ApexUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apexApiClient", url = "https://api.mozambiquehe.re", configuration = {ProxyFeignConfig.class, ApexApiFeignConfig.class})
public interface ApexApiClient {

    @GetMapping("/bridge")
    ApexUser getByName(@RequestParam("platform") String platform, @RequestParam("player") String player);

    @GetMapping("/bridge")
    ApexUser getByUid(@RequestParam("platform") String platform, @RequestParam("uid") String uid);

}
