/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.uc.dei.aor.projeto6.grupod.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import pt.uc.dei.aor.projeto6.grupod.entities.Lyric;
import pt.uc.dei.aor.projeto6.grupod.entities.Music;
import pt.uc.dei.aor.projeto6.grupod.entities.UserPlay;
import pt.uc.dei.aor.projeto6.grupod.exceptions.CreateLyricException;

/**
 *
 * @author User
 */
@Stateless
public class LyricFacade extends AbstractFacade<Lyric> {

    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LyricFacade() {
        super(Lyric.class);
    }

    /**
     * method to find
     *
     * @param m
     * @param u
     * @return the single result of the query and null if there is no result
     */
    public Lyric findLyricBySongIdAndUserId(Music m, UserPlay u) {
        try {
            return (Lyric) em.createNamedQuery("Lyric.findLyricByUserAndMusic").setParameter("music", m)
                    .setParameter("user", u).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public void createLyric(Lyric l, Music m, UserPlay u) throws CreateLyricException {
        try {
            em.persist(l);
            l.setMusic(m);
            l.setUser(u);
            m.getLyricsList();
            u.getLyricsList();
            em.merge(m);
            em.merge(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CreateLyricException();

        }

    }

}
