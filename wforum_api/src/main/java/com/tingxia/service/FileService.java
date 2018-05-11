package com.tingxia.service;

import com.tingxia.config.WForumConfig;
import com.tingxia.dao.FileMapper;
import com.tingxia.dao.UserFileMapper;
import com.tingxia.dao.UserMapper;
import com.tingxia.pojo.User;
import com.tingxia.pojo.UserFile;
import com.tingxia.util.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Service
public class FileService {
    @Autowired
    private WForumConfig wForumConfig;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public String saveFile(MultipartFile file,Integer userId){
        // 获取文件md5
        try {
            UserFile userFile = new UserFile();
            userFile.setAuthority(1);
            userFile.setLock(false);
            userFile.setName(file.getName());
            userFile.setNote("用户上传");
            userFile.setUserId(userId);
            userFile.setCreatedAt(new Date());
            userFile.setUpdatedAt(new Date());

            String md5 = DigestUtils.md5Hex(file.getInputStream());
            // 查询文件是否存在
            com.tingxia.pojo.File smFile = fileMapper.selectByMD5(md5);
            // 插入或更新文件状态。
            if(smFile == null){  // 新的
                com.tingxia.pojo.File mFile  = new com.tingxia.pojo.File();
                String filePath = wForumConfig.getBaseFilePath() + "/"+file.getContentType();
                mFile.setSize(file.getSize()+"Bit");
                mFile.setType(file.getContentType());
                mFile.setStatus(1);
                mFile.setMd5(md5);
                mFile.setUserCount(1);
                mFile.setLocalPath(filePath);
                if(fileMapper.insert(mFile)>0){
                    userFile.setFileId(mFile.getId());
                    userFileMapper.insert(userFile);
                }else{
                    return "";
                }
                File file_ = new File(filePath,md5);
                if(!file_.getParentFile().exists()){
                    if(!file_.getParentFile().mkdirs()){
                        return "";
                    }
                }
                file.transferTo(file_);
                return "/files/read/"+mFile.getId();
            }else{ // 存在的
                userFile.setFileId(smFile.getId());
                if(userFileMapper.insert(userFile)>0){
                    fileMapper.changeUserCount(smFile.getId(),1);
                }
                return "/files/read/"+smFile.getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public FileInputStream getFile(Integer id, HttpServletResponse response){
        com.tingxia.pojo.File file = fileMapper.selectByPrimaryKey(id);
        if(file != null && file.getLocalPath() != null && !"".equals(file.getLocalPath())){
            response.setContentType(file.getType());
            return FileUtil.readFile(file.getLocalPath(),file.getMd5());
        }
        return null;
    }

    // -----------用户头像--------------
    @Transactional
    public String saveAvatar(Integer userId, MultipartFile file){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(null != image){
                // 是图片文件,保存图片
                String contentType = file.getContentType();
                String type = contentType.replaceFirst("image/","");
                String fileName = file.getName();
                File file_ = new File(wForumConfig.getBaseFilePath()+"/"+wForumConfig.getAvatarBasePath()+
                        "/"+userId);
                if(!file_.getParentFile().exists()){
                    if(!file_.getParentFile().mkdirs()){
                        return "";
                    }
                }
                file.transferTo(file_);
                String url = "/files/user/avatar/"+type+"/"+userId;
                User user = new User();
                user.setId(userId);
                user.setAvatarUrl(url);
                if(userMapper.updateByPrimaryKeySelective(user)>0){
                    return url;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public FileInputStream getAvatar(Integer userId) {
        return FileUtil.readFile(wForumConfig.getBaseFilePath()+"/"+wForumConfig.getAvatarBasePath(),""+userId);
    }
}
