package com.ggggght.serviceverficationcode.controller;

import com.ggggght.internalcommon.dto.ResponseResult;
import com.ggggght.serviceverficationcode.service.IVerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送短信验证码
 */
@Validated
@SuppressWarnings("all")
@RestController("/verify-code")
public class VerifyCodeController {
	private IVerifyCodeService iVerifyCodeService;
	
	@GetMapping("/geterate/{identify}/{phoneNumber}")
	public ResponseResult generate(@PathVariable("identify") int identify,
	                               @PathVariable("phoneNumber") String phoneNumber) throws Exception {
		
		return iVerifyCodeService.generate(identify, phoneNumber);
	}
	
	@Autowired
	public void setiVerifyCodeService(IVerifyCodeService iVerifyCodeService) {
		this.iVerifyCodeService = iVerifyCodeService;
	}
}
