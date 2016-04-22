// Source File Name:   SmartUpload.java
/**
 * SmartUpload类这个类完成上传下载工作
 *
 * 将源码编译后打包成jspSmartUpload.jar，拷贝到Tomcat的shared/lib目录下（可为所有WEB应用程序所共享），
 * 然后重启Tomcat服务器就可以正常下载含有中文名字的文件了。另，toUtf8String方法也可用于转换含
 * 有中文的超级链接，以保证链接的有效，因为有的WEB服务器不支持中文链接。
 *
 * 注意，执行下载的页面，在Java脚本范围外（即<% ... %>之外），不要包含HTML代码、空格、回车或换行等字符，
 * 有的话将不能正确下载。不信的话，可以在上述源码中%><%之间加入一个换行符，再下载一下，保证出错。
 * 因为它影响了返回给浏览器的数据流，导致解析出错。
 */

package com.linjj.thinkweixin.upload;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

// Referenced classes of package com.jspsmart.upload:
//            Files, Request, SmartUploadException, File

public class SmartUpload
{

    //存放收到的字节内容
    protected byte m_binArray[];
    protected HttpServletRequest m_request;
    protected HttpServletResponse m_response;
    protected ServletContext m_application;
    protected int m_totalBytes;
    //当前指针位置
    protected int m_currentIndex;
    protected int m_startData;
    protected int m_endData;
    //分隔串
    protected String m_boundary;
    protected long m_totalMaxFileSize;
    protected long m_maxFileSize;
    //接受和拒绝字串
    protected Vector m_deniedFilesList;
    protected Vector m_allowedFilesList;

    protected boolean m_denyPhysicalPath;
    protected boolean m_forcePhysicalPath;

    protected String m_contentDisposition;
    public static final int SAVE_AUTO = 0;
    public static final int SAVE_VIRTUAL = 1;
    public static final int SAVE_PHYSICAL = 2;
    protected Files m_files;
    protected Request m_formRequest;

