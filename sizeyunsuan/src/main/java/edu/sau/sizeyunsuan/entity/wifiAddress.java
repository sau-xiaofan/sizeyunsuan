package edu.sau.sizeyunsuan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("wifi_address")
public class wifiAddress {

    /**
     * ID
     */
    @TableId(value = "address_id")
    private String addressId;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

}
