/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "rh")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rh.findAll", query = "SELECT r FROM Rh r"),
    @NamedQuery(name = "Rh.findByIdRH", query = "SELECT r FROM Rh r WHERE r.idRH = :idRH"),
    @NamedQuery(name = "Rh.findByNombre", query = "SELECT r FROM Rh r WHERE r.nombre = :nombre")})
public class Rh implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idRH")
    private Integer idRH;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "Nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rHidRH")
    private List<Paciente> pacienteList;

    public Rh() {
    }

    public Rh(Integer idRH) {
        this.idRH = idRH;
    }

    public Rh(Integer idRH, String nombre) {
        this.idRH = idRH;
        this.nombre = nombre;
    }

    public Integer getIdRH() {
        return idRH;
    }

    public void setIdRH(Integer idRH) {
        this.idRH = idRH;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Paciente> getPacienteList() {
        return pacienteList;
    }

    public void setPacienteList(List<Paciente> pacienteList) {
        this.pacienteList = pacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRH != null ? idRH.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rh)) {
            return false;
        }
        Rh other = (Rh) object;
        if ((this.idRH == null && other.idRH != null) || (this.idRH != null && !this.idRH.equals(other.idRH))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
}
