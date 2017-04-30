package com.krishan.balaji.fh.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CategoryModel {
    //public static final Map<String,CategoryModel> map = new HashMap<>();

    private long id;
    private String name;
    private Map<String,SubCategoryModel> subcategories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,SubCategoryModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Map<String,SubCategoryModel> subcategories) {
        this.subcategories = subcategories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
