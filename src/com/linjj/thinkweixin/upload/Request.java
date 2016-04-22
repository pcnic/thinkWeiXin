// Source File Name:   Request.java
/**
 * 这个类的功能等同于JSP内置的对象request。只所以提供这个类，是因为对于文件上传表单，
 * 通过request对象无法获得表单项的值，必须通过jspSmartUpload组件提供的Request对象来获取。
 */
package com.linjj.thinkweixin.upload;

import java.util.Enumeration;
import java.util.Hashtable;

public class Request
{

    private Hashtable m_parameters;
    private int m_counter;

    Request()
    {
        m_parameters = new Hashtable();
        m_counter = 0;
    }

    public void putParameter(String name, String value)
    {
        if(name == null)
            throw new IllegalArgumentException("The name of an element cannot be null.");
        if(m_parameters.containsKey(name))
        {
            Hashtable values = (Hashtable)m_parameters.get(name);
            values.put(new Integer(values.size()), value);
        }
        else
        {
            Hashtable values = new Hashtable();
            values.put(new Integer(0), value);
            m_parameters.put(name, values);
            m_counter++;
        }
    }
    /**
     * @param name String
     * @param value String
     * 作用：获取指定参数之值。当参数不存在时，返回值为null。
     */
    public String getParameter(String name)
    {
        if(name == null)
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        Hashtable values = (Hashtable)m_parameters.get(name);
        if(values == null)
            return null;
        else
            return (String)values.get(new Integer(0));
    }
    /**
     * @return Enumeration
     * 作用：取得Request对象中所有参数的名字，用于遍历所有参数。它返回的是一个枚举型的对象。
     */
    public Enumeration getParameterNames()
    {
        return m_parameters.keys();
    }
    /**
     * @param name String
     * @return String[]
     * 作用：当一个参数可以有多个值时，用此方法来取其值。它返回的是一个字符串数组。当参数不存在时，返回值为null。
     */
    public String[] getParameterValues(String name)
    {
        if(name == null)
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        Hashtable values = (Hashtable)m_parameters.get(name);
        if(values == null)
            return null;
        String strValues[] = new String[values.size()];
        for(int i = 0; i < values.size(); i++)
            strValues[i] = (String)values.get(new Integer(i));

        return strValues;
    }
}
