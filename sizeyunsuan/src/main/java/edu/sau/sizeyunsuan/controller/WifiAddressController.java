package edu.sau.sizeyunsuan.controller;


import edu.sau.sizeyunsuan.entity.Page;
import edu.sau.sizeyunsuan.entity.PageData;
import edu.sau.sizeyunsuan.service.wifiAddressService;
import edu.sau.sizeyunsuan.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Controller
@RequestMapping("sauwifi")
public class WifiAddressController extends BaseController{

    @Autowired
    private wifiAddressService wifiAddressService;

    public String get32UUID(){
        return UuidUtil.get32UUID();
    }

    /**
     * 保存学生信息
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveStudentAddress")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object addFromExcel(@RequestParam(value = "excel", required = false) MultipartFile file) throws Exception  {
        String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE;                                //文件上传路径
        String fileName = FileUpload.fileUp(file, filePath, "sauWifi学生信息表");
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        String b = fileName.substring(fileName.lastIndexOf("."));
        List<PageData> listPd = new ArrayList<>();
        int updateNum = 0;
        int addNum = 0;
        if(".xls".equals(b)){
            listPd = (List) ObjectExcelRead.readExcelOld(filePath, fileName, 1, 0, 0);
        } else if (".xlsx".equals(b)){
            listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);
        }
        if (listPd.size()>0){
            for(int i = 0; i < listPd.size(); i ++  ) {
                String uid = this.get32UUID();
                String address = listPd.get(i).getString("var12");
                String created = listPd.get(i).getString("var2");
                String xuegonghao = listPd.get(i).getString("var3");
                String xingming = listPd.get(i).getString("var4");
                String beizhu = listPd.get(i).getString("var8");
                String id = listPd.get(i).getString("var13");
                int num = wifiAddressService.findStudentNum(id);
                if(num == 0) {
                    wifiAddressService.saveStudentAddress(uid, created, xuegonghao, xingming, beizhu, address, id);
                    addNum ++;
                } else {
                    wifiAddressService.updateStudentAddress(created, xuegonghao, xingming, beizhu, address, id);
                    updateNum ++;
                }
            }
        }
        map.put("result", errInfo);
        map.put("updateNum", updateNum);
        map.put("addNum", addNum);
        return map;
    }

    /**
     * 保存地址
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveAddress")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object addAddressFromExcel(@RequestParam(value = "excel", required = false) MultipartFile file) throws Exception  {
        String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE;                                //文件上传路径
        String fileName = FileUpload.fileUp(file, filePath, "sauWifi地址表");
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        String b = fileName.substring(fileName.lastIndexOf("."));
        List<PageData> listPd = new ArrayList<>();
        int ans = 0;
        if(".xls".equals(b)){
            listPd = (List) ObjectExcelRead.readExcelOld(filePath, fileName, 1, 0, 0);
        } else if (".xlsx".equals(b)){
            listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);
        }
        if (listPd.size()>0){
            for(int i = 0; i < listPd.size(); i ++  ) {
                String uid = this.get32UUID();
                String address = listPd.get(i).getString("var0");
                if(address == null || address.equals("")) continue;
                int num = wifiAddressService.findAddressNum(address);
                if(num > 0) continue;
                wifiAddressService.saveAddress(uid, address);
                ans ++;
            }
        }
        map.put("result", errInfo);
        map.put("successNum", ans);
        return map;
    }

    /**
     * 寻找问题学生
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findErrorStudent")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object findErrorStudent(@RequestParam(value = "excel", required = false) MultipartFile file) throws Exception  {
        String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE;                                //文件上传路径
        String fileName = FileUpload.fileUp(file, filePath, "sauWifi学生信息表");
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        String b = fileName.substring(fileName.lastIndexOf("."));
        List<PageData> listPd = new ArrayList<>();
        int errorNum = 0;
        List<PageData> errorStudentList = new ArrayList<>();
        if(".xls".equals(b)){
            listPd = (List) ObjectExcelRead.readExcelOld(filePath, fileName, 1, 0, 0);
        } else if (".xlsx".equals(b)){
            listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);
        }
        if (listPd.size()>0){
            for(int i = 0; i < listPd.size(); i ++  ) {
                String address = listPd.get(i).getString("var12");
                String created = listPd.get(i).getString("var2");
                String xuegonghao = listPd.get(i).getString("var3");
                String xingming = listPd.get(i).getString("var4");
                String beizhu = listPd.get(i).getString("var8");
                String id = listPd.get(i).getString("var13");
                int num1 = wifiAddressService.findAddressNum(address);
                PageData student = wifiAddressService.findStudentById(id);
                int num2 = wifiAddressService.findAddressNum(student.getString("address"));
                if(num1 == 0 && num2 == 0) {
                    errorNum ++;
                    errorStudentList.add(student);
                } else {
                    wifiAddressService.updateStudentAddress(created, xuegonghao, xingming, beizhu, address, id);
                }
            }
        }
        map.put("result", errInfo);
        map.put("errorNum", errorNum);
        map.put("errorStudentList", errorStudentList);
        return map;
    }

    /**
     * 查询学生信息
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listStudentInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object listStudentInfo(@RequestBody PageData pd) throws Exception {
        Page page = new Page();
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        String studentName = pd.getString("studentName");                        //关键词检索条件
        if (Tools.notEmpty(studentName)) pd.put("studentName", studentName.trim());
        String studentNum = pd.getString("studentNum");                        //关键词检索条件
        if (Tools.notEmpty(studentNum)) pd.put("studentNum", studentNum.trim());
        String studentId = pd.getString("studentId");                        //关键词检索条件
        if (Tools.notEmpty(studentId)) pd.put("studentId", studentId.trim());
        page.setPd(pd);
        pd.put("currentPage", ((Integer) pd.get("currentPage") - 1) * (Integer) pd.get("showCount"));
        List<PageData> varList = wifiAddressService.listByStudentInfo(page);    //列出学生列表
        int num = wifiAddressService.studentNumByStudentInfo(page);
        page.setTotalResult(num);
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }


}
