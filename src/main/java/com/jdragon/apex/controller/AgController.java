package com.jdragon.apex.controller;


import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.entity.AgUser;
import com.jdragon.apex.service.AgKeysService;
import com.jdragon.apex.service.AgMachineKeysService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ag")
public class AgController {


    private final AgKeysService agKeysService;

    private final AgMachineKeysService agMachineKeysService;

    public AgController(AgKeysService agKeysService, AgMachineKeysService agMachineKeysService) {
        this.agKeysService = agKeysService;
        this.agMachineKeysService = agMachineKeysService;
    }

    @Operation(summary = "申请体验keys并绑定qq，同一类型不会重新生成")
    @GetMapping("/createExperienceCardByQQ")
    public String createExperienceCardByQQ(@RequestParam String qq,
                                           @RequestParam String createGroup,
                                           @RequestParam String validateType) {
        return agMachineKeysService.createExperienceCardByQQ(qq, createGroup, validateType);
    }

    @Operation(summary = "创建域外卡")
    @GetMapping("/createKeyExt")
    public String createKeyExt(@RequestParam Integer createNumber,
                               @RequestParam String keyType,
                               @RequestParam String validateType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AgUser agUser = (AgUser) authentication.getPrincipal();
        return Strings.join(agKeysService.createKeysExt(createNumber, keyType, validateType, agUser.getId()), '\n');
    }


    @Operation(summary = "创建域内卡")
    @GetMapping("/createKey")
    public String createKey(@RequestParam String qq,
                            @RequestParam String keyType,
                            @RequestParam String createGroup,
                            @RequestParam String validateType) {
        return agKeysService.createKey(qq, keyType, validateType, createGroup);
    }

    @Operation(summary = "key绑定机器码，同一类型未过期时，不能重复绑定")
    @GetMapping("/machineBindKeys")
    public String createKeyExt(@RequestParam String key,
                               @RequestParam String machineCode) {
        return agMachineKeysService.bind(machineCode, key);
    }

    @Operation(summary = "校验机器码类型key是否过期")
    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestParam String machineCode,
                                        @RequestParam String validateType) {
        AgMachinesKeys agMachinesKeys = agMachineKeysService.validate(machineCode, validateType);
        Map<String, Object> map = new HashMap<>();
        map.put("machine_code", machineCode);
        map.put("validate_type", validateType);
        map.put("access_granted", agMachinesKeys != null);
        map.put("expiration_time", agMachinesKeys == null ? null :
                agMachinesKeys.getExpirationTime() == null ? null :
                        agMachinesKeys.getExpirationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return map;
    }
}
