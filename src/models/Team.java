/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Conta 2
 */
@Entity
@Table(name = "teams")
@NamedQueries({
    @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t"),
    @NamedQuery(name = "Team.findById", query = "SELECT t FROM Team t WHERE t.id = :id"),
    @NamedQuery(name = "Team.findByName", query = "SELECT t FROM Team t WHERE t.name = :name"),
    @NamedQuery(name = "Team.findByImg", query = "SELECT t FROM Team t WHERE t.img = :img"),
    @NamedQuery(name = "Team.findByFrom", query = "SELECT t FROM Team t WHERE t.from = :from")})
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "img")
    private String img;
    @Column(name = "from")
    private String from;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamId")
    private Collection<Coach> coachCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamHome")
    private Collection<Play> playCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamVisitor")
    private Collection<Play> playCollection1;
    @JoinColumn(name = "campeonato_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Campeonato campeonatoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamId")
    private Collection<Player> playerCollection;

    public Team() {
    }

    public Team(Integer id) {
        this.id = id;
    }

    public Team(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Collection<Coach> getCoachCollection() {
        return coachCollection;
    }

    public void setCoachCollection(Collection<Coach> coachCollection) {
        this.coachCollection = coachCollection;
    }

    public Collection<Play> getPlayCollection() {
        return playCollection;
    }

    public void setPlayCollection(Collection<Play> playCollection) {
        this.playCollection = playCollection;
    }

    public Collection<Play> getPlayCollection1() {
        return playCollection1;
    }

    public void setPlayCollection1(Collection<Play> playCollection1) {
        this.playCollection1 = playCollection1;
    }

    public Campeonato getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Campeonato campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public Collection<Player> getPlayerCollection() {
        return playerCollection;
    }

    public void setPlayerCollection(Collection<Player> playerCollection) {
        this.playerCollection = playerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Team[ id=" + id + " ]";
    }
    
}
