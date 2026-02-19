/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ioann
 */
@Entity
@Table(name = "STATISTICS")
@NamedQueries({
    @NamedQuery(name = "Statistics.findAll", query = "SELECT s FROM Statistics s"),
    @NamedQuery(name = "Statistics.findByStatisticsId", query = "SELECT s FROM Statistics s WHERE s.statisticsId = :statisticsId"),
    @NamedQuery(name = "Statistics.findBySearchedword", query = "SELECT s FROM Statistics s WHERE s.searchedword = :searchedword")})
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "STATISTICS_ID")
    private Integer statisticsId;
    @Basic(optional = false)
    @Column(name = "SEARCHEDWORD")
    private String searchedword;

    public Statistics() {
    }

    public Statistics(Integer statisticsId) {
        this.statisticsId = statisticsId;
    }

    public Statistics(Integer statisticsId, String searchedword) {
        this.statisticsId = statisticsId;
        this.searchedword = searchedword;
    }

    public Integer getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(Integer statisticsId) {
        this.statisticsId = statisticsId;
    }

    public String getSearchedword() {
        return searchedword;
    }

    public void setSearchedword(String searchedword) {
        this.searchedword = searchedword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statisticsId != null ? statisticsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Statistics)) {
            return false;
        }
        Statistics other = (Statistics) object;
        if ((this.statisticsId == null && other.statisticsId != null) || (this.statisticsId != null && !this.statisticsId.equals(other.statisticsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pojo.Statistics[ statisticsId=" + statisticsId + " ]";
    }
    
}
