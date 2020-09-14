package com.ggggght.cloudeureka.listen;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class EurekaEvent {
	public void listenEurekaFailEvent(EurekaInstanceCanceledEvent event) {
		// 发邮件
		log.error("Eureka Server down. Id: {}",event.getServerId());
	}
}
