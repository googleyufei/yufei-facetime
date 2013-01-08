/*
 *  Copyright (C) 2010 SUNRico Inc.
 *  ------------------------------------------------------------------------------
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *       http://www.streets.cn
 *
 *  ----------------------------------------------------------------------------------
 */

package com.facetime.core.file;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * @author dzb2k9 dzb2k9@gmail.com 
 */
public abstract class ImageUtils {

	public static final String code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private static Random random = new Random();

	//    /**
	//     * converts an image org.streets.commons.resource to an AWT image.
	//     *
	//     * @param imageURL image org.streets.commons.resource URL
	//     *
	//     * @return AWT image
	//     */
	//    private static Image toAwtImage(URL imageURL) {
	//        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
	//        MediaTracker mediaTracker = new MediaTracker(new Container());
	//        mediaTracker.addImage(image, 0);
	//        try
	//        {
	//            mediaTracker.waitForID(0);
	//        }
	//        catch (InterruptedException e)
	//        {
	//            throw new RuntimeException(e);
	//        }
	//        return image;
	//    }

	//    /**
	//     * reduce the quality of an image.
	//     *
	//     * @param image   the original image
	//     * @param quality quality
	//     * @param output  data stream of the quality reduced image
	//     */
	//    public static void reduceQuality(BufferedImage image, float quality, OutputStream output)
	//    {
	//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
	//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
	//        quality = Math.max(0, Math.min(quality, 100));
	//        param.setQuality(quality / 100.0f, false);
	//        encoder.setJPEGEncodeParam(param);
	//
	//        try
	//        {
	//            encoder.encode(image);
	//        }
	//        catch (IOException e)
	//        {
	//            throw new RuntimeException(e);
	//        }
	//    }

	/**
	 * scale image object to a new size.
	 *
	 * @param image image org.streets.commons.resource to scale
	 * @param height        scale to height
	 *
	 * @return scaled image
	 */
	public static BufferedImage scale(Image image, int height) {
		int thumbWidth;
		double imageRatio = (double) image.getWidth(null) / (double) image.getHeight(null);
		thumbWidth = (int) (height * imageRatio);

		BufferedImage thumbImage = new BufferedImage(thumbWidth, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, height, null);

		return thumbImage;
	}

	public static Color getRandColor(int fc, int bc) {
		fc = Math.min(fc, 255);
		bc = Math.min(bc, 255);
		final int r = fc + random.nextInt(bc - fc);
		final int g = fc + random.nextInt(bc - fc);
		final int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static String genVerifyCode(final int width, final int height, final OutputStream outputStream)
			throws IOException {
		final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = bi.createGraphics();

		final GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, width, height, getRandColor(120, 200), false);
		g.setPaint(gp);
		g.fillRect(0, 0, width, height);
		for (int i = 0; i < 50; i++) {
			g.setColor(getRandColor(210, 220));
			g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
		}
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, width - 1, height - 1);

		final Font font = new Font("comic sans ms", Font.BOLD, 24);
		g.setFont(font);

		final FontMetrics metrics = g.getFontMetrics();
		int stringWidth = 0;
		final char[] text = new char[4];
		final int[] textWidth = new int[4];
		for (int i = 0; i < 4; i++) {
			final int j = random.nextInt(code.length());
			text[i] = code.charAt(j);
			textWidth[i] = metrics.charWidth(text[i]);
			stringWidth += textWidth[i];
		}
		int posX = (width - stringWidth) / 2;
		final int posY = (height - metrics.getHeight()) / 2 + metrics.getAscent();
		for (int i = 0; i < text.length; i++) {
			g.setColor(getRandColor(50, 120));
			g.drawString(String.valueOf(text[i]), posX, posY);
			posX += textWidth[i];
		}
		ImageIO.write(bi, "png", outputStream);
		return String.valueOf(text);
	}

	public static void thumbnail(final InputStream inputStream, final int width, final int height,
			final OutputStream outputStream) throws IOException {
		thumbnail(inputStream, width, height, false, outputStream);
	}

	public static void thumbnail(final InputStream inputStream, final int width, final int height,
			final boolean stretch, final OutputStream outputStream) throws IOException {
		int w, h;
		final BufferedImage sbi = ImageIO.read(inputStream);
		if (sbi == null) {
			return;
		}
		if (width == 0 || height == 0) {
			w = sbi.getWidth();
			h = sbi.getHeight();
		} else {
			if (!stretch) {
				final double d = (double) width / (double) height;
				final double d0 = (double) sbi.getWidth() / (double) sbi.getHeight();
				if (d < d0) {
					w = width;
					h = (int) (width / d0);
				} else {
					w = (int) (height * d0);
					h = height;
				}
			} else {
				w = width;
				h = height;
			}
		}
		final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = bi.createGraphics();
		if (w != width) {
			g.drawImage(sbi, Math.abs(w - width) / 2, 0, w, h, null);
		} else if (h != height) {
			g.drawImage(sbi, 0, Math.abs(h - height) / 2, w, h, null);
		} else {
			g.drawImage(sbi, 0, 0, w, h, null);
		}
		g.dispose();
		ImageIO.write(bi, "png", outputStream);
	}
}
