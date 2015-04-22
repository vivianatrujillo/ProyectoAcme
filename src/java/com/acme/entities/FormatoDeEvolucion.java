/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "formato_de_evolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormatoDeEvolucion.findAll", query = "SELECT f FROM FormatoDeEvolucion f"),
    @NamedQuery(name = "FormatoDeEvolucion.findByIdFormatodeevolucioncol", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.idFormatodeevolucioncol = :idFormatodeevolucioncol"),
    @NamedQuery(name = "FormatoDeEvolucion.findByFecha", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FormatoDeEvolucion.findByHora", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.hora = :hora"),
    @NamedQuery(name = "FormatoDeEvolucion.findByLocalizacion", query = "SELECT f FROM FormatoDeEvolucion f WHERE f.localizacion = :localizacion")})
public class FormatoDeEvolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFormato_de_evolucioncol")
    private Integer idFormatodeevolucioncol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Localizacion")
    private String localizacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formatodeevolucionidFormatodeevolucioncol")
    private List<Historiaclinica> historiaclinicaList;

    public FormatoDeEvolucion() {
    }

    public FormatoDeEvolucion(Integer idFormatodeevolucioncol) {
        this.idFormatodeevolucioncol = idFormatodeevolucioncol;
    }

    public FormatoDeEvolucion(Integer idFormatodeevolucioncol, Date fecha, Date hora, String localizacion) {
        this.idFormatodeevolucioncol = idFormatodeevolucioncol;
        this.fecha = fecha;
        this.hora = hora;
        this.localizacion = localizacion;
    }

    public Integer getIdFormatodeevolucioncol() {
        return idFormatodeevolucioncol;
    }

    public void setIdFormatodeevolucioncol(Integer idFormatodeevolucioncol) {
        this.idFormatodeevolucioncol = idFormatodeevolucioncol;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    @XmlTransient
    public List<Historiaclinica> getHistoriaclinicaList() {
        return historiaclinicaList;
    }

    public void setHistoriaclinicaList(List<Historiaclinica> historiaclinicaList) {
        this.historiaclinicaList = historiaclinicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormatodeevolucioncol != null ? idFormatodeevolucioncol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormatoDeEvolucion)) {
            return false;
        }
        FormatoDeEvolucion other = (FormatoDeEvolucion) object;
        if ((this.idFormatodeevolucioncol == null && other.idFormatodeevolucioncol != null) || (this.idFormatodeevolucioncol != null && !this.idFormatodeevolucioncol.equals(other.idFormatodeevolucioncol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + getIdFormatodeevolucioncol();
    }
    
}
