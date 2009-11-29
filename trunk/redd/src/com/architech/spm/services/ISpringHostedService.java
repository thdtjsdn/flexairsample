package com.architech.spm.services;

/**
 * This interface is a marker interface for SPM services that can be accessed from multiple
 * web services and that need to share a singleton.  This interface intentionally does not specify
 * any functions.  At this time, the two known service frameworks are JAX-WS web services and BlazeDS.
 * 
 * Classes that implement this interface must accept service requests using public functions
 * that load the Spring-hosted singleton instance of the class and execute a protected function
 * which performs the actual application logic.
 * 
 * @author nate
 *
 */
public interface ISpringHostedService {

}
