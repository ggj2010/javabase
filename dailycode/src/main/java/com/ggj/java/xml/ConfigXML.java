package com.ggj.java.xml;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@Component
@ImportResource(locations={"classpath:overrideparam.xml"})
public class ConfigXML {
}
