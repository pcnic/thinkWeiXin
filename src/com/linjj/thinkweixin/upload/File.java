// Source File Name:   File.java
 /**
  *这个类包装了一个上传文件的所有信息。通过它，可以得到上传文件的
  *文件名、文件大小、扩展名、文件数据等信息。
  */

package com.linjj.thinkweixin.upload;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;

import com.linjj.thinkweixin.util.ExtString;

// Referenced classes of package com.jspsmart.upload:
//            SmartUploadException, SmartUpload

public class File
{
    //上传组件
    private SmartUpload m_parent;
    //文件内容开始位置
    private int m_startData;
    //文件内容结束位置
    private int m_endData;
    //文件大小
    private int m_size;
    private String m_fieldname;
    private String m_filename;
    private String m_fileExt;
    private String m_filePathName;
    private String m_contentType;
    private String m_contentDisp;
    private String m_typeMime;
    private String m_subTypeMime;
    private String m_contentString;
    private boolean m_isMissing;
    public static final int SAVEAS_AUTO = 0;
    public static final int SAVEAS_VIRTUAL = 1;
    public static final int SAVEAS_PHYSICAL = 2;

    public File()
    {
        m_startData = 0;
        m_endData = 0;
        m_size = 0;
        m_fieldname = new String();
        m_filename = new String();
        m_fileExt = new String();
        m_filePathName = new String();
        m_contentType = new String();
        m_contentDisp = new String();
        m_typeMime = new String();
        m_subTypeMime = new String();
        m_contentString = new String();
        m_isMissing = true;
    }

    /**
     * @param destFilePathName String
     * @throws SmartUploadException
     * @throws IOException
     *
     * 其中，destFilePathName是另存的文件名，optionSaveAs是另存的选项，该选项有三个值，
     * 分别是SAVEAS_PHYSICAL,SAVEAS_VIRTUAL，SAVEAS_AUTO。
     * SAVEAS_PHYSICAL表明以操作系统的根目录为文件根目录另存文件，
     * SAVEAS_VIRTUAL表明以Web应用程序的根目录为文件根目录另存文件，
     * SAVEAS_AUTO则表示让组件决定，当Web应用程序的根目录存在另存文件的目录时，
     * 它会选择SAVEAS_VIRTUAL，否则会选择SAVEAS_PHYSICAL。
     * 建议：对于Web程序的开发来说，最好使用SAVEAS_VIRTUAL，以便移植。
     */

    //保存文件
    public void saveAs(String destFilePathName)
        throws SmartUploadException, IOException
    {
    	destFilePathName=ExtString.toChineseUTF8(destFilePathName); 
        saveAs(destFilePathName, 0);
    }
    //保存文件
    public void saveAs(String destFilePathName, int optionSaveAs)
        throws SmartUploadException, IOException
    {
        String path = new String();
        //得到物理路径
        destFilePathName=ExtString.toChineseUTF8(destFilePathName); 
        path = m_parent.getPhysicalPath(destFilePathName, optionSaveAs);
        if(path == null)
            throw new IllegalArgumentException("There is no specified destination file (1140).");
        try
        {
            java.io.File file = new java.io.File(path);
            file.createNewFile();
            try
            {
                FileOutputStream fileOut = new FileOutputStream(file);
                fileOut.write(m_parent.m_binArray, m_startData, m_size);
                fileOut.flush();
                fileOut.close();
            }
            catch(Exception e)
            {
                throw new IllegalArgumentException("Path's name is invalid or does not exist (1135).");
            }
        }
        catch(IOException e)
        {
            throw new SmartUploadException("File can't be saved (1120).");
        }
    }
    //保存到数据表里的字段
    public void fileToField(ResultSet rs, String columnName)
        throws SQLException, SmartUploadException, IOException, ServletException
    {
        long numBlocks = 0L;
        int blockSize = 0x10000;
        int leftOver = 0;
        int pos = 0;
        columnName=ExtString.toChineseUTF8(columnName); 
        if(rs == null)
            throw new IllegalArgumentException("The RecordSet cannot be null (1145).");
        if(columnName == null)
            throw new IllegalArgumentException("The columnName cannot be null (1150).");
        if(columnName.length() == 0)
            throw new IllegalArgumentException("The columnName cannot be empty (1155).");
        //计算块的个数
        numBlocks = BigInteger.valueOf(m_size).divide(BigInteger.valueOf(blockSize)).longValue();
        //取余数
        leftOver = BigInteger.valueOf(m_size).mod(BigInteger.valueOf(blockSize)).intValue();
        //---------pos初始是否不对------
        try
        {
            for(int i = 1; (long)i < numBlocks; i++)
            {
                rs.updateBinaryStream(columnName, new ByteArrayInputStream(m_parent.m_binArray, pos, blockSize), blockSize);
                pos = pos != 0 ? pos : 1;
                pos = i * blockSize;
            }

            if(leftOver > 0)
                rs.updateBinaryStream(columnName, new ByteArrayInputStream(m_parent.m_binArray, pos, leftOver), leftOver);
        }
        catch(SQLException e)
        {
            byte binByte2[] = new byte[m_size];
            System.arraycopy(m_parent.m_binArray, m_startData, binByte2, 0, m_size);
            rs.updateBytes(columnName, binByte2);
        }
        catch(Exception e)
        {
            throw new SmartUploadException("Unable to save file in the DataBase (1130).");
        }
    }
    /**
     * @return boolean
     * 这个方法用于判断用户是否选择了文件，也即对应的表单项是否有值。选择了文件时，它返回false。
     * 未选文件时，它返回true。
     */
    public boolean isMissing()
    {
        return m_isMissing;
    }
    /**
     * @return String
     * 取HTML表单中对应于此上传文件的表单项的名字。
     */
    public String getFieldName()
    {
    	 m_fieldname=ExtString.toChineseUTF8(m_fieldname)+" ";
        return m_fieldname;
    }
    /**
     * @return String
     * 取文件名（不含目录信息）
     */
    public String getFileName()
    { 
    	 m_fieldname=ExtString.toChineseUTF8(m_fieldname); 
        return m_filename;
    }
    /**
     * @return String
     * 作用：取文件全名（带目录）
     */
    public String getFilePathName()
    {
    	m_filePathName=ExtString.toChineseUTF8(m_filePathName)+" "; 
        return m_filePathName;
    }
    /**
     * @return String
     * 作用：取文件扩展名（后缀）
     */
    public String getFileExt()
    {
    	m_fileExt=ExtString.toChineseUTF8(m_fileExt)+" "; 
        return m_fileExt;
    }

