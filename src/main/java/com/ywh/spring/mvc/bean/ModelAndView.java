package com.ywh.spring.mvc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ywh
 * @since 4/20/2021
 */
public class ModelAndView {

    /**
     * 页面路径
     */
    private String view;

    /**
     * 页面data数据
     */
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public String getView() {
        return view;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public ModelAndView addAllObjects(Map<String, ?> modelMap) {
        model.putAll(modelMap);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }


}
