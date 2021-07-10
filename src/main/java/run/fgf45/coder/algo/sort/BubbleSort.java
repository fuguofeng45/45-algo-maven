package run.fgf45.coder.algo.sort;

/**
 * @author fuguofeng
 */
public class BubbleSort {

    public static int[] bubbleSort(int[] data) {
        for (int i = data.length; i >= 0; i--) {
            boolean flag = true;
            for (int j = 0; j < i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    //swap
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    flag = false;
                }
            }
            //如果flag为true，则说明所有数据均已排序完毕
            if (flag) {
                break;
            }
        }
        return data;
    }

    public static void main(String[] args) {
        int[] data = {1,2,3,4,5,6,7,9,8};
        data = bubbleSort(data);
        System.out.println(data);
    }

}
