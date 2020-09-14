package com.ggggght.serviceverficationcode.service.impl;

import com.ggggght.internalcommon.constant.CommonStatusEnum;
import com.ggggght.internalcommon.constant.IdentityConstant;
import com.ggggght.internalcommon.dto.ResponseResult;
import com.ggggght.internalcommon.dto.VerifyCodeResponse;
import com.ggggght.serviceverficationcode.constant.VerifyCodeConstant;
import com.ggggght.serviceverficationcode.service.IVerifyCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings("all")
public class VerifyCodeServiceImpl implements IVerifyCodeService {
	private RedisTemplate<String, String> redisTemplate;
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 生成验证码
	 *
	 * @param identity
	 * @param phoneNumber
	 * @return
	 */
	@Override
	public ResponseResult<VerifyCodeResponse> generate(int identity, String phoneNumber) throws Exception {
		checkSendCodeTimeLimit(phoneNumber);
		
		// 生成6位code
		String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
		// 生成存储redis的Key
		String keyPre = generateKeyPreByIdentity(identity);
		String reidsKey = keyPre + phoneNumber;
		String script = "redis.call('SETNX',KEYS[1]) \t"
				+ "redis.call('EXPIRE',KEYS[1],120)";
		
		
		redisTemplate.execute(new DefaultRedisScript<>(script, Object.class),
				Collections.singletonList(reidsKey),
				null);
		
		VerifyCodeResponse response = new VerifyCodeResponse(code);
		return ResponseResult.success(response);
	}
	
	@Override
	public ResponseResult verify(int identity, String phoneNumber, String code) throws Exception {
		final String keyPre = generateKeyPreByIdentity(identity);
		final String redisKey = keyPre + phoneNumber;
		
		final String redisCache = stringRedisTemplate.opsForValue().get(keyPre);
		
		if (StringUtils.isBlank(redisCache) || !Objects.equals(code.trim(),redisCache)) {
			return ResponseResult.fail(CommonStatusEnum.VERIFY_CODE_ERROR.getCode(), CommonStatusEnum.VERIFY_CODE_ERROR.getValue());
		}
		
		return ResponseResult.success(null);
	}
	
	/**
	 * 根据用户的类型生成对应的缓存Key
	 *
	 * @param identity
	 * @return
	 */
	private String generateKeyPreByIdentity(int identity) {
		return identity == IdentityConstant.PASSENGER
				? VerifyCodeConstant.PASSENGER_LOGIN_KEY_PRE
				: VerifyCodeConstant.DRIVER_LOGIN_KEY_PRE;
	}
	
	/**
	 * 校验发送验证码的时限
	 *
	 * @param phoneNumber
	 */
	private void checkSendCodeTimeLimit(String phoneNumber) {
	
	}
	
	@Autowired
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Autowired
	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}
}
