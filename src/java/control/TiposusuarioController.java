package control;

import modelo.Tiposusuario;
import control.util.JsfUtil;
import control.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("tiposusuarioController")
@SessionScoped
public class TiposusuarioController implements Serializable {

    @EJB
    private control.TiposusuarioFacade ejbFacade;
    private List<Tiposusuario> items = null;
    private List<Tiposusuario> itemsEliminados = null;
    private Tiposusuario selected;

    public TiposusuarioController() {
    }

    // consultarEliminados() -> sustituye a findAll() que venia por defecto
    //Y se crea en el Facade
    public List<Tiposusuario> getItemsEliminados() {
        if (itemsEliminados == null) {
            //items = getFacade().findAll();
            itemsEliminados = getFacade().consultarEliminados();
        }
        return itemsEliminados;
    }

    public void setItemsEliminados(List<Tiposusuario> itemsEliminados) {
        this.itemsEliminados = itemsEliminados;
    }

    
    
    public Tiposusuario getSelected() {
        return selected;
    }

    public void setSelected(Tiposusuario selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TiposusuarioFacade getFacade() {
        return ejbFacade;
    }

    public Tiposusuario prepareCreate() {
        selected = new Tiposusuario();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        //Hacer el valor por defecto del status
        selected.setStatus(1);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioUpdated"));
    }

    public void destroy() {
        //BORRADO LOGICO: cambio para que en lugar de eliminar solo actualice el status a baja (0)
        selected.setStatus(0);
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioDeleted"));
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            //Llamamos a estos itemsEliminados para que los vuelva a referescar
            itemsEliminados = null;
        }
    }
    
    public void restaurar() {
        selected.setStatus(1);
        //persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioDeleted"));
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TiposusuarioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            itemsEliminados = null;
            //Llamamos a estos items para que los vuelva a referescar
            items = null;// Invalidate list of items to trigger re-query.
        }
    }

    // consultarActivos() -> sustituye a findAll() que venia por defecto
    //Y se crea en PaisesFacade
    public List<Tiposusuario> getItems() {
        if (items == null) {
            //items = getFacade().findAll();
            items = getFacade().consultarActivos();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Tiposusuario getTiposusuario(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Tiposusuario> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Tiposusuario> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Tiposusuario.class)
    public static class TiposusuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TiposusuarioController controller = (TiposusuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tiposusuarioController");
            return controller.getTiposusuario(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Tiposusuario) {
                Tiposusuario o = (Tiposusuario) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Tiposusuario.class.getName()});
                return null;
            }
        }

    }

}
