package run.fgf45.coder.algo.fibonacci;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fuguofeng
 */
public class FibonacciFunction {

    private static Map<Integer, Integer> cache = new HashMap<>();

    public static int function(int i){
        if(i < 0){
            throw new IllegalArgumentException();
        }
        if(i == 0) {
            return 0;
        }
        if(i == 1){
            return 1;
        }
        return function(i - 1) + function(i - 2);
    }

    public static int functionWithCache(int i){
        if(i < 0){
            throw new IllegalArgumentException();
        }
        if(i == 0) {
            return 0;
        }
        if(i == 1){
            return 1;
        }
        if(cache.containsKey(i)){
            return cache.get(i);
        }
        int result = function(i - 1) + function(i - 2);
        cache.put(result, i);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(function(4));
        System.out.println(functionWithCache(4));
    }


}