    public SmartUpload()
    {
        m_totalBytes = 0;
        m_currentIndex = 0;
        m_startData = 0;
        m_endData = 0;
        m_boundary = new String();
        m_totalMaxFileSize = 0L;
        m_maxFileSize = 0L;
        m_deniedFilesList = new Vector();
        m_allowedFilesList = new Vector();
        m_denyPhysicalPath = false;
        m_forcePhysicalPath = false;
        m_contentDisposition = new String();
        m_files = new Files();
        m_formRequest = new Request();
    }
    //初始化
    public final void init(ServletConfig config)
        throws ServletException
    {
        m_application = config.getServletContext();
    }
    //初始化
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        m_request = request;
        m_response = response;
    }
    //初始化
    /**
     * @param config ServletConfig
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * 作用：执行上传下载的初始化工作，必须第一个执行。
     */
    public final void initialize(ServletConfig config, HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        m_application = config.getServletContext();
        m_request = request;
        m_response = response;
    }
    //初始化
    public final void initialize(PageContext pageContext)
        throws ServletException
    {
        m_application = pageContext.getServletContext();
        m_request = (HttpServletRequest)pageContext.getRequest();
        m_response = (HttpServletResponse)pageContext.getResponse();
    }
    //初始化
    public final void initialize(ServletContext application, HttpSession session, HttpServletRequest request, HttpServletResponse response, JspWriter out)
        throws ServletException
    {
        m_application = application;
        m_request = request;
        m_response = response;
    }
    //上载文件
    /**
     * @throws SmartUploadException
     * @throws IOException
     * @throws ServletException
     * 作用：上传文件数据。对于上传操作，第一步执行initialize方法，第二步就要执行这个方法。
     */
    public void upload()
        throws SmartUploadException, IOException, ServletException
    {
        int totalRead = 0;
        int readBytes = 0;
        long totalFileSize = 0L;
        boolean found = false;
        String dataHeader = new String();
        String fieldName = new String();
        String fileName = new String();
        String fileExt = new String();
        String filePathName = new String();
        String contentType = new String();
        String contentDisp = new String();
        String typeMIME = new String();
        String subTypeMIME = new String();
        boolean isFile = false;
        //读数据长度(包括一些标志)
        m_totalBytes = m_request.getContentLength();
        m_binArray = new byte[m_totalBytes];
        //读取数据
        for(; totalRead < m_totalBytes; totalRead += readBytes)
        {
            try
            {
                m_request.getInputStream();
                readBytes = m_request.getInputStream().read(m_binArray, totalRead, m_totalBytes - totalRead);
            }
            catch(Exception e)
            {
                throw new SmartUploadException("Unable to upload.");
            }
        }
        //取分隔符串
        for(; !found && m_currentIndex < m_totalBytes; m_currentIndex++)
        {
            if(m_binArray[m_currentIndex] == 13)
                found = true;
            else
                m_boundary = m_boundary + (char)m_binArray[m_currentIndex];
        }

        if(m_currentIndex == 1) return;

        m_currentIndex++;
        //解析内容
        do
        {
            if(m_currentIndex >= m_totalBytes) break;

            dataHeader = getDataHeader();
            m_currentIndex = m_currentIndex + 2;
            //判断是否为文件
            isFile = dataHeader.indexOf("filename") > 0;
            fieldName = getDataFieldValue(dataHeader, "name");
            if(isFile)
            {
                filePathName = getDataFieldValue(dataHeader, "filename");
                fileName = getFileName(filePathName);
                fileExt = getFileExt(fileName);
                contentType = getContentType(dataHeader);
                contentDisp = getContentDisp(dataHeader);
                typeMIME = getTypeMIME(contentType);
                subTypeMIME = getSubTypeMIME(contentType);
            }
            //取文件数据
            getDataSection();
            if(isFile && fileName.length() > 0)
            {
               /*
               if(m_deniedFilesList.contains(fileExt))
                   throw new SecurityException("The extension of the file is denied to be uploaded (1015).");
               if(!m_allowedFilesList.isEmpty() && !m_allowedFilesList.contains(fileExt))
                   throw new SecurityException("The extension of the file is not allowed to be uploaded (1010).");
               */
            	//判断文件是否被禁止 //wtj fileExt变为小写 2006-04-11
                if(m_deniedFilesList.contains(fileExt.toLowerCase()))
                    throw new SecurityException("The extension of the file is denied to be uploaded (1015).");
                if(!m_allowedFilesList.isEmpty() && !m_allowedFilesList.contains(fileExt.toLowerCase()))
                    throw new SecurityException("The extension of the file is not allowed to be uploaded (1010).");
                if(m_maxFileSize > (long)0 && (long)((m_endData - m_startData) + 1) > m_maxFileSize)
                    throw new SecurityException(String.valueOf((new StringBuffer("Size exceeded for this file : ")).append(fileName).append(" (1105).")));
                totalFileSize += (m_endData - m_startData) + 1;
                if(m_totalMaxFileSize > (long)0 && totalFileSize > m_totalMaxFileSize)
                    throw new SecurityException("Total File Size exceeded (1110).");
            }
            if(isFile)
            {
            	com.linjj.thinkweixin.upload.File newFile = new com.linjj.thinkweixin.upload.File();
                newFile.setParent(this);
                newFile.setFieldName(fieldName);
                newFile.setFileName(fileName);
                newFile.setFileExt(fileExt);
                newFile.setFilePathName(filePathName);
                newFile.setIsMissing(filePathName.length() == 0);
                newFile.setContentType(contentType);
                newFile.setContentDisp(contentDisp);
                newFile.setTypeMIME(typeMIME);
                newFile.setSubTypeMIME(subTypeMIME);
                if(contentType.indexOf("application/x-macbinary") > 0)
                    m_startData = m_startData + 128;
                newFile.setSize((m_endData - m_startData) + 1);
                newFile.setStartData(m_startData);
                newFile.setEndData(m_endData);
                //加入文件集合
                m_files.addFile(newFile);
            }
            else
            {
                //2009-10-28 WTJ 增加字符处理,汉字处理
                //String value = new String(m_binArray, m_startData, (m_endData - m_startData) + 1);
                String value = new String(m_binArray, m_startData, (m_endData - m_startData) + 1,"GBK");
                m_formRequest.putParameter(fieldName, value);
            }
            if((char)m_binArray[m_currentIndex + 1] == '-')
                break;
            m_currentIndex = m_currentIndex + 2;
        } while(true);
    }
    //直接把数据流写到文件
    public void uploadInFile(String destFilePathName)
        throws SmartUploadException, IOException
    {
        int intsize = 0;
        int pos = 0;
        int readBytes = 0;
        if(destFilePathName == null)
            throw new IllegalArgumentException("There is no specified destination file (1025).");
        if(destFilePathName.length() == 0)
            throw new IllegalArgumentException("There is no specified destination file (1025).");
        if(!isVirtual(destFilePathName) && m_denyPhysicalPath)
            throw new SecurityException("Physical path is denied (1035).");
        //读长度
        intsize = m_request.getContentLength();
        m_binArray = new byte[intsize];
        //读取数据
        for(; pos < intsize; pos += readBytes)
        {
            try
            {
                readBytes = m_request.getInputStream().read(m_binArray, pos, intsize - pos);
            }
            catch(Exception e)
            {
                throw new SmartUploadException("Unable to upload.");
            }
        }
        if(isVirtual(destFilePathName))
            destFilePathName = m_application.getRealPath(destFilePathName);
        try
        {
            java.io.File file = new java.io.File(destFilePathName);
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(m_binArray);
            fileOut.close();
        }
        catch(Exception e)
        {
            throw new SmartUploadException("The Form cannot be saved in the specified file (1030).");
        }
    }

    //保存文件
    /**
     * @param destPathName String
     * @throws SmartUploadException
     * @throws IOException
     * @throws ServletException
     * @return int
     * 作用：将全部上传文件保存到指定目录下，并返回保存的文件个数。
     */
    public int save(String destPathName)
        throws SmartUploadException, IOException, ServletException
    {
        return save(destPathName, 0);
    }
    //保存文件
    public int save(String destPathName, int option)
        throws SmartUploadException, IOException, ServletException
    {
        int count = 0;
        if(destPathName == null)
            destPathName = m_application.getRealPath("/");
        if(destPathName.indexOf("/") != -1)
        {
            if(destPathName.charAt(destPathName.length() - 1) != '/')
                destPathName = String.valueOf(destPathName).concat("/");
        }
        else if(destPathName.charAt(destPathName.length() - 1) != '\\')
            destPathName = String.valueOf(destPathName).concat("\\");

        for(int i = 0; i < m_files.getCount(); i++)
            if(!m_files.getFile(i).isMissing())
            {
                m_files.getFile(i).saveAs(destPathName + m_files.getFile(i).getFileName(), option);
                count++;
            }

        return count;
    }

    //下载文件
    /**
     * @param sourceFilePathName String
     * @throws SmartUploadException
     * @throws IOException
     * @throws ServletException
     * 原型：共有以下三个原型可用，第一个最常用，后两个用于特殊情况下的文件下载（如更改内容类型，更改另存的文件名）。
     */
    public void downloadFile(String sourceFilePathName)
        throws SmartUploadException, IOException, ServletException
    {
        downloadFile(sourceFilePathName, null, null);
    }
    //下载文件
    public void downloadFile(String sourceFilePathName, String contentType)
        throws SmartUploadException, IOException, ServletException
    {
        downloadFile(sourceFilePathName, contentType, null);
    }
    //下载文件
    public void downloadFile(String sourceFilePathName, String contentType, String destFileName)
        throws SmartUploadException, IOException, ServletException
    {
        downloadFile(sourceFilePathName, contentType, destFileName, 65000);
    }
    //下载文件
   /*
    public void downloadFile(String sourceFilePathName, String contentType, String destFileName, int blockSize)
        throws SmartUploadException, IOException, ServletException
    {
        if(sourceFilePathName == null)
            throw new IllegalArgumentException(String.valueOf((new StringBuffer("File '")).append(sourceFilePathName).append("' not found (1040).")));
        if(sourceFilePathName.equals(""))
            throw new IllegalArgumentException(String.valueOf((new StringBuffer("File '")).append(sourceFilePathName).append("' not found (1040).")));
        if(!isVirtual(sourceFilePathName) && m_denyPhysicalPath)
            throw new SecurityException("Physical path is denied (1035).");
        if(isVirtual(sourceFilePathName))
            sourceFilePathName = m_application.getRealPath(sourceFilePathName);

        java.io.File file = new java.io.File(sourceFilePathName);
        FileInputStream fileIn = new FileInputStream(file);
        long fileLen = file.length();
        int readBytes = 0;
        int totalRead = 0;
        byte b[] = new byte[blockSize];
        if(contentType == null)
            m_response.setContentType("application/x-msdownload");
        else if(contentType.length() == 0)
            m_response.setContentType("application/x-msdownload");
        else
            m_response.setContentType(contentType);
        m_response.setContentLength((int)fileLen);

        if(destFileName == null)
            m_response.setHeader("Content-Disposition", "attachment; filename=".concat(String.valueOf(getFileName(sourceFilePathName))));
        else if(destFileName.length() == 0)
            m_response.setHeader("Content-Disposition", "attachment;");
        else
            m_response.setHeader("Content-Disposition", "attachment; filename=".concat(String.valueOf(destFileName)));
        while((long)totalRead < fileLen)
        {
            readBytes = fileIn.read(b, 0, blockSize);
            totalRead += readBytes;
            m_response.getOutputStream().write(b, 0, readBytes);
        }
        fileIn.close();
    }
    */
   /**
    * @param s String
    * @param s1 String
    * @param s2 String
    * @param i int
    * @throws ServletException
    * @throws IOException
    * @throws SmartUploadException
    * 增加函数toUtf8String 可以支持中文的文件名称
    */

   public void downloadFile(String sourceFilePathName, String contentType, String destFileName, int blockSize)
        throws ServletException, IOException, SmartUploadException
    {
        if(sourceFilePathName == null)
            throw new IllegalArgumentException("File '" + sourceFilePathName + "' not found (1040).");
        if(sourceFilePathName.equals(""))
            throw new IllegalArgumentException("File '" + sourceFilePathName + "' not found (1040).");
        if(!isVirtual(sourceFilePathName) && m_denyPhysicalPath)
            throw new SecurityException("Physical path is denied (1035).");
        if(isVirtual(sourceFilePathName))
            sourceFilePathName = m_application.getRealPath(sourceFilePathName);
        java.io.File file = new java.io.File(sourceFilePathName);
        FileInputStream fileinputstream = new FileInputStream(file);
        long l = file.length();
        boolean flag = false;
        int k = 0;
        byte abyte0[] = new byte[blockSize];
        if(contentType == null)
            m_response.setContentType("application/x-msdownload");
        else
        if(contentType.length() == 0)
            m_response.setContentType("application/x-msdownload");
        else
            m_response.setContentType(contentType);
        m_response.setContentLength((int)l);
        m_contentDisposition = m_contentDisposition != null ? m_contentDisposition : "attachment;";
        if(destFileName == null)
            m_response.setHeader("Content-Disposition",  m_contentDisposition + " filename=" + toUtf8String(getFileName(sourceFilePathName)));
        else
        if(destFileName.length() == 0)
            m_response.setHeader("Content-Disposition", m_contentDisposition);
        else
            m_response.setHeader("Content-Disposition", m_contentDisposition + " filename=" + toUtf8String(destFileName));
        while((long)k < l)
        {
            int j = fileinputstream.read(abyte0, 0, blockSize);
            k += j;
            m_response.getOutputStream().write(abyte0, 0, j);
        }
        fileinputstream.close();
    }

    /**
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     * @param s 原文件名
     * @return 重新编码后的文件名
     */
    public static String toUtf8String(String s)
    {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).
                    toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    //从数据库下载文件
    public void downloadField(ResultSet rs, String columnName, String contentType, String destFileName)
        throws SQLException, IOException, ServletException
    {
        if(rs == null)
            throw new IllegalArgumentException("The RecordSet cannot be null (1045).");
        if(columnName == null)
            throw new IllegalArgumentException("The columnName cannot be null (1050).");
        if(columnName.length() == 0)
            throw new IllegalArgumentException("The columnName cannot be empty (1055).");
        byte b[] = rs.getBytes(columnName);
        if(contentType == null)
            m_response.setContentType("application/x-msdownload");
        else if(contentType.length() == 0)
            m_response.setContentType("application/x-msdownload");
        else
            m_response.setContentType(contentType);
        //设置长度
        m_response.setContentLength(b.length);

        if(destFileName == null)
            m_response.setHeader("Content-Disposition", "attachment;");
        else if(destFileName.length() == 0)
            m_response.setHeader("Content-Disposition", "attachment;");
        else
            m_response.setHeader("Content-Disposition", "attachment; filename=".concat(String.valueOf(destFileName)));
        m_response.getOutputStream().write(b, 0, b.length);
    }
    //从字段写到文件
    public void fieldToFile(ResultSet rs, String columnName, String destFilePathName)
        throws SQLException, SmartUploadException, IOException, ServletException
    {
        try
        {
           if(m_application.getRealPath(destFilePathName) != null)
                destFilePathName = m_application.getRealPath(destFilePathName);

            InputStream is_data = rs.getBinaryStream(columnName);
            FileOutputStream file = new FileOutputStream(destFilePathName);
            int c;
            while((c = is_data.read()) != -1)
                file.write(c);
            file.close();
        }
        catch(Exception e)
        {
            throw new SmartUploadException("Unable to save file from the DataBase (1020).");
        }
    }

    protected String getDataFieldValue(String dataHeader, String fieldName)
    {
        String token = new String();
        String value = new String();
        int pos = 0;
        int i = 0;
        int start = 0;
        int end = 0;
        token = String.valueOf((new StringBuffer(String.valueOf(fieldName))).append("=").append('"'));
        pos = dataHeader.indexOf(token);
        if(pos > 0)
        {
            i = pos + token.length();
            start = i;
            token = "\"";
            end = dataHeader.indexOf(token, i);
            if(start > 0 && end > 0)
                value = dataHeader.substring(start, end);
        }
        return value;
    }
    /**
     * @return int
     * 作用：取上传文件数据的总长度
     */
    public int getSize()
    {
        return m_totalBytes;
    }
    //从数据流取字节
    public byte getBinaryData(int index)
    {
        byte retval;
        try
        {
            retval = m_binArray[index];
        }
        catch(Exception e)
        {
            throw new ArrayIndexOutOfBoundsException("Index out of range (1005).");
        }
        return retval;
    }
    /**
     * @return Files
     * 作用：取全部上传文件，以Files对象形式返回，可以利用Files类的操作方法来获得上传文件的数目等信息。
     */
    public Files getFiles()
    {
        return m_files;
    }
    /**
     * @return Request
     * 作用：取得Request对象，以便由此对象获得上传表单参数之值。
     */
    public Request getRequest()
    {
        return m_formRequest;
    }
    protected String getFileExt(String fileName)
    {
        String value = new String();
        int start = 0;
        int end = 0;
        if(fileName == null)
            return null;
        start = fileName.lastIndexOf(46) + 1;
        end = fileName.length();
        value = fileName.substring(start, end);
        if(fileName.lastIndexOf(46) > 0)
            return value;
        else
            return "";
    }

    protected String getContentType(String dataHeader)
    {
        String token = new String();
        String value = new String();
        int start = 0;
        int end = 0;
        token = "Content-Type:";
        start = dataHeader.indexOf(token) + token.length();
        if(start != -1)
        {
            end = dataHeader.length();
            value = dataHeader.substring(start, end);
        }
        return value;
    }

    protected String getTypeMIME(String ContentType)
    {
        String value = new String();
        int pos = 0;
        pos = ContentType.indexOf("/");
        if(pos != -1)
            return ContentType.substring(1, pos);
        else
            return ContentType;
    }

    protected String getSubTypeMIME(String ContentType)
    {
        String value = new String();
        int start = 0;
        int end = 0;
        start = ContentType.indexOf("/") + 1;
        if(start != -1)
        {
            end = ContentType.length();
            return ContentType.substring(start, end);
        } else
        {
            return ContentType;
        }
    }

    protected String getContentDisp(String dataHeader)
    {
        String value = new String();
        int start = 0;
        int end = 0;
        start = dataHeader.indexOf(":") + 1;
        end = dataHeader.indexOf(";");
        value = dataHeader.substring(start, end);
        return value;
    }

    protected void getDataSection()
    {
        boolean found = false;
        String dataHeader = new String();
        int searchPos = m_currentIndex;
        int keyPos = 0;
        int boundaryLen = m_boundary.length();
        m_startData = m_currentIndex;
        m_endData = 0;
        do
        {
            if(searchPos >= m_totalBytes)
                break;
            if(m_binArray[searchPos] == (byte)m_boundary.charAt(keyPos))
            {
                if(keyPos == boundaryLen - 1)
                {
                    m_endData = ((searchPos - boundaryLen) + 1) - 3;
                    break;
                }
                searchPos++;
                keyPos++;
            }
            else
            {
                searchPos++;
                keyPos = 0;
            }
        } while(true);
        m_currentIndex = m_endData + boundaryLen + 3;
    }

    protected String getDataHeader()
    {
        int start = m_currentIndex;
        int end = 0;
        int len = 0;
        boolean found = false;
        while(!found)
            if(m_binArray[m_currentIndex] == 13 && m_binArray[m_currentIndex + 2] == 13)
            {
                found = true;
                end = m_currentIndex - 1;
                m_currentIndex = m_currentIndex + 2;
            } else
            {
                m_currentIndex++;
            }
        //String dataHeader = new String(m_binArray, start, (end - start) + 1);
        //2009-10-28 WTJ 增加字符处理,汉字处理
        String dataHeader;
        try
        {
          dataHeader= new String(m_binArray, start, (end - start) + 1,"GBK");
        }
        catch(Exception e)
        {
          dataHeader = "";
        }

        return dataHeader;
    }

    protected String getFileName(String filePathName)
    {
        String token = new String();
        String value = new String();
        int pos = 0;
        int i = 0;
        int start = 0;
        int end = 0;
        pos = filePathName.lastIndexOf(47);
        if(pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        pos = filePathName.lastIndexOf(92);
        if(pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        else
            return filePathName;
    }
    /**
     * @param deniedFilesList String
     * @throws SQLException
     * @throws IOException
     * @throws ServletException
     * 作用：用于限制上传那些带有指定扩展名的文件。若有文件扩展名被限制，则上传时组件将抛出异常。
     */
    public void setDeniedFilesList(String deniedFilesList)
        throws SQLException, IOException, ServletException
    {
        String ext = "";
        if(deniedFilesList != null)
        {
            ext = "";
            for(int i = 0; i < deniedFilesList.length(); i++)
                if(deniedFilesList.charAt(i) == ',')
                {
                    if(!m_deniedFilesList.contains(ext))
                        m_deniedFilesList.addElement(ext);
                    ext = "";
                } else
                {
                    ext = ext + deniedFilesList.charAt(i);
                }

            if(ext != "")
                m_deniedFilesList.addElement(ext);
        } else
        {
            m_deniedFilesList = null;
        }
    }
    /**
     * @param allowedFilesList String
     * 作用：设定允许上传带有指定扩展名的文件，当上传过程中有文件名不允许时，组件将抛出异常。
     */
    public void setAllowedFilesList(String allowedFilesList)
    {
        String ext = "";
        if(allowedFilesList != null)
        {
            ext = "";
            for(int i = 0; i < allowedFilesList.length(); i++)
                if(allowedFilesList.charAt(i) == ',')
                {
                    if(!m_allowedFilesList.contains(ext))
                        m_allowedFilesList.addElement(ext);
                    ext = "";
                } else
                {
                    ext = ext + allowedFilesList.charAt(i);
                }

            if(ext != "")
                m_allowedFilesList.addElement(ext);
        } else
        {
            m_allowedFilesList = null;
        }
    }

    public void setDenyPhysicalPath(boolean deny)
    {
        m_denyPhysicalPath = deny;
    }

    public void setForcePhysicalPath(boolean force)
    {
        m_forcePhysicalPath = force;
    }
    /**
     * @param contentDisposition String
     * 作用：将数据追加到MIME文件头的CONTENT-DISPOSITION域。jspSmartUpload组件会在
     * 返回下载的信息时自动填写MIME文件头的CONTENT-DISPOSITION域，如果用户需要添加额外信息，请用此方法。
     */
    public void setContentDisposition(String contentDisposition)
    {
        m_contentDisposition = contentDisposition;
    }
    /**
     * @param totalMaxFileSize long
     * 作用：设定允许上传的文件的总长度，用于限制一次性上传的数据量大小。
     */
    public void setTotalMaxFileSize(long totalMaxFileSize)
    {
        m_totalMaxFileSize = totalMaxFileSize;
    }
    /**
     * @param maxFileSize long
     * 作用：设定每个文件允许上传的最大长度。
     */
    public void setMaxFileSize(long maxFileSize)
    {
        m_maxFileSize = maxFileSize;
    }
    //取文件路径
    protected String getPhysicalPath(String filePathName, int option)
        throws IOException
    {
        String path = new String();
        String fileName = new String();
        String fileSeparator = new String();
        boolean isPhysical = false;
        fileSeparator = System.getProperty("file.separator");
        if(filePathName == null)
            throw new IllegalArgumentException("There is no specified destination file (1140).");
        if(filePathName.equals(""))
            throw new IllegalArgumentException("There is no specified destination file (1140).");
        if(filePathName.lastIndexOf("\\") >= 0)
        {
            path = filePathName.substring(0, filePathName.lastIndexOf("\\"));
            fileName = filePathName.substring(filePathName.lastIndexOf("\\") + 1);
        }
        if(filePathName.lastIndexOf("/") >= 0)
        {
            path = filePathName.substring(0, filePathName.lastIndexOf("/"));
            fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
        }
        path = path.length() != 0 ? path : "/";
        java.io.File physicalPath = new java.io.File(path);
        if(physicalPath.exists())
            isPhysical = true;
        if(option == 0)
        {
            if(isVirtual(path))
            {
                path = m_application.getRealPath(path);
                if(path.endsWith(fileSeparator))
                    path = path + fileName;
                else
                    path = String.valueOf((new StringBuffer(String.valueOf(path))).append(fileSeparator).append(fileName));
                return path;
            }
            if(isPhysical)
            {
                if(m_denyPhysicalPath)
                    throw new IllegalArgumentException("Physical path is denied (1125).");
                else
                    return filePathName;
            } else
            {
                throw new IllegalArgumentException("This path does not exist (1135).");
            }
        }
        if(option == 1)
        {
            if(isVirtual(path))
            {
                path = m_application.getRealPath(path);
                if(path.endsWith(fileSeparator))
                    path = path + fileName;
                else
                    path = String.valueOf((new StringBuffer(String.valueOf(path))).append(fileSeparator).append(fileName));
                return path;
            }
            if(isPhysical)
                throw new IllegalArgumentException("The path is not a virtual path.");
            else
                throw new IllegalArgumentException("This path does not exist (1135).");
        }
        if(option == 2)
        {
            if(isPhysical)
                if(m_denyPhysicalPath)
                    throw new IllegalArgumentException("Physical path is denied (1125).");
                else
                    return filePathName;
            if(isVirtual(path))
                throw new IllegalArgumentException("The path is not a physical path.");
            else
                throw new IllegalArgumentException("This path does not exist (1135).");
        } else
        {
            return null;
        }
    }

    protected boolean isVirtual(String pathName)
    {
        if(m_application.getRealPath(pathName) != null)
        {
            java.io.File virtualFile = new java.io.File(m_application.getRealPath(pathName));
            return virtualFile.exists();
        } else
        {
            return false;
        }
    }
}
