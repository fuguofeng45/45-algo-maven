package run.fgf45.coder.algo.sort;

/**
 * @author fuguofeng
 */
public class InsertSort {

    public static int[] insertSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int j = i - 1;
            int tmp = data[i];
            for (; j >= 0; j--) {
                //swap
                if (data[j] > tmp) {
                    data[j + 1] = data[j];
                } else {
                    //说明已经无需移动元素
                    break;
                }
            }
            data[j + 1] = tmp;
        }
        return data;
    }

    public static void main(String[] args) {
        int[] data = {1, 4, 4, 7, 8, 1, 2, 4, 9, 8};
        data = insertSort(data);
        System.out.println(data);
    }

}
