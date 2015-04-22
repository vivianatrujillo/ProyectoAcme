/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.controller;

import com.acme.controller.exceptions.NonexistentEntityException;
import com.acme.controller.exceptions.PreexistingEntityException;
import com.acme.controller.exceptions.RollbackFailureException;
import com.acme.entities.Enfermedad;
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
public class EnfermedadJpaController implements Serializable {

    public EnfermedadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Enfermedad enfermedad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (enfermedad.getPacienteList() == null) {
            enfermedad.setPacienteList(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : enfermedad.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            enfermedad.setPacienteList(attachedPacienteList);
            em.persist(enfermedad);
            for (Paciente pacienteListPaciente : enfermedad.getPacienteList()) {
                pacienteListPaciente.getEnfermedadList().add(enfermedad);
                pacienteListPaciente = em.merge(pacienteListPaciente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEnfermedad(enfermedad.getIdEnfermedad()) != null) {
                throw new PreexistingEntityException("Enfermedad " + enfermedad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Enfermedad enfermedad) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Enfermedad persistentEnfermedad = em.find(Enfermedad.class, enfermedad.getIdEnfermedad());
            List<Paciente> pacienteListOld = persistentEnfermedad.getPacienteList();
            List<Paciente> pacienteListNew = enfermedad.getPacienteList();
            List<Paciente> attachedPacienteListNew = new ArrayList<Paciente>();
            for (Paciente pacienteListNewPacienteToAttach : pacienteListNew) {
                pacienteListNewPacienteToAttach = em.getReference(pacienteListNewPacienteToAttach.getClass(), pacienteListNewPacienteToAttach.getIdPaciente());
                attachedPacienteListNew.add(pacienteListNewPacienteToAttach);
            }
            pacienteListNew = attachedPacienteListNew;
            enfermedad.setPacienteList(pacienteListNew);
            enfermedad = em.merge(enfermedad);
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    pacienteListOldPaciente.getEnfermedadList().remove(enfermedad);
                    pacienteListOldPaciente = em.merge(pacienteListOldPaciente);
                }
            }
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    pacienteListNewPaciente.getEnfermedadList().add(enfermedad);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
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
                Integer id = enfermedad.getIdEnfermedad();
                if (findEnfermedad(id) == null) {
                    throw new NonexistentEntityException("The enfermedad with id " + id + " no longer exists.");
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
            Enfermedad enfermedad;
            try {
                enfermedad = em.getReference(Enfermedad.class, id);
                enfermedad.getIdEnfermedad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enfermedad with id " + id + " no longer exists.", enfe);
            }
            List<Paciente> pacienteList = enfermedad.getPacienteList();
            for (Paciente pacienteListPaciente : pacienteList) {
                pacienteListPaciente.getEnfermedadList().remove(enfermedad);
                pacienteListPaciente = em.merge(pacienteListPaciente);
            }
            em.remove(enfermedad);
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

    public List<Enfermedad> findEnfermedadEntities() {
        return findEnfermedadEntities(true, -1, -1);
    }

    public List<Enfermedad> findEnfermedadEntities(int maxResults, int firstResult) {
        return findEnfermedadEntities(false, maxResults, firstResult);
    }

    private List<Enfermedad> findEnfermedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Enfermedad.class));
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

    public Enfermedad findEnfermedad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Enfermedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnfermedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Enfermedad> rt = cq.from(Enfermedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
