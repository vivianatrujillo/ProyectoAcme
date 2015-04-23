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
import com.Acme.entities.Tratamiento;
import java.util.ArrayList;
import java.util.List;
import com.Acme.entities.Paciente;
import com.Acme.entities.Valoracion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class ValoracionJpaController implements Serializable {

    public ValoracionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valoracion valoracion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (valoracion.getTratamientoList() == null) {
            valoracion.setTratamientoList(new ArrayList<Tratamiento>());
        }
        if (valoracion.getPacienteList() == null) {
            valoracion.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Tratamiento> attachedTratamientoList = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListTratamientoToAttach : valoracion.getTratamientoList()) {
                tratamientoListTratamientoToAttach = em.getReference(tratamientoListTratamientoToAttach.getClass(), tratamientoListTratamientoToAttach.getIdTratamiento());
                attachedTratamientoList.add(tratamientoListTratamientoToAttach);
            }
            valoracion.setTratamientoList(attachedTratamientoList);
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : valoracion.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            valoracion.setPacienteList(attachedPacienteList);
            em.persist(valoracion);
            for (Tratamiento tratamientoListTratamiento : valoracion.getTratamientoList()) {
                tratamientoListTratamiento.getValoracionList().add(valoracion);
                tratamientoListTratamiento = em.merge(tratamientoListTratamiento);
            }
            for (Paciente pacienteListPaciente : valoracion.getPacienteList()) {
                Valoracion oldValoracionidValoracionOfPacienteListPaciente = pacienteListPaciente.getValoracionidValoracion();
                pacienteListPaciente.setValoracionidValoracion(valoracion);
                pacienteListPaciente = em.merge(pacienteListPaciente);
                if (oldValoracionidValoracionOfPacienteListPaciente != null) {
                    oldValoracionidValoracionOfPacienteListPaciente.getPacienteList().remove(pacienteListPaciente);
                    oldValoracionidValoracionOfPacienteListPaciente = em.merge(oldValoracionidValoracionOfPacienteListPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findValoracion(valoracion.getIdValoracion()) != null) {
                throw new PreexistingEntityException("Valoracion " + valoracion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valoracion valoracion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Valoracion persistentValoracion = em.find(Valoracion.class, valoracion.getIdValoracion());
            List<Tratamiento> tratamientoListOld = persistentValoracion.getTratamientoList();
            List<Tratamiento> tratamientoListNew = valoracion.getTratamientoList();
            List<Paciente> pacienteListOld = persistentValoracion.getPacienteList();
            List<Paciente> pacienteListNew = valoracion.getPacienteList();
            List<String> illegalOrphanMessages = null;
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteListOldPaciente + " since its valoracionidValoracion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tratamiento> attachedTratamientoListNew = new ArrayList<Tratamiento>();
            for (Tratamiento tratamientoListNewTratamientoToAttach : tratamientoListNew) {
                tratamientoListNewTratamientoToAttach = em.getReference(tratamientoListNewTratamientoToAttach.getClass(), tratamientoListNewTratamientoToAttach.getIdTratamiento());
                attachedTratamientoListNew.add(tratamientoListNewTratamientoToAttach);
            }
            tratamientoListNew = attachedTratamientoListNew;
            valoracion.setTratamientoList(tratamientoListNew);
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            valoracion.setPacienteList(pacienteListNew);
            valoracion = em.merge(valoracion);
            for (Tratamiento tratamientoListOldTratamiento : tratamientoListOld) {
                if (!tratamientoListNew.contains(tratamientoListOldTratamiento)) {
                    tratamientoListOldTratamiento.getValoracionList().remove(valoracion);
                    tratamientoListOldTratamiento = em.merge(tratamientoListOldTratamiento);
                }
            }
            for (Tratamiento tratamientoListNewTratamiento : tratamientoListNew) {
                if (!tratamientoListOld.contains(tratamientoListNewTratamiento)) {
                    tratamientoListNewTratamiento.getValoracionList().add(valoracion);
                    tratamientoListNewTratamiento = em.merge(tratamientoListNewTratamiento);
                }
            }
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    Valoracion oldValoracionidValoracionOfPacienteListNewPaciente = pacienteListNewPaciente.getValoracionidValoracion();
                    pacienteListNewPaciente.setValoracionidValoracion(valoracion);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                    if (oldValoracionidValoracionOfPacienteListNewPaciente != null && !oldValoracionidValoracionOfPacienteListNewPaciente.equals(valoracion)) {
                        oldValoracionidValoracionOfPacienteListNewPaciente.getPacienteList().remove(pacienteListNewPaciente);
                        oldValoracionidValoracionOfPacienteListNewPaciente = em.merge(oldValoracionidValoracionOfPacienteListNewPaciente);
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
                Integer id = valoracion.getIdValoracion();
                if (findValoracion(id) == null) {
                    throw new NonexistentEntityException("The valoracion with id " + id + " no longer exists.");
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
            Valoracion valoracion;
            try {
                valoracion = em.getReference(Valoracion.class, id);
                valoracion.getIdValoracion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoracion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Paciente> pacienteListOrphanCheck = valoracion.getPacienteList();
            for (Paciente pacienteListOrphanCheckPaciente : pacienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Valoracion (" + valoracion + ") cannot be destroyed since the Paciente " + pacienteListOrphanCheckPaciente + " in its pacienteList field has a non-nullable valoracionidValoracion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tratamiento> tratamientoList = valoracion.getTratamientoList();
            for (Tratamiento tratamientoListTratamiento : tratamientoList) {
                tratamientoListTratamiento.getValoracionList().remove(valoracion);
                tratamientoListTratamiento = em.merge(tratamientoListTratamiento);
            }
            em.remove(valoracion);
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

    public List<Valoracion> findValoracionEntities() {
        return findValoracionEntities(true, -1, -1);
    }

    public List<Valoracion> findValoracionEntities(int maxResults, int firstResult) {
        return findValoracionEntities(false, maxResults, firstResult);
    }

    private List<Valoracion> findValoracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valoracion.class));
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

    public Valoracion findValoracion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valoracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valoracion> rt = cq.from(Valoracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
