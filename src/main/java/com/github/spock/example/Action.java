package com.github.spock.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Action {

	@Autowired
	private XmlDao xmlAction;
	
	public String getHtml(String html){
		return "html:" + html;
	}
	
	public void getException(){
		throw new NullPointerException();
	}
	
	public String getXml(String xml){
		return xmlAction.getXml(xml);
	}
}
