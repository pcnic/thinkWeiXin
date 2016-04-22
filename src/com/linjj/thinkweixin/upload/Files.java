// Source File Name:   Files.java
/**
 * 这个类表示所有上传文件的集合，通过它可以得到上传文件的数目、大小等信息。
 */
package com.linjj.thinkweixin.upload;

import java.io.IOException;
import java.util.*;

// Referenced classes of package com.jspsmart.upload:
//            File, SmartUpload

public class Files
{

    private SmartUpload m_parent;
    private Hashtable m_files;
    private int m_counter;

    Files()
    {
        m_files = new Hashtable();
        m_counter = 0;
    }
    //添加文件列表
    public void addFile(File newFile)
    {
        if(newFile == null)
        {
            throw new IllegalArgumentException("newFile cannot be null.");
        } else
        {
            m_files.put(new Integer(m_counter), newFile);
            m_counter++;
            return;
        }
    }
    /**
     * @param index int
     * @return File
     * 作用：取得指定位移处的文件对象File（这是com.jspsmart.upload.File，不是java.io.File，注意区分）。
     */
    public File getFile(int index)
    {
        if(index < 0)
            throw new IllegalArgumentException("File's index cannot be a negative value (1210).");
        File retval = (File)m_files.get(new Integer(index));
        if(retval == null)
            throw new IllegalArgumentException("Files' name is invalid or does not exist (1205).");
        else
            return retval;
    }
    /**
     * @return int
     * 作用：取得上传文件的数目。
     */
    public int getCount()
    {
        return m_counter;
    }
    //得到总文件的大小
    /**
     * @throws IOException
     * @return long
     * 作用：取得上传文件的总长度，可用于限制一次性上传的数据量大小。
     */
    public long getSize()
        throws IOException
    {
        long tmp = 0L;
        for(int i = 0; i < m_counter; i++)
            tmp += getFile(i).getSize();

        return tmp;
    }
    /**
     * @return Collection
     * 作用：将所有上传文件对象以Collection的形式返回，以便其它应用程序引用，浏览上传文件信息。
     */
    public Collection getCollection()
    {
        return m_files.values();
    }
    /**
     * @return Enumeration
     * 作用：将所有上传文件对象以Enumeration（枚举）的形式返回，以便其它应用程序浏览上传文件信息。
     */
    public Enumeration getEnumeration()
    {
        return m_files.elements();
    }
}
