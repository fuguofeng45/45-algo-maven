package run.fgf45.coder.algo.bloomfilter;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import io.codis.jodis.JedisResourcePool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author fuguofeng
 */
public class RedisBloomFilter {

    private static final String BF_KEY_PREFIX = "bf:";

    private int numApproxElements;
    private double fpp;
    private int numHashFunctions;
    private int bitmapLength;

    private JedisResourcePool jedisResourcePool;

    /**
     * 构造布隆过滤器。默认重复率0.03
     *
     * @param numApproxElements 预估元素数量
     * @param jedisResourcePool Codis专用的Jedis连接池
     */
    public RedisBloomFilter(int numApproxElements, JedisResourcePool jedisResourcePool) {
        this.numApproxElements = numApproxElements;
        this.fpp = 0.03D;
        this.jedisResourcePool = jedisResourcePool;

        bitmapLength = (int) (-numApproxElements * Math.log(fpp) / (Math.log(2) * Math.log(2)));
        numHashFunctions = Math.max(1, (int) Math.round((double) bitmapLength / numApproxElements * Math.log(2)));
    }

    /**
     * 构造布隆过滤器。注意：在同一业务场景下，三个参数务必相同
     *
     * @param numApproxElements 预估元素数量
     * @param fpp               可接受的最大误差（假阳性率）
     * @param jedisResourcePool Codis专用的Jedis连接池
     */
    public RedisBloomFilter(int numApproxElements, double fpp, JedisResourcePool jedisResourcePool) {
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        this.jedisResourcePool = jedisResourcePool;

        bitmapLength = (int) (-numApproxElements * Math.log(fpp) / (Math.log(2) * Math.log(2)));
        numHashFunctions = Math.max(1, (int) Math.round((double) bitmapLength / numApproxElements * Math.log(2)));
    }

    /**
     * 取得自动计算的最优哈希函数个数
     */
    public int getNumHashFunctions() {
        return numHashFunctions;
    }

    /**
     * 取得自动计算的最优Bitmap长度
     */
    public int getBitmapLength() {
        return bitmapLength;
    }

    /**
     * 计算一个元素值哈希后映射到Bitmap的哪些bit上
     *
     * @param element 元素值
     * @return bit下标的数组
     */
    private long[] getBitIndices(String element) {
        long[] indices = new long[numHashFunctions];

        byte[] bytes = Hashing.murmur3_128()
                .hashObject(element, Funnels.stringFunnel(Charset.forName("UTF-8")))
                .asBytes();

        long hash1 = Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]
        );
        long hash2 = Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]
        );

        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            indices[i] = (combinedHash & Long.MAX_VALUE) % bitmapLength;
            combinedHash += hash2;
        }

        return indices;
    }

    /**
     * 插入元素
     *
     * @param key       原始Redis键，会自动加上'bf:'前缀
     * @param element   元素值，字符串类型
     * @param expireSec 过期时间（秒）
     */
    public void insert(String key, String element, int expireSec) {
        if (key == null || element == null) {
            throw new RuntimeException("键值均不能为空");
        }
        String actualKey = BF_KEY_PREFIX.concat(key);

        try (Jedis jedis = jedisResourcePool.getResource()) {
            try (Pipeline pipeline = jedis.pipelined()) {
                for (long index : getBitIndices(element)) {
                    pipeline.setbit(actualKey, index, true);
                }
                pipeline.syncAndReturnAll();
            } catch (IOException ex) {
            }
            jedis.expire(actualKey, expireSec);
        }
    }

    /**
     * 检查元素在集合中是否（可能）存在
     *
     * @param key     原始Redis键，会自动加上'bf:'前缀
     * @param element 元素值，字符串类型
     */
    public boolean mayExist(String key, String element) {
        if (key == null || element == null) {
            throw new RuntimeException("键值均不能为空");
        }
        String actualKey = BF_KEY_PREFIX.concat(key);
        boolean result = false;

        try (Jedis jedis = jedisResourcePool.getResource()) {
            try (Pipeline pipeline = jedis.pipelined()) {
                for (long index : getBitIndices(element)) {
                    pipeline.getbit(actualKey, index);
                }
                result = !pipeline.syncAndReturnAll().contains(false);
            } catch (IOException ex) {
            }
        }

        return result;
    }

}
