package com.jdragon.cqhttp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CqResult<T> {

    private String status;

    private Integer retcode;

    private T data;

    private String message;

    private String wording;

    private Object echo;

}
