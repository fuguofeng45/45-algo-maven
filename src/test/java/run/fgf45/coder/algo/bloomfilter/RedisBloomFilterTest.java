package run.fgf45.coder.algo.bloomfilter;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RedisBloomFilterTest {

    private static final int NUM_APPROX_ELEMENTS = 3000;
    private static final double FPP = 0.03;
    private static final int DAY_SEC = 60 * 60 * 24;
    private static JedisResourcePool jedisResourcePool;
    private static RedisBloomFilter redisBloomFilter;

    @BeforeClass
    public static void beforeClass() throws Exception {
        jedisResourcePool = RoundRobinJedisPool.create()
                .curatorClient("10.10.99.130:2181,10.10.99.132:2181,10.10.99.133:2181,10.10.99.124:2181,10.10.99.125:2181,", 10000)
                .zkProxyDir("/jodis/bd-redis")
                .build();
        redisBloomFilter = new RedisBloomFilter(NUM_APPROX_ELEMENTS, FPP, jedisResourcePool);
        System.out.println("numHashFunctions: " + redisBloomFilter.getNumHashFunctions());
        System.out.println("bitmapLength: " + redisBloomFilter.getBitmapLength());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        jedisResourcePool.close();
    }

    @Test
    public void testInsert() throws Exception {
        redisBloomFilter.insert("topic_read:8839540:20190609", "76930242", DAY_SEC);
        redisBloomFilter.insert("topic_read:8839540:20190609", "76930243", DAY_SEC);
        redisBloomFilter.insert("topic_read:8839540:20190609", "76930244", DAY_SEC);
        redisBloomFilter.insert("topic_read:8839540:20190609", "76930245", DAY_SEC);
        redisBloomFilter.insert("topic_read:8839540:20190609", "76930246", DAY_SEC);
    }

    @Test
    public void testMayExist() throws Exception {
        System.out.println(redisBloomFilter.mayExist("topic_read:8839540:20190609", "76930242"));
        System.out.println(redisBloomFilter.mayExist("topic_read:8839540:20190609", "76930244"));
        System.out.println(redisBloomFilter.mayExist("topic_read:8839540:20190609", "76930246"));
        System.out.println(redisBloomFilter.mayExist("topic_read:8839540:20190609", "76930248"));
    }
}
