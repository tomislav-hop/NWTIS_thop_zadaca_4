/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.foi.nwtis.thop.ejb.eb.Adrese;

/**
 *
 * @author NWTiS_1
 */
@Stateless
public class AdreseFacade extends AbstractFacade<Adrese> {
    @PersistenceContext(unitName = "thop_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdreseFacade() {
        super(Adrese.class);
    }
    
    public List<Adrese> findByAdresa(String zaAdresu) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Adrese> adresa = cq.from(Adrese.class);
        Expression<String> premaAdresi = adresa.get("adresa");
        cq.where(cb.and(cb.equal(premaAdresi, zaAdresu)));
        return getEntityManager().createQuery(cq).getResultList();    
    }
}