    public String getContentType()
    {
    	m_contentType=ExtString.toChineseUTF8(m_contentType)+" "; 
        return m_contentType;
    }

    public String getContentDisp()
    {
    	m_contentDisp=ExtString.toChineseUTF8(m_contentDisp)+" "; 
        return m_contentDisp;
    }
    //得到文件流
    public String getContentString()
    {
        //String strTMP = new String(m_parent.m_binArray, m_startData, m_size);
        //2009-10-28 WTJ 增加字符处理,汉字处理
    	//2010-9-27增加字符处理,汉字处理
        String strTMP="";
        try
       {
        strTMP = new String(m_parent.m_binArray,m_startData,m_size,"GBK");
       }
       catch(Exception e)
       {
         strTMP = "";
       }
        return strTMP;
    }

    public String getTypeMIME()
        throws IOException
    {
        return m_typeMime;
    }

    public String getSubTypeMIME()
    {
        return m_subTypeMime;
    }
    /**
     * @return int
     * 作用：取文件长度（以字节计）
     */
    public int getSize()
    {
        return m_size;
    }

    protected int getStartData()
    {
        return m_startData;
    }

    protected int getEndData()
    {
        return m_endData;
    }

    public void setParent(SmartUpload parent)
    {
        m_parent = parent;
    }

    public void setStartData(int startData)
    {
        m_startData = startData;
    }

    public void setEndData(int endData)
    {
        m_endData = endData;
    }

    public void setSize(int size)
    {
    	
        m_size = size;
    }

    public void setIsMissing(boolean isMissing)
    {
        m_isMissing = isMissing;
    }

    public void setFieldName(String fieldName)
    {
    	fieldName=ExtString.toChineseUTF8(fieldName)+" "; 
        m_fieldname = fieldName;
    }

    public void setFileName(String fileName)
    {
    	fileName=ExtString.toChineseUTF8(fileName)+" "; 
        m_filename = fileName;
    }

    public void setFilePathName(String filePathName)
    {
    	filePathName=ExtString.toChineseUTF8(filePathName)+" "; 
        m_filePathName = filePathName;
    }

    public void setFileExt(String fileExt)
    {
    	fileExt=ExtString.toChineseUTF8(fileExt)+" "; 
        m_fileExt = fileExt;
    }

    public void setContentType(String contentType)
    {
    	contentType=ExtString.toChineseUTF8(contentType)+" "; 
        m_contentType = contentType;
    }

    public void setContentDisp(String contentDisp)
    {
    	contentDisp=ExtString.toChineseUTF8(contentDisp)+" ";
        m_contentDisp = contentDisp;
    }

    public void setTypeMIME(String TypeMime)
    {
    	m_typeMime=ExtString.toChineseUTF8(m_typeMime)+" ";
        m_typeMime = TypeMime;
    }

    public void setSubTypeMIME(String subTypeMime)
    {
    	subTypeMime=ExtString.toChineseUTF8(subTypeMime)+" ";
        m_subTypeMime = subTypeMime;
    }
    //得到字符流的字节
    /**
     * @param index int
     * @return byte
     * 作用：取文件数据中指定位移处的一个字节，用于检测文件等处理。
     */
    public byte getBinaryData(int index)
    {
        if(m_startData + index > m_endData)
            throw new ArrayIndexOutOfBoundsException("Index Out of range (1115).");
        if(m_startData + index <= m_endData)
            return m_parent.m_binArray[m_startData + index];
        else
            return 0;
    }
}
