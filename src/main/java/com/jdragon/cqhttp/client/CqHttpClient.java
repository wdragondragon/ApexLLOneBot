package com.jdragon.cqhttp.client;

import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "messageClient", url = "http://localhost:3000")
public interface CqHttpClient {

    @PostMapping("/send_private_msg")
    void sendPrivateMsg(@RequestBody Map<String, Object> request);

    @PostMapping("/send_group_msg")
    void sendGroupMsg(@RequestBody Map<String, Object> request);

    @PostMapping("/get_group_member_list")
    CqResult<List<GroupMember>> getGroupMemberList(@RequestBody Map<String, Object> request);

}
