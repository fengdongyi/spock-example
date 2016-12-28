package com.github.spock.test

import spock.lang.Shared;
import spock.lang.Specification
import spock.lang.Unroll;

import com.github.spock.example.Action

class TestAction extends Specification {
	
	def action = new Action()
	
	@Shared
	action1 = new Action()
	
	Action action3 = Mock()
	
	def "test Action getHtml"(){
		expect:
		println action
		println action1
		action.getHtml('Hello World') == 'html:Hello World'
	}
	
	def "test Action getHtml1"(){
		expect:
		println action
		println action1
		action.getHtml('Hello World') == 'html:Hello World2'
	}
	
	def "test Action getHtml2"(){
		given:
		def action2 = new Action()
		def html
		
		when:
		html = action2.getHtml('Hello World')
		
		then:
		html!=null
		html == 'html:Hello World2'
	}
	
	def "test exception throw"(){
		given:
		def action2 = new Action()
		
		when:
		action2.getException()
		
		then:
		thrown(NullPointerException)
	}
	
	def "test expect"(){
		given:
		def action2 = new Action()
		
		expect:
		'html:Hello World' == action2.getHtml('Hello World')
	}
	
	def "test where"(){
		given:
		def action2 = new Action()
		
		expect:
		result == action2.getHtml(data)
		
		where:
		data         | result
		'Hello World'|'html:Hello World'
		'Hello World2'|'html:Hello World2'
		'Hello World3'|'html:Hello World3'
	}
	
	def "test mock"(){
		action3.getHtml(_) >> 'hello'
		
		expect:
		'hello' == action3.getHtml('666')
	}
}
