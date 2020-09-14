package com.ggggght.serviceverficationcode.service;

import com.ggggght.internalcommon.dto.ResponseResult;
import com.ggggght.internalcommon.dto.VerifyCodeResponse;

public interface IVerifyCodeService {
	/**
	 * 生成验证码
	 * @param identity
	 * @param phoneNumber
	 * @return
	 */
	ResponseResult<VerifyCodeResponse> generate(int identity, String phoneNumber) throws Exception;
	
	ResponseResult verify(int identity, String phoneNumber, String code) throws Exception;
}
