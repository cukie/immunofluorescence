package imunofilter.v2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

// encapsulates two buffered images. The original, from which the mask will be created, 
// and the second image that the mask will be placed upon
public class ImunoSet {
	private BufferedImage originalNoMask;
	private BufferedImage originalToMask;
	private BufferedImage maskNoRed;
	private BufferedImage maskGreen;
	private BufferedImage maskGreenNoRed;

	private BufferedImage resultNoRed;
	private BufferedImage resultGreen;
	private BufferedImage resultGreenNoRed;

	private List<Integer> listOfGreenUnderGreen;
	private List<Integer> listOfGreenUnderNoRed;
	private List<Integer> listOfGreenUnderGreenNoRed;

	private List<Integer> listOfRedUnderGreen;
	private List<Integer> listOfRedUnderNoRed;
	private List<Integer> listOfRedUnderGreenNoRed;

	private List<Integer> listOfGreenCombinedUnderGreen;
	private List<Integer> listOfGreenCombinedUnderNoRed;
	private List<Integer> listOfGreenCombinedUnderGreenNoRed;

	private List<Integer> listOfRedCombinedUnderGreen;
	private List<Integer> listOfRedCombinedUnderNoRed;
	private List<Integer> listOfRedCombinedUnderGreenNoRed;

	private int purpleINT;
	private int blackINT;

	private final int totalPixels;

	public ImunoSet(String originalNoMaskPath, String originalToMaskPath) {
		try {
			originalNoMask = ImageIO.read(new File(originalNoMaskPath));
			
		} catch (Exception e) {
			System.out.println("OriginalNoMask File Path invalid");
		}

		try {
			originalToMask = ImageIO.read(new File(originalToMaskPath));
		} catch (Exception e) {
			System.out.println("OriginalToMask File Path invalid");
		}


		// check to make sure that both images are the same size
		if (!(originalNoMask.getHeight() == originalToMask.getHeight())) {
			throw new RuntimeException("heights of images do not match");
		}

		if (!(originalNoMask.getWidth() == originalToMask.getWidth())) {
			throw new RuntimeException("widths of images do not match");
		}

		// make the mask and the result the same size as the original images
		maskNoRed = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		maskGreen = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		maskGreenNoRed = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		resultNoRed = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		resultGreen = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		resultGreenNoRed = new BufferedImage(originalNoMask.getWidth(),
				originalNoMask.getHeight(), BufferedImage.TYPE_INT_RGB);

		// calculation for int purple
		Color purple = new Color(255, 0, 255);
		Color black = new Color(0, 0, 0);
		purpleINT = purple.getRGB();
		blackINT = black.getRGB();

		totalPixels = originalNoMask.getHeight() * originalNoMask.getWidth();

		// green under mask
		listOfGreenUnderGreen = new ArrayList<Integer>();
		listOfGreenUnderNoRed = new ArrayList<Integer>();
		listOfGreenUnderGreenNoRed = new ArrayList<Integer>();

		// red under mask
		listOfRedUnderGreen = new ArrayList<Integer>();
		listOfRedUnderNoRed = new ArrayList<Integer>();
		listOfRedUnderGreenNoRed = new ArrayList<Integer>();

		// green under mask when there is green and red
		listOfGreenCombinedUnderGreen = new ArrayList<Integer>();
		listOfGreenCombinedUnderNoRed = new ArrayList<Integer>();
		listOfGreenCombinedUnderGreenNoRed = new ArrayList<Integer>();

		// red under mask when there is green and red
		listOfRedCombinedUnderGreen = new ArrayList<Integer>();
		listOfRedCombinedUnderNoRed = new ArrayList<Integer>();
		listOfRedCombinedUnderGreenNoRed = new ArrayList<Integer>();

	}

	// getters for all lists of arraylist values
	public List<Integer> getListOfGreenUnderGreen() {
		return listOfGreenUnderGreen;
	}

	public List<Integer> getListOfGreenUnderNoRed() {
		return listOfGreenUnderNoRed;
	}

	public List<Integer> getListOfGreenUnderGreenNoRed() {
		return listOfGreenUnderGreenNoRed;
	}

	public List<Integer> getListOfRedUnderGreen() {
		return listOfRedUnderGreen;
	}

	public List<Integer> getListOfRedUnderNoRed() {
		return listOfRedUnderNoRed;
	}

	public List<Integer> getListOfRedUnderGreenNoRed() {
		return listOfRedUnderGreenNoRed;
	}

	public List<Integer> getListOfGreenCombinedUnderGreen() {
		return listOfGreenCombinedUnderGreen;
	}

	public List<Integer> getListOfGreenCombinedUnderNoRed() {
		return listOfGreenCombinedUnderNoRed;
	}

	public List<Integer> getListOfGreenCombinedUnderGreenNoRed() {
		return listOfGreenCombinedUnderGreenNoRed;
	}

