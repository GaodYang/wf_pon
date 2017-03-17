package localhost.wf_pon.services.IrmsWebServices;

public class IrmsWebServicesProxy implements localhost.wf_pon.services.IrmsWebServices.IrmsWebServices {
  private String _endpoint = null;
  private localhost.wf_pon.services.IrmsWebServices.IrmsWebServices irmsWebServices = null;
  
  public IrmsWebServicesProxy() {
    _initIrmsWebServicesProxy();
  }
  
  public IrmsWebServicesProxy(String endpoint) {
    _endpoint = endpoint;
    _initIrmsWebServicesProxy();
  }
  
  private void _initIrmsWebServicesProxy() {
    try {
      irmsWebServices = (new localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesServiceLocator()).getIrmsWebServices();
      if (irmsWebServices != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)irmsWebServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)irmsWebServices)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (irmsWebServices != null)
      ((javax.xml.rpc.Stub)irmsWebServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public localhost.wf_pon.services.IrmsWebServices.IrmsWebServices getIrmsWebServices() {
    if (irmsWebServices == null)
      _initIrmsWebServicesProxy();
    return irmsWebServices;
  }
  
  public java.lang.String createProject(java.lang.String xml) throws java.rmi.RemoteException{
    if (irmsWebServices == null)
      _initIrmsWebServicesProxy();
    return irmsWebServices.createProject(xml);
  }
  
  
}