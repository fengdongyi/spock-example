package com.github.spock.example;

import org.springframework.stereotype.Service;

@Service
public class XmlDao {

	public String getXml(String xml){
		return "xml:"+xml;
	}
}
