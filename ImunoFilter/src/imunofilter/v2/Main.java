package imunofilter.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {

		/*
		 * USER INPUT:
		 * 
		 * User input for mask ranges. User gives input for ranges of RGB values
		 * of marker and mask and original file paths, to be used in creation of
		 * each mask, the maskOverlay, and the writing of results
		 * 
		 * Done by creating a scanner object and accepting int and/or string
		 * values as parameters. No real error checking instituted yet...
		 */

		Scanner keyboard = new Scanner(System.in);

		System.out
		.println("Enter the absolute file path of the mask directory: ");
		String maskPath = keyboard.nextLine();

		System.out
		.println("Enter the absolute file path of the markers directory: ");
		String markerPath = keyboard.nextLine();

		System.out
		.println("Enter the min and max values for mask red paramater:");
		int redMin = keyboard.nextInt();
		int redMax = keyboard.nextInt();

		System.out
		.println("Enter the min and max values for mask green parameter:");
		int greenMin = keyboard.nextInt();
		int greenMax = keyboard.nextInt();

		System.out
		.println("Enter the min and max values for mask blue parameter:");
		int blueMin = keyboard.nextInt();
		int blueMax = keyboard.nextInt();

		System.out.println("Enter the threshold value for the red marker: ");
		int markerRedThreshold = keyboard.nextInt();

		System.out.println("Enter the threshold value for the green marker: ");
		int markerGreenThreshold = keyboard.nextInt();

		System.out.println("Enter an absolute filepath for your results");
		String resultsPath = keyboard.next();

		System.out
		.println("Enter a file name for your results(do not add file ending): ");
		String resultsName = keyboard.next();

		/*
		 * LIST FILES IN DIRECTORIES: Image files (and by association, their
		 * paths) are stored in an array There is one array for masks and
		 * another for markers
		 * 
		 * 
		 * User input should be used to determine the file paths for each of
		 * these files USER INPUT NOT YET INSTITUTED
		 */
		File[] mask = new File(maskPath).listFiles();
		File[] markers = new File(markerPath).listFiles();

		Arrays.sort(mask);
		Arrays.sort(markers);

		for (File f : mask) {
			System.out.println(f.getName());
		}

		/*
		 * CREATING FILE WRITER FOR TEXT RESULTS OUTPUT
		 */

		FileWriter fw = new FileWriter(
				resultsPath + resultsName + ".txt");
		BufferedWriter bw = new BufferedWriter(fw);

		String delimeter = "|";

		/*
		 * MASK CREATION AND OUTPUT TO FILE: for each files in the directory
		 * (start at one because the .ds_store file will be detected NOTE: this
		 * might need to be adjusted based on hidden files on each operating
		 * system Names after Th in Toshiba drive need adjustment because of
		 * hidden "Thumb."
		 */

		System.out.println(mask.length);

		// ImunoSet[] imageSet = new ImunoSet[mask.length];

		for (int filenum = 0; filenum < mask.length; filenum++) {
			ImunoSet[] imageSet = new ImunoSet[mask.length];

			// it would be great to do a check here to make sure the files are
			// what we need...

			imageSet[filenum] = new ImunoSet(mask[filenum].getAbsolutePath(),
					markers[filenum].getAbsolutePath());

			imageSet[filenum].createMaskNoRed(redMin, redMax, 0, 255, 0, 255);
			imageSet[filenum].createMaskGreen(0, 255, greenMin, greenMax, 0,
					255);
			imageSet[filenum].createMaskGreenNoRed(redMin, redMax, greenMin,
					greenMax, 0, 255);

			// logic for mask overlays

			imageSet[filenum].maskOverlayGreen(markerGreenThreshold,
					markerRedThreshold);
			imageSet[filenum].maskOverlayNoRed(markerGreenThreshold,
					markerRedThreshold);
			imageSet[filenum].maskOverlayGreenNoRed(markerGreenThreshold,
					markerRedThreshold);

			// outputting the mask and then markers to the desktop (6 total
			// images)

			try {
				ImageIO.write(
						imageSet[filenum].getMaskNoRed(),
						"jpg",
						new File(
								resultsPath + "/maskNoRed" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("failure to write image");
			}

			try {
				ImageIO.write(
						imageSet[filenum].getMaskGreen(),
						"jpg",
						new File(
								resultsPath + "/maskGreen" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("failure to write image");
			}

			try {
				ImageIO.write(
						imageSet[filenum].getMaskGreenNoRed(),
						"jpg",
						new File(
								resultsPath + "/maskGreenNoRed" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("failure to write image");
			}

			try {
				ImageIO.write(
						imageSet[filenum].getResultGreen(),
						"jpg",
						new File(
								resultsPath + "/resultGreen" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("failure to write image");
			}

			try {
				ImageIO.write(
						imageSet[filenum].getResultNoRed(),
						"jpg",
						new File(
								resultsPath + "/resultNoRed" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("failure to write image");
			}

			try {
				ImageIO.write(
						imageSet[filenum].getResultGreenNoRed(),
						"jpg",
						new File(
								resultsPath + "/resultGreenNoRed" + filenum + ".jpg"));
			} catch (Exception e) {
				System.out.println("well this failed");
			}


			// getting data from the image results we have
			double greenCoverage;
			double noRedCoverage;
			double greenNoRedCoverage;

			try {
				greenCoverage = imageSet[filenum].getMaskCoverageGreen();
			} catch (Exception e) {
				greenCoverage = 0;
			}
			try {
				noRedCoverage = imageSet[filenum].getMaskCoverageNoRed();

			} catch (Exception e) {
				noRedCoverage = 0;
			}
			try {
				greenNoRedCoverage = imageSet[filenum]
						.getMaskCoverageGreenNoRed();
			} catch (Exception e) {
				greenNoRedCoverage = 0;
			}


			// distributions (averages)
			double greenUnderGreenMean;
			double greenUnderNoRedMean;
			double greenUnderGreenNoRedMean;

			try {
				greenUnderGreenMean = imageSet[filenum]
						.getMean(imageSet[filenum].getListOfGreenUnderGreen());
			} catch (Exception e) {
				greenUnderGreenMean = 0;
			}
			try {
				greenUnderNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum].getListOfGreenUnderNoRed());
			} catch (Exception e) {
				greenUnderNoRedMean = 0;
			}
			try {
				greenUnderGreenNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfGreenUnderGreenNoRed());
			} catch (Exception e) {
				greenUnderGreenNoRedMean = 0;
			}

			double redUnderGreenMean;
			double redUnderNoRedMean;
			double redUnderGreenNoRedMean;

			try {
				redUnderGreenMean = imageSet[filenum].getMean(imageSet[filenum]
						.getListOfRedUnderGreen());
			} catch (Exception e) {
				redUnderGreenMean = 0;
			}
			try {
				redUnderNoRedMean = imageSet[filenum].getMean(imageSet[filenum]
						.getListOfRedUnderNoRed());
			} catch (Exception e) {
				redUnderNoRedMean = 0;
			}
			try {
				redUnderGreenNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfRedUnderGreenNoRed());
			} catch (Exception e) {
				redUnderGreenNoRedMean = 0;
			}

			double greenCombinedUnderGreenMean;
			double greenCombinedUnderNoRedMean;
			double greenCombinedUnderGreenNoRedMean;

			try {
				greenCombinedUnderGreenMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfGreenCombinedUnderGreen());
			} catch (Exception e) {
				greenCombinedUnderGreenMean = 0;
			}
			try {
				greenCombinedUnderNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfGreenCombinedUnderNoRed());
			} catch (Exception e) {
				greenCombinedUnderNoRedMean = 0;
			}
			try {
				greenCombinedUnderGreenNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfGreenCombinedUnderGreenNoRed());
			} catch (Exception e) {
				greenCombinedUnderGreenNoRedMean = 0;
			}

			double redCombinedUnderGreenMean;
			double redCombinedUnderNoRedMean;
			double redCombinedUnderGreenNoRedMean;

			try {
				redCombinedUnderGreenMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfRedCombinedUnderGreen());
			} catch (Exception e) {
				redCombinedUnderGreenMean = 0;
			}
			try {
				redCombinedUnderNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfRedCombinedUnderNoRed());
			} catch (Exception e) {
				redCombinedUnderNoRedMean = 0;
			}
			try {
				redCombinedUnderGreenNoRedMean = imageSet[filenum]
						.getMean(imageSet[filenum]
								.getListOfRedCombinedUnderGreenNoRed());
			} catch (Exception e) {
				redCombinedUnderGreenNoRedMean = 0;
			}

			// absolute intensities

			double totalGreenUnderGreen;
			double totalGreenUnderNoRed;
			double totalGreenUnderGreenNoRed;

			try {
				totalGreenUnderGreen = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenUnderGreen());
			} catch (Exception e) {
				totalGreenUnderGreen = 0;
			}

			try {
				totalGreenUnderNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenUnderNoRed());
			} catch (Exception e) {
				totalGreenUnderNoRed = 0;
			}

			try {
				totalGreenUnderGreenNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenUnderGreenNoRed());
			} catch (Exception e) {
				totalGreenUnderGreenNoRed = 0;
			}

			double totalRedUnderGreen;
			double totalRedUnderNoRed;
			double totalRedUnderGreenNoRed;

			try {
				totalRedUnderGreen = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedUnderGreen());
			} catch (Exception e) {
				totalRedUnderGreen = 0;
			}

			try {
				totalRedUnderNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedUnderNoRed());
			} catch (Exception e) {
				totalRedUnderNoRed = 0;
			}

			try {
				totalRedUnderGreenNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedUnderGreenNoRed());
			} catch (Exception e) {
				totalRedUnderGreenNoRed = 0;
			}

			double totalGreenColocalizedUnderGreen;
			double totalGreenColocalizedUnderNoRed;
			double totalGreenColocalizedUnderGreenNoRed;

			try {
				totalGreenColocalizedUnderGreen = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenCombinedUnderGreen());
			} catch (Exception e) {
				totalGreenColocalizedUnderGreen = 0;
			}

			try {
				totalGreenColocalizedUnderNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenCombinedUnderNoRed());
			} catch (Exception e) {
				totalGreenColocalizedUnderNoRed = 0;
			}

			try {
				totalGreenColocalizedUnderGreenNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfGreenCombinedUnderGreenNoRed());
			} catch (Exception e) {
				totalGreenColocalizedUnderGreenNoRed = 0;
			}

			double totalRedColocalizedUnderGreen;
			double totalRedColocalizedUnderNoRed;
			double totalRedColocalizedUnderGreenNoRed;

			try {
				totalRedColocalizedUnderGreen = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedCombinedUnderGreen());
			} catch (Exception e) {
				totalRedColocalizedUnderGreen = 0;
			}

			try {
				totalRedColocalizedUnderNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedCombinedUnderNoRed());
			} catch (Exception e) {
				totalRedColocalizedUnderNoRed = 0;
			}

			try {
				totalRedColocalizedUnderGreenNoRed = imageSet[filenum]
						.getTotalIntensity(imageSet[filenum]
								.getListOfRedCombinedUnderGreenNoRed());
			} catch (Exception e) {
				totalRedColocalizedUnderGreenNoRed = 0;
			}

			// median of green under each mask

			double medianGreenUnderGreen;
			double medianGreenUnderNoRed;
			double medianGreenUnderGreenNoRed;

			try {
				medianGreenUnderGreen = imageSet[filenum]
						.getMedian(imageSet[filenum].getListOfGreenUnderGreen());
			} catch (Exception e) {
				medianGreenUnderGreen = 0;
			}
			try {
				medianGreenUnderNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum].getListOfGreenUnderNoRed());
			} catch (Exception e) {
				medianGreenUnderNoRed = 0;
			}
			try {
				medianGreenUnderGreenNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfGreenUnderGreenNoRed());
			} catch (Exception e) {
				medianGreenUnderGreenNoRed = 0;
			}

			// median of red under each mask
			double medianRedUnderGreen;
			double medianRedUnderNoRed;
			double medianRedUnderGreenNoRed;

			try {
				medianRedUnderGreen = imageSet[filenum]
						.getMedian(imageSet[filenum].getListOfRedUnderGreen());
			} catch (Exception e) {
				medianRedUnderGreen = 0;
			}
			try {
				medianRedUnderNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum].getListOfRedUnderNoRed());
			} catch (Exception e) {
				medianRedUnderNoRed = 0;
			}

			try {
				medianRedUnderGreenNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfRedUnderGreenNoRed());
			} catch (Exception e) {
				medianRedUnderGreenNoRed = 0;
			}

			// median of green colocalized under each mask
			double medianGreenColocalizedUnderGreen;
			double medianGreenColocalizedUnderNoRed;
			double medianGreenColocalizedUnderGreenNoRed;

			try {
				medianGreenColocalizedUnderGreen = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfGreenCombinedUnderGreen());
			} catch (Exception e) {
				medianGreenColocalizedUnderGreen = 0;
			}
			try {
				medianGreenColocalizedUnderNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfGreenCombinedUnderNoRed());
			} catch (Exception e) {
				medianGreenColocalizedUnderNoRed = 0;
			}
			try {
				medianGreenColocalizedUnderGreenNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfGreenCombinedUnderGreenNoRed());
			} catch (Exception e) {
				medianGreenColocalizedUnderGreenNoRed = 0;
			}

			// median of red colocalized under each mask
			double medianRedColocalizedUnderGreen;
			double medianRedColocalizedUnderNoRed;
			double medianRedColocalizedUnderGreenNoRed;

			try {
				medianRedColocalizedUnderGreen = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfRedCombinedUnderGreen());
			} catch (Exception e) {
				medianRedColocalizedUnderGreen = 0;
			}
			try {
				medianRedColocalizedUnderNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfRedCombinedUnderNoRed());
			} catch (Exception e) {
				medianRedColocalizedUnderNoRed = 0;
			}
			try {
				medianRedColocalizedUnderGreenNoRed = imageSet[filenum]
						.getMedian(imageSet[filenum]
								.getListOfRedCombinedUnderGreenNoRed());
			} catch (Exception e) {
				medianRedColocalizedUnderGreenNoRed = 0;
			}

			// st dev of green under each mask

			double stDevGreenUnderGreen;
			double stDevGreenUnderNoRed;
			double stDevGreenUnderGreenNoRed;

			try {
				stDevGreenUnderGreen = imageSet[filenum]
						.getStDev(imageSet[filenum].getListOfGreenUnderGreen());
			} catch (Exception e) {
				stDevGreenUnderGreen = 0;
			}
			try {
				stDevGreenUnderNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum].getListOfGreenUnderNoRed());
			} catch (Exception e) {
				stDevGreenUnderNoRed = 0;
			}
			try {
				stDevGreenUnderGreenNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfGreenUnderGreenNoRed());
			} catch (Exception e) {
				stDevGreenUnderGreenNoRed = 0;
			}

			// st dev of red under each mask
			double stDevRedUnderGreen;
			double stDevRedUnderNoRed;
			double stDevRedUnderGreenNoRed;
			try {
				stDevRedUnderGreen = imageSet[filenum]
						.getStDev(imageSet[filenum].getListOfRedUnderGreen());
			} catch (Exception e) {
				stDevRedUnderGreen = 0;
			}
			try {
				stDevRedUnderNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum].getListOfRedUnderNoRed());
			} catch (Exception e) {
				stDevRedUnderNoRed = 0;
			}
			try {
				stDevRedUnderGreenNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfRedUnderGreenNoRed());
			} catch (Exception e) {
				stDevRedUnderGreenNoRed = 0;
			}

			// st dev of green colocalized under each mask
			double stDevGreenColocalizedUnderGreenNoRed;
			double stDevGreenColocalizedUnderGreen;
			double stDevGreenColocalizedUnderNoRed;

			try {
				stDevGreenColocalizedUnderGreen = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfGreenCombinedUnderGreen());
			} catch (Exception e) {
				stDevGreenColocalizedUnderGreen = 0;
			}

			try {
				stDevGreenColocalizedUnderNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfGreenCombinedUnderNoRed());
			} catch (Exception e) {
				stDevGreenColocalizedUnderNoRed = 0;
			}

			try {
				stDevGreenColocalizedUnderGreenNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfGreenCombinedUnderGreenNoRed());
			} catch (Exception e) {
				stDevGreenColocalizedUnderGreenNoRed = 0;
			}

			// st dev of red colocalized under each mask
			double stDevRedColocalizedUnderGreen;
			double stDevRedColocalizedUnderNoRed;
			double stDevRedColocalizedUnderGreenNoRed;

			try {
				stDevRedColocalizedUnderGreen = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfRedCombinedUnderGreen());
			} catch (Exception e) {
				stDevRedColocalizedUnderGreen = 0;
			}

			try {
				stDevRedColocalizedUnderNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfRedCombinedUnderNoRed());
			} catch (Exception e) {
				stDevRedColocalizedUnderNoRed = 0;
			}

			try {
				stDevRedColocalizedUnderGreenNoRed = imageSet[filenum]
						.getStDev(imageSet[filenum]
								.getListOfRedCombinedUnderGreenNoRed());
			} catch (Exception e) {
				stDevRedColocalizedUnderGreenNoRed = 0;
			}

			// percent of green under each mask
			double percentGreenUnderGreen = (double) ((double) imageSet[filenum]
					.getListOfGreenUnderGreen().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenCoverage;
			double percentGreenUnderNoRed = (double) ((double) imageSet[filenum]
					.getListOfGreenUnderNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ noRedCoverage;
			double percentGreenUnderGreenNoRed = (double) ((double) imageSet[filenum]
					.getListOfGreenUnderGreenNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenNoRedCoverage;

			// percent of red under each mask
			double percentRedUnderGreen = (double) ((double) imageSet[filenum]
					.getListOfRedUnderGreen().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenCoverage;
			double percentRedUnderNoRed = (double) ((double) imageSet[filenum]
					.getListOfRedUnderNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ noRedCoverage;
			double percentRedUnderGreenNoRed = (double) ((double) imageSet[filenum]
					.getListOfRedUnderGreenNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenNoRedCoverage;

			// colocalized under each mask
			// error checking - make sure greenCombined and redCombined are the
			// same size lists
			if (!(imageSet[filenum].getListOfGreenCombinedUnderGreen().size() == imageSet[filenum]
					.getListOfRedCombinedUnderGreen().size())) {
				throw new RuntimeException("you messed up");
			}

			double percentColocalizedUnderGreen = (double) ((double) imageSet[filenum]
					.getListOfGreenCombinedUnderGreen().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenCoverage;
			double percentColocalizedUnderNoRed = (double) ((double) imageSet[filenum]
					.getListOfGreenCombinedUnderNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ noRedCoverage;
			double percentColocalizedUnderGreenNoRed = (double) ((double) imageSet[filenum]
					.getListOfGreenCombinedUnderGreenNoRed().size() / (double) imageSet[filenum]
							.getTotalPixels())
							/ greenNoRedCoverage;

			// logic for writing results to file

			// if this is the first line of the text file, create the headings
			// needed for the excel file
			if (filenum == 0) {
				bw.write("Number" + delimeter + "Mask Name" + delimeter
						+ "Marker Name" + delimeter + "% mask coverage (green)"
						+ delimeter + "% mask coverage (noRed)" + delimeter
						+ "% mask coverage (greenNoRed)" + delimeter
						+ "% green under green" + delimeter
						+ "% green under noRed" + delimeter
						+ "% green under greenNoRed" + delimeter
						+ "%redUnderGreen" + delimeter + "% red under noRed"
						+ delimeter + "% red under greenNoRed" + delimeter
						+ "%colocalized under green" + delimeter
						+ "%colocalized under noRed" + delimeter
						+ "%colocalized under greenNoRed" + delimeter
						+ "mean green under green" + delimeter
						+ "mean green under noRed" + delimeter
						+ "mean green under greenNoRed" + delimeter
						+ "mean red under green" + delimeter
						+ "mean red under noRed" + delimeter
						+ "mean red under greenNoRed" + delimeter
						+ "mean green colocalized under green" + delimeter
						+ "mean green colocalized under noRed" + delimeter
						+ "mean green colocalized under greenNoRed" + delimeter
						+ "mean red colocalized under green" + delimeter
						+ "mean red colocalized under noRed" + delimeter
						+ "mean red colocalized under greenNoRed" + delimeter
						+ "median green under green" + delimeter
						+ "median green under noRed" + delimeter
						+ "median green under greenNoRed" + delimeter
						+ "median red under green" + delimeter
						+ "median red under noRed" + delimeter
						+ "median red under greenNoRed" + delimeter
						+ "median green colocalized under green" + delimeter
						+ "median green colocalized under noRed" + delimeter
						+ "median green colocalized under greenNoRed"
						+ delimeter + "median red colocalized under green"
						+ delimeter + "median red colocalized under noRed"
						+ delimeter + "median red colocalized under greenNoRed"
						+ delimeter + "st.dev green under green" + delimeter
						+ "st.dev green under noRed" + delimeter
						+ "st.dev green under greenNoRed" + delimeter
						+ "st.dev red under green" + delimeter
						+ "st.dev red under noRed" + delimeter
						+ "st.dev red under greenNoRed" + delimeter
						+ "st.dev green colocalized under green" + delimeter
						+ "st.dev green colocalized under noRed" + delimeter
						+ "st.dev green colocalized under greenNoRed"
						+ delimeter + "st.dev red colocalized under green"
						+ delimeter + "st.dev red colocalized under noRed"
						+ delimeter + "st.dev red colocalized under greenNoRed"
						+ delimeter + "total intensity green under green"
						+ delimeter + "total intensity green under noRed"
						+ delimeter + "total intensity green under greenNoRed"
						+ delimeter + "total intensity red under green"
						+ delimeter + "total intensity red under noRed"
						+ delimeter + "total intensity red under greenNoRed"
						+ delimeter
						+ "total intensity green colocalized under green"
						+ delimeter
						+ "total intensity green colocalized under noRed"
						+ delimeter
						+ "total intensity green colocalized under greenNoRed"
						+ delimeter
						+ "total intensity red colocalized under green"
						+ delimeter
						+ "total intensity red colocalized under noRed"
						+ delimeter
						+ "total intensity red colocalized under greenNoRed");
			}
			// fill in the data as appropriate. comma delimeters, newline
			// characters before each line
			bw.write("\n" + filenum + delimeter);
			bw.write(mask[filenum].getName() + delimeter);
			bw.write(markers[filenum].getName() + delimeter);
			bw.write(Double.toString(greenCoverage) + delimeter);
			bw.write(Double.toString(noRedCoverage) + delimeter);
			bw.write(Double.toString(greenNoRedCoverage) + delimeter);
			bw.write(Double.toString(percentGreenUnderGreen) + delimeter);
			bw.write(Double.toString(percentGreenUnderNoRed) + delimeter);
			bw.write(Double.toString(percentGreenUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(percentRedUnderGreen) + delimeter);
			bw.write(Double.toString(percentRedUnderNoRed) + delimeter);
			bw.write(Double.toString(percentRedUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(percentColocalizedUnderGreen) + delimeter);
			bw.write(Double.toString(percentColocalizedUnderNoRed) + delimeter);
			bw.write(Double.toString(percentColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(greenUnderGreenMean) + delimeter);
			bw.write(Double.toString(greenUnderNoRedMean) + delimeter);
			bw.write(Double.toString(greenUnderGreenNoRedMean) + delimeter);
			bw.write(Double.toString(redUnderGreenMean) + delimeter);
			bw.write(Double.toString(redUnderNoRedMean) + delimeter);
			bw.write(Double.toString(redUnderGreenNoRedMean) + delimeter);
			bw.write(Double.toString(greenCombinedUnderGreenMean) + delimeter);
			bw.write(Double.toString(greenCombinedUnderNoRedMean) + delimeter);
			bw.write(Double.toString(greenCombinedUnderGreenNoRedMean)
					+ delimeter);
			bw.write(Double.toString(redCombinedUnderGreenMean) + delimeter);
			bw.write(Double.toString(redCombinedUnderNoRedMean) + delimeter);
			bw.write(Double.toString(redCombinedUnderGreenNoRedMean)
					+ delimeter);
			bw.write(Double.toString(medianGreenUnderGreen) + delimeter);
			bw.write(Double.toString(medianGreenUnderNoRed) + delimeter);
			bw.write(Double.toString(medianGreenUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(medianRedUnderGreen) + delimeter);
			bw.write(Double.toString(medianRedUnderNoRed) + delimeter);
			bw.write(Double.toString(medianRedUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(medianGreenColocalizedUnderGreen)
					+ delimeter);
			bw.write(Double.toString(medianGreenColocalizedUnderNoRed)
					+ delimeter);
			bw.write(Double.toString(medianGreenColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(medianRedColocalizedUnderGreen)
					+ delimeter);
			bw.write(Double.toString(medianRedColocalizedUnderNoRed)
					+ delimeter);
			bw.write(Double.toString(medianRedColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(stDevGreenUnderGreen) + delimeter);
			bw.write(Double.toString(stDevGreenUnderNoRed) + delimeter);
			bw.write(Double.toString(stDevGreenUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(stDevRedUnderGreen) + delimeter);
			bw.write(Double.toString(stDevRedUnderNoRed) + delimeter);
			bw.write(Double.toString(stDevRedUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(stDevGreenColocalizedUnderGreen)
					+ delimeter);
			bw.write(Double.toString(stDevGreenColocalizedUnderNoRed)
					+ delimeter);
			bw.write(Double.toString(stDevGreenColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(stDevRedColocalizedUnderGreen) + delimeter);
			bw.write(Double.toString(stDevRedColocalizedUnderNoRed) + delimeter);
			bw.write(Double.toString(stDevRedColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(totalGreenUnderGreen) + delimeter);
			bw.write(Double.toString(totalGreenUnderNoRed) + delimeter);
			bw.write(Double.toString(totalGreenUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(totalRedUnderGreen) + delimeter);
			bw.write(Double.toString(totalRedUnderNoRed) + delimeter);
			bw.write(Double.toString(totalRedUnderGreenNoRed) + delimeter);
			bw.write(Double.toString(totalGreenColocalizedUnderGreen)
					+ delimeter);
			bw.write(Double.toString(totalGreenColocalizedUnderNoRed)
					+ delimeter);
			bw.write(Double.toString(totalGreenColocalizedUnderGreenNoRed)
					+ delimeter);
			bw.write(Double.toString(totalRedColocalizedUnderGreen) + delimeter);
			bw.write(Double.toString(totalRedColocalizedUnderNoRed) + delimeter);
			bw.write(Double.toString(totalRedColocalizedUnderGreenNoRed)
					+ delimeter);

		}
		bw.close();

	}
}
