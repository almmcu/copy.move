package image.operations.distance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10.01.2017.
 * Similarity Matric Calculation
 */
public class VecorSimilarities {

    public static void main(String[] args) {
        ArrayList<ArrayList> vectorLİst = new ArrayList<ArrayList>();
        ArrayList <Integer> vector;
        for (int i = 0; i <10; i++) {
            vector = new ArrayList<Integer>();
            for (int j = 0; j < 128; j++) {
                vector.add((int) (0 + (Math.random() * (128))));
            }
            vectorLİst.add(vector);
        }
        System.out.println(vectorLİst);
        VecorSimilarities vecorSimilarities =  new VecorSimilarities();
        vecorSimilarities.similarityMatrix(vectorLİst);
    }

    public ArrayList similarityMatrix (List<ArrayList> vList){
        double similarityMatrix [][] = new double[vList.size()][vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            for (int j = 0; j < vList.size(); j++) {

                double sum = 0;
                for (int k = 0; k < 128; k++) {
                    sum += Math.pow((Integer) vList.get(i).get(k) - (Integer) vList.get(j).get(k), 2);
                }

                if (sum == 0 )
                    similarityMatrix[i][j] = Integer.MAX_VALUE;

                else
                    similarityMatrix[i][j] = Math.sqrt(sum);

            }
        }
        //System.out.println("similarityMatrix = " + similarityMatrix[0][0]);
        //similarityMatrix[5][4] = -2;
        int counter = 0 ;
        ArrayList s_arrayLists = new ArrayList();
        for (int i = 0; i <vList.size(); i++) {
            double min =  similarityMatrix[i][0];
            int min_index_row = 0;
            for (int j = 0; j < vList.size(); j++) {
                if (min > similarityMatrix[i][j]) {
                    min = similarityMatrix[i][j];
                    min_index_row = j;
                }
            }
            int min_index_column = min_index_row;
            double min_2 = similarityMatrix[i][min_index_row];
            // min = similarityMatrix[i][min_index_row];
            for (int k = 0; k < vList.size(); k++) {
                /*if (min > similarityMatrix[k][min_index_row]) {
                    min = similarityMatrix[k][min_index_row];
                    min_index_column = k;
                }*/
                if (min_2 > similarityMatrix[k][min_index_row]) {
                    min_2 = similarityMatrix[k][min_index_row];
                    min_index_column = k;
                }
            }
            if (min_index_column == min_index_row ) {
             //   System.out.println("[ " + i + " ] [" + min_index_column + " ]  " +
               //         "Değer = " + similarityMatrix[i][min_index_column]);
                s_arrayLists.add(i);
                s_arrayLists.add(min_index_column);

            }
            /*if (min == min_2)
                System.out.println("Eşitlik var" +  ++counter);*/

        }
        return s_arrayLists;
    }
}
