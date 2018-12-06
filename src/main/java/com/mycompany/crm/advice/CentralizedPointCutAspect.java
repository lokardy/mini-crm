package com.mycompany.crm.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CentralizedPointCutAspect {
	@Pointcut("bean(*Service) || bean(*Controller)")
	private void allServices(){
		
	}
	
	@Pointcut("bean(*Controller)")
	private void controllers(){
		
	}
	
	@Pointcut("within(com.audibene.crm.service.impl.*)")
	private void packagesToInclude() {
	}
	
	@Pointcut("within(com.audibene.crm.controller..*)")
	private void controllerPackages() {
	}
	
	@Pointcut("(packagesToInclude() || controllerPackages()) && (allServices() || controllers()) ")
	public void serviceClassesToLog() {
	}
}