/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import servicio.*;

/**
 *
 * @author UsuarioObj
 */
public class accesoAction extends org.apache.struts.action.Action {
servicio ser;

    public void setSer(servicio ser) {
        this.ser = ser;
    }
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        accesoForm acceso=(accesoForm)form;
        request.getSession().setAttribute("usuario", ser.acceso(acceso.getUsuario(), acceso.getContraseña()));
        return mapping.findForward("pedido");
    }
}
