package com.jdragon.apex.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdragon.apex.entity.AgCareBan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgCareBanMapper extends BaseMapper<AgCareBan> {
    List<AgCareBan> queryCareBan(@Param("value") String value);

}
