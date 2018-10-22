package java.com.sdra;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class TestRedisTemplate {
	
	public static ApplicationContext ctx;
	
	public static RedisTemplate<String, String> redisTemplate;
	public static StringRedisTemplate stringRedisTemplate;
	
	/**
	 * only once
	 */
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setup() {
		ctx = new ClassPathXmlApplicationContext("spring-redis.xml");
		redisTemplate = (RedisTemplate<String, String>) ctx.getBean("redisTemplate");
		stringRedisTemplate = (StringRedisTemplate) ctx.getBean("stringRedisTemplate");
	}
	
	//测试RedisTemplate，自主处理key的可读性(String序列化)
	@Test
	public void testRedisTemplate() {
//		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		
		ListOperations<String, String> lop = redisTemplate.opsForList();
		RedisSerializer<String> serializer = new StringRedisSerializer();
		
		redisTemplate.setKeySerializer(serializer);
		redisTemplate.setValueSerializer(serializer);
		
//		redisTemplate.setDefaultSerializer(serializer);
		
		String key = "spring";
		
		lop.leftPush(key, "aaa");
		lop.leftPush(key, "bbb");
		long size = lop.size(key); // rt.boundListOps(key).size();
	    Assert.assertEquals(2, size);
	}
	
	// 测试便捷对象StringRedisTemplate
//	  @Ignore
	  @Test
	  public void testStringRedisTemplate() {
		  
		  ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
		  
		  String key = "hobby";
		  String value = "swimming";
		  
		  vop.set(key, value);
		  
		  String rst = vop.get(key);
		  System.out.println("rst: " + rst);
		  Assert.assertEquals(value, rst);
	  }
	  
	  //测试RedisCallback
	  @Test
	  public void testCallback() {
		  
		  Long dbSize = stringRedisTemplate.execute(new RedisCallback<Long>() {

			  @Override
			  public Long doInRedis(RedisConnection connection) throws DataAccessException {
				  StringRedisConnection conn = (StringRedisConnection) connection;
				  return conn.dbSize();
			  }
			  
		  });
		  System.out.println("dbSize: " + dbSize);
	  }
	
	//测试SessionCallback
	@Test
	public void testSessionCallback() {
		List<Object> result = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {

			@SuppressWarnings("unchecked")
			@Override
			public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
				operations.multi();
				operations.opsForHash().put((K) "hkey", "multikey4", "multivalue4");
				operations.opsForHash().get((K) "hkey", "k1");
				return operations.exec();
			}
			
		});
		
		result.forEach(System.out::println);
	}
	
	
	
}
