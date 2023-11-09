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
@Table(name = "campeonatos")
@NamedQueries({
    @NamedQuery(name = "Campeonato.findAll", query = "SELECT c FROM Campeonato c"),
    @NamedQuery(name = "Campeonato.findById", query = "SELECT c FROM Campeonato c WHERE c.id = :id"),
    @NamedQuery(name = "Campeonato.findByPlace", query = "SELECT c FROM Campeonato c WHERE c.place = :place"),
    @NamedQuery(name = "Campeonato.findByMinIdade", query = "SELECT c FROM Campeonato c WHERE c.minIdade = :minIdade"),
    @NamedQuery(name = "Campeonato.findByMaxIdade", query = "SELECT c FROM Campeonato c WHERE c.maxIdade = :maxIdade"),
    @NamedQuery(name = "Campeonato.findByGender", query = "SELECT c FROM Campeonato c WHERE c.gender = :gender"),
    @NamedQuery(name = "Campeonato.findByCategory", query = "SELECT c FROM Campeonato c WHERE c.category = :category"),
    @NamedQuery(name = "Campeonato.findByReward", query = "SELECT c FROM Campeonato c WHERE c.reward = :reward"),
    @NamedQuery(name = "Campeonato.findByImg", query = "SELECT c FROM Campeonato c WHERE c.img = :img"),
    @NamedQuery(name = "Campeonato.findByMaxJogadores", query = "SELECT c FROM Campeonato c WHERE c.maxJogadores = :maxJogadores"),
    @NamedQuery(name = "Campeonato.findByClosed", query = "SELECT c FROM Campeonato c WHERE c.closed = :closed")})
public class Campeonato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "place")
    private String place;
    @Column(name = "min_idade")
    private Integer minIdade;
    @Column(name = "max_idade")
    private Integer maxIdade;
    @Column(name = "gender")
    private String gender;
    @Basic(optional = false)
    @Column(name = "category")
    private String category;
    @Column(name = "reward")
    private String reward;
    @Column(name = "img")
    private String img;
    @Basic(optional = false)
    @Column(name = "max_jogadores")
    private int maxJogadores;
    @Basic(optional = false)
    @Column(name = "closed")
    private boolean closed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campeonatoId")
    private Collection<Play> playCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campeonatoId")
    private Collection<Team> teamCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public Campeonato() {
    }

    public Campeonato(Integer id) {
        this.id = id;
    }

    public Campeonato(Integer id, String place, String category, int maxJogadores, boolean closed) {
        this.id = id;
        this.place = place;
        this.category = category;
        this.maxJogadores = maxJogadores;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getMinIdade() {
        return minIdade;
    }

    public void setMinIdade(Integer minIdade) {
        this.minIdade = minIdade;
    }

    public Integer getMaxIdade() {
        return maxIdade;
    }

    public void setMaxIdade(Integer maxIdade) {
        this.maxIdade = maxIdade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getMaxJogadores() {
        return maxJogadores;
    }

    public void setMaxJogadores(int maxJogadores) {
        this.maxJogadores = maxJogadores;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Collection<Play> getPlayCollection() {
        return playCollection;
    }

    public void setPlayCollection(Collection<Play> playCollection) {
        this.playCollection = playCollection;
    }

    public Collection<Team> getTeamCollection() {
        return teamCollection;
    }

    public void setTeamCollection(Collection<Team> teamCollection) {
        this.teamCollection = teamCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Campeonato)) {
            return false;
        }
        Campeonato other = (Campeonato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Campeonato[ id=" + id + " ]";
    }
    
}
