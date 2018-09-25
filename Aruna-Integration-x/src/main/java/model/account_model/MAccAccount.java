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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author kasun
 */
@Entity
@Table(name = "m_acc_account")
public class MAccAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "index_no")
    private Integer indexNo;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "`level`")
    private String level;

    @Basic(optional = false)
    @Column(name = "acc_code")
    private String accCode;

    @Basic(optional = false)
    @Column(name = "cop")
    private boolean cop;

    @Basic(optional = false)
    @Column(name = "`user`")
    private int user;

    @Basic(optional = false)
    @Column(name = "acc_type")
    private String accType;

    @Basic(optional = false)
    @Column(name = "is_acc_account")
    private boolean isAccAccount;

    @Column(name = "description")
    private String description;

    @Column(name = "sub_account_of")
    private Integer subAccountOf;

    @JoinColumn(name = "acc_main", referencedColumnName = "index_no")
    @ManyToOne(optional = false)
    private Integer accMain;

    @Column(name = "sub_account_count")
    private Integer subAccountCount;

    public MAccAccount() {
    }

    public MAccAccount(Integer indexNo, String name, String level, String accCode, boolean cop, int user, String accType, boolean isAccAccount, String description, Integer subAccountOf, Integer accMain, Integer subAccountCount) {
        this.indexNo = indexNo;
        this.name = name;
        this.level = level;
        this.accCode = accCode;
        this.cop = cop;
        this.user = user;
        this.accType = accType;
        this.isAccAccount = isAccAccount;
        this.description = description;
        this.subAccountOf = subAccountOf;
        this.accMain = accMain;
        this.subAccountCount = subAccountCount;
    }

   
    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public boolean getCop() {
        return cop;
    }

    public void setCop(boolean cop) {
        this.cop = cop;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public boolean getIsAccAccount() {
        return isAccAccount;
    }

    public void setIsAccAccount(boolean isAccAccount) {
        this.isAccAccount = isAccAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSubAccountOf() {
        return subAccountOf;
    }

    public void setSubAccountOf(Integer subAccountOf) {
        this.subAccountOf = subAccountOf;
    }

    public Integer getSubAccountCount() {
        return subAccountCount;
    }

    public void setSubAccountCount(Integer subAccountCount) {
        this.subAccountCount = subAccountCount;
    }

    public Integer getAccMain() {
        return accMain;
    }

    public void setAccMain(Integer accMain) {
        this.accMain = accMain;
    }

}
