/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.uc.dei.aor.projeto6.grupod.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "LYRIC")
@NamedQueries({
    @NamedQuery(name = "Lyric.findLyricByUserAndMusic", query = "SELECT l FROM Lyric l WHERE l.music "
            + "= :music AND l.user = :user")})
public class Lyric implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lyricID;

    @ManyToOne
    private Music music;

    @ManyToOne
    private UserPlay user;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "LYRIC_TEXT", nullable = false, length = 1000000000)
    private String lyricText;

    public Long getLyricID() {
        return lyricID;
    }

    public void setLyricID(Long lyricID) {
        this.lyricID = lyricID;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public UserPlay getUser() {
        return user;
    }

    public void setUser(UserPlay user) {
        this.user = user;
    }

    public String getLyricText() {
        return lyricText;
    }

    public void setLyricText(String lyricText) {
        this.lyricText = lyricText;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lyricID != null ? lyricID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the lyricID fields are not set
        if (!(object instanceof Lyric)) {
            return false;
        }
        Lyric other = (Lyric) object;
        if ((this.lyricID == null && other.lyricID != null) || (this.lyricID != null && !this.lyricID.equals(other.lyricID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.uc.dei.aor.projeto6.grupod.entities.Lyric[ id=" + lyricID + " ]";
    }

}
