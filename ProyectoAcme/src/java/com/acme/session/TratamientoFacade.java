/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.session;

import com.acme.entities.Tratamiento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author usuario
 */
@Stateless
public class TratamientoFacade extends AbstractFacade<Tratamiento> {
    @PersistenceContext(unitName = "ProyectoAcmePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TratamientoFacade() {
        super(Tratamiento.class);
    }
    
}
