package com.tingxia.service;

import com.tingxia.dao.CategorieMapper;
import com.tingxia.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategorieMapper categorieMapper;

    public List<Category> getCategorie(){
        return categorieMapper.selectAll(null,0);
    }

    @Transactional
    public Category create(Integer parentId, String name) {
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setSequence(0);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());
        if(categorieMapper.insert(category)>0){
            return category;
        }
        return null;
    }

    public Category update(Integer id, Integer parentId, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParentId(parentId);
        category.setSequence(0);
        category.setUpdatedAt(new Date());
        if(categorieMapper.updateByPrimaryKeySelective(category)>0){
            return category;
        }
        return null;
    }
}
