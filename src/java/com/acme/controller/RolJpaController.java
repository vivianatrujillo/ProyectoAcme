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
import com.acme.entities.Trabajador;
import com.acme.entities.Permisos;
import com.acme.entities.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rol.getPermisosList() == null) {
            rol.setPermisosList(new ArrayList<Permisos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trabajador trabajadoridTrabajador = rol.getTrabajadoridTrabajador();
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador = em.getReference(trabajadoridTrabajador.getClass(), trabajadoridTrabajador.getIdTrabajador());
                rol.setTrabajadoridTrabajador(trabajadoridTrabajador);
            }
            List<Permisos> attachedPermisosList = new ArrayList<Permisos>();
            for (Permisos permisosListPermisosToAttach : rol.getPermisosList()) {
                permisosListPermisosToAttach = em.getReference(permisosListPermisosToAttach.getClass(), permisosListPermisosToAttach.getIdPermisos());
                attachedPermisosList.add(permisosListPermisosToAttach);
            }
            rol.setPermisosList(attachedPermisosList);
            em.persist(rol);
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador.getRolList().add(rol);
                trabajadoridTrabajador = em.merge(trabajadoridTrabajador);
            }
            for (Permisos permisosListPermisos : rol.getPermisosList()) {
                permisosListPermisos.getRolList().add(rol);
                permisosListPermisos = em.merge(permisosListPermisos);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRol(rol.getIdRol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getIdRol());
            Trabajador trabajadoridTrabajadorOld = persistentRol.getTrabajadoridTrabajador();
            Trabajador trabajadoridTrabajadorNew = rol.getTrabajadoridTrabajador();
            List<Permisos> permisosListOld = persistentRol.getPermisosList();
            List<Permisos> permisosListNew = rol.getPermisosList();
            if (trabajadoridTrabajadorNew != null) {
                trabajadoridTrabajadorNew = em.getReference(trabajadoridTrabajadorNew.getClass(), trabajadoridTrabajadorNew.getIdTrabajador());
                rol.setTrabajadoridTrabajador(trabajadoridTrabajadorNew);
            }
            List<Permisos> attachedPermisosListNew = new ArrayList<Permisos>();
            for (Permisos permisosListNewPermisosToAttach : permisosListNew) {
                permisosListNewPermisosToAttach = em.getReference(permisosListNewPermisosToAttach.getClass(), permisosListNewPermisosToAttach.getIdPermisos());
                attachedPermisosListNew.add(permisosListNewPermisosToAttach);
            }
            permisosListNew = attachedPermisosListNew;
            rol.setPermisosList(permisosListNew);
            rol = em.merge(rol);
            if (trabajadoridTrabajadorOld != null && !trabajadoridTrabajadorOld.equals(trabajadoridTrabajadorNew)) {
                trabajadoridTrabajadorOld.getRolList().remove(rol);
                trabajadoridTrabajadorOld = em.merge(trabajadoridTrabajadorOld);
            }
            if (trabajadoridTrabajadorNew != null && !trabajadoridTrabajadorNew.equals(trabajadoridTrabajadorOld)) {
                trabajadoridTrabajadorNew.getRolList().add(rol);
                trabajadoridTrabajadorNew = em.merge(trabajadoridTrabajadorNew);
            }
            for (Permisos permisosListOldPermisos : permisosListOld) {
                if (!permisosListNew.contains(permisosListOldPermisos)) {
                    permisosListOldPermisos.getRolList().remove(rol);
                    permisosListOldPermisos = em.merge(permisosListOldPermisos);
                }
            }
            for (Permisos permisosListNewPermisos : permisosListNew) {
                if (!permisosListOld.contains(permisosListNewPermisos)) {
                    permisosListNewPermisos.getRolList().add(rol);
                    permisosListNewPermisos = em.merge(permisosListNewPermisos);
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
                Integer id = rol.getIdRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            Trabajador trabajadoridTrabajador = rol.getTrabajadoridTrabajador();
            if (trabajadoridTrabajador != null) {
                trabajadoridTrabajador.getRolList().remove(rol);
                trabajadoridTrabajador = em.merge(trabajadoridTrabajador);
            }
            List<Permisos> permisosList = rol.getPermisosList();
            for (Permisos permisosListPermisos : permisosList) {
                permisosListPermisos.getRolList().remove(rol);
                permisosListPermisos = em.merge(permisosListPermisos);
            }
            em.remove(rol);
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

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
