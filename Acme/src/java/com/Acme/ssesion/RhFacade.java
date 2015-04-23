/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.ssesion;

import com.Acme.entities.Rh;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yiyi
 */
@Stateless
public class RhFacade extends AbstractFacade<Rh> {
    @PersistenceContext(unitName = "AcmePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RhFacade() {
        super(Rh.class);
    }
    
}
