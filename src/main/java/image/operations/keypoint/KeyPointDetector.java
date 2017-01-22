package image.operations.keypoint;

import image.operations.Consts;
import image.operations.distance.VecorSimilarities;
import image.operations.distance.CosineSimilarity;
import image.operations.model.Angles;
import org.opencv.core.Core;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;

import java.util.*;

/**
 * Created by Oda114 on 6.12.2016.
 * Key Point Detection and Description
 */
public class KeyPointDetector {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    List<ArrayList> descriptor_list;//  128 bit desriptors list
    List<Angles> points_angles_list;//  128 bit desriptors list
    Mat objectImage;
    public void detectKeyPoint(){
        System.out.println("Started....");
        System.out.println("Loading images...");
        objectImage = Highgui.imread(Consts.IMAGE_PATH, Highgui.CV_LOAD_IMAGE_COLOR);
        //Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println("keypoints = " + keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);



        KeyPoint[] descpriptors = objectDescriptors.toArray();
        System.out.println("descpriptors = " + descpriptors);;

        Size ketP = objectKeyPoints.size();
        System.out.println("Keypoint number:  "+ketP);

        Size desc = objectDescriptors.size();
        System.out.println("Descriptor size:  "+desc);

        String[] mat_dump = objectDescriptors.dump().split(";");

        // Descriptor leri array liste aktar
        System.out.println("Getting decriptor vector list...");
        getDescriptorList (mat_dump);


        /**
         *
         * Computing Cosine Similarity...
         *
         * */

        //System.out.println("Computing Cosine Similarity...");
        //System.out.println("Cosine Similarity match point number = " + cosineSimilarity(descriptor_list, keypoints) );


        /**
         *
         * Computing Similarity using Euclidean distance...
         *
         * */
        System.out.println("Computing Similarity using Euclidean distance...");
        System.out.println("Euclidean distance match point number = " + euclideanDistance(descriptor_list, keypoints) );



        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);


        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);
        //Features2d.drawMatches2(objectImage,);
        Highgui.imwrite(Consts.IMAGE_PATH_OUTPUT, outputImage);


        System.out.println("Writing output image...");

    }

    private int cosineSimilarity(List<ArrayList> descriptor_list, KeyPoint[] keypoints){
        /**
         *
         *  Similarity Matrix Calculation
         *  Cosine Similarity
         *
         * */
        ArrayList matcingPointList;
        CosineSimilarity cosineSimilarity =  new CosineSimilarity();
        matcingPointList = cosineSimilarity.getSimilarityLİst(descriptor_list, keypoints);
        System.out.println("Matching point number " + matcingPointList.size()/4);

        /**
         System.out.println("Sorting angles...");
         // Sıralama işlemi
         Collections.sort(points_angles_list, new AnglesCompare());

         */


        System.out.println("Drawing matching lines on object image...");


       return appyRansac( matcingPointList, keypoints);

    /*    for (int i = 0; i < matcingPointList.size()-1; i= i+2) {

            Core.line(
                    objectImage,
                    new Point(keypoints[(Integer) matcingPointList.get(i)].pt.x, keypoints[(Integer) matcingPointList.get(i)].pt.y),
                    new Point(keypoints[(Integer) matcingPointList.get(i+1)].pt.x, keypoints[(Integer) matcingPointList.get(i+1)].pt.y),
                    new Scalar(0, 255, 0)
            );
        }
*/


    }

    private int euclideanDistance(List<ArrayList> descriptor_list, KeyPoint[] keypoints){

        /**
         *
         *  Similarity Matrix Calculation
         *  Euclidean distance
         *
         * */
        VecorSimilarities vecorSimilarities = new VecorSimilarities();
        ArrayList s_arrayLists = vecorSimilarities.similarityMatrix(descriptor_list);

       return appyRansac(s_arrayLists, keypoints);
    }
    private int appyRansac(ArrayList s_arrayLists, KeyPoint[] keypoints){

        /**
         *
         * RANSAC remove false detection
         *
         * */

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

        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 15, outputMask);

        /**
         *
         * Draw lines after RANSAC
         *
         * */
        int matchPointNumber = 0;
        for (int i = 0; i < pts1.size(); i++) {
            if (outputMask.get(i, 0)[0] == 0.0) continue;
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



    public class AnglesCompare implements Comparator<Angles> {

        public int compare(Angles o1, Angles o2) {

            if (o1.getAngle() < o2.getAngle()) return -1;
            if (o1.getAngle() == o2.getAngle()) return 0;
            return 1;

        }
    }
}
