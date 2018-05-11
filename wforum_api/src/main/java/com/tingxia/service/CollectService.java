package com.tingxia.service;

import com.github.pagehelper.PageHelper;
import com.tingxia.dao.*;
import com.tingxia.pojo.Article;
import com.tingxia.pojo.Collect;
import com.tingxia.pojo.Folder;
import com.tingxia.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectService {
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;

    public Boolean isCollect(Integer articleId,Integer userId){
        int i = collectMapper.checkExist(articleId,userId);
        return i>0;
    }

    @Transactional
    public Collect addCollect(Integer articleId,Integer folderId,Integer userId){
        Article article = articleMapper.selectByPrimaryKey(articleId);
        Folder folder = folderMapper.selectByPrimaryKey(folderId);

        if(article !=null && folder !=null){
            Boolean isCollect = collectMapper.selectIsCollect(articleId,userId) > 0;
            if(isCollect){return null;}
            Collect collect = new Collect();
            Date date = new Date();
            collect.setCreatedAt(date);
            collect.setUpdatedAt(date);
            collect.setUserId(userId);
            collect.setFolderId(folderId);
            collect.setArticleId(articleId);
            Integer id = collectMapper.insert(collect);
            if(id>0){
                id = collect.getId();
                //增加文章收藏数
                articleMapper.changCount(articleId,1,1);
                //增加用户收藏数
                userMapper.changCount(userId,1,1);
                if (!userId.equals(article.getUserId())) {
                    //添加消息
                    Message message = new Message();
                    message.setArticleId(articleId);
                    message.setFromUserId(userId);
                    message.setToUserId(article.getUserId());
                    message.setReaded(false);
                    message.setSourceId(id);
                    message.setSourceName("collect");
                    message.setCreatedAt(new Date());
                    message.setUpdatedAt(new Date());
                    message.setType("collect");
                    messageMapper.insert(message);
                }
            }
            return collectMapper.selectByPrimaryKey(id);
        }
        return null;
    }
    public List<Folder> getUserFolder(Integer userId){
        return folderMapper.selectByUserId(userId);
    }
    public List<Folder> getFoldersWithCollect(Integer userId){
        return folderMapper.selectFoldersWithCollects(userId);
    }
    public Folder getFolderWithCollect(Integer folderId,Integer userId){
        return folderMapper.selectWithCollects(folderId,userId);
    }
    public Folder getFolder(Integer folderId,Integer userId){
        Folder folder = folderMapper.selectByPrimaryKey(folderId);
        if(folder.getUserId().equals(userId)){
            return folder;
        }
        return null;
    }
    public List<Collect> getFolderCollects(Integer folderId,Integer userId,Integer pageNo,Integer pageSize){
        PageHelper.startPage(pageNo,pageSize);
        Map<String,Integer> p = new HashMap<>();
        p.put("folderId",folderId);
        p.put("userId",userId);
        return collectMapper.selectFolderCollects(p);
    }
    @Transactional
    public Folder addFolder(String name, Integer parentId, Integer userId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setParentId(parentId);
        folder.setUserId(userId);
        folder.setCreatedAt(new Date());
        folder.setUpdatedAt(new Date());
        if(folderMapper.insert(folder)>0){
            return folder;
        }
        return null;
    }
    @Transactional
    public Integer delete(Integer id, Integer userId) {
        Collect collect = collectMapper.selectByPrimaryKey(id);
        if(collect != null) {
            Integer r = collectMapper.deleteByPrimaryKey(id);
            if (r > 0) {
                Integer articleId = collect.getArticleId();
                //减少文章收藏数
                articleMapper.changCount(articleId, -1, 1);
                //减少用户收藏数
                userMapper.changCount(userId, -1, 1);
            }
            return r;
        }
        return 0;
    }
}
