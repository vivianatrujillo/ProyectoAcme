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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yiyi
 */
@Entity
@Table(name = "trabajador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajador.findAll", query = "SELECT t FROM Trabajador t"),
    @NamedQuery(name = "Trabajador.findByIdTrabajador", query = "SELECT t FROM Trabajador t WHERE t.idTrabajador = :idTrabajador"),
    @NamedQuery(name = "Trabajador.findByTurnotrabajador", query = "SELECT t FROM Trabajador t WHERE t.turnotrabajador = :turnotrabajador")})
public class Trabajador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTrabajador")
    private Integer idTrabajador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Turno_trabajador")
    private int turnotrabajador;
    @ManyToMany(mappedBy = "trabajadorList")
    private List<Paciente> pacienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajadoridTrabajador")
    private List<Rol> rolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajadoridTrabajador")
    private List<Historiaclinica> historiaclinicaList;

    public Trabajador() {
    }

    public Trabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Trabajador(Integer idTrabajador, int turnotrabajador) {
        this.idTrabajador = idTrabajador;
        this.turnotrabajador = turnotrabajador;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getTurnotrabajador() {
        return turnotrabajador;
    }

    public void setTurnotrabajador(int turnotrabajador) {
        this.turnotrabajador = turnotrabajador;
    }

    @XmlTransient
    public List<Paciente> getPacienteList() {
        return pacienteList;
    }

    public void setPacienteList(List<Paciente> pacienteList) {
        this.pacienteList = pacienteList;
    }

    @XmlTransient
    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
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
        hash += (idTrabajador != null ? idTrabajador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.idTrabajador == null && other.idTrabajador != null) || (this.idTrabajador != null && !this.idTrabajador.equals(other.idTrabajador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + idTrabajador;
    }
    
}
