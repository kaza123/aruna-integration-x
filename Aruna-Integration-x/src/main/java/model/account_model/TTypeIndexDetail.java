/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.account_model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chama
 */
@Entity
@Table(name = "t_type_index_detail")
public class TTypeIndexDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "index_no")
    private Integer indexNo;

    @Column(name = "master_ref_id")
    private String masterRefId;

    @Column(name = "type")
    private String type;

    @Column(name = "account_ref_id")
    private Integer accountRefId;

    @Column(name = "account_index")
    private Integer accountIndex;

    public TTypeIndexDetail() {
    }

    public TTypeIndexDetail(Integer indexNo, String masterRefId, String type, Integer accountRefId, Integer accountIndex) {
        this.indexNo = indexNo;
        this.masterRefId = masterRefId;
        this.type = type;
        this.accountRefId = accountRefId;
        this.accountIndex = accountIndex;
    }

    public Integer getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(Integer accountIndex) {
        this.accountIndex = accountIndex;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getMasterRefId() {
        return masterRefId;
    }

    public void setMasterRefId(String masterRefId) {
        this.masterRefId = masterRefId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAccountRefId() {
        return accountRefId;
    }

    public void setAccountRefId(Integer accountRefId) {
        this.accountRefId = accountRefId;
    }

}