	public List<Integer> getListOfRedCombinedUnderGreen() {
		return listOfRedCombinedUnderGreen;
	}

	public List<Integer> getListOfRedCombinedUnderNoRed() {
		return listOfRedCombinedUnderNoRed;
	}

	public List<Integer> getListOfRedCombinedUnderGreenNoRed() {
		return listOfRedCombinedUnderGreenNoRed;
	}

	// getters for all encapsulated images in ImunoSet
	public BufferedImage getOriginalNoMask() {
		return originalNoMask;
	}

	public BufferedImage getOriginalToMask() {
		return originalToMask;
	}

	public BufferedImage getMaskNoRed() {
		return maskNoRed;
	}

	public BufferedImage getMaskGreen() {
		return maskGreen;
	}

	public BufferedImage getMaskGreenNoRed() {
		return maskGreenNoRed;
	}

	public BufferedImage getResultGreen() {
		return resultGreen;
	}

	public BufferedImage getResultNoRed() {
		return resultNoRed;
	}

	public BufferedImage getResultGreenNoRed() {
		return resultGreenNoRed;
	}

	public int getTotalPixels() {
		return totalPixels;
	}

	public double getMaskCoverageGreen() {
		return getMaskCoverage(maskGreen);
	}

	public double getMaskCoverageNoRed() {
		return getMaskCoverage(maskNoRed);
	}

	public double getMaskCoverageGreenNoRed() {
		return getMaskCoverage(maskGreenNoRed);
	}

	// get the mean of the list of values
	public double getMean(List<Integer> list) {
		int sum = 0;
		for (Integer i : list) {
			sum += i;
		}

		return (double) sum / list.size();
	}

	// get median of list aka divide by 2 (middle value)
	public int getMedian(List<Integer> list) {
		int index = list.size() / 2;
		return list.get(index);
	}
	
	public int getTotalIntensity(List<Integer> list){
		int sum = 0;
		for (Integer i : list){
			sum += i;
		}
		
		return sum;
	}

	public double getStDev(List<Integer> list) {

		int sum = 0;
		int average;
		for (Integer i : list) {
			sum += i;
		}
		average = sum / list.size();

		List<Integer> differenceOfValues = new ArrayList<Integer>();
		for (Integer j : list) {
			differenceOfValues.add(j - average);
		}

		List<Integer> squares = new ArrayList<Integer>();
		for (Integer k : differenceOfValues) {
			squares.add(k * k);
		}

		int sumOfSquares = 0;
		for (Integer l : squares) {
			sumOfSquares += l;
		}

		return Math.sqrt((double) sumOfSquares / (double) (list.size() - 1));
	}

	// helper method for mask coverage methods
	private double getMaskCoverage(BufferedImage mask) {
		int purpleCount = 0;
		for (int i = 0; i < mask.getHeight(); i++) {
			for (int j = 0; j < mask.getWidth(); j++) {
				Color c = new Color(mask.getRGB(j, i));

				if (c.getRed() == 255 && c.getBlue() == 255) {
					purpleCount++;
				}
			}
		}

		return (double) purpleCount / (double) totalPixels;
	}

	// first version of the mask takes in one color parameter
	// returns mask

	// note - just do one of these methods and give all parameters for all
	// colors in one method.
	// e.g. range for red green and blue all in one. if blue doesn't matter,
	// than 0-255
	// NOTE values are inclusive on both ends

