/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.controller;

import com.Acme.controller.exceptions.IllegalOrphanException;
import com.Acme.controller.exceptions.NonexistentEntityException;
import com.Acme.controller.exceptions.PreexistingEntityException;
import com.Acme.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Acme.entities.Eps;
import com.Acme.entities.Genero;
import com.Acme.entities.Rh;
import com.Acme.entities.Valoracion;
import com.Acme.entities.Trabajador;
import java.util.ArrayList;
import java.util.List;
import com.Acme.entities.Enfermedad;
import com.Acme.entities.Paciente;
import com.Acme.entities.Referencia;
import com.Acme.entities.PacienteHasCita;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (paciente.getTrabajadorList() == null) {
            paciente.setTrabajadorList(new ArrayList<Trabajador>());
        }
        if (paciente.getEnfermedadList() == null) {
            paciente.setEnfermedadList(new ArrayList<Enfermedad>());
        }
        if (paciente.getReferenciaList() == null) {
            paciente.setReferenciaList(new ArrayList<Referencia>());
        }
        if (paciente.getPacienteHasCitaList() == null) {
            paciente.setPacienteHasCitaList(new ArrayList<PacienteHasCita>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Eps epsidEps = paciente.getEpsidEps();
            if (epsidEps != null) {
                epsidEps = em.getReference(epsidEps.getClass(), epsidEps.getIdEps());
                paciente.setEpsidEps(epsidEps);
            }
            Genero generoidGenero = paciente.getGeneroidGenero();
            if (generoidGenero != null) {
                generoidGenero = em.getReference(generoidGenero.getClass(), generoidGenero.getIdGenero());
                paciente.setGeneroidGenero(generoidGenero);
            }
            Rh RHidRH = paciente.getRHidRH();
            if (RHidRH != null) {
                RHidRH = em.getReference(RHidRH.getClass(), RHidRH.getIdRH());
                paciente.setRHidRH(RHidRH);
            }
            Valoracion valoracionidValoracion = paciente.getValoracionidValoracion();
            if (valoracionidValoracion != null) {
                valoracionidValoracion = em.getReference(valoracionidValoracion.getClass(), valoracionidValoracion.getIdValoracion());
                paciente.setValoracionidValoracion(valoracionidValoracion);
            }
            List<Trabajador> attachedTrabajadorList = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListTrabajadorToAttach : paciente.getTrabajadorList()) {
                trabajadorListTrabajadorToAttach = em.getReference(trabajadorListTrabajadorToAttach.getClass(), trabajadorListTrabajadorToAttach.getIdTrabajador());
                attachedTrabajadorList.add(trabajadorListTrabajadorToAttach);
            }
            paciente.setTrabajadorList(attachedTrabajadorList);
            List<Enfermedad> attachedEnfermedadList = new ArrayList<Enfermedad>();
            for (Enfermedad enfermedadListEnfermedadToAttach : paciente.getEnfermedadList()) {
                enfermedadListEnfermedadToAttach = em.getReference(enfermedadListEnfermedadToAttach.getClass(), enfermedadListEnfermedadToAttach.getIdEnfermedad());
                attachedEnfermedadList.add(enfermedadListEnfermedadToAttach);
            }
            paciente.setEnfermedadList(attachedEnfermedadList);
            List<Referencia> attachedReferenciaList = new ArrayList<Referencia>();
            for (Referencia referenciaListReferenciaToAttach : paciente.getReferenciaList()) {
                referenciaListReferenciaToAttach = em.getReference(referenciaListReferenciaToAttach.getClass(), referenciaListReferenciaToAttach.getIdReferencia());
                attachedReferenciaList.add(referenciaListReferenciaToAttach);
            }
            paciente.setReferenciaList(attachedReferenciaList);
            List<PacienteHasCita> attachedPacienteHasCitaList = new ArrayList<PacienteHasCita>();
            for (PacienteHasCita pacienteHasCitaListPacienteHasCitaToAttach : paciente.getPacienteHasCitaList()) {
                pacienteHasCitaListPacienteHasCitaToAttach = em.getReference(pacienteHasCitaListPacienteHasCitaToAttach.getClass(), pacienteHasCitaListPacienteHasCitaToAttach.getPacienteHasCitaPK());
                attachedPacienteHasCitaList.add(pacienteHasCitaListPacienteHasCitaToAttach);
            }
            paciente.setPacienteHasCitaList(attachedPacienteHasCitaList);
            em.persist(paciente);
            if (epsidEps != null) {
                epsidEps.getPacienteList().add(paciente);
                epsidEps = em.merge(epsidEps);
            }
            if (generoidGenero != null) {
                generoidGenero.getPacienteList().add(paciente);
                generoidGenero = em.merge(generoidGenero);
            }
            if (RHidRH != null) {
                RHidRH.getPacienteList().add(paciente);
                RHidRH = em.merge(RHidRH);
            }
            if (valoracionidValoracion != null) {
                valoracionidValoracion.getPacienteList().add(paciente);
                valoracionidValoracion = em.merge(valoracionidValoracion);
            }
            for (Trabajador trabajadorListTrabajador : paciente.getTrabajadorList()) {
                trabajadorListTrabajador.getPacienteList().add(paciente);
                trabajadorListTrabajador = em.merge(trabajadorListTrabajador);
            }
            for (Enfermedad enfermedadListEnfermedad : paciente.getEnfermedadList()) {
                enfermedadListEnfermedad.getPacienteList().add(paciente);
                enfermedadListEnfermedad = em.merge(enfermedadListEnfermedad);
            }
            for (Referencia referenciaListReferencia : paciente.getReferenciaList()) {
                referenciaListReferencia.getPacienteList().add(paciente);
                referenciaListReferencia = em.merge(referenciaListReferencia);
            }
            for (PacienteHasCita pacienteHasCitaListPacienteHasCita : paciente.getPacienteHasCitaList()) {
                Paciente oldPacienteOfPacienteHasCitaListPacienteHasCita = pacienteHasCitaListPacienteHasCita.getPaciente();
                pacienteHasCitaListPacienteHasCita.setPaciente(paciente);
                pacienteHasCitaListPacienteHasCita = em.merge(pacienteHasCitaListPacienteHasCita);
                if (oldPacienteOfPacienteHasCitaListPacienteHasCita != null) {
                    oldPacienteOfPacienteHasCitaListPacienteHasCita.getPacienteHasCitaList().remove(pacienteHasCitaListPacienteHasCita);
                    oldPacienteOfPacienteHasCitaListPacienteHasCita = em.merge(oldPacienteOfPacienteHasCitaListPacienteHasCita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPaciente(paciente.getIdPaciente()) != null) {
                throw new PreexistingEntityException("Paciente " + paciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getIdPaciente());
            Eps epsidEpsOld = persistentPaciente.getEpsidEps();
            Eps epsidEpsNew = paciente.getEpsidEps();
            Genero generoidGeneroOld = persistentPaciente.getGeneroidGenero();
            Genero generoidGeneroNew = paciente.getGeneroidGenero();
            Rh RHidRHOld = persistentPaciente.getRHidRH();
            Rh RHidRHNew = paciente.getRHidRH();
            Valoracion valoracionidValoracionOld = persistentPaciente.getValoracionidValoracion();
            Valoracion valoracionidValoracionNew = paciente.getValoracionidValoracion();
            List<Trabajador> trabajadorListOld = persistentPaciente.getTrabajadorList();
            List<Trabajador> trabajadorListNew = paciente.getTrabajadorList();
            List<Enfermedad> enfermedadListOld = persistentPaciente.getEnfermedadList();
            List<Enfermedad> enfermedadListNew = paciente.getEnfermedadList();
            List<Referencia> referenciaListOld = persistentPaciente.getReferenciaList();
            List<Referencia> referenciaListNew = paciente.getReferenciaList();
            List<PacienteHasCita> pacienteHasCitaListOld = persistentPaciente.getPacienteHasCitaList();
            List<PacienteHasCita> pacienteHasCitaListNew = paciente.getPacienteHasCitaList();
            List<String> illegalOrphanMessages = null;
            for (PacienteHasCita pacienteHasCitaListOldPacienteHasCita : pacienteHasCitaListOld) {
                if (!pacienteHasCitaListNew.contains(pacienteHasCitaListOldPacienteHasCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteHasCita " + pacienteHasCitaListOldPacienteHasCita + " since its paciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (epsidEpsNew != null) {
                epsidEpsNew = em.getReference(epsidEpsNew.getClass(), epsidEpsNew.getIdEps());
                paciente.setEpsidEps(epsidEpsNew);
            }
            if (generoidGeneroNew != null) {
                generoidGeneroNew = em.getReference(generoidGeneroNew.getClass(), generoidGeneroNew.getIdGenero());
                paciente.setGeneroidGenero(generoidGeneroNew);
            }
            if (RHidRHNew != null) {
                RHidRHNew = em.getReference(RHidRHNew.getClass(), RHidRHNew.getIdRH());
                paciente.setRHidRH(RHidRHNew);
            }
            if (valoracionidValoracionNew != null) {
                valoracionidValoracionNew = em.getReference(valoracionidValoracionNew.getClass(), valoracionidValoracionNew.getIdValoracion());
                paciente.setValoracionidValoracion(valoracionidValoracionNew);
            }
            List<Trabajador> attachedTrabajadorListNew = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListNewTrabajadorToAttach : trabajadorListNew) {
                trabajadorListNewTrabajadorToAttach = em.getReference(trabajadorListNewTrabajadorToAttach.getClass(), trabajadorListNewTrabajadorToAttach.getIdTrabajador());
                attachedTrabajadorListNew.add(trabajadorListNewTrabajadorToAttach);
            }
            trabajadorListNew = attachedTrabajadorListNew;
            paciente.setTrabajadorList(trabajadorListNew);
            List<Enfermedad> attachedEnfermedadListNew = new ArrayList<Enfermedad>();
            for (Enfermedad enfermedadListNewEnfermedadToAttach : enfermedadListNew) {
                enfermedadListNewEnfermedadToAttach = em.getReference(enfermedadListNewEnfermedadToAttach.getClass(), enfermedadListNewEnfermedadToAttach.getIdEnfermedad());
                attachedEnfermedadListNew.add(enfermedadListNewEnfermedadToAttach);
            }
            enfermedadListNew = attachedEnfermedadListNew;
            paciente.setEnfermedadList(enfermedadListNew);
            List<Referencia> attachedReferenciaListNew = new ArrayList<Referencia>();
            for (Referencia referenciaListNewReferenciaToAttach : referenciaListNew) {
                referenciaListNewReferenciaToAttach = em.getReference(referenciaListNewReferenciaToAttach.getClass(), referenciaListNewReferenciaToAttach.getIdReferencia());
                attachedReferenciaListNew.add(referenciaListNewReferenciaToAttach);
            }
            referenciaListNew = attachedReferenciaListNew;
            paciente.setReferenciaList(referenciaListNew);
            List<PacienteHasCita> attachedPacienteHasCitaListNew = new ArrayList<PacienteHasCita>();
            for (PacienteHasCita pacienteHasCitaListNewPacienteHasCitaToAttach : pacienteHasCitaListNew) {
                pacienteHasCitaListNewPacienteHasCitaToAttach = em.getReference(pacienteHasCitaListNewPacienteHasCitaToAttach.getClass(), pacienteHasCitaListNewPacienteHasCitaToAttach.getPacienteHasCitaPK());
                attachedPacienteHasCitaListNew.add(pacienteHasCitaListNewPacienteHasCitaToAttach);
            }
            pacienteHasCitaListNew = attachedPacienteHasCitaListNew;
            paciente.setPacienteHasCitaList(pacienteHasCitaListNew);
            paciente = em.merge(paciente);
            if (epsidEpsOld != null && !epsidEpsOld.equals(epsidEpsNew)) {
                epsidEpsOld.getPacienteList().remove(paciente);
                epsidEpsOld = em.merge(epsidEpsOld);
            }
            if (epsidEpsNew != null && !epsidEpsNew.equals(epsidEpsOld)) {
                epsidEpsNew.getPacienteList().add(paciente);
                epsidEpsNew = em.merge(epsidEpsNew);
            }
            if (generoidGeneroOld != null && !generoidGeneroOld.equals(generoidGeneroNew)) {
                generoidGeneroOld.getPacienteList().remove(paciente);
                generoidGeneroOld = em.merge(generoidGeneroOld);
            }
            if (generoidGeneroNew != null && !generoidGeneroNew.equals(generoidGeneroOld)) {
                generoidGeneroNew.getPacienteList().add(paciente);
                generoidGeneroNew = em.merge(generoidGeneroNew);
            }
            if (RHidRHOld != null && !RHidRHOld.equals(RHidRHNew)) {
                RHidRHOld.getPacienteList().remove(paciente);
                RHidRHOld = em.merge(RHidRHOld);
            }
            if (RHidRHNew != null && !RHidRHNew.equals(RHidRHOld)) {
                RHidRHNew.getPacienteList().add(paciente);
                RHidRHNew = em.merge(RHidRHNew);
            }
            if (valoracionidValoracionOld != null && !valoracionidValoracionOld.equals(valoracionidValoracionNew)) {
                valoracionidValoracionOld.getPacienteList().remove(paciente);
                valoracionidValoracionOld = em.merge(valoracionidValoracionOld);
            }
            if (valoracionidValoracionNew != null && !valoracionidValoracionNew.equals(valoracionidValoracionOld)) {
                valoracionidValoracionNew.getPacienteList().add(paciente);
                valoracionidValoracionNew = em.merge(valoracionidValoracionNew);
            }
            for (Trabajador trabajadorListOldTrabajador : trabajadorListOld) {
                if (!trabajadorListNew.contains(trabajadorListOldTrabajador)) {
                    trabajadorListOldTrabajador.getPacienteList().remove(paciente);
                    trabajadorListOldTrabajador = em.merge(trabajadorListOldTrabajador);
                }
            }
            for (Trabajador trabajadorListNewTrabajador : trabajadorListNew) {
                if (!trabajadorListOld.contains(trabajadorListNewTrabajador)) {
                    trabajadorListNewTrabajador.getPacienteList().add(paciente);
                    trabajadorListNewTrabajador = em.merge(trabajadorListNewTrabajador);
                }
            }
            for (Enfermedad enfermedadListOldEnfermedad : enfermedadListOld) {
                if (!enfermedadListNew.contains(enfermedadListOldEnfermedad)) {
                    enfermedadListOldEnfermedad.getPacienteList().remove(paciente);
                    enfermedadListOldEnfermedad = em.merge(enfermedadListOldEnfermedad);
                }
            }
            for (Enfermedad enfermedadListNewEnfermedad : enfermedadListNew) {
                if (!enfermedadListOld.contains(enfermedadListNewEnfermedad)) {
                    enfermedadListNewEnfermedad.getPacienteList().add(paciente);
                    enfermedadListNewEnfermedad = em.merge(enfermedadListNewEnfermedad);
                }
            }
            for (Referencia referenciaListOldReferencia : referenciaListOld) {
                if (!referenciaListNew.contains(referenciaListOldReferencia)) {
                    referenciaListOldReferencia.getPacienteList().remove(paciente);
                    referenciaListOldReferencia = em.merge(referenciaListOldReferencia);
                }
            }
            for (Referencia referenciaListNewReferencia : referenciaListNew) {
                if (!referenciaListOld.contains(referenciaListNewReferencia)) {
                    referenciaListNewReferencia.getPacienteList().add(paciente);
                    referenciaListNewReferencia = em.merge(referenciaListNewReferencia);
                }
            }
            for (PacienteHasCita pacienteHasCitaListNewPacienteHasCita : pacienteHasCitaListNew) {
                if (!pacienteHasCitaListOld.contains(pacienteHasCitaListNewPacienteHasCita)) {
                    Paciente oldPacienteOfPacienteHasCitaListNewPacienteHasCita = pacienteHasCitaListNewPacienteHasCita.getPaciente();
                    pacienteHasCitaListNewPacienteHasCita.setPaciente(paciente);
                    pacienteHasCitaListNewPacienteHasCita = em.merge(pacienteHasCitaListNewPacienteHasCita);
                    if (oldPacienteOfPacienteHasCitaListNewPacienteHasCita != null && !oldPacienteOfPacienteHasCitaListNewPacienteHasCita.equals(paciente)) {
                        oldPacienteOfPacienteHasCitaListNewPacienteHasCita.getPacienteHasCitaList().remove(pacienteHasCitaListNewPacienteHasCita);
                        oldPacienteOfPacienteHasCitaListNewPacienteHasCita = em.merge(oldPacienteOfPacienteHasCitaListNewPacienteHasCita);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paciente.getIdPaciente();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getIdPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteHasCita> pacienteHasCitaListOrphanCheck = paciente.getPacienteHasCitaList();
            for (PacienteHasCita pacienteHasCitaListOrphanCheckPacienteHasCita : pacienteHasCitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the PacienteHasCita " + pacienteHasCitaListOrphanCheckPacienteHasCita + " in its pacienteHasCitaList field has a non-nullable paciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Eps epsidEps = paciente.getEpsidEps();
            if (epsidEps != null) {
                epsidEps.getPacienteList().remove(paciente);
                epsidEps = em.merge(epsidEps);
            }
            Genero generoidGenero = paciente.getGeneroidGenero();
            if (generoidGenero != null) {
                generoidGenero.getPacienteList().remove(paciente);
                generoidGenero = em.merge(generoidGenero);
            }
            Rh RHidRH = paciente.getRHidRH();
            if (RHidRH != null) {
                RHidRH.getPacienteList().remove(paciente);
                RHidRH = em.merge(RHidRH);
            }
            Valoracion valoracionidValoracion = paciente.getValoracionidValoracion();
            if (valoracionidValoracion != null) {
                valoracionidValoracion.getPacienteList().remove(paciente);
                valoracionidValoracion = em.merge(valoracionidValoracion);
            }
            List<Trabajador> trabajadorList = paciente.getTrabajadorList();
            for (Trabajador trabajadorListTrabajador : trabajadorList) {
                trabajadorListTrabajador.getPacienteList().remove(paciente);
                trabajadorListTrabajador = em.merge(trabajadorListTrabajador);
            }
            List<Enfermedad> enfermedadList = paciente.getEnfermedadList();
            for (Enfermedad enfermedadListEnfermedad : enfermedadList) {
                enfermedadListEnfermedad.getPacienteList().remove(paciente);
                enfermedadListEnfermedad = em.merge(enfermedadListEnfermedad);
            }
            List<Referencia> referenciaList = paciente.getReferenciaList();
            for (Referencia referenciaListReferencia : referenciaList) {
                referenciaListReferencia.getPacienteList().remove(paciente);
                referenciaListReferencia = em.merge(referenciaListReferencia);
            }
            em.remove(paciente);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Paciente findPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
