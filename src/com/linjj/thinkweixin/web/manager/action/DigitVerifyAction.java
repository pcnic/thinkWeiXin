package com.linjj.thinkweixin.web.manager.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import com.linjj.thinkweixin.web.base.BaseAction;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class DigitVerifyAction extends BaseAction {
	private Font mFont = new Font("", Font.PLAIN, 12);
    
	
	public String code() throws Exception {
		String s = "";
		int intCount = 0;
		intCount = (new Random()).nextInt(9999);
		if (intCount < 1000)
			intCount += 1000;
		s = intCount + "";
		request.getSession().setAttribute("rand", s);
		response.setContentType("image/gif");
		ServletOutputStream out=null;
		try {
			out = response.getOutputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//在内存中创建图片
		   int width=60,height=20;
		   BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		 //获取图形上下文
		   Graphics gra = image.getGraphics();
		// 
		gra.setColor(new Color(160,200,100));
		gra.fillRect(0,0,width,height);
		   //设置字体
		gra.setFont(new Font("Times New Roman",Font.PLAIN,18));
		// 
		Random random= new Random();
		char c;
		for (int i = 0; i < 4; i++) {
			c = s.charAt(i);
			 gra.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
			gra.drawString(c + "", 13*i+6,16);
		}
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		try {
			encoder.encode(image);
			out.close();
		} catch (ImageFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
}
