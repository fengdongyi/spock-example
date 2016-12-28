package com.github.spock.test

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration

import com.github.spock.example.Action
import com.github.spock.example.XmlDao;

import spock.lang.Specification;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
class TestActionWithSpring extends Specification {

	XmlDao xmlAction = Mock();
	
	@Autowired
	Action action;
	
	def setup(){
		xmlAction.getXml(_) >> '666'
		action.xmlAction = xmlAction
	}
	
	def "test with spring"(){
		expect:
		'html:Hello World' == action.getHtml('Hello World')
	}
	
	def "test spring mock"(){
		expect:
		'666' == action.getXml('Hello World')
	}
}
