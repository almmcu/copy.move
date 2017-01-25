package image.operations;

/**
 * <h1>Constants</h1>
 *
 * Constants of the project
 *
 * @author  alim
 * @version 1.0
 * @since   6.12.2016
 */
public class Consts {

   /**
     * {@value #IMAGE_PATH} Holds the path of the images
     */
    public static final String IMAGE_PATH = "C:\\Users\\User\\Desktop\\JavaOpenCv1-master\\Pictures\\028_F.png";

    /**
     * {@value #IMAGE_PATH_OUTPUT} Holds the path of the output image
     */
    public static final String IMAGE_PATH_OUTPUT = "C:\\Users\\User\\Desktop\\JavaOpenCv1-master\\Pictures\\098_F_OUTPUT.jpg";



    public static final String IMAGE_PATH_2 = "C:\\Users\\Oda\\Desktop\\ICCESEN_2016_Pictures\\2.jpg";
    public static final String IMAGE_PATH_2_1 = "C:\\Users\\Oda\\Desktop\\deney\\resim1\\1.jpg";
    public static final String IMAGE_PATH_2_1_FORGERY = "C:\\Users\\Ali\\Desktop\\JavaOpenCv1\\Camera\\4_forgery.jpg";
    public static final String IMAGE_PATH_2_2 = "C:\\Users\\Oda\\Desktop\\deney\\resim1\\2.jpg";
    public static final String IMAGE_PATH_2_3 = "C:\\Users\\Oda\\Desktop\\deney\\resim1\\3.jpg";


    public static final String IMAGE_PATH_OUTPUT_2_1 = "C:\\Users\\Ali\\Desktop\\JavaOpenCv1\\Camera\\4_Output.jpg";
    public static final String IMAGE_PATH_OUTPUT_2_2 = "C:\\Users\\Ali\\Desktop\\JavaOpenCv1\\Camera\\4_Output1.jpg";
    public static final String IMAGE_PATH_OUTPUT_2_3 = "C:\\Users\\Oda\\Desktop\\deney\\resim1\\3_Output.jpg";


    public static final String IMAGE_PATH_028_F = "C:\\Users\\Ali\\Desktop\\JavaOpenCv1\\Camera\\098_F.png";
    public static final String IMAGE_PATH_028_F_OUTPUT = "C:\\Users\\Ali\\Desktop\\JavaOpenCv1\\Camera\\098_F_Output.png";



    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Consts(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}

