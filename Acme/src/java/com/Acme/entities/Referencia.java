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
@Table(name = "referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Referencia.findAll", query = "SELECT r FROM Referencia r"),
    @NamedQuery(name = "Referencia.findByIdReferencia", query = "SELECT r FROM Referencia r WHERE r.idReferencia = :idReferencia"),
    @NamedQuery(name = "Referencia.findByReferenciafamiliar", query = "SELECT r FROM Referencia r WHERE r.referenciafamiliar = :referenciafamiliar")})
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idReferencia")
    private Integer idReferencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Referencia_familiar")
    private String referenciafamiliar;
    @ManyToMany(mappedBy = "referenciaList")
    private List<Paciente> pacienteList;

    public Referencia() {
    }

    public Referencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Referencia(Integer idReferencia, String referenciafamiliar) {
        this.idReferencia = idReferencia;
        this.referenciafamiliar = referenciafamiliar;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getReferenciafamiliar() {
        return referenciafamiliar;
    }

    public void setReferenciafamiliar(String referenciafamiliar) {
        this.referenciafamiliar = referenciafamiliar;
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
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referencia)) {
            return false;
        }
        Referencia other = (Referencia) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getReferenciafamiliar();
    }
    
}
