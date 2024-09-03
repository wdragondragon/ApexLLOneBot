package com.jdragon.apex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.vo.TodayBanStatic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgBanHistoryMapper extends BaseMapper<AgBanHistory> {

    List<TodayBanStatic> todayBanStatic();

    List<AgBanHistory> todayBan();
}
