/**
 * IrmsWebServicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package localhost.wf_pon.services.IrmsWebServices;

public class IrmsWebServicesServiceLocator extends org.apache.axis.client.Service implements localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesService {

    public IrmsWebServicesServiceLocator() {
    }


    public IrmsWebServicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IrmsWebServicesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IrmsWebServices
    private java.lang.String IrmsWebServices_address = "http://localhost:8080/wf_pon/services/IrmsWebServices";

    public java.lang.String getIrmsWebServicesAddress() {
        return IrmsWebServices_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IrmsWebServicesWSDDServiceName = "IrmsWebServices";

    public java.lang.String getIrmsWebServicesWSDDServiceName() {
        return IrmsWebServicesWSDDServiceName;
    }

    public void setIrmsWebServicesWSDDServiceName(java.lang.String name) {
        IrmsWebServicesWSDDServiceName = name;
    }

    public localhost.wf_pon.services.IrmsWebServices.IrmsWebServices getIrmsWebServices() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IrmsWebServices_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIrmsWebServices(endpoint);
    }

    public localhost.wf_pon.services.IrmsWebServices.IrmsWebServices getIrmsWebServices(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesSoapBindingStub _stub = new localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getIrmsWebServicesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIrmsWebServicesEndpointAddress(java.lang.String address) {
        IrmsWebServices_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.wf_pon.services.IrmsWebServices.IrmsWebServices.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesSoapBindingStub _stub = new localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesSoapBindingStub(new java.net.URL(IrmsWebServices_address), this);
                _stub.setPortName(getIrmsWebServicesWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IrmsWebServices".equals(inputPortName)) {
            return getIrmsWebServices();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/wf_pon/services/IrmsWebServices", "IrmsWebServicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/wf_pon/services/IrmsWebServices", "IrmsWebServices"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IrmsWebServices".equals(portName)) {
            setIrmsWebServicesEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
