package run.fgf45.coder.algo.sort;

/**
 * @author fuguofeng
 */
public class InsertSort {

    public static int[] insertSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int temp = data[i];
            int j = i - 1;
            for (; j < i; j--) {
                //swap
                if (data[j] > temp) {
                    data[j + 1] = data[j];
                } else {
                    break;
                }
            }
            data[j + 1] = temp;
        }
        return data;
    }

    public static void main(String[] args) {
        int [] data = {1,4,4,7,8,1,2,4,9,8};
        data = insertSort(data);
        System.out.println(data);
    }

}
