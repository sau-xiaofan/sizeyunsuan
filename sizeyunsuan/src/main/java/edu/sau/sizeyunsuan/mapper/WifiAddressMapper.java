package edu.sau.sizeyunsuan.mapper;

import edu.sau.sizeyunsuan.entity.Page;
import edu.sau.sizeyunsuan.entity.PageData;
import edu.sau.sizeyunsuan.entity.wifiAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WifiAddressMapper extends BaseMapper<wifiAddress> {

    void saveStudentAddress(String uid, String created, String xuegonghao, String xingming, String beizhu, String address, String id);

    int findAddressNum(String address);

    void saveAddress(String uid, String address);

    int findStudentNum(String id);

    void updateStudentAddress(String created, String xuegonghao, String xingming, String beizhu, String address, String id);

    PageData findStudentById(String id);

    List<PageData> datalistPage(Page page);

    int studentNumByStudentInfo(Page page);
}
