package site.laoc.list;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LList {

    private static final int MIN_INITIAL_CAPACITY = 8;

    // ******  Array allocation and resizing utilities ******

    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }

    public static void main(String args[]){

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        for(int i = 1;i <= 17;i++){
            queue.addFirst(i);
        }

        queue.removeLastOccurrence(11);

//        queue.add(2);
//        queue.add(3);
//        queue.add(4);
//        queue.add(5);
//        queue.add(6);
//        queue.add(7);
//        queue.add(8);
//
//        queue.offerLast(9);
//
//        while(!queue.isEmpty()){
//            System.out.print(queue.pop() + " ");
//        }
//
//        Integer elements[] = new Integer[16];
//        int head = 0;
//        elements[head = (head - 1) & (elements.length - 1)] = 1;
//
//        for(int i = 0;i < elements.length;i++){
//            if(elements[i] != null){
//                System.out.println(i + " == " + elements[i]);
//            }
//        }

        int i = 32;
        int j = 31;

        System.out.println((i & j));



    }
}
