package org.basic.datastructures.sorting.ex1;

import org.basic.datastructures.sorting.Sorter;

public class Ex1BubbleSort extends Sorter {
    @Override
    int[] sort(int[] arr, int a, int b) {
        while (true) {
            boolean worked = false;
            for (int i = a; i < b - 1; i++) {
                if (arr[i] > arr[i+1]) {
                    worked = true;
                    swap(arr, i, i+1);
                }
            }

            if (!worked)
                return arr;
        }
    }

    static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}
