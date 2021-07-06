package run.fgf45.coder.algo.linklist;

import java.util.Objects;

/**
 * @author fuguofeng
 */
public class ReverseLinkList {

    private static Node reverse(int [] data){
        Node head = generate(data);
        Node tail = head.next;
        head.next = null;
        while(Objects.nonNull(tail)){
            Node temp = tail;
            tail = tail.next;
            temp.next = head.next;
            head.next = temp;
        }
        return head;
    }

    private static Node generate(int[] data) {
        Node head = new Node();
        for (int i = data.length - 1; i >= 0; i--) {
            Node temp = new Node(data[i], null);
            temp.next = head.next;
            head.next = temp;
        }
        return head;
    }

    static class Node {
        private int data;
        private Node next;

        public Node() {
            this.data = -1;
            this.next = null;
        }

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    public static void main(String[] args) {
        int [] data = {1,3,5,6,7,8,9};
        //generate(data);
        reverse(data);
    }

}
