package run.fgf45.coder.algo.bloomfilter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class GuavaBloomFilter {

    public static void main(String[] args) {
        int total = 1000000;
        BloomFilter<CharSequence> filter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total, 0.0002);
        for (int i = 0; i < total; i++) {
            filter.put("" + i);
        }
        // 判断值是否存在过滤器中
        int count = 0;
        for (int i = 0; i < total + 10000; i++) {
            if (filter.mightContain("" + i)) {
                count++;
            }
        }
        System.out.println("已匹配数量 " + count);

    }


}
