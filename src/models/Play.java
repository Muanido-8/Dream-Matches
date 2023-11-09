/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Conta 2
 */
@Entity
@Table(name = "plays")
@NamedQueries({
    @NamedQuery(name = "Play.findAll", query = "SELECT p FROM Play p"),
    @NamedQuery(name = "Play.findById", query = "SELECT p FROM Play p WHERE p.id = :id"),
    @NamedQuery(name = "Play.findByCampo", query = "SELECT p FROM Play p WHERE p.campo = :campo"),
    @NamedQuery(name = "Play.findByImg", query = "SELECT p FROM Play p WHERE p.img = :img"),
    @NamedQuery(name = "Play.findByPlayTime", query = "SELECT p FROM Play p WHERE p.playTime = :playTime"),
    @NamedQuery(name = "Play.findByStage", query = "SELECT p FROM Play p WHERE p.stage = :stage")})
public class Play implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "campo")
    private String campo;
    @Column(name = "img")
    private String img;
    @Basic(optional = false)
    @Column(name = "play_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playTime;
    @Basic(optional = false)
    @Column(name = "stage")
    private int stage;
    @JoinColumn(name = "team_home", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Team teamHome;
    @JoinColumn(name = "team_visitor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Team teamVisitor;
    @JoinColumn(name = "campeonato_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Campeonato campeonatoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playId")
    private Collection<Result> resultCollection;

    public Play() {
    }

    public Play(Integer id) {
        this.id = id;
    }

    public Play(Integer id, String campo, Date playTime, int stage) {
        this.id = id;
        this.campo = campo;
        this.playTime = playTime;
        this.stage = stage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public Team getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    public Team getTeamVisitor() {
        return teamVisitor;
    }

    public void setTeamVisitor(Team teamVisitor) {
        this.teamVisitor = teamVisitor;
    }

    public Campeonato getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Campeonato campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public Collection<Result> getResultCollection() {
        return resultCollection;
    }

    public void setResultCollection(Collection<Result> resultCollection) {
        this.resultCollection = resultCollection;
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
        if (!(object instanceof Play)) {
            return false;
        }
        Play other = (Play) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Play[ id=" + id + " ]";
    }
    
}
