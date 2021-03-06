/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Acme.ssesion;

import com.Acme.entities.Historiaclinica;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yiyi
 */
@Stateless
public class HistoriaclinicaFacade extends AbstractFacade<Historiaclinica> {
    @PersistenceContext(unitName = "AcmePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoriaclinicaFacade() {
        super(Historiaclinica.class);
    }
    
}
