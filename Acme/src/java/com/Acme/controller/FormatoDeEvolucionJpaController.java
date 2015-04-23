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
import com.Acme.entities.FormatoDeEvolucion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Acme.entities.Historiaclinica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class FormatoDeEvolucionJpaController implements Serializable {

    public FormatoDeEvolucionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FormatoDeEvolucion formatoDeEvolucion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (formatoDeEvolucion.getHistoriaclinicaList() == null) {
            formatoDeEvolucion.setHistoriaclinicaList(new ArrayList<Historiaclinica>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Historiaclinica> attachedHistoriaclinicaList = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListHistoriaclinicaToAttach : formatoDeEvolucion.getHistoriaclinicaList()) {
                historiaclinicaListHistoriaclinicaToAttach = em.getReference(historiaclinicaListHistoriaclinicaToAttach.getClass(), historiaclinicaListHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaList.add(historiaclinicaListHistoriaclinicaToAttach);
            }
            formatoDeEvolucion.setHistoriaclinicaList(attachedHistoriaclinicaList);
            em.persist(formatoDeEvolucion);
            for (Historiaclinica historiaclinicaListHistoriaclinica : formatoDeEvolucion.getHistoriaclinicaList()) {
                FormatoDeEvolucion oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListHistoriaclinica = historiaclinicaListHistoriaclinica.getFormatodeevolucionidFormatodeevolucioncol();
                historiaclinicaListHistoriaclinica.setFormatodeevolucionidFormatodeevolucioncol(formatoDeEvolucion);
                historiaclinicaListHistoriaclinica = em.merge(historiaclinicaListHistoriaclinica);
                if (oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListHistoriaclinica != null) {
                    oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListHistoriaclinica);
                    oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListHistoriaclinica = em.merge(oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListHistoriaclinica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFormatoDeEvolucion(formatoDeEvolucion.getIdFormatodeevolucioncol()) != null) {
                throw new PreexistingEntityException("FormatoDeEvolucion " + formatoDeEvolucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FormatoDeEvolucion formatoDeEvolucion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FormatoDeEvolucion persistentFormatoDeEvolucion = em.find(FormatoDeEvolucion.class, formatoDeEvolucion.getIdFormatodeevolucioncol());
            List<Historiaclinica> historiaclinicaListOld = persistentFormatoDeEvolucion.getHistoriaclinicaList();
            List<Historiaclinica> historiaclinicaListNew = formatoDeEvolucion.getHistoriaclinicaList();
            List<String> illegalOrphanMessages = null;
            for (Historiaclinica historiaclinicaListOldHistoriaclinica : historiaclinicaListOld) {
                if (!historiaclinicaListNew.contains(historiaclinicaListOldHistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historiaclinica " + historiaclinicaListOldHistoriaclinica + " since its formatodeevolucionidFormatodeevolucioncol field is not nullable.");
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
            formatoDeEvolucion.setHistoriaclinicaList(historiaclinicaListNew);
            formatoDeEvolucion = em.merge(formatoDeEvolucion);
            for (Historiaclinica historiaclinicaListNewHistoriaclinica : historiaclinicaListNew) {
                if (!historiaclinicaListOld.contains(historiaclinicaListNewHistoriaclinica)) {
                    FormatoDeEvolucion oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica = historiaclinicaListNewHistoriaclinica.getFormatodeevolucionidFormatodeevolucioncol();
                    historiaclinicaListNewHistoriaclinica.setFormatodeevolucionidFormatodeevolucioncol(formatoDeEvolucion);
                    historiaclinicaListNewHistoriaclinica = em.merge(historiaclinicaListNewHistoriaclinica);
                    if (oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica != null && !oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica.equals(formatoDeEvolucion)) {
                        oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListNewHistoriaclinica);
                        oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica = em.merge(oldFormatodeevolucionidFormatodeevolucioncolOfHistoriaclinicaListNewHistoriaclinica);
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
                Integer id = formatoDeEvolucion.getIdFormatodeevolucioncol();
                if (findFormatoDeEvolucion(id) == null) {
                    throw new NonexistentEntityException("The formatoDeEvolucion with id " + id + " no longer exists.");
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
            FormatoDeEvolucion formatoDeEvolucion;
            try {
                formatoDeEvolucion = em.getReference(FormatoDeEvolucion.class, id);
                formatoDeEvolucion.getIdFormatodeevolucioncol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formatoDeEvolucion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Historiaclinica> historiaclinicaListOrphanCheck = formatoDeEvolucion.getHistoriaclinicaList();
            for (Historiaclinica historiaclinicaListOrphanCheckHistoriaclinica : historiaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FormatoDeEvolucion (" + formatoDeEvolucion + ") cannot be destroyed since the Historiaclinica " + historiaclinicaListOrphanCheckHistoriaclinica + " in its historiaclinicaList field has a non-nullable formatodeevolucionidFormatodeevolucioncol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(formatoDeEvolucion);
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

    public List<FormatoDeEvolucion> findFormatoDeEvolucionEntities() {
        return findFormatoDeEvolucionEntities(true, -1, -1);
    }

    public List<FormatoDeEvolucion> findFormatoDeEvolucionEntities(int maxResults, int firstResult) {
        return findFormatoDeEvolucionEntities(false, maxResults, firstResult);
    }

    private List<FormatoDeEvolucion> findFormatoDeEvolucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FormatoDeEvolucion.class));
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

    public FormatoDeEvolucion findFormatoDeEvolucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FormatoDeEvolucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormatoDeEvolucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FormatoDeEvolucion> rt = cq.from(FormatoDeEvolucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
