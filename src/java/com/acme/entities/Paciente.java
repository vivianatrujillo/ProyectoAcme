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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    @NamedQuery(name = "Paciente.findByIdPaciente", query = "SELECT p FROM Paciente p WHERE p.idPaciente = :idPaciente"),
    @NamedQuery(name = "Paciente.findByEstadocivil", query = "SELECT p FROM Paciente p WHERE p.estadocivil = :estadocivil"),
    @NamedQuery(name = "Paciente.findByNombre1", query = "SELECT p FROM Paciente p WHERE p.nombre1 = :nombre1"),
    @NamedQuery(name = "Paciente.findByNombre2", query = "SELECT p FROM Paciente p WHERE p.nombre2 = :nombre2"),
    @NamedQuery(name = "Paciente.findByApellido1", query = "SELECT p FROM Paciente p WHERE p.apellido1 = :apellido1"),
    @NamedQuery(name = "Paciente.findByApellido2", query = "SELECT p FROM Paciente p WHERE p.apellido2 = :apellido2"),
    @NamedQuery(name = "Paciente.findByTelfijo", query = "SELECT p FROM Paciente p WHERE p.telfijo = :telfijo"),
    @NamedQuery(name = "Paciente.findByEmail", query = "SELECT p FROM Paciente p WHERE p.email = :email"),
    @NamedQuery(name = "Paciente.findByDireccion", query = "SELECT p FROM Paciente p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "Paciente.findByFechaNacimiento", query = "SELECT p FROM Paciente p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Paciente.findByNumcelular", query = "SELECT p FROM Paciente p WHERE p.numcelular = :numcelular")})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPaciente")
    private Integer idPaciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Estado_civil")
    private String estadocivil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "Nombre_1")
    private String nombre1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "Nombre_2")
    private String nombre2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "Apellido_1")
    private String apellido1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "Apellido_2")
    private String apellido2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tel_fijo")
    private long telfijo;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "E_mail")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Num_celular")
    private long numcelular;
    @JoinTable(name = "paciente_has_trabajador", joinColumns = {
        @JoinColumn(name = "Paciente_idPaciente", referencedColumnName = "idPaciente")}, inverseJoinColumns = {
        @JoinColumn(name = "Trabajador_idTrabajador", referencedColumnName = "idTrabajador")})
    @ManyToMany
    private List<Trabajador> trabajadorList;
    @ManyToMany(mappedBy = "pacienteList")
    private List<Enfermedad> enfermedadList;
    @JoinTable(name = "referencia_has_paciente", joinColumns = {
        @JoinColumn(name = "Paciente_idPaciente", referencedColumnName = "idPaciente")}, inverseJoinColumns = {
        @JoinColumn(name = "Referencia_idReferencia", referencedColumnName = "idReferencia")})
    @ManyToMany
    private List<Referencia> referenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<PacienteHasCita> pacienteHasCitaList;
    @JoinColumn(name = "RH_idRH", referencedColumnName = "idRH")
    @ManyToOne(optional = false)
    private Rh rHidRH;
    @JoinColumn(name = "Genero_idGenero", referencedColumnName = "idGenero")
    @ManyToOne(optional = false)
    private Genero generoidGenero;
    @JoinColumn(name = "Eps_idEps", referencedColumnName = "idEps")
    @ManyToOne(optional = false)
    private Eps epsidEps;
    @JoinColumn(name = "Valoracion_idValoracion", referencedColumnName = "idValoracion")
    @ManyToOne(optional = false)
    private Valoracion valoracionidValoracion;

    public Paciente() {
    }

    public Paciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Paciente(Integer idPaciente, String estadocivil, String nombre1, String nombre2, String apellido1, String apellido2, long telfijo, String email, String direccion, Date fechaNacimiento, long numcelular) {
        this.idPaciente = idPaciente;
        this.estadocivil = estadocivil;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telfijo = telfijo;
        this.email = email;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.numcelular = numcelular;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public long getTelfijo() {
        return telfijo;
    }

    public void setTelfijo(long telfijo) {
        this.telfijo = telfijo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public long getNumcelular() {
        return numcelular;
    }

    public void setNumcelular(long numcelular) {
        this.numcelular = numcelular;
    }

    @XmlTransient
    public List<Trabajador> getTrabajadorList() {
        return trabajadorList;
    }

    public void setTrabajadorList(List<Trabajador> trabajadorList) {
        this.trabajadorList = trabajadorList;
    }

    @XmlTransient
    public List<Enfermedad> getEnfermedadList() {
        return enfermedadList;
    }

    public void setEnfermedadList(List<Enfermedad> enfermedadList) {
        this.enfermedadList = enfermedadList;
    }

    @XmlTransient
    public List<Referencia> getReferenciaList() {
        return referenciaList;
    }

    public void setReferenciaList(List<Referencia> referenciaList) {
        this.referenciaList = referenciaList;
    }

    @XmlTransient
    public List<PacienteHasCita> getPacienteHasCitaList() {
        return pacienteHasCitaList;
    }

    public void setPacienteHasCitaList(List<PacienteHasCita> pacienteHasCitaList) {
        this.pacienteHasCitaList = pacienteHasCitaList;
    }

    public Rh getRHidRH() {
        return rHidRH;
    }

    public void setRHidRH(Rh rHidRH) {
        this.rHidRH = rHidRH;
    }

    public Genero getGeneroidGenero() {
        return generoidGenero;
    }

    public void setGeneroidGenero(Genero generoidGenero) {
        this.generoidGenero = generoidGenero;
    }

    public Eps getEpsidEps() {
        return epsidEps;
    }

    public void setEpsidEps(Eps epsidEps) {
        this.epsidEps = epsidEps;
    }

    public Valoracion getValoracionidValoracion() {
        return valoracionidValoracion;
    }

    public void setValoracionidValoracion(Valoracion valoracionidValoracion) {
        this.valoracionidValoracion = valoracionidValoracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaciente != null ? idPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.idPaciente == null && other.idPaciente != null) || (this.idPaciente != null && !this.idPaciente.equals(other.idPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + getIdPaciente();
    }
    
}
