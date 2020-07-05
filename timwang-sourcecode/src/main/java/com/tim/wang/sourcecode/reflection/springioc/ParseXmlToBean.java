package com.tim.wang.sourcecode.reflection.springioc;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.InputStream;

/**
 * @author wangjun
 * @date 2020-07-04
 */
public class ParseXmlToBean {
    private static String getOneBean(String beanName) {
        return null;
    }

    private static String getFromXml(String xmlPath) {
        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(xmlPath);
        Document document = XmlUtil.readXML(systemResourceAsStream);
        Element rootElement = XmlUtil.getRootElement(document);
        Element bean = XmlUtil.getElement(rootElement, "bean");
        System.out.println(JSONUtil.toJsonStr(bean));
        return null;
    }
}
