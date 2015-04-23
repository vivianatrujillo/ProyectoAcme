/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yiyi
 */
@Entity
@Table(name = "historiaclinica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historiaclinica.findAll", query = "SELECT h FROM Historiaclinica h"),
    @NamedQuery(name = "Historiaclinica.findByIdHistoriaClinica", query = "SELECT h FROM Historiaclinica h WHERE h.idHistoriaClinica = :idHistoriaClinica")})
public class Historiaclinica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idHistoriaClinica")
    private Integer idHistoriaClinica;
    @JoinColumn(name = "Diagnostico_idDiagnostico", referencedColumnName = "idDiagnostico")
    @ManyToOne(optional = false)
    private Diagnostico diagnosticoidDiagnostico;
    @JoinColumn(name = "Formato_de_evolucion_idFormato_de_evolucioncol", referencedColumnName = "idFormato_de_evolucioncol")
    @ManyToOne(optional = false)
    private FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncol;
    @JoinColumn(name = "Trabajador_idTrabajador", referencedColumnName = "idTrabajador")
    @ManyToOne(optional = false)
    private Trabajador trabajadoridTrabajador;
    @JoinColumn(name = "Tratamiento_idTratamiento", referencedColumnName = "idTratamiento")
    @ManyToOne(optional = false)
    private Tratamiento tratamientoidTratamiento;

    public Historiaclinica() {
    }

    public Historiaclinica(Integer idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public Integer getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    public void setIdHistoriaClinica(Integer idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public Diagnostico getDiagnosticoidDiagnostico() {
        return diagnosticoidDiagnostico;
    }

    public void setDiagnosticoidDiagnostico(Diagnostico diagnosticoidDiagnostico) {
        this.diagnosticoidDiagnostico = diagnosticoidDiagnostico;
    }

    public FormatoDeEvolucion getFormatodeevolucionidFormatodeevolucioncol() {
        return formatodeevolucionidFormatodeevolucioncol;
    }

    public void setFormatodeevolucionidFormatodeevolucioncol(FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncol) {
        this.formatodeevolucionidFormatodeevolucioncol = formatodeevolucionidFormatodeevolucioncol;
    }

    public Trabajador getTrabajadoridTrabajador() {
        return trabajadoridTrabajador;
    }

    public void setTrabajadoridTrabajador(Trabajador trabajadoridTrabajador) {
        this.trabajadoridTrabajador = trabajadoridTrabajador;
    }

    public Tratamiento getTratamientoidTratamiento() {
        return tratamientoidTratamiento;
    }

    public void setTratamientoidTratamiento(Tratamiento tratamientoidTratamiento) {
        this.tratamientoidTratamiento = tratamientoidTratamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoriaClinica != null ? idHistoriaClinica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historiaclinica)) {
            return false;
        }
        Historiaclinica other = (Historiaclinica) object;
        if ((this.idHistoriaClinica == null && other.idHistoriaClinica != null) || (this.idHistoriaClinica != null && !this.idHistoriaClinica.equals(other.idHistoriaClinica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + idHistoriaClinica;
    }
    
}
