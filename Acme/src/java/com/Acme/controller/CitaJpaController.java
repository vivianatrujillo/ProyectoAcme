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
import com.Acme.entities.Cita;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Acme.entities.PacienteHasCita;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cita.getPacienteHasCitaList() == null) {
            cita.setPacienteHasCitaList(new ArrayList<PacienteHasCita>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PacienteHasCita> attachedPacienteHasCitaList = new ArrayList<PacienteHasCita>();
            for (PacienteHasCita pacienteHasCitaListPacienteHasCitaToAttach : cita.getPacienteHasCitaList()) {
                pacienteHasCitaListPacienteHasCitaToAttach = em.getReference(pacienteHasCitaListPacienteHasCitaToAttach.getClass(), pacienteHasCitaListPacienteHasCitaToAttach.getPacienteHasCitaPK());
                attachedPacienteHasCitaList.add(pacienteHasCitaListPacienteHasCitaToAttach);
            }
            cita.setPacienteHasCitaList(attachedPacienteHasCitaList);
            em.persist(cita);
            for (PacienteHasCita pacienteHasCitaListPacienteHasCita : cita.getPacienteHasCitaList()) {
                Cita oldCitaOfPacienteHasCitaListPacienteHasCita = pacienteHasCitaListPacienteHasCita.getCita();
                pacienteHasCitaListPacienteHasCita.setCita(cita);
                pacienteHasCitaListPacienteHasCita = em.merge(pacienteHasCitaListPacienteHasCita);
                if (oldCitaOfPacienteHasCitaListPacienteHasCita != null) {
                    oldCitaOfPacienteHasCitaListPacienteHasCita.getPacienteHasCitaList().remove(pacienteHasCitaListPacienteHasCita);
                    oldCitaOfPacienteHasCitaListPacienteHasCita = em.merge(oldCitaOfPacienteHasCitaListPacienteHasCita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCita(cita.getIdCita()) != null) {
                throw new PreexistingEntityException("Cita " + cita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita persistentCita = em.find(Cita.class, cita.getIdCita());
            List<PacienteHasCita> pacienteHasCitaListOld = persistentCita.getPacienteHasCitaList();
            List<PacienteHasCita> pacienteHasCitaListNew = cita.getPacienteHasCitaList();
            List<String> illegalOrphanMessages = null;
            for (PacienteHasCita pacienteHasCitaListOldPacienteHasCita : pacienteHasCitaListOld) {
                if (!pacienteHasCitaListNew.contains(pacienteHasCitaListOldPacienteHasCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PacienteHasCita " + pacienteHasCitaListOldPacienteHasCita + " since its cita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PacienteHasCita> attachedPacienteHasCitaListNew = new ArrayList<PacienteHasCita>();
            for (PacienteHasCita pacienteHasCitaListNewPacienteHasCitaToAttach : pacienteHasCitaListNew) {
                pacienteHasCitaListNewPacienteHasCitaToAttach = em.getReference(pacienteHasCitaListNewPacienteHasCitaToAttach.getClass(), pacienteHasCitaListNewPacienteHasCitaToAttach.getPacienteHasCitaPK());
                attachedPacienteHasCitaListNew.add(pacienteHasCitaListNewPacienteHasCitaToAttach);
            }
            pacienteHasCitaListNew = attachedPacienteHasCitaListNew;
            cita.setPacienteHasCitaList(pacienteHasCitaListNew);
            cita = em.merge(cita);
            for (PacienteHasCita pacienteHasCitaListNewPacienteHasCita : pacienteHasCitaListNew) {
                if (!pacienteHasCitaListOld.contains(pacienteHasCitaListNewPacienteHasCita)) {
                    Cita oldCitaOfPacienteHasCitaListNewPacienteHasCita = pacienteHasCitaListNewPacienteHasCita.getCita();
                    pacienteHasCitaListNewPacienteHasCita.setCita(cita);
                    pacienteHasCitaListNewPacienteHasCita = em.merge(pacienteHasCitaListNewPacienteHasCita);
                    if (oldCitaOfPacienteHasCitaListNewPacienteHasCita != null && !oldCitaOfPacienteHasCitaListNewPacienteHasCita.equals(cita)) {
                        oldCitaOfPacienteHasCitaListNewPacienteHasCita.getPacienteHasCitaList().remove(pacienteHasCitaListNewPacienteHasCita);
                        oldCitaOfPacienteHasCitaListNewPacienteHasCita = em.merge(oldCitaOfPacienteHasCitaListNewPacienteHasCita);
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
                Integer id = cita.getIdCita();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PacienteHasCita> pacienteHasCitaListOrphanCheck = cita.getPacienteHasCitaList();
            for (PacienteHasCita pacienteHasCitaListOrphanCheckPacienteHasCita : pacienteHasCitaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cita (" + cita + ") cannot be destroyed since the PacienteHasCita " + pacienteHasCitaListOrphanCheckPacienteHasCita + " in its pacienteHasCitaList field has a non-nullable cita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cita);
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

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
