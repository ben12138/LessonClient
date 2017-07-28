package com.lesson.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.lesson.bean.GOLBALVALUE;
import com.lesson.net.NetConnection;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {

    /**
     * 重新设置图像大小
     * @param sfile 图片源文件
     * @param tfile 目标文件
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param quality 质量系数
     * @throws IOException
     */
    public static void resizeImg(String sfile, String tfile, int newWidth,
            int newHeight, float quality) throws IOException {
        if(quality > 1) {  
            throw new IllegalArgumentException(  
                    "Quality has to be between 0 and 1");  
        }

        if(newWidth <=0 || newHeight<=0) {
            throw new IllegalArgumentException(  
                    "Width or Height must greater than zero");
        }

        //This code ensures that all the pixels in the image are loaded.
        Image srcImg = new ImageIcon(sfile).getImage();
        Image resizedImage = srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // This code ensures that all the pixels in the image are loaded.  
        Image temp = new ImageIcon(resizedImage).getImage();  

        // Create the buffered image.  
        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        // Copy image to buffered image.  
        Graphics g = bufferedImage.createGraphics();  

        // Clear background and paint the image.  
        g.setColor(Color.white);  
        g.fillRect(0, 0, newWidth, newHeight);  
        g.drawImage(temp, 0, 0, null);  
        g.dispose();  

        // Soften.  
        float softenFactor = 0.05f;  
        float[] softenArray = { 0, softenFactor, 0, softenFactor,  
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };  
        Kernel kernel = new Kernel(3, 3, softenArray);  
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
        bufferedImage = cOp.filter(bufferedImage, null);

        // Write the jpeg to a file.  
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(new File(tfile));  

            // Encodes image as a JPEG data stream  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  

            JPEGEncodeParam param = encoder  
                    .getDefaultJPEGEncodeParam(bufferedImage);  

            param.setQuality(quality, true);  

            encoder.setJPEGEncodeParam(param);  
            encoder.encode(bufferedImage); 
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public static void makeRoundedCornerImg(String sfile, String tfile,
            int cornerRadius) throws IOException {
        BufferedImage image = ImageIO.read(new File(sfile));
        if(image.getWidth()>150 || image.getHeight()>150){
        	resizeImg(sfile, tfile, 150, 150, 1.0f);
        	image = ImageIO.read(new File(tfile));
        }
        int w = 150;
        int h = 150;
        cornerRadius = 150;
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(image.getWidth()/2-75, image.getHeight()/2-75, w, h, cornerRadius,
                cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        String filetype = tfile.substring(tfile.lastIndexOf(".")+1);
        try {
            ImageIO.write(output, filetype, new File(tfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
		uploadFile("d:\\1.png");
	}
    
    public static boolean changeImage(String path){
    	try {
			ImageUtil.makeRoundedCornerImg(path, "d://"+GOLBALVALUE.user.getEmail()+".png", 90);
//			ImageUtil.makeRoundedCornerImg(path, "d://1.png", 90);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
    }
    
    public static void uploadFile(final String path){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(path);
				String rsp = "";
				HttpURLConnection conn = null;
				String BOUNDARY = "|"; 
				try {
					URL url = new URL("http://localhost:8081/HLServer/news/UpLoadAction");
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(30000);
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
					conn.setRequestProperty("Content-Type",
							"multipart/form-data; boundary=" + BOUNDARY);

					OutputStream out = new DataOutputStream(conn.getOutputStream());
					String filename = file.getName();
					String contentType = "";
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (filename.endsWith(".jpg")) {
						contentType = "image/jpg";
					}
					if (filename.endsWith(".gif")) {
						contentType = "image/gif";
					}
					if (filename.endsWith(".bmp")) {
						contentType = "image/bmp";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + file.getAbsolutePath()
							+ "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					int len = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
						len+=bytes;
//						System.out.println(len);
//						System.out.println(file.length());
					}
					in.close();
					byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
					out.write(endData);
					out.flush();
					out.close();

					StringBuffer buffer = new StringBuffer();
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
					String line = null;
					while ((line = reader.readLine()) != null) {
						buffer.append(line).append("\n");
					}
					rsp = buffer.toString();
					reader.close();
					reader = null;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (conn != null) {
						conn.disconnect();
						conn = null;
					}
				}
			}
		}).start();
      
    }
    
}
