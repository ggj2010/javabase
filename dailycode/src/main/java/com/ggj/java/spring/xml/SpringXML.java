package com.ggj.java.spring.xml;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@Component
@ImportResource(locations={"classpath:overrideparam.xml"})
public class SpringXML {
}
