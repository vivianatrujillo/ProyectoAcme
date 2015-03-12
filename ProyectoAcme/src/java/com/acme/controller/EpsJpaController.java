/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.IllegalOrphanException;
import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import com.acme.entities.Eps;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class EpsJpaController implements Serializable {

    public EpsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Eps eps) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (eps.getPacienteList() == null) {
            eps.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : eps.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            eps.setPacienteList(attachedPacienteList);
            em.persist(eps);
            for (Paciente pacienteListPaciente : eps.getPacienteList()) {
                Eps oldEpsidEpsOfPacienteListPaciente = pacienteListPaciente.getEpsidEps();
                pacienteListPaciente.setEpsidEps(eps);
                pacienteListPaciente = em.merge(pacienteListPaciente);
                if (oldEpsidEpsOfPacienteListPaciente != null) {
                    oldEpsidEpsOfPacienteListPaciente.getPacienteList().remove(pacienteListPaciente);
                    oldEpsidEpsOfPacienteListPaciente = em.merge(oldEpsidEpsOfPacienteListPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEps(eps.getIdEps()) != null) {
                throw new PreexistingEntityException("Eps " + eps + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Eps eps) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Eps persistentEps = em.find(Eps.class, eps.getIdEps());
            List<Paciente> pacienteListOld = persistentEps.getPacienteList();
            List<Paciente> pacienteListNew = eps.getPacienteList();
            List<String> illegalOrphanMessages = null;
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteListOldPaciente + " since its epsidEps field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            eps.setPacienteList(pacienteListNew);
            eps = em.merge(eps);
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    Eps oldEpsidEpsOfPacienteListNewPaciente = pacienteListNewPaciente.getEpsidEps();
                    pacienteListNewPaciente.setEpsidEps(eps);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                    if (oldEpsidEpsOfPacienteListNewPaciente != null && !oldEpsidEpsOfPacienteListNewPaciente.equals(eps)) {
                        oldEpsidEpsOfPacienteListNewPaciente.getPacienteList().remove(pacienteListNewPaciente);
                        oldEpsidEpsOfPacienteListNewPaciente = em.merge(oldEpsidEpsOfPacienteListNewPaciente);
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
                Integer id = eps.getIdEps();
                if (findEps(id) == null) {
                    throw new NonexistentEntityException("The eps with id " + id + " no longer exists.");
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
            Eps eps;
            try {
                eps = em.getReference(Eps.class, id);
                eps.getIdEps();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The eps with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Paciente> pacienteListOrphanCheck = eps.getPacienteList();
            for (Paciente pacienteListOrphanCheckPaciente : pacienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Eps (" + eps + ") cannot be destroyed since the Paciente " + pacienteListOrphanCheckPaciente + " in its pacienteList field has a non-nullable epsidEps field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(eps);
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

    public List<Eps> findEpsEntities() {
        return findEpsEntities(true, -1, -1);
    }

    public List<Eps> findEpsEntities(int maxResults, int firstResult) {
        return findEpsEntities(false, maxResults, firstResult);
    }

    private List<Eps> findEpsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Eps.class));
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

    public Eps findEps(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Eps.class, id);
        } finally {
            em.close();
        }
    }

    public int getEpsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Eps> rt = cq.from(Eps.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
