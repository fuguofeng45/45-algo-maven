package run.fgf45.coder.algo.sort;

/**
 * @author fuguofeng
 */
public class ChooseSort {

    public static int[] chooseSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int j = i;
            int index = j;
            //find index of max value
            for (; j < data.length; j++) {
                if (data[index] >= data[j]) {
                    index = j;
                }
            }
            //swap
            int temp = data[index];
            data[index] = data[i];
            data[i] = temp;
        }
        return data;
    }

    public static void main(String[] args) {
        int[] data = {0,1,2,3,4,5,6,7,8,9,1};
        data = chooseSort(data);
        System.out.println(data);
    }

}
