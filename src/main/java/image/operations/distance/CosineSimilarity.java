package image.operations.distance;

import image.operations.model.Angles;
import org.opencv.core.Point;
import org.opencv.features2d.KeyPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 17.01.2017.
 * Cosine Similarity
 */
public class CosineSimilarity {

  public ArrayList getSimilarityList (List<ArrayList> descriptor_list, KeyPoint[] keypoints) {

        double[][] angles = new double[descriptor_list.size()][descriptor_list.size()];

        List<Angles> points_angles_list = new ArrayList<Angles>();

        for (int i = 0; i < descriptor_list.size(); i++) {

            for (int j = 0; j < descriptor_list.size(); j++) {

                int num = 0;
                double dense_1 = 0;
                double dense_2 = 0;
                for (int z = 0; z < 128; z++) {

                    num += (Integer) descriptor_list.get(i).get(z) * (Integer) descriptor_list.get(j).get(z);
                    dense_1 += Math.pow((Integer) descriptor_list.get(i).get(z), 2);
                    dense_2 += Math.pow((Integer) descriptor_list.get(j).get(z), 2);
                }
                double dense = Math.sqrt(dense_1 * dense_2);
                double cos = num / dense;
                angles[i][j] = Math.acos(cos);
                if (angles[i][j] == 0) angles[i][j]=Double.MAX_VALUE;
               if (cos < 0.6) continue;
                Angles angle = new Angles();
                angle.setAngle(angles[i][j]);
                angle.setPoint1(i);
                angle.setPoint2(j);

                angle.setP1(new Point(keypoints[i].pt.x, keypoints[i].pt.y));
                angle.setP2(new Point(keypoints[j].pt.x, keypoints[j].pt.y));

                points_angles_list.add(angle);

            }
            }
      return getSimilarity(angles, descriptor_list.size());


    }

    ArrayList getSimilarity (double similarityMatrix [][], int size){

        ArrayList matcingPointList = new ArrayList<Integer>();
        for (int i = 0; i <size; i++) {
            double min =  similarityMatrix[i][0];
            int min_index_row = 0;
            for (int j = 0; j < size; j++) {
                if (min > similarityMatrix[i][j]) {
                    min = similarityMatrix[i][j];
                    min_index_row = j;
                }
            }
            int min_index_column = min_index_row;
            min = similarityMatrix[0][min_index_row];
            for (int k = 0; k < size; k++) {
                if (min > similarityMatrix[k][min_index_row]) {
                    min = similarityMatrix[k][min_index_row];
                    min_index_column = k;
                }
            }
            if (min_index_column == i ) {
                // System.out.println("[ " + min_index_column + " ] [" + min_index_row + " ]  " +
                //      "Değer = " + similarityMatrix[min_index_column][min_index_row] );

                matcingPointList.add(min_index_column);
                matcingPointList.add(min_index_row);
            }
        }
        return matcingPointList;
    }
}


