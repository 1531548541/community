package com.atguigu.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        int[] T={73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println(Arrays.toString(dailyTemperatures(T)));
    }

    @Test
    public int[] dailyTemperatures(int[] T) {
         int[] ans = new int[T.length];
//         Stack<Integer> stack = new Stack<Integer>();
        Deque<Integer> stack = new LinkedList<Integer>();
         for (int i = 0; i < T.length; i++) {
             while (!stack.isEmpty() && T[i] > T[stack.peek()]) {
                 int prevIndex = stack.pop();
                 ans[prevIndex] = i - prevIndex;
             }
             stack.push(i);
         }
         return ans;
    }

}
