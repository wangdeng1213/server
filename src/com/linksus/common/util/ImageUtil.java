package com.linksus.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.ResampleOp;

/**
*
*/
public class ImageUtil{

	protected static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * read image from file, now it can support image type:
	 * bmp,wbmp,gif,jpge,png
	 * @param file
	 * @return BufferedImage
	 * <pre>
	 * BufferedImage image;
	 * image = ImageUtils.readImage(new File(&quot;myImage.jpg&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.gif&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.bmp&quot;));
	 * image = ImageUtils.readImage(new File(&quot;myImage.png&quot;));
	 * </pre>
	 */
	public BufferedImage readImage(File file){
		BufferedImage image = null;
		if(file != null && file.isFile() && file.exists()){
			try{
				image = ImageIO.read(file);
			}catch (IOException e){
				e.printStackTrace();
				LogUtil.saveException(logger, e);
			}
		}
		return image;
	}

	/**
	 * get the image width
	 * 
	 * @param image
	 * @return image width
	 */
	public double getWidth(BufferedImage image){
		return image.getWidth();
	}

	/**
	 * get the image height
	 * 
	 * @param image
	 * @return image height
	 */
	public double getHeight(BufferedImage image){
		return image.getHeight();
	}

	/**
	 * @param image
	 *            BufferedImage.
	 * @param width
	 *            zoom width.
	 * @param heigth
	 *            zoom heigth.
	 * @return BufferedImage
	 */
	public BufferedImage zoom(BufferedImage image,int width,int heigth){
		ResampleOp resampleOp = new ResampleOp(width, heigth);
		BufferedImage tag = resampleOp.filter(image, null);
		return tag;
	}

	/**
	 * 
	 * @param image
	 *            BufferedImage
	 * @param width
	 *            zoom width.
	 * @param heigth
	 *            zoom heigth.
	 * @return double array.double[0]:width,double[1]:heigth.
	 */
	public double[] zoomSize(BufferedImage image,int width,int heigth){
		double[] zoomSize = new double[2];
		double srcWidth = getWidth(image);
		double srcHeigth = getHeight(image);

		if(srcWidth > srcHeigth){
			zoomSize[0] = (srcWidth / srcHeigth) * heigth;
			zoomSize[1] = heigth;
		}else if(srcWidth < srcHeigth){
			zoomSize[0] = width;
			zoomSize[1] = (srcHeigth / srcWidth) * width;
		}else{// 相等 按值小的缩放
			int size = width >= heigth ? heigth : width;
			zoomSize[0] = size;
			zoomSize[1] = size;
		}
		return zoomSize;
	}

	/**
	 * 
	 * Output to file out according to the style<code>BufferedImage</code> If
	 * can not appoint image or formateName or file out ,do nothing. Now it can
	 * support image type：bmp,wbmp,jpeg,png.
	 * 
	 * @param image
	 *            BufferedImage.
	 * @param formatName
	 *            format name.
	 * @param out
	 *            output path.
	 * @throws IOException
	 *             IOException
	 */
	public void writeImage(BufferedImage image,String formatName,File file) throws IOException{
		if(image != null && formatName != null && !"".equals(formatName) && file != null){
			ImageIO.write(image, formatName, file);
		}
	}

	public static void zoomImage(File sFile,File tFile,String imgType,int width,int heigth) throws IOException{
		ImageUtil m = new ImageUtil();
		BufferedImage image = m.readImage(sFile);
		double[] size = m.zoomSize(image, width, heigth);
		m.writeImage(m.zoom(image, (int) size[0], (int) size[1]), imgType, tFile);
	}

	public static void main(String[] args) throws IOException{
		File sFile = new File("d:/IMG_3035.JPG");
		File tFile = new File("d:/41.jpg");
		zoomImage(sFile, tFile, "jpg", 100, 100);
	}
}