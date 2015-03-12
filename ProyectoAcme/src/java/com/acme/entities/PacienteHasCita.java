/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "paciente_has_cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PacienteHasCita.findAll", query = "SELECT p FROM PacienteHasCita p"),
    @NamedQuery(name = "PacienteHasCita.findByPacienteidPaciente", query = "SELECT p FROM PacienteHasCita p WHERE p.pacienteHasCitaPK.pacienteidPaciente = :pacienteidPaciente"),
    @NamedQuery(name = "PacienteHasCita.findByCitaidCita", query = "SELECT p FROM PacienteHasCita p WHERE p.pacienteHasCitaPK.citaidCita = :citaidCita"),
    @NamedQuery(name = "PacienteHasCita.findByFecha", query = "SELECT p FROM PacienteHasCita p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "PacienteHasCita.findByHora", query = "SELECT p FROM PacienteHasCita p WHERE p.hora = :hora")})
public class PacienteHasCita implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PacienteHasCitaPK pacienteHasCitaPK;
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
    @JoinColumn(name = "Paciente_idPaciente", referencedColumnName = "idPaciente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "Cita_idCita", referencedColumnName = "idCita", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cita cita;

    public PacienteHasCita() {
    }

    public PacienteHasCita(PacienteHasCitaPK pacienteHasCitaPK) {
        this.pacienteHasCitaPK = pacienteHasCitaPK;
    }

    public PacienteHasCita(PacienteHasCitaPK pacienteHasCitaPK, Date fecha, Date hora) {
        this.pacienteHasCitaPK = pacienteHasCitaPK;
        this.fecha = fecha;
        this.hora = hora;
    }

    public PacienteHasCita(int pacienteidPaciente, int citaidCita) {
        this.pacienteHasCitaPK = new PacienteHasCitaPK(pacienteidPaciente, citaidCita);
    }

    public PacienteHasCitaPK getPacienteHasCitaPK() {
        return pacienteHasCitaPK;
    }

    public void setPacienteHasCitaPK(PacienteHasCitaPK pacienteHasCitaPK) {
        this.pacienteHasCitaPK = pacienteHasCitaPK;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacienteHasCitaPK != null ? pacienteHasCitaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteHasCita)) {
            return false;
        }
        PacienteHasCita other = (PacienteHasCita) object;
        if ((this.pacienteHasCitaPK == null && other.pacienteHasCitaPK != null) || (this.pacienteHasCitaPK != null && !this.pacienteHasCitaPK.equals(other.pacienteHasCitaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+ getPaciente() + getHora() + getCita();
    }
    
}
