package run.fgf45.coder.algo.sort;

/**
 * @author fuguofeng
 */
public class ChooseSort {

    public static int[] chooseSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int j = i;
            int index = j;
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

    public static int findMaxValueIndex(int[] data) {
        return -1;
    }

    public static void main(String[] args) {
        int[] data = {1, 3, 2, 6, 8, 9, 0, 12314, 5};
        data = chooseSort(data);
        System.out.println(data);
    }

}
