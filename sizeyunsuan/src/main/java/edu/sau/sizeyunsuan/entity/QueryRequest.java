package edu.sau.sizeyunsuan.entity;

import lombok.Data;


@Data
public class QueryRequest {

    // 当前页面数据量
    private int pageSize = 10;
    // 当前页码
    private int pageNum = 1;
    // 排序字段
    private String field;
    // 排序规则，asc升序，desc降序
    private String order;
}
