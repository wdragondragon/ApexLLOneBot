package com.jdragon.apex.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.entity.AgMachinesKeys;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgMachineNewMapper extends BaseMapper<AgMachineNew> {
    List<AgMachinesKeys> getAuthList(@Param("type") String type,
                                     @Param("condition") String condition,
                                     @Param("validateType") String validateType);

}
