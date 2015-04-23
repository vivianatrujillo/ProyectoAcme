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
import com.Acme.entities.Valoracion;
import java.util.ArrayList;
import java.util.List;
import com.Acme.entities.Historiaclinica;
import com.Acme.entities.Tratamiento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class TratamientoJpaController implements Serializable {

    public TratamientoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tratamiento tratamiento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tratamiento.getValoracionList() == null) {
            tratamiento.setValoracionList(new ArrayList<Valoracion>());
        }
        if (tratamiento.getHistoriaclinicaList() == null) {
            tratamiento.setHistoriaclinicaList(new ArrayList<Historiaclinica>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Valoracion> attachedValoracionList = new ArrayList<Valoracion>();
            for (Valoracion valoracionListValoracionToAttach : tratamiento.getValoracionList()) {
                valoracionListValoracionToAttach = em.getReference(valoracionListValoracionToAttach.getClass(), valoracionListValoracionToAttach.getIdValoracion());
                attachedValoracionList.add(valoracionListValoracionToAttach);
            }
            tratamiento.setValoracionList(attachedValoracionList);
            List<Historiaclinica> attachedHistoriaclinicaList = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListHistoriaclinicaToAttach : tratamiento.getHistoriaclinicaList()) {
                historiaclinicaListHistoriaclinicaToAttach = em.getReference(historiaclinicaListHistoriaclinicaToAttach.getClass(), historiaclinicaListHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaList.add(historiaclinicaListHistoriaclinicaToAttach);
            }
            tratamiento.setHistoriaclinicaList(attachedHistoriaclinicaList);
            em.persist(tratamiento);
            for (Valoracion valoracionListValoracion : tratamiento.getValoracionList()) {
                valoracionListValoracion.getTratamientoList().add(tratamiento);
                valoracionListValoracion = em.merge(valoracionListValoracion);
            }
            for (Historiaclinica historiaclinicaListHistoriaclinica : tratamiento.getHistoriaclinicaList()) {
                Tratamiento oldTratamientoidTratamientoOfHistoriaclinicaListHistoriaclinica = historiaclinicaListHistoriaclinica.getTratamientoidTratamiento();
                historiaclinicaListHistoriaclinica.setTratamientoidTratamiento(tratamiento);
                historiaclinicaListHistoriaclinica = em.merge(historiaclinicaListHistoriaclinica);
                if (oldTratamientoidTratamientoOfHistoriaclinicaListHistoriaclinica != null) {
                    oldTratamientoidTratamientoOfHistoriaclinicaListHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListHistoriaclinica);
                    oldTratamientoidTratamientoOfHistoriaclinicaListHistoriaclinica = em.merge(oldTratamientoidTratamientoOfHistoriaclinicaListHistoriaclinica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTratamiento(tratamiento.getIdTratamiento()) != null) {
                throw new PreexistingEntityException("Tratamiento " + tratamiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tratamiento tratamiento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tratamiento persistentTratamiento = em.find(Tratamiento.class, tratamiento.getIdTratamiento());
            List<Valoracion> valoracionListOld = persistentTratamiento.getValoracionList();
            List<Valoracion> valoracionListNew = tratamiento.getValoracionList();
            List<Historiaclinica> historiaclinicaListOld = persistentTratamiento.getHistoriaclinicaList();
            List<Historiaclinica> historiaclinicaListNew = tratamiento.getHistoriaclinicaList();
            List<String> illegalOrphanMessages = null;
            for (Historiaclinica historiaclinicaListOldHistoriaclinica : historiaclinicaListOld) {
                if (!historiaclinicaListNew.contains(historiaclinicaListOldHistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historiaclinica " + historiaclinicaListOldHistoriaclinica + " since its tratamientoidTratamiento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Valoracion> attachedValoracionListNew = new ArrayList<Valoracion>();
            for (Valoracion valoracionListNewValoracionToAttach : valoracionListNew) {
                valoracionListNewValoracionToAttach = em.getReference(valoracionListNewValoracionToAttach.getClass(), valoracionListNewValoracionToAttach.getIdValoracion());
                attachedValoracionListNew.add(valoracionListNewValoracionToAttach);
            }
            valoracionListNew = attachedValoracionListNew;
            tratamiento.setValoracionList(valoracionListNew);
            List<Historiaclinica> attachedHistoriaclinicaListNew = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListNewHistoriaclinicaToAttach : historiaclinicaListNew) {
                historiaclinicaListNewHistoriaclinicaToAttach = em.getReference(historiaclinicaListNewHistoriaclinicaToAttach.getClass(), historiaclinicaListNewHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaListNew.add(historiaclinicaListNewHistoriaclinicaToAttach);
            }
            historiaclinicaListNew = attachedHistoriaclinicaListNew;
            tratamiento.setHistoriaclinicaList(historiaclinicaListNew);
            tratamiento = em.merge(tratamiento);
            for (Valoracion valoracionListOldValoracion : valoracionListOld) {
                if (!valoracionListNew.contains(valoracionListOldValoracion)) {
                    valoracionListOldValoracion.getTratamientoList().remove(tratamiento);
                    valoracionListOldValoracion = em.merge(valoracionListOldValoracion);
                }
            }
            for (Valoracion valoracionListNewValoracion : valoracionListNew) {
                if (!valoracionListOld.contains(valoracionListNewValoracion)) {
                    valoracionListNewValoracion.getTratamientoList().add(tratamiento);
                    valoracionListNewValoracion = em.merge(valoracionListNewValoracion);
                }
            }
            for (Historiaclinica historiaclinicaListNewHistoriaclinica : historiaclinicaListNew) {
                if (!historiaclinicaListOld.contains(historiaclinicaListNewHistoriaclinica)) {
                    Tratamiento oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica = historiaclinicaListNewHistoriaclinica.getTratamientoidTratamiento();
                    historiaclinicaListNewHistoriaclinica.setTratamientoidTratamiento(tratamiento);
                    historiaclinicaListNewHistoriaclinica = em.merge(historiaclinicaListNewHistoriaclinica);
                    if (oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica != null && !oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica.equals(tratamiento)) {
                        oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListNewHistoriaclinica);
                        oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica = em.merge(oldTratamientoidTratamientoOfHistoriaclinicaListNewHistoriaclinica);
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
                Integer id = tratamiento.getIdTratamiento();
                if (findTratamiento(id) == null) {
                    throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.");
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
            Tratamiento tratamiento;
            try {
                tratamiento = em.getReference(Tratamiento.class, id);
                tratamiento.getIdTratamiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Historiaclinica> historiaclinicaListOrphanCheck = tratamiento.getHistoriaclinicaList();
            for (Historiaclinica historiaclinicaListOrphanCheckHistoriaclinica : historiaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tratamiento (" + tratamiento + ") cannot be destroyed since the Historiaclinica " + historiaclinicaListOrphanCheckHistoriaclinica + " in its historiaclinicaList field has a non-nullable tratamientoidTratamiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Valoracion> valoracionList = tratamiento.getValoracionList();
            for (Valoracion valoracionListValoracion : valoracionList) {
                valoracionListValoracion.getTratamientoList().remove(tratamiento);
                valoracionListValoracion = em.merge(valoracionListValoracion);
            }
            em.remove(tratamiento);
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

    public List<Tratamiento> findTratamientoEntities() {
        return findTratamientoEntities(true, -1, -1);
    }

    public List<Tratamiento> findTratamientoEntities(int maxResults, int firstResult) {
        return findTratamientoEntities(false, maxResults, firstResult);
    }

    private List<Tratamiento> findTratamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tratamiento.class));
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

    public Tratamiento findTratamiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tratamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tratamiento> rt = cq.from(Tratamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
