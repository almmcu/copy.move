package image.operations.keypoint;

import image.operations.Consts;
import image.operations.distance.CosineSimilarity2;
import image.operations.distance.VecorSimilarities;
import image.operations.distance.CosineSimilarity;
import org.opencv.core.Core;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;

import java.util.*;

/**
 *<h1>Key Point Detection and Description</h1>
 *
 * @author alim
 * @version 1.0
 * @since 6.12.2016.
 * @see <a href="http://www.opencv.org">OpenCV</a>
 */
public class KeyPointDetector {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private List<ArrayList> descriptor_list;//  128 bit descriptors list
    public static Mat objectImage;

    private String inputImagePath = Consts.absolutePath.concat(Consts.IMAGE_PATH ); // Input Image Path
    private String outputImagePath = Consts.absolutePath.concat(Consts.IMAGE_PATH_OUTPUT ); // Output Image Path

    // 0 for Cosine Similarity,
    // 1 for Euclidean Distance,
    // 2 for Mix Distance,
    // others for Cosine Similarity 2 (with the threshold 0.6 )
    private int distance = 0;

    /**
     * This method is used to detect image key points and their descriptors.
     * SIFT algorithm is applied on an image to detect the key points
     *
     */
    public void detectKeyPoint(){
        System.out.println("Started....");
        System.out.println("Loading images...");
        objectImage = Highgui.imread(inputImagePath, Highgui.CV_LOAD_IMAGE_COLOR);
        //objectImage = Highgui.imread(inputImagePath, Highgui.IMREAD_GRAYSCALE);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.FAST);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.printf("keypoints = %s%n", keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);



        KeyPoint[] descpriptors = objectDescriptors.toArray();
        System.out.println("descriptors = " + descpriptors);;

        Size ketP = objectKeyPoints.size();
        System.out.println("Key point number:  "+ketP);

        Size desc = objectDescriptors.size();
        System.out.println("Descriptor size:  "+desc);

        String[] mat_dump = objectDescriptors.dump().split(";");

        // Descriptor leri array liste aktar
        System.out.println("Getting descriptor vector list...");
        getDescriptorList (mat_dump);
        DesriptorFile desriptorFile = new DesriptorFile();
        desriptorFile.writeFile(keypoints, descriptor_list);

        /*
         *
         * Computing Cosine Similarities 1 or 2
         * or
         * Computing Euclidean distance
         * for Matching
         *
         * */
        int matchPointNumber=0;

        if (distance == 0){
            matchPointNumber = cosineSimilarity(descriptor_list, keypoints);
            System.out.println("Cosine Similarity match point number = " +  matchPointNumber);
            outputImagePath += "_C_" + matchPointNumber + ".jpg";
        }
        else if (distance == 1) {
            matchPointNumber = euclideanDistance(descriptor_list, keypoints);
            System.out.println("Euclidean distance match point number = " + +  matchPointNumber);
            outputImagePath += "_O_" + matchPointNumber + ".jpg";
        }else if (distance == 2) {
            matchPointNumber = mixDistance(descriptor_list, keypoints);
            System.out.println("Mix distance match point number = " + +  matchPointNumber);
            outputImagePath += "_M_" + matchPointNumber + ".jpg";
        }
        else if (distance == 3){
            matchPointNumber = cosineSimilarity2(descriptor_list, keypoints);
            System.out.println("Cosine Similarity match point number = " + matchPointNumber);
            outputImagePath += "_C2_" + matchPointNumber + ".jpg";
        }

        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);


        System.out.println("Drawing key points on object image...");
        //Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);
        //Features2d.drawMatches2(objectImage,);
        //Highgui.imwrite(outputImagePath, outputImage);
        Highgui.imwrite(outputImagePath, objectImage);


