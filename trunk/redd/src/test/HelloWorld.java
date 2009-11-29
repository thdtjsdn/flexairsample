package test;

import org.apache.xpath.operations.String;

import com.architech.spm.app.App;

/**
 * HelloWorld acts as a proxy and is loaded for each services model (e.g. JAX-WS and BlazeDS).
 * @author nate
 *
 */

//@ WebService(name="Hello World v2")
public class HelloWorld {
	
	// INTERFACE
	
	public synchronized String say(String something) {
		return getBean().sayImpl(something);
	}
	
	public synchronized String getLast() {
		return getBean().getLastImpl();
	}
	
	
	// IMPLEMENTATION

	private String lastSaid;
	private String testProp = "You";
	
	synchronized public void setTestProp(String value) {
		testProp = value;
	}
	synchronized public String getTestProp() {
		return testProp;
	}
	
	synchronized protected String sayImpl(String something) {
		lastSaid = something;
		return String.format("%s said: %s", testProp, lastSaid);
	}
	
	synchronized protected String getLastImpl() {
		return String.format("%s last said: %s", testProp, lastSaid);
	}
	
	
	// UTILITIES
	
	private HelloWorld getBean() {
		return (HelloWorld)App.getSpringContext().getBean("HelloWorld");
	}
	
}
