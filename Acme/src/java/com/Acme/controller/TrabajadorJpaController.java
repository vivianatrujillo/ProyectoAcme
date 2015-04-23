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
import com.Acme.entities.Paciente;
import java.util.ArrayList;
import java.util.List;
import com.Acme.entities.Rol;
import com.Acme.entities.Historiaclinica;
import com.Acme.entities.Trabajador;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Yiyi
 */
public class TrabajadorJpaController implements Serializable {

    public TrabajadorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trabajador trabajador) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (trabajador.getPacienteList() == null) {
            trabajador.setPacienteList(new ArrayList<Paciente>());
        }
        if (trabajador.getRolList() == null) {
            trabajador.setRolList(new ArrayList<Rol>());
        }
        if (trabajador.getHistoriaclinicaList() == null) {
            trabajador.setHistoriaclinicaList(new ArrayList<Historiaclinica>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Paciente> attachedPacienteList = new ArrayList<Paciente>();
            for (Paciente pacienteListPacienteToAttach : trabajador.getPacienteList()) {
                pacienteListPacienteToAttach = em.getReference(pacienteListPacienteToAttach.getClass(), pacienteListPacienteToAttach.getIdPaciente());
                attachedPacienteList.add(pacienteListPacienteToAttach);
            }
            trabajador.setPacienteList(attachedPacienteList);
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : trabajador.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getIdRol());
                attachedRolList.add(rolListRolToAttach);
            }
            trabajador.setRolList(attachedRolList);
            List<Historiaclinica> attachedHistoriaclinicaList = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListHistoriaclinicaToAttach : trabajador.getHistoriaclinicaList()) {
                historiaclinicaListHistoriaclinicaToAttach = em.getReference(historiaclinicaListHistoriaclinicaToAttach.getClass(), historiaclinicaListHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaList.add(historiaclinicaListHistoriaclinicaToAttach);
            }
            trabajador.setHistoriaclinicaList(attachedHistoriaclinicaList);
            em.persist(trabajador);
            for (Paciente pacienteListPaciente : trabajador.getPacienteList()) {
                pacienteListPaciente.getTrabajadorList().add(trabajador);
                pacienteListPaciente = em.merge(pacienteListPaciente);
            }
            for (Rol rolListRol : trabajador.getRolList()) {
                Trabajador oldTrabajadoridTrabajadorOfRolListRol = rolListRol.getTrabajadoridTrabajador();
                rolListRol.setTrabajadoridTrabajador(trabajador);
                rolListRol = em.merge(rolListRol);
                if (oldTrabajadoridTrabajadorOfRolListRol != null) {
                    oldTrabajadoridTrabajadorOfRolListRol.getRolList().remove(rolListRol);
                    oldTrabajadoridTrabajadorOfRolListRol = em.merge(oldTrabajadoridTrabajadorOfRolListRol);
                }
            }
            for (Historiaclinica historiaclinicaListHistoriaclinica : trabajador.getHistoriaclinicaList()) {
                Trabajador oldTrabajadoridTrabajadorOfHistoriaclinicaListHistoriaclinica = historiaclinicaListHistoriaclinica.getTrabajadoridTrabajador();
                historiaclinicaListHistoriaclinica.setTrabajadoridTrabajador(trabajador);
                historiaclinicaListHistoriaclinica = em.merge(historiaclinicaListHistoriaclinica);
                if (oldTrabajadoridTrabajadorOfHistoriaclinicaListHistoriaclinica != null) {
                    oldTrabajadoridTrabajadorOfHistoriaclinicaListHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListHistoriaclinica);
                    oldTrabajadoridTrabajadorOfHistoriaclinicaListHistoriaclinica = em.merge(oldTrabajadoridTrabajadorOfHistoriaclinicaListHistoriaclinica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTrabajador(trabajador.getIdTrabajador()) != null) {
                throw new PreexistingEntityException("Trabajador " + trabajador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trabajador trabajador) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trabajador persistentTrabajador = em.find(Trabajador.class, trabajador.getIdTrabajador());
            List<Paciente> pacienteListOld = persistentTrabajador.getPacienteList();
            List<Paciente> pacienteListNew = trabajador.getPacienteList();
            List<Rol> rolListOld = persistentTrabajador.getRolList();
            List<Rol> rolListNew = trabajador.getRolList();
            List<Historiaclinica> historiaclinicaListOld = persistentTrabajador.getHistoriaclinicaList();
            List<Historiaclinica> historiaclinicaListNew = trabajador.getHistoriaclinicaList();
            List<String> illegalOrphanMessages = null;
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rol " + rolListOldRol + " since its trabajadoridTrabajador field is not nullable.");
                }
            }
            for (Historiaclinica historiaclinicaListOldHistoriaclinica : historiaclinicaListOld) {
                if (!historiaclinicaListNew.contains(historiaclinicaListOldHistoriaclinica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historiaclinica " + historiaclinicaListOldHistoriaclinica + " since its trabajadoridTrabajador field is not nullable.");
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
            trabajador.setPacienteList(pacienteListNew);
            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getIdRol());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            trabajador.setRolList(rolListNew);
            List<Historiaclinica> attachedHistoriaclinicaListNew = new ArrayList<Historiaclinica>();
            for (Historiaclinica historiaclinicaListNewHistoriaclinicaToAttach : historiaclinicaListNew) {
                historiaclinicaListNewHistoriaclinicaToAttach = em.getReference(historiaclinicaListNewHistoriaclinicaToAttach.getClass(), historiaclinicaListNewHistoriaclinicaToAttach.getIdHistoriaClinica());
                attachedHistoriaclinicaListNew.add(historiaclinicaListNewHistoriaclinicaToAttach);
            }
            historiaclinicaListNew = attachedHistoriaclinicaListNew;
            trabajador.setHistoriaclinicaList(historiaclinicaListNew);
            trabajador = em.merge(trabajador);
            for (Paciente pacienteListOldPaciente : pacienteListOld) {
                if (!pacienteListNew.contains(pacienteListOldPaciente)) {
                    pacienteListOldPaciente.getTrabajadorList().remove(trabajador);
                    pacienteListOldPaciente = em.merge(pacienteListOldPaciente);
                }
            }
            for (Paciente pacienteListNewPaciente : pacienteListNew) {
                if (!pacienteListOld.contains(pacienteListNewPaciente)) {
                    pacienteListNewPaciente.getTrabajadorList().add(trabajador);
                    pacienteListNewPaciente = em.merge(pacienteListNewPaciente);
                }
            }
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    Trabajador oldTrabajadoridTrabajadorOfRolListNewRol = rolListNewRol.getTrabajadoridTrabajador();
                    rolListNewRol.setTrabajadoridTrabajador(trabajador);
                    rolListNewRol = em.merge(rolListNewRol);
                    if (oldTrabajadoridTrabajadorOfRolListNewRol != null && !oldTrabajadoridTrabajadorOfRolListNewRol.equals(trabajador)) {
                        oldTrabajadoridTrabajadorOfRolListNewRol.getRolList().remove(rolListNewRol);
                        oldTrabajadoridTrabajadorOfRolListNewRol = em.merge(oldTrabajadoridTrabajadorOfRolListNewRol);
                    }
                }
            }
            for (Historiaclinica historiaclinicaListNewHistoriaclinica : historiaclinicaListNew) {
                if (!historiaclinicaListOld.contains(historiaclinicaListNewHistoriaclinica)) {
                    Trabajador oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica = historiaclinicaListNewHistoriaclinica.getTrabajadoridTrabajador();
                    historiaclinicaListNewHistoriaclinica.setTrabajadoridTrabajador(trabajador);
                    historiaclinicaListNewHistoriaclinica = em.merge(historiaclinicaListNewHistoriaclinica);
                    if (oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica != null && !oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica.equals(trabajador)) {
                        oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica.getHistoriaclinicaList().remove(historiaclinicaListNewHistoriaclinica);
                        oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica = em.merge(oldTrabajadoridTrabajadorOfHistoriaclinicaListNewHistoriaclinica);
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
                Integer id = trabajador.getIdTrabajador();
                if (findTrabajador(id) == null) {
                    throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.");
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
            Trabajador trabajador;
            try {
                trabajador = em.getReference(Trabajador.class, id);
                trabajador.getIdTrabajador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rol> rolListOrphanCheck = trabajador.getRolList();
            for (Rol rolListOrphanCheckRol : rolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Rol " + rolListOrphanCheckRol + " in its rolList field has a non-nullable trabajadoridTrabajador field.");
            }
            List<Historiaclinica> historiaclinicaListOrphanCheck = trabajador.getHistoriaclinicaList();
            for (Historiaclinica historiaclinicaListOrphanCheckHistoriaclinica : historiaclinicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Historiaclinica " + historiaclinicaListOrphanCheckHistoriaclinica + " in its historiaclinicaList field has a non-nullable trabajadoridTrabajador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Paciente> pacienteList = trabajador.getPacienteList();
            for (Paciente pacienteListPaciente : pacienteList) {
                pacienteListPaciente.getTrabajadorList().remove(trabajador);
                pacienteListPaciente = em.merge(pacienteListPaciente);
            }
            em.remove(trabajador);
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

    public List<Trabajador> findTrabajadorEntities() {
        return findTrabajadorEntities(true, -1, -1);
    }

    public List<Trabajador> findTrabajadorEntities(int maxResults, int firstResult) {
        return findTrabajadorEntities(false, maxResults, firstResult);
    }

    private List<Trabajador> findTrabajadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trabajador.class));
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

    public Trabajador findTrabajador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trabajador.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrabajadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trabajador> rt = cq.from(Trabajador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
