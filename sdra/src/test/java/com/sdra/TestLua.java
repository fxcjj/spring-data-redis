package java.com.sdra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

public class TestLua {

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

	@Test
	public void test71() {
		List<String> keys = new ArrayList<String>();
		RedisScript<Long> script = new DefaultRedisScript<Long>("local size = redis.call('dbsize'); return size;",
				Long.class);
		Long dbsize = stringRedisTemplate.execute(script, keys, new Object[] {});
		System.out.println("sha1:" + script.getSha1());
		System.out.println("Lua:" + script.getScriptAsString());
		System.out.println("dbsize:" + dbsize);
	}

	@Test
	public void test72() {
		DefaultRedisScript<Boolean> script = new DefaultRedisScript<Boolean>();
		/**
		 * isexistskey.lua内容如下：
		 * 
		 * return tonumber(redis.call("exists",KEYS[1])) == 1;
		 */
		script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/isexistskey.lua")));

		script.setResultType(Boolean.class);// Must Set

		System.out.println("script:" + script.getScriptAsString());
		Boolean isExist = stringRedisTemplate.execute(script, Collections.singletonList("k2"), new Object[] {});
		Assert.assertTrue(isExist);
	}
}
