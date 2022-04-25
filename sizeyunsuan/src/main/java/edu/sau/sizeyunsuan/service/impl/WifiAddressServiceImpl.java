package edu.sau.sizeyunsuan.service.impl;

import edu.sau.sizeyunsuan.entity.Page;
import edu.sau.sizeyunsuan.entity.PageData;
import edu.sau.sizeyunsuan.entity.wifiAddress;
import edu.sau.sizeyunsuan.mapper.WifiAddressMapper;
import edu.sau.sizeyunsuan.service.wifiAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WifiAddressServiceImpl extends ServiceImpl<WifiAddressMapper, wifiAddress> implements wifiAddressService {

    @Autowired
    private WifiAddressMapper wifiAddressMapper;


    @Override
    public void saveStudentAddress(String uid, String created, String xuegonghao, String xingming, String beizhu, String address, String id) {
        wifiAddressMapper.saveStudentAddress(uid, created, xuegonghao, xingming, beizhu, address, id);
    }

    @Override
    public void saveAddress(String uid, String address) {
        wifiAddressMapper.saveAddress(uid, address);
    }

    @Override
    public int findStudentNum(String id) {
        return wifiAddressMapper.findStudentNum(id);
    }

    @Override
    public void updateStudentAddress(String created, String xuegonghao, String xingming, String beizhu, String address, String id) {
        wifiAddressMapper.updateStudentAddress(created, xuegonghao, xingming, beizhu, address, id);
    }

    @Override
    public PageData findStudentById(String id) {
        return wifiAddressMapper.findStudentById(id);
    }

    @Override
    public List<PageData> listByStudentInfo(Page page) {
        return wifiAddressMapper.datalistPage(page);
    }

    @Override
    public int studentNumByStudentInfo(Page page) {
        return wifiAddressMapper.studentNumByStudentInfo(page);
    }

    @Override
    public int findAddressNum(String address) {
        return wifiAddressMapper.findAddressNum(address);
    }
}
