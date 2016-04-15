package RayTracerObj;

import java.awt.Color;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RayCast {
	//Access to data should be with setPixel or getPixel
	private byte[] rgbData;
	private int imageWidth;
	private int imageHeight;

	public RayCast(int width, int height) {
		imageWidth = width;
		imageHeight = height;

		//Create byte array to represent each pixel in image
		//3 for RGB
		rgbData = new byte[this.imageWidth * this.imageHeight * 3];

	}

	/**
	 * Each parameter represented AWT.Color (i.e. RGB)
	 * 
	 * @param xshift
	 * @param yshift
	 * @param color
	 */
	public void setPixel(int x, int y, Color color) {
		int xshift = x + imageWidth;
		int yshift = y + imageHeight;
		rgbData[(xshift + yshift * imageWidth)*3 + 0 /*red*/] = (byte) color.getRed();
		rgbData[(xshift + yshift * imageWidth)*3 + 1 /*green*/] = (byte) color.getGreen();
		rgbData[(xshift + yshift * imageWidth)*3 + 2 /*blue*/] = (byte) color.getBlue();
	}

	public Color getPixel(int x, int y) {
		// Write pixel color values in RGB format to rgbData:
		// Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
		//            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
		//             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
		//
		// Each of the red, green and blue components should be a byte, i.e. 0-255


		return new Color(
				rgbData[(x + y * imageWidth)*3 + 0 /*red*/],
				rgbData[(x + y * imageWidth)*3 + 1 /*green*/],
				rgbData[(x + y * imageWidth)*3 + 1 /*green*/]
				);
	}


	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(Camera camera, Scene scene, String outputFileName)
	{
		long startTime = System.currentTimeMillis();
		
		for (int x = -imageWidth/2; x < imageWidth/2; x++) {
			for (int y = -imageHeight/2; y < imageHeight/2;y++) {
				Ray ray = new Ray(camera, x, y);
				Intersection intersection = findIntersection(ray, scene);
				setPixel(x, y, intersection.getColor());
			}
		}
		// Put your ray tracing code here!

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		// The time is measured for your own convenience, rendering speed will not affect your score
		// unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}




	private Intersection findIntersection(Ray ray, Scene scene) {
		// TODO Auto-generated method stub
		return new Intersection(ray, scene);
	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
		int height = buffer.length / width / 3;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);

		return result;
	}


}