	public BufferedImage createMaskNoRed(double rmin, double rmax, double gmin,
			double gmax, double bmin, double bmax) {

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(originalNoMask.getRGB(j, i));

				// test to see if original pixel is in given range, if it is,
				// make mask purple
				// else make mask black
				if (c.getRed() >= rmin && c.getRed() <= rmax
						&& c.getGreen() >= gmin && c.getGreen() <= gmax
						&& c.getBlue() >= bmin && c.getBlue() <= bmax) {
					maskNoRed.setRGB(j, i, purpleINT);
				} else {
					maskNoRed.setRGB(j, i, blackINT);
				}
			}
		}

		return maskNoRed;

	}

	public BufferedImage createMaskGreen(double rmin, double rmax, double gmin,
			double gmax, double bmin, double bmax) {

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(originalNoMask.getRGB(j, i));

				// test to see if original pixel is in given range, if it is,
				// make mask purple
				// else make mask black
				if (c.getRed() >= rmin && c.getRed() <= rmax
						&& c.getGreen() >= gmin && c.getGreen() <= gmax
						&& c.getBlue() >= bmin && c.getBlue() <= bmax) {
					maskGreen.setRGB(j, i, purpleINT);
				} else {
					maskGreen.setRGB(j, i, blackINT);
				}
			}
		}

		return maskGreen;

	}

	public BufferedImage createMaskGreenNoRed(double rmin, double rmax,
			double gmin, double gmax, double bmin, double bmax) {

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(originalNoMask.getRGB(j, i));

				// test to see if original pixel is in given range, if it is,
				// make mask purple
				// else make mask black
				if (c.getRed() >= rmin && c.getRed() <= rmax
						&& c.getGreen() >= gmin && c.getGreen() <= gmax
						&& c.getBlue() >= bmin && c.getBlue() <= bmax) {
					maskGreenNoRed.setRGB(j, i, purpleINT);
				} else {
					maskGreenNoRed.setRGB(j, i, blackINT);
				}
			}
		}

		return maskGreenNoRed;

	}

	// takes the mask for the object and overlays it onto the originalToMask
	// image
	// writes the results into the result image
	// the result image can than be used for analysis
	public void maskOverlayGreen(int gMarkerThreshold, int rMarkerThreshold) {
		int maskCount;
		int maskRed;
		int maskofGreenisRed;
		int maskofRedisGreen;

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(maskGreen.getRGB(j, i));
				Color d = new Color(originalToMask.getRGB(j, i));

				// if mask is purple, set underlying marker pixel to results
				// frame
				// else, set to black
				if (c.getRed() == 255 && c.getBlue() == 255) {
					resultGreen.setRGB(j, i, d.getRGB());

					// check to see if pixel contains green, if so, add green
					// level to appropriate list
					Color r = new Color(resultGreen.getRGB(j, i));

					if (r.getGreen() >= gMarkerThreshold) {
						listOfGreenUnderGreen.add(r.getGreen());
					}
					if (r.getRed() >= rMarkerThreshold) {
						listOfRedUnderGreen.add(r.getRed());
					}
					if (r.getRed() >= rMarkerThreshold
							&& r.getGreen() >= gMarkerThreshold) {
						listOfGreenCombinedUnderGreen.add(r.getGreen());
						listOfRedCombinedUnderGreen.add(r.getRed());
					}
				} else {
					resultGreen.setRGB(j, i, blackINT);
				}
			}

		}

	}

	public void maskOverlayNoRed(int gMarkerThreshold, int rMarkerThreshold) {
		int maskCount;
		int maskRed;
		int maskofGreenisRed;
		int maskofRedisGreen;

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(maskNoRed.getRGB(j, i)); // error created on
																// purpose. i
																// have to pull
																// from mask,
																// not original
																// right?
				Color d = new Color(originalToMask.getRGB(j, i));

				// if mask is purple, set underlying marker pixel to results
				// frame
				// else, set to black
				if (c.getRed() == 255 && c.getBlue() == 255) {
					resultNoRed.setRGB(j, i, d.getRGB());

					// check to see if pixel contains green, if so, add green
					// level to appropriate list
					Color r = new Color(resultNoRed.getRGB(j, i));

					if (r.getGreen() >= gMarkerThreshold) {
						listOfGreenUnderNoRed.add(r.getGreen());
					}
					if (r.getRed() >= rMarkerThreshold) {
						listOfRedUnderNoRed.add(r.getRed());
					}
					if (r.getRed() >= rMarkerThreshold
							&& r.getGreen() >= gMarkerThreshold) {
						listOfGreenCombinedUnderNoRed.add(r.getGreen());
						listOfRedCombinedUnderNoRed.add(r.getRed());
					}
				} else {
					resultNoRed.setRGB(j, i, blackINT);
				}
			}

		}

	}

	public void maskOverlayGreenNoRed(int gMarkerThreshold, int rMarkerThreshold) {
		int maskCount;
		int maskRed;
		int maskofGreenisRed;
		int maskofRedisGreen;

		for (int i = 0; i < originalNoMask.getHeight(); i++) {
			for (int j = 0; j < originalNoMask.getWidth(); j++) {

				// create color object to split component values
				Color c = new Color(maskGreenNoRed.getRGB(j, i)); 
				Color d = new Color(originalToMask.getRGB(j, i));

				// if mask is purple, set underlying marker pixel to results
				// frame
				// else, set to black
				if (c.getRed() == 255 && c.getBlue() == 255) {
					resultGreenNoRed.setRGB(j, i, d.getRGB());

					// check to see if pixel contains green, if so, add green
					// level to appropriate list
					Color r = new Color(resultGreenNoRed.getRGB(j, i));

					if (r.getGreen() >= gMarkerThreshold) {
						listOfGreenUnderGreenNoRed.add(r.getGreen());
					}
					if (r.getRed() >= rMarkerThreshold) {
						listOfRedUnderGreenNoRed.add(r.getRed());
					}
					if (r.getRed() >= rMarkerThreshold
							&& r.getGreen() >= gMarkerThreshold) {
						listOfGreenCombinedUnderGreenNoRed.add(r.getGreen());
						listOfRedCombinedUnderGreenNoRed.add(r.getRed());
					}
				} else {
					resultGreenNoRed.setRGB(j, i, blackINT);
				}
			}

		}

	}
}
