/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yiyi
 */
@Entity
@Table(name = "tratamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tratamiento.findAll", query = "SELECT t FROM Tratamiento t"),
    @NamedQuery(name = "Tratamiento.findByIdTratamiento", query = "SELECT t FROM Tratamiento t WHERE t.idTratamiento = :idTratamiento"),
    @NamedQuery(name = "Tratamiento.findByNombretratamiento", query = "SELECT t FROM Tratamiento t WHERE t.nombretratamiento = :nombretratamiento")})
public class Tratamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTratamiento")
    private Integer idTratamiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre_tratamiento")
    private String nombretratamiento;
    @JoinTable(name = "valoracion_has_tratamiento", joinColumns = {
        @JoinColumn(name = "Tratamiento_idTratamiento", referencedColumnName = "idTratamiento")}, inverseJoinColumns = {
        @JoinColumn(name = "Valoracion_idValoracion", referencedColumnName = "idValoracion")})
    @ManyToMany
    private List<Valoracion> valoracionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tratamientoidTratamiento")
    private List<Historiaclinica> historiaclinicaList;

    public Tratamiento() {
    }

    public Tratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public Tratamiento(Integer idTratamiento, String nombretratamiento) {
        this.idTratamiento = idTratamiento;
        this.nombretratamiento = nombretratamiento;
    }

    public Integer getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Integer idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getNombretratamiento() {
        return nombretratamiento;
    }

    public void setNombretratamiento(String nombretratamiento) {
        this.nombretratamiento = nombretratamiento;
    }

    @XmlTransient
    public List<Valoracion> getValoracionList() {
        return valoracionList;
    }

    public void setValoracionList(List<Valoracion> valoracionList) {
        this.valoracionList = valoracionList;
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
        hash += (idTratamiento != null ? idTratamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tratamiento)) {
            return false;
        }
        Tratamiento other = (Tratamiento) object;
        if ((this.idTratamiento == null && other.idTratamiento != null) || (this.idTratamiento != null && !this.idTratamiento.equals(other.idTratamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombretratamiento();
    }
    
}
