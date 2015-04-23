/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "enfermedad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enfermedad.findAll", query = "SELECT e FROM Enfermedad e"),
    @NamedQuery(name = "Enfermedad.findByIdEnfermedad", query = "SELECT e FROM Enfermedad e WHERE e.idEnfermedad = :idEnfermedad"),
    @NamedQuery(name = "Enfermedad.findByNombreenfermedad", query = "SELECT e FROM Enfermedad e WHERE e.nombreenfermedad = :nombreenfermedad")})
public class Enfermedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEnfermedad")
    private Integer idEnfermedad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre_enfermedad")
    private String nombreenfermedad;
    @JoinTable(name = "paciente_has_enfermedad", joinColumns = {
        @JoinColumn(name = "Enfermedad_idEnfermedad", referencedColumnName = "idEnfermedad")}, inverseJoinColumns = {
        @JoinColumn(name = "Paciente_idPaciente", referencedColumnName = "idPaciente")})
    @ManyToMany
    private List<Paciente> pacienteList;

    public Enfermedad() {
    }

    public Enfermedad(Integer idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    public Enfermedad(Integer idEnfermedad, String nombreenfermedad) {
        this.idEnfermedad = idEnfermedad;
        this.nombreenfermedad = nombreenfermedad;
    }

    public Integer getIdEnfermedad() {
        return idEnfermedad;
    }

    public void setIdEnfermedad(Integer idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    public String getNombreenfermedad() {
        return nombreenfermedad;
    }

    public void setNombreenfermedad(String nombreenfermedad) {
        this.nombreenfermedad = nombreenfermedad;
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
        hash += (idEnfermedad != null ? idEnfermedad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enfermedad)) {
            return false;
        }
        Enfermedad other = (Enfermedad) object;
        if ((this.idEnfermedad == null && other.idEnfermedad != null) || (this.idEnfermedad != null && !this.idEnfermedad.equals(other.idEnfermedad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombreenfermedad();
    }
    
}
