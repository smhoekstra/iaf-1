/*
 * $Log: IfsaFFSessionBean.java,v $
 * Revision 1.1.2.1  2007-10-29 12:25:34  europe\M00035F
 * Create EJb Beans required to connect to IFSA J2EE implementation as an IFSA Provider application
 *
 * 
 */

package nl.nn.adapterframework.extensions.ifsa.ejb;

import com.ing.ifsa.api.FireForgetService;
import com.ing.ifsa.api.ServiceRequest;
import com.ing.ifsa.exceptions.ServiceException;
import java.rmi.RemoteException;
import javax.ejb.SessionBean;

/**
 *
 * @author Tim van der Leeuw
 * @version Id
 */
public class IfsaFFSessionBean extends IfsaEjbBeanBase implements SessionBean, FireForgetService {

    public void onServiceRequest(ServiceRequest request) throws RemoteException, ServiceException {
        processRequest(request);
    }

}