package java.com.sdra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJedis {
	
	public static ApplicationContext ctx;
	public static JedisConnectionFactory jedisConnetionFactory;
	public JedisConnection jedisConnection;
	
	/**
	 * only once
	 */
	@BeforeClass
	public static void setup() {
		ctx = new ClassPathXmlApplicationContext("spring-redis.xml");
		jedisConnetionFactory = (JedisConnectionFactory) ctx.getBean("jedisConnectionFactory");
	}

	@Before
	public void setBefore() {
		jedisConnection = jedisConnetionFactory.getConnection();
	}

	@After
	public void setAfter() {
		jedisConnection.close();
	}
	
	@Test
	public void testGet() {
		byte[] bs = jedisConnection.get("hobby".getBytes());
		System.out.println(new String(bs));
	}
	
	@Test
	public void testGetAll() {
		Set<byte[]> keys = jedisConnection.keys("*".getBytes());
		for (Iterator<byte[]> iter = keys.iterator(); iter.hasNext();) {
			System.out.println(new String(iter.next()));
		}
	}
	
	@Test
	public void testSet() {
		byte[] key = "name".getBytes();
		byte[] value = "martin".getBytes();
		jedisConnection.set(key, value);
	}
	
	@Test
	public void testExists() {
		byte[] key = "age".getBytes();
		byte[] value = "19".getBytes();
		if(!jedisConnection.exists(key)) {
			jedisConnection.set(key, value);
		}
		
		jedisConnection.expire(key, 20);
	}
	
	@Test
	public void testIncr() {
		byte[] key = "count".getBytes();
//		byte[] value = "1".getBytes();
		
		Long incrBy = jedisConnection.incrBy(key, 2);
//		Long incrBy = jedisConnection.incr(key);
		System.out.println(incrBy);
	}
	
	

	/*private void print(Collection<RedisServer> c) {
		for (Iterator<RedisServer> iter = c.iterator(); iter.hasNext();) {
			RedisServer rs = (RedisServer) iter.next();
			System.out.println(rs.getHost() + ":" + rs.getPort());
		}
	}*/
	
	/*public void test3() throws InterruptedException {
		if (jedisConnetionFactory.getSentinelConnection().isOpen()) {
			Collection<RedisServer> c = jedisConnetionFactory.getSentinelConnection().masters();
			print(c);
			RedisNode rn = new RedisNode("192.168.88.153", 6380);
			rn.setName("mymaster");
			c = jedisConnetionFactory.getSentinelConnection().slaves(rn);
			print(c);
		}
		for (int i = 0; i < 1000; i++) {
			jedisConnection.set(new String("k" + i).getBytes(), new String("v" + i).getBytes());
			Thread.sleep(1000);
		}
		Set<byte[]> keys = jedisConnection.keys(new String("k*").getBytes());
		Assert.assertEquals(1000, keys.size());
	}*/

}
