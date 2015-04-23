package com.Acme.jsf;

import com.Acme.entities.PacienteHasCita;
import com.Acme.jsf.util.JsfUtil;
import com.Acme.jsf.util.PaginationHelper;
import com.Acme.ssesion.PacienteHasCitaFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "pacienteHasCitaController")
@SessionScoped
public class PacienteHasCitaController implements Serializable {

    private PacienteHasCita current;
    private DataModel items = null;
    @EJB
    private com.Acme.ssesion.PacienteHasCitaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PacienteHasCitaController() {
    }

    public PacienteHasCita getSelected() {
        if (current == null) {
            current = new PacienteHasCita();
            current.setPacienteHasCitaPK(new com.Acme.entities.PacienteHasCitaPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private PacienteHasCitaFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (PacienteHasCita) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new PacienteHasCita();
        current.setPacienteHasCitaPK(new com.Acme.entities.PacienteHasCitaPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getPacienteHasCitaPK().setCitaidCita(current.getCita().getIdCita());
            current.getPacienteHasCitaPK().setPacienteidPaciente(current.getPaciente().getIdPaciente());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PacienteHasCitaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (PacienteHasCita) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getPacienteHasCitaPK().setCitaidCita(current.getCita().getIdCita());
            current.getPacienteHasCitaPK().setPacienteidPaciente(current.getPaciente().getIdPaciente());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PacienteHasCitaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (PacienteHasCita) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PacienteHasCitaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = PacienteHasCita.class)
    public static class PacienteHasCitaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PacienteHasCitaController controller = (PacienteHasCitaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pacienteHasCitaController");
            return controller.ejbFacade.find(getKey(value));
        }

        com.Acme.entities.PacienteHasCitaPK getKey(String value) {
            com.Acme.entities.PacienteHasCitaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.Acme.entities.PacienteHasCitaPK();
            key.setPacienteidPaciente(Integer.parseInt(values[0]));
            key.setCitaidCita(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(com.Acme.entities.PacienteHasCitaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPacienteidPaciente());
            sb.append(SEPARATOR);
            sb.append(value.getCitaidCita());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PacienteHasCita) {
                PacienteHasCita o = (PacienteHasCita) object;
                return getStringKey(o.getPacienteHasCitaPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PacienteHasCita.class.getName());
            }
        }

    }

}
