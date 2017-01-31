import image.operations.Consts;
import image.operations.keypoint.KeyPointDetector;
import org.opencv.core.*;

/**
 * Created by Oda114 on 15.11.2016.
 * Basic OpenCV operations
 */
public class Test {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static final String TAG = "OpenCV in Test Class";

    public static void main(String[] args) {

        // Welcome to OpenCV
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());

        String a = Consts.absolutePath;
        KeyPointDetector keyPointDetector = new KeyPointDetector();
        keyPointDetector.detectKeyPoint();

        /*
        // reading image
        Mat img = Highgui.imread(Consts.IMAGE_PATH);

        // Keypoint extracting
        FeatureDetector SURF = FeatureDetector.create(FeatureDetector.SURF);
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        // İki reim içinde keypoints hesabı
        SURF.detect(img, keyPoints);

        // Print number of extracted key points
        Size ketP = keyPoints.size();
        System.out.println(ketP);

        // description initializing
        DescriptorExtractor SurfExtractor = DescriptorExtractor
                .create(DescriptorExtractor.SURF);
        Mat descriptors = new Mat();

        // Description
        SurfExtractor.compute(img, keyPoints, descriptors);

        // Showing readed image to user
        ConvertMatToBufferedImage convertMatToBufferedImage =
                new ConvertMatToBufferedImage(img);
        DisplayImage displayImage =
                new DisplayImage(convertMatToBufferedImage.Mat2BufferedImage());
        displayImage.display();
*/
    }
}
