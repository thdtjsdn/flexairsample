package test;

import javax.jws.WebService;

//@ WebService
public class HelloGalaxy {
	public String sayHello(String value) {
		return "You said " + value;
	}
}
