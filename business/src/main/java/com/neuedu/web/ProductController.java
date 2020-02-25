package com.neuedu.web;

import com.neuedu.common.Consts;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/manage/product/")
public class ProductController {

    @Resource
    IProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }


    /**
     * 图片上传
     * @param file 待上传的文件
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam("pic") MultipartFile file){

        if (file == null){
            return null;
        }

        //1.获取文件名称 xxx.pn
        String fileName = file.getOriginalFilename();
        if (fileName == null){
            return ServerResponse.serverResponseByFail(StatusEnum.UPLOAD_FILENAME_NOT_EMPTY.getStatus(),
                                                       StatusEnum.UPLOAD_FILENAME_NOT_EMPTY.getDesc());
        }
        //2.获取源文件的扩展名
        String ext = fileName.substring(fileName.lastIndexOf("."));
        //3.重新命名，为文件生成一个唯一的名称
        String name = UUID.randomUUID().toString();
        //4.新的文件名
        String newFilename = name+ext;

        //创建保存文件的目录
        File target = new File(uploadPath);
        if (!target.exists()){
            target.mkdirs();
        }

        //5.创建文件
        File newFile = new File(uploadPath,newFilename);

        try {
            //6.将文件写入到磁盘
            file.transferTo(newFile);
            //返回前端
            return ServerResponse.serverResponseBySucess(null,newFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public ServerResponse addOrUpdate(Product product, HttpSession session){
        //1.先判断用户是否登录
        User user = (User)session.getAttribute(Consts.USER);
        /*if (user==null){//未登录
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }*/
        //2.只有管理员权限才能添加类别
        if (user.getRole() != RoleEnum.ADMIN.getRole()){//无管理员权限
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getStatus(),StatusEnum.NO_AUTHORITY.getDesc());
        }

        ServerResponse serverResponse = productService.addorUpdate(product);

        return serverResponse;
    }

}
