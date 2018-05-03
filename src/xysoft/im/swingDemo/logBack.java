package xysoft.im.swingDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 

public class logBack {
	Logger log = LoggerFactory.getLogger(logBack.class);
	
    public void log() {   
        log.debug("Debug info.");   
        log.info("Info info");   
        log.warn("Warn info");   
        log.error("Error info");   
    } 

	public logBack() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logBack lb = new logBack();
		lb.log();
	}

}
