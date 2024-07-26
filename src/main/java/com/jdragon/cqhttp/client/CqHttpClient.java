package com.jdragon.cqhttp.client;

import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import com.jdragon.cqhttp.entity.SendGroupMsg;
import com.jdragon.cqhttp.entity.SendPrivateMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "messageClient", url = "http://localhost:3000")
public interface CqHttpClient {

    @PostMapping("/send_private_msg")
    String sendPrivateMsg(@RequestBody SendPrivateMsg request);

    @PostMapping("/send_group_msg")
    String sendGroupMsg(@RequestBody SendGroupMsg request);

    @PostMapping("/get_group_member_list")
    CqResult<List<GroupMember>> getGroupMemberList(@RequestBody Map<String, Object> request);

}
