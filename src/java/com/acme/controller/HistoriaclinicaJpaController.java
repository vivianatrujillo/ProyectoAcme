/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.acme.entities.Tratamiento;
import com.acme.entities.Diagnostico;
import com.acme.entities.Trabajador;
import com.acme.entities.FormatoDeEvolucion;
import com.acme.entities.Historiaclinica;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class HistoriaclinicaJpaController implements Serializable {

    public HistoriaclinicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historiaclinica historiaclinica) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tratamiento tratamientoidTratamiento = historiaclinica.getTratamientoidTratamiento();
            if (tratamientoidTratamiento != null) {
                tratamientoidTratamiento = em.getReference(tratamientoidTratamiento.getClass(), tratamientoidTratamiento.getIdTratamiento());
                historiaclinica.setTratamientoidTratamiento(tratamientoidTratamiento);
            }
            Diagnostico diagnosticoidDiagnostico = historiaclinica.getDiagnosticoidDiagnostico();
            if (diagnosticoidDiagnostico != null) {
                diagnosticoidDiagnostico = em.getReference(diagnosticoidDiagnostico.getClass(), diagnosticoidDiagnostico.getIdDiagnostico());
                historiaclinica.setDiagnosticoidDiagnostico(diagnosticoidDiagnostico);
            }
            Trabajador trabajadoridTrabajador = historiaclinica.getTrabajadoridTrabajador();
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador = em.getReference(trabajadoridTrabajador.getClass(), trabajadoridTrabajador.getIdTrabajador());
                historiaclinica.setTrabajadoridTrabajador(trabajadoridTrabajador);
            }
            FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncol = historiaclinica.getFormatodeevolucionidFormatodeevolucioncol();
            if (formatodeevolucionidFormatodeevolucioncol != null) {
                formatodeevolucionidFormatodeevolucioncol = em.getReference(formatodeevolucionidFormatodeevolucioncol.getClass(), formatodeevolucionidFormatodeevolucioncol.getIdFormatodeevolucioncol());
                historiaclinica.setFormatodeevolucionidFormatodeevolucioncol(formatodeevolucionidFormatodeevolucioncol);
            }
            em.persist(historiaclinica);
            if (tratamientoidTratamiento != null) {
                tratamientoidTratamiento.getHistoriaclinicaList().add(historiaclinica);
                tratamientoidTratamiento = em.merge(tratamientoidTratamiento);
            }
            if (diagnosticoidDiagnostico != null) {
                diagnosticoidDiagnostico.getHistoriaclinicaList().add(historiaclinica);
                diagnosticoidDiagnostico = em.merge(diagnosticoidDiagnostico);
            }
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador.getHistoriaclinicaList().add(historiaclinica);
                trabajadoridTrabajador = em.merge(trabajadoridTrabajador);
            }
            if (formatodeevolucionidFormatodeevolucioncol != null) {
                formatodeevolucionidFormatodeevolucioncol.getHistoriaclinicaList().add(historiaclinica);
                formatodeevolucionidFormatodeevolucioncol = em.merge(formatodeevolucionidFormatodeevolucioncol);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHistoriaclinica(historiaclinica.getIdHistoriaClinica()) != null) {
                throw new PreexistingEntityException("Historiaclinica " + historiaclinica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historiaclinica historiaclinica) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Historiaclinica persistentHistoriaclinica = em.find(Historiaclinica.class, historiaclinica.getIdHistoriaClinica());
            Tratamiento tratamientoidTratamientoOld = persistentHistoriaclinica.getTratamientoidTratamiento();
            Tratamiento tratamientoidTratamientoNew = historiaclinica.getTratamientoidTratamiento();
            Diagnostico diagnosticoidDiagnosticoOld = persistentHistoriaclinica.getDiagnosticoidDiagnostico();
            Diagnostico diagnosticoidDiagnosticoNew = historiaclinica.getDiagnosticoidDiagnostico();
            Trabajador trabajadoridTrabajadorOld = persistentHistoriaclinica.getTrabajadoridTrabajador();
            Trabajador trabajadoridTrabajadorNew = historiaclinica.getTrabajadoridTrabajador();
            FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncolOld = persistentHistoriaclinica.getFormatodeevolucionidFormatodeevolucioncol();
            FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncolNew = historiaclinica.getFormatodeevolucionidFormatodeevolucioncol();
            if (tratamientoidTratamientoNew != null) {
                tratamientoidTratamientoNew = em.getReference(tratamientoidTratamientoNew.getClass(), tratamientoidTratamientoNew.getIdTratamiento());
                historiaclinica.setTratamientoidTratamiento(tratamientoidTratamientoNew);
            }
            if (diagnosticoidDiagnosticoNew != null) {
                diagnosticoidDiagnosticoNew = em.getReference(diagnosticoidDiagnosticoNew.getClass(), diagnosticoidDiagnosticoNew.getIdDiagnostico());
                historiaclinica.setDiagnosticoidDiagnostico(diagnosticoidDiagnosticoNew);
            }
            if (trabajadoridTrabajadorNew != null) {
                trabajadoridTrabajadorNew = em.getReference(trabajadoridTrabajadorNew.getClass(), trabajadoridTrabajadorNew.getIdTrabajador());
                historiaclinica.setTrabajadoridTrabajador(trabajadoridTrabajadorNew);
            }
            if (formatodeevolucionidFormatodeevolucioncolNew != null) {
                formatodeevolucionidFormatodeevolucioncolNew = em.getReference(formatodeevolucionidFormatodeevolucioncolNew.getClass(), formatodeevolucionidFormatodeevolucioncolNew.getIdFormatodeevolucioncol());
                historiaclinica.setFormatodeevolucionidFormatodeevolucioncol(formatodeevolucionidFormatodeevolucioncolNew);
            }
            historiaclinica = em.merge(historiaclinica);
            if (tratamientoidTratamientoOld != null && !tratamientoidTratamientoOld.equals(tratamientoidTratamientoNew)) {
                tratamientoidTratamientoOld.getHistoriaclinicaList().remove(historiaclinica);
                tratamientoidTratamientoOld = em.merge(tratamientoidTratamientoOld);
            }
            if (tratamientoidTratamientoNew != null && !tratamientoidTratamientoNew.equals(tratamientoidTratamientoOld)) {
                tratamientoidTratamientoNew.getHistoriaclinicaList().add(historiaclinica);
                tratamientoidTratamientoNew = em.merge(tratamientoidTratamientoNew);
            }
            if (diagnosticoidDiagnosticoOld != null && !diagnosticoidDiagnosticoOld.equals(diagnosticoidDiagnosticoNew)) {
                diagnosticoidDiagnosticoOld.getHistoriaclinicaList().remove(historiaclinica);
                diagnosticoidDiagnosticoOld = em.merge(diagnosticoidDiagnosticoOld);
            }
            if (diagnosticoidDiagnosticoNew != null && !diagnosticoidDiagnosticoNew.equals(diagnosticoidDiagnosticoOld)) {
                diagnosticoidDiagnosticoNew.getHistoriaclinicaList().add(historiaclinica);
                diagnosticoidDiagnosticoNew = em.merge(diagnosticoidDiagnosticoNew);
            }
            if (trabajadoridTrabajadorOld != null && !trabajadoridTrabajadorOld.equals(trabajadoridTrabajadorNew)) {
                trabajadoridTrabajadorOld.getHistoriaclinicaList().remove(historiaclinica);
                trabajadoridTrabajadorOld = em.merge(trabajadoridTrabajadorOld);
            }
            if (trabajadoridTrabajadorNew != null && !trabajadoridTrabajadorNew.equals(trabajadoridTrabajadorOld)) {
                trabajadoridTrabajadorNew.getHistoriaclinicaList().add(historiaclinica);
                trabajadoridTrabajadorNew = em.merge(trabajadoridTrabajadorNew);
            }
            if (formatodeevolucionidFormatodeevolucioncolOld != null && !formatodeevolucionidFormatodeevolucioncolOld.equals(formatodeevolucionidFormatodeevolucioncolNew)) {
                formatodeevolucionidFormatodeevolucioncolOld.getHistoriaclinicaList().remove(historiaclinica);
                formatodeevolucionidFormatodeevolucioncolOld = em.merge(formatodeevolucionidFormatodeevolucioncolOld);
            }
            if (formatodeevolucionidFormatodeevolucioncolNew != null && !formatodeevolucionidFormatodeevolucioncolNew.equals(formatodeevolucionidFormatodeevolucioncolOld)) {
                formatodeevolucionidFormatodeevolucioncolNew.getHistoriaclinicaList().add(historiaclinica);
                formatodeevolucionidFormatodeevolucioncolNew = em.merge(formatodeevolucionidFormatodeevolucioncolNew);
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
                Integer id = historiaclinica.getIdHistoriaClinica();
                if (findHistoriaclinica(id) == null) {
                    throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Historiaclinica historiaclinica;
            try {
                historiaclinica = em.getReference(Historiaclinica.class, id);
                historiaclinica.getIdHistoriaClinica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.", enfe);
            }
            Tratamiento tratamientoidTratamiento = historiaclinica.getTratamientoidTratamiento();
            if (tratamientoidTratamiento != null) {
                tratamientoidTratamiento.getHistoriaclinicaList().remove(historiaclinica);
                tratamientoidTratamiento = em.merge(tratamientoidTratamiento);
            }
            Diagnostico diagnosticoidDiagnostico = historiaclinica.getDiagnosticoidDiagnostico();
            if (diagnosticoidDiagnostico != null) {
                diagnosticoidDiagnostico.getHistoriaclinicaList().remove(historiaclinica);
                diagnosticoidDiagnostico = em.merge(diagnosticoidDiagnostico);
            }
            Trabajador trabajadoridTrabajador = historiaclinica.getTrabajadoridTrabajador();
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador.getHistoriaclinicaList().remove(historiaclinica);
                trabajadoridTrabajador = em.merge(trabajadoridTrabajador);
            }
            FormatoDeEvolucion formatodeevolucionidFormatodeevolucioncol = historiaclinica.getFormatodeevolucionidFormatodeevolucioncol();
            if (formatodeevolucionidFormatodeevolucioncol != null) {
                formatodeevolucionidFormatodeevolucioncol.getHistoriaclinicaList().remove(historiaclinica);
                formatodeevolucionidFormatodeevolucioncol = em.merge(formatodeevolucionidFormatodeevolucioncol);
            }
            em.remove(historiaclinica);
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

    public List<Historiaclinica> findHistoriaclinicaEntities() {
        return findHistoriaclinicaEntities(true, -1, -1);
    }

    public List<Historiaclinica> findHistoriaclinicaEntities(int maxResults, int firstResult) {
        return findHistoriaclinicaEntities(false, maxResults, firstResult);
    }

    private List<Historiaclinica> findHistoriaclinicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historiaclinica.class));
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

    public Historiaclinica findHistoriaclinica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historiaclinica.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriaclinicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historiaclinica> rt = cq.from(Historiaclinica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
