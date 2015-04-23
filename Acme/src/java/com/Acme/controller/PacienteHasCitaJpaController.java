/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.controller;

import com.Acme.controller.exceptions.NonexistentEntityException;
import com.Acme.controller.exceptions.PreexistingEntityException;
import com.Acme.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Acme.entities.Cita;
import com.Acme.entities.Paciente;
import com.Acme.entities.PacienteHasCita;
import com.Acme.entities.PacienteHasCitaPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class PacienteHasCitaJpaController implements Serializable {

    public PacienteHasCitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PacienteHasCita pacienteHasCita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pacienteHasCita.getPacienteHasCitaPK() == null) {
            pacienteHasCita.setPacienteHasCitaPK(new PacienteHasCitaPK());
        }
        pacienteHasCita.getPacienteHasCitaPK().setPacienteidPaciente(pacienteHasCita.getPaciente().getIdPaciente());
        pacienteHasCita.getPacienteHasCitaPK().setCitaidCita(pacienteHasCita.getCita().getIdCita());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita cita = pacienteHasCita.getCita();
            if (cita != null) {
                cita = em.getReference(cita.getClass(), cita.getIdCita());
                pacienteHasCita.setCita(cita);
            }
            Paciente paciente = pacienteHasCita.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getIdPaciente());
                pacienteHasCita.setPaciente(paciente);
            }
            em.persist(pacienteHasCita);
            if (cita != null) {
                cita.getPacienteHasCitaList().add(pacienteHasCita);
                cita = em.merge(cita);
            }
            if (paciente != null) {
                paciente.getPacienteHasCitaList().add(pacienteHasCita);
                paciente = em.merge(paciente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPacienteHasCita(pacienteHasCita.getPacienteHasCitaPK()) != null) {
                throw new PreexistingEntityException("PacienteHasCita " + pacienteHasCita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PacienteHasCita pacienteHasCita) throws NonexistentEntityException, RollbackFailureException, Exception {
        pacienteHasCita.getPacienteHasCitaPK().setPacienteidPaciente(pacienteHasCita.getPaciente().getIdPaciente());
        pacienteHasCita.getPacienteHasCitaPK().setCitaidCita(pacienteHasCita.getCita().getIdCita());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteHasCita persistentPacienteHasCita = em.find(PacienteHasCita.class, pacienteHasCita.getPacienteHasCitaPK());
            Cita citaOld = persistentPacienteHasCita.getCita();
            Cita citaNew = pacienteHasCita.getCita();
            Paciente pacienteOld = persistentPacienteHasCita.getPaciente();
            Paciente pacienteNew = pacienteHasCita.getPaciente();
            if (citaNew != null) {
                citaNew = em.getReference(citaNew.getClass(), citaNew.getIdCita());
                pacienteHasCita.setCita(citaNew);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getIdPaciente());
                pacienteHasCita.setPaciente(pacienteNew);
            }
            pacienteHasCita = em.merge(pacienteHasCita);
            if (citaOld != null && !citaOld.equals(citaNew)) {
                citaOld.getPacienteHasCitaList().remove(pacienteHasCita);
                citaOld = em.merge(citaOld);
            }
            if (citaNew != null && !citaNew.equals(citaOld)) {
                citaNew.getPacienteHasCitaList().add(pacienteHasCita);
                citaNew = em.merge(citaNew);
            }
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getPacienteHasCitaList().remove(pacienteHasCita);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getPacienteHasCitaList().add(pacienteHasCita);
                pacienteNew = em.merge(pacienteNew);
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
                PacienteHasCitaPK id = pacienteHasCita.getPacienteHasCitaPK();
                if (findPacienteHasCita(id) == null) {
                    throw new NonexistentEntityException("The pacienteHasCita with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PacienteHasCitaPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PacienteHasCita pacienteHasCita;
            try {
                pacienteHasCita = em.getReference(PacienteHasCita.class, id);
                pacienteHasCita.getPacienteHasCitaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacienteHasCita with id " + id + " no longer exists.", enfe);
            }
            Cita cita = pacienteHasCita.getCita();
            if (cita != null) {
                cita.getPacienteHasCitaList().remove(pacienteHasCita);
                cita = em.merge(cita);
            }
            Paciente paciente = pacienteHasCita.getPaciente();
            if (paciente != null) {
                paciente.getPacienteHasCitaList().remove(pacienteHasCita);
                paciente = em.merge(paciente);
            }
            em.remove(pacienteHasCita);
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

    public List<PacienteHasCita> findPacienteHasCitaEntities() {
        return findPacienteHasCitaEntities(true, -1, -1);
    }

    public List<PacienteHasCita> findPacienteHasCitaEntities(int maxResults, int firstResult) {
        return findPacienteHasCitaEntities(false, maxResults, firstResult);
    }

    private List<PacienteHasCita> findPacienteHasCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PacienteHasCita.class));
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

    public PacienteHasCita findPacienteHasCita(PacienteHasCitaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PacienteHasCita.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteHasCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PacienteHasCita> rt = cq.from(PacienteHasCita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
