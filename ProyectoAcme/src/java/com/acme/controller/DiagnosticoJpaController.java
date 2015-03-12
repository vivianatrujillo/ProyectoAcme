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
import com.acme.entities.Diagnostico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Historiaclinica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class DiagnosticoJpaController implements Serializable {

    public DiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diagnostico diagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (diagnostico.getHistoriaclinicaList() == null) {
            diagnostico.setHistoriaclinicaList(new ArrayList<Historiaclinica>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Historiaclinica> attachedHistoriaclinicaList = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListHistoriaclinicaToAttach : diagnostico.getHistoriaclinicaList()) {
                historiaclinicaListHistoriaclinicaToAttach = em.getReference(historiaclinicaListHistoriaclinicaToAttach.getClass(), historiaclinicaListHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaList.add(historiaclinicaListHistoriaclinicaToAttach);
            }
            diagnostico.setHistoriaclinicaList(attachedHistoriaclinicaList);
            em.persist(diagnostico);
            for (Historiaclinica historiaclinicaListHistoriaclinica : diagnostico.getHistoriaclinicaList()) {
                Diagnostico oldDiagnosticoidDiagnosticoOfHistoriaclinicaListHistoriaclinica = historiaclinicaListHistoriaclinica.getDiagnosticoidDiagnostico();
                historiaclinicaListHistoriaclinica.setDiagnosticoidDiagnostico(diagnostico);
                historiaclinicaListHistoriaclinica = em.merge(historiaclinicaListHistoriaclinica);
                if (oldDiagnosticoidDiagnosticoOfHistoriaclinicaListHistoriaclinica != null) {
                    oldDiagnosticoidDiagnosticoOfHistoriaclinicaListHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListHistoriaclinica);
                    oldDiagnosticoidDiagnosticoOfHistoriaclinicaListHistoriaclinica = em.merge(oldDiagnosticoidDiagnosticoOfHistoriaclinicaListHistoriaclinica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDiagnostico(diagnostico.getIdDiagnostico()) != null) {
                throw new PreexistingEntityException("Diagnostico " + diagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diagnostico diagnostico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Diagnostico persistentDiagnostico = em.find(Diagnostico.class, diagnostico.getIdDiagnostico());
            List<Historiaclinica> historiaclinicaListOld = persistentDiagnostico.getHistoriaclinicaList();
            List<Historiaclinica> historiaclinicaListNew = diagnostico.getHistoriaclinicaList();
            List<String> illegalOrphanMessages = null;
            for (Historiaclinica historiaclinicaListOldHistoriaclinica : historiaclinicaListOld) {
                if (!historiaclinicaListNew.contains(historiaclinicaListOldHistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historiaclinica " + historiaclinicaListOldHistoriaclinica + " since its diagnosticoidDiagnostico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Historiaclinica> attachedHistoriaclinicaListNew = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListNewHistoriaclinicaToAttach : historiaclinicaListNew) {
                historiaclinicaListNewHistoriaclinicaToAttach = em.getReference(historiaclinicaListNewHistoriaclinicaToAttach.getClass(), historiaclinicaListNewHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaListNew.add(historiaclinicaListNewHistoriaclinicaToAttach);
            }
            historiaclinicaListNew = attachedHistoriaclinicaListNew;
            diagnostico.setHistoriaclinicaList(historiaclinicaListNew);
            diagnostico = em.merge(diagnostico);
            for (Historiaclinica historiaclinicaListNewHistoriaclinica : historiaclinicaListNew) {
                if (!historiaclinicaListOld.contains(historiaclinicaListNewHistoriaclinica)) {
                    Diagnostico oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica = historiaclinicaListNewHistoriaclinica.getDiagnosticoidDiagnostico();
                    historiaclinicaListNewHistoriaclinica.setDiagnosticoidDiagnostico(diagnostico);
                    historiaclinicaListNewHistoriaclinica = em.merge(historiaclinicaListNewHistoriaclinica);
                    if (oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica != null && !oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica.equals(diagnostico)) {
                        oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListNewHistoriaclinica);
                        oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica = em.merge(oldDiagnosticoidDiagnosticoOfHistoriaclinicaListNewHistoriaclinica);
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
                Integer id = diagnostico.getIdDiagnostico();
                if (findDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.");
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
            Diagnostico diagnostico;
            try {
                diagnostico = em.getReference(Diagnostico.class, id);
                diagnostico.getIdDiagnostico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Historiaclinica> historiaclinicaListOrphanCheck = diagnostico.getHistoriaclinicaList();
            for (Historiaclinica historiaclinicaListOrphanCheckHistoriaclinica : historiaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Diagnostico (" + diagnostico + ") cannot be destroyed since the Historiaclinica " + historiaclinicaListOrphanCheckHistoriaclinica + " in its historiaclinicaList field has a non-nullable diagnosticoidDiagnostico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(diagnostico);
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

    public List<Diagnostico> findDiagnosticoEntities() {
        return findDiagnosticoEntities(true, -1, -1);
    }

    public List<Diagnostico> findDiagnosticoEntities(int maxResults, int firstResult) {
        return findDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Diagnostico> findDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diagnostico.class));
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

    public Diagnostico findDiagnostico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diagnostico> rt = cq.from(Diagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
