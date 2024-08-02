package com.jdragon.apex.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.mapper.AgBanHistoryMapper;
import com.jdragon.cqhttp.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AgBanHistoryService extends ServiceImpl<AgBanHistoryMapper, AgBanHistory> {

    private final DiscordService discordService;

    private final MessageService messageService;

    private final AgCareBanService agCareBanService;

    public AgBanHistoryService(DiscordService discordService, MessageService messageService, AgCareBanService agCareBanService) {
        this.discordService = discordService;
        this.messageService = messageService;
        this.agCareBanService = agCareBanService;
    }

    public void refreshBanHistory() {
        List<DiscordMessage> discordMessages = discordService.getDiscordMessages("1171120349698592859", 10);
        for (DiscordMessage discordMessage : discordMessages) {
            List<DiscordMessage.Embed> embeds = discordMessage.getEmbeds();
            Optional<DiscordMessage.Embed> rich = embeds.stream().filter(embed -> embed.getType().equals("rich")).findFirst();
            if (rich.isEmpty()) {
                continue;
            }
            DiscordMessage.Embed richEmbed = rich.get();
            String description = richEmbed.getDescription();
            String[] descSplit = description.split("\\n+");
            List<String> descList = new ArrayList<>();
            for (String desc : descSplit) {
                String s = desc.replaceAll("\\*", "");
                descList.add(s);
            }
            String link = descList.get(0);
            String uid = descList.get(1);
            String desc = descList.get(2);
            String msg = descList.get(3);
            AgBanHistory agBanHistory = queryBanHistoryByUid(uid);
            if (agBanHistory == null) {
                agBanHistory = new AgBanHistory();
                agBanHistory.setUid(uid);
                agBanHistory.setDescMsg(desc);
                agBanHistory.setMsg(msg);
                agBanHistory.setLink(link);
                agBanHistory.setBanDate(discordMessage.getTimestamp());
                Matcher matcher = Pattern.compile("(Master|Apex Predator) @\\d+ #(\\d+)").matcher(desc);
                if (matcher.find()) {
                    String rank = matcher.group(1);
                    String number = matcher.group(2);
                    agBanHistory.setRankRole(rank);
                    agBanHistory.setRankRange(number);
                }
                agBanHistory.setUsername(msg.replaceAll(" has been banned", ""));
                this.saveOrUpdate(agBanHistory);
                messageService.sendGroupMsg(null, 206666041L, agBanHistory.toString());
                agCareBanService.sendCareMessage(agBanHistory);
            }
        }
    }

    public AgBanHistory queryBanHistoryByUid(String uid) {
        return getOne(new LambdaQueryWrapper<AgBanHistory>().eq(AgBanHistory::getUid, uid));
    }

}
