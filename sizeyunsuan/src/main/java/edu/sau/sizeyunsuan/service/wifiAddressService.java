package edu.sau.sizeyunsuan.service;

import edu.sau.sizeyunsuan.entity.Page;
import edu.sau.sizeyunsuan.entity.PageData;
import edu.sau.sizeyunsuan.entity.wifiAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface
wifiAddressService extends IService<wifiAddress> {

    int findAddressNum(String address);

    void saveStudentAddress(String uid, String created, String xuegonghao, String xingming, String beizhu, String address, String id);

    void saveAddress(String uid, String address);

    int findStudentNum(String id);

    void updateStudentAddress(String created, String xuegonghao, String xingming, String beizhu, String address, String id);

    PageData findStudentById(String id);

    List<PageData> listByStudentInfo(Page page);

    int studentNumByStudentInfo(Page page);
}
