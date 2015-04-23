/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yiyi
 */
@Embeddable
public class PacienteHasCitaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "Paciente_idPaciente")
    private int pacienteidPaciente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cita_idCita")
    private int citaidCita;

    public PacienteHasCitaPK() {
    }

    public PacienteHasCitaPK(int pacienteidPaciente, int citaidCita) {
        this.pacienteidPaciente = pacienteidPaciente;
        this.citaidCita = citaidCita;
    }

    public int getPacienteidPaciente() {
        return pacienteidPaciente;
    }

    public void setPacienteidPaciente(int pacienteidPaciente) {
        this.pacienteidPaciente = pacienteidPaciente;
    }

    public int getCitaidCita() {
        return citaidCita;
    }

    public void setCitaidCita(int citaidCita) {
        this.citaidCita = citaidCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pacienteidPaciente;
        hash += (int) citaidCita;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PacienteHasCitaPK)) {
            return false;
        }
        PacienteHasCitaPK other = (PacienteHasCitaPK) object;
        if (this.pacienteidPaciente != other.pacienteidPaciente) {
            return false;
        }
        if (this.citaidCita != other.citaidCita) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + pacienteidPaciente + citaidCita;
    }
    
}