        System.out.println("Writing output image...");

    }
    /**
     * <h1>Similarity Matrix Calculation using Cosine Similarity</h1>
     *
     * This method is used to find the similarities of one key point to
     * all the other key points.
     * Vectors similarities is detected by using Cosine Similarity
     *
     * @param descriptor_list Vektor description of the key points. Vector size is 128
     * @param keypoints Key points detected by the SIFT algorithm
     * @return int This returns the number of similar key points
     *             that represent the copy move forgery region
     */
    private int cosineSimilarity(List<ArrayList> descriptor_list, KeyPoint[] keypoints){

        System.out.println("Computing Cosine Similarity...");
        CosineSimilarity cosineSimilarity =  new CosineSimilarity();
        ArrayList finalList = cosineSimilarity.getSimilarityList(descriptor_list, keypoints);
        return appyRansac(
                matchingPoints(finalList,finalList),
                keypoints
        );
        //return appyRansac( cosineSimilarity.getSimilarityList(descriptor_list, keypoints), keypoints);
    }

    /**
     * <h1>Similarity Matrix Calculation using Cosine Similarity (with the threshold 0.6 )</h1>
     *
     * This method is used to find the similarities of one key point to
     * all the other key points.
     * Vectors similarities is detected by using Cosine Similarity
     *
     * @param descriptor_list Vektor description of the key points. Vector size is 128
     * @param keypoints Key points detected by the SIFT algorithm
     * @return int This returns the number of similar key points
     *             that represent the copy move forgery region
     */
    private int cosineSimilarity2(List<ArrayList> descriptor_list, KeyPoint[] keypoints){

        System.out.println("Computing Cosine Similarity (with the threshold 0.6 ) ...");
        CosineSimilarity2 cosineSimilarity2 =  new CosineSimilarity2();
        return cosineSimilarity2.getSimilarityList(descriptor_list, keypoints);
    }

    /**
     * <h1>Similarity Matrix Calculation using Euclidean Distance</h1>
     *
     * This method is used to find the similarities of one key point to
     * all the other key points.
     * Vectors similarities is detected by using Euclidean Distance
     *
     * @param descriptor_list Vektor description of the key points. Vector size is 128
     * @param keypoints Key points detected by the SIFT algorithm
     * @return int This returns the number of similar key points
     *             that represent the copy move forgery region
     */
    private int euclideanDistance(List<ArrayList> descriptor_list, KeyPoint[] keypoints){

        System.out.println("Computing Similarity using Euclidean distance...");
        VecorSimilarities vecorSimilarities = new VecorSimilarities();
        ArrayList finalList =  vecorSimilarities.similarityMatrix(descriptor_list);

        return appyRansac(
                matchingPoints(finalList,finalList),
                keypoints
        );
        //return appyRansac(vecorSimilarities.similarityMatrix(descriptor_list), keypoints);
    }

    /**
     * <h1>RANSAC remove false detection</h1>
     *
     * This method is used to eliminate the false matching key points and
     * detect the copy move forgery region correctly
     *
     * @param s_arrayLists Similar key point list after similarity matrix calculation
     * @param keypoints Key points detected by the SIFT algorithm
     * @return int This returns the number of similar key points
     *             that represent the copy move forgery region
     */

    /**
     * <h1>Mixing Method for distances</h1>
     *
     * @param descriptor_list Vektor description of the key points. Vector size is 128
     * @param keypoints Key points detected by the SIFT algorithm
     * @return int This returns the number of similar key points
     *             that represent the copy move forgery region
     */
    private int mixDistance(List<ArrayList> descriptor_list, KeyPoint[] keypoints){

        System.out.println("Computing Similarity using Euclidean distance...");
        VecorSimilarities vecorSimilarities = new VecorSimilarities();

        System.out.println("Computing Cosine Similarity...");
        CosineSimilarity cosineSimilarity =  new CosineSimilarity();

        return appyRansac(
                matchingPoints(
                        cosineSimilarity.getSimilarityList(descriptor_list,keypoints),
                        cosineSimilarity.getSimilarityList(descriptor_list,keypoints)),
                keypoints
        );
        //return appyRansac( cosineSimilarity.getSimilarityList(descriptor_list, keypoints), keypoints);
    }

    private int appyRansac(ArrayList s_arrayLists, KeyPoint[] keypoints){
        List<Point> pts1 = new ArrayList<Point>();
        List<Point> pts2 = new ArrayList<Point>();

        for (int i = 0; i < s_arrayLists.size(); i = i+2) {

            pts1.add(new Point( keypoints[(Integer) s_arrayLists.get(i)].pt.x,keypoints[(Integer) s_arrayLists.get(i)].pt.y) );
            pts2.add(new Point( keypoints[(Integer) s_arrayLists.get(i + 1)].pt.x,keypoints[(Integer) s_arrayLists.get(i + 1)].pt.y) );
        }

        Mat outputMask = new Mat();
        MatOfPoint2f pts1Mat = new MatOfPoint2f();
        pts1Mat.fromList(pts1);
        MatOfPoint2f pts2Mat = new MatOfPoint2f();
        pts2Mat.fromList(pts2);

        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 80, outputMask);

        /*
         * Draw lines after RANSAC elimination
         */
        System.out.println("Drawing matching lines on object image...");
        int matchPointNumber = 0;
        for (int i = 0; i < pts1.size(); i++) {
            if (outputMask.get(i, 0)[0]!= 0.0) continue;
            Core.line(
                    objectImage,
                    pts1.get(i),
                    pts2.get(i),
                    new Scalar(64, 16, 128)
            );
            //System.out.println(pts1.get(i) + "     "+ pts2.get(i));
            matchPointNumber++;

        }
        return matchPointNumber;
    }

    /**
     * <h1>Get Descriptor List</h1>
     *
     * This method is used to convert descriptor of key points string to int values
     *
     * @param mat_dump String of key points descriptors
     *
     */
    private  void getDescriptorList (String[] mat_dump){
        descriptor_list = new ArrayList<ArrayList>();
        ArrayList<Integer> singleDescriptorList ;
        for (String d:mat_dump) {
            //    System.out.println("d = " + d);
            String[] desc_row = d.split(",");
            singleDescriptorList = new ArrayList();
            for (String desc_numbers :
                    desc_row) {
                if (desc_numbers.contains("\n "))
                    // desc_numbers.replace("\n ", "");
                    desc_numbers =  desc_numbers.substring(2);

                if (desc_numbers.contains("]"))
                    desc_numbers = desc_numbers.replace("]", "");
                String one_number = desc_numbers.substring(1);
                singleDescriptorList.add(Integer.parseInt(one_number));
                //System.out.println(one_number);
            }
            descriptor_list.add(singleDescriptorList);
        }
    }

    private ArrayList matchingPoints(ArrayList euclideanDistanceList, ArrayList cosineSimilarityList){
        ArrayList matchList = new ArrayList();
        int index = 0;
        ArrayList l1 = new ArrayList();
        ArrayList l2 = new ArrayList();
        ArrayList l3 = new ArrayList();
        ArrayList l4 = new ArrayList();
        for (int i = 0; i <euclideanDistanceList.size(); i=i+2) {
            l1.add(euclideanDistanceList.get(i));
            l2.add(euclideanDistanceList.get(i +1));
        }for (int i = 0; i <cosineSimilarityList.size(); i=i+2) {
            l3.add(cosineSimilarityList.get(i));
            l4.add(cosineSimilarityList.get(i+1));
        }
        for (int i = 0; i < euclideanDistanceList.size(); i = i+2) {
            if (cosineSimilarityList.contains( euclideanDistanceList.get(i))){
                index = cosineSimilarityList.indexOf(euclideanDistanceList.get(i));
                if (cosineSimilarityList.get(index + 1).equals(euclideanDistanceList.get(i + 1)) && index%2==0){
                    matchList.add(euclideanDistanceList.get(i));
                    matchList.add(euclideanDistanceList.get(i+1));
                }
            }
        }
        return matchList;
    }
}