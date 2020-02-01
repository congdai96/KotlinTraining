package com.dainc.kotlintraining.model

import java.io.Serializable

class UserModel : Serializable {


    private var sNo: String? = null
    private  var userId: String? = null
    private var familyName: String? = null
    private var firstName: String? = null
    private var age: Int = 0
    private var genderId: Int = 0
    private var authorityId: Int = 0
    private var admin: Int = 0
    private var password: String? = null
    private var authorityName: String? = null
    private var genderName: String? = null

    constructor(sNo: String, userId: String, familyName: String, firstName: String, authorityName: String, admin: Int) {
        this.sNo = sNo
        this.userId = userId
        this.familyName = familyName
        this.firstName = firstName
        this.authorityName = authorityName
        this.admin = admin
    }

    constructor(
        userId: String,
        familyName: String,
        firstName: String,
        age: Int,
        genderId: Int,
        authorityId: Int,
        admin: Int,
        password: String,
        authorityName: String,
        genderName: String
    ) {
        this.userId = userId
        this.familyName = familyName
        this.firstName = firstName
        this.age = age
        this.genderId = genderId
        this.authorityId = authorityId
        this.admin = admin
        this.password = password
        this.authorityName = authorityName
        this.genderName = genderName
    }

    fun getsNo(): String? {
        return sNo
    }

    fun setsNo(sNo: String) {
        this.sNo = sNo
    }

    fun getAuthorityName(): String? {
        return authorityName
    }

    fun setAuthorityName(authorityName: String) {
        this.authorityName = authorityName
    }

    fun getGenderName(): String? {
        return genderName
    }

    fun setGenderName(genderName: String) {
        this.genderName = genderName
    }


    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getFamilyName(): String? {
        return familyName
    }

    fun setFamilyName(familyName: String) {
        this.familyName = familyName
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getAge(): Int {
        return age
    }

    fun setAge(age: Int) {
        this.age = age
    }

    fun getGenderId(): Int {
        return genderId
    }

    fun setGenderId(genderId: Int) {
        this.genderId = genderId
    }

    fun getAuthorityId(): Int {
        return authorityId
    }

    fun setAuthorityId(authorityId: Int) {
        this.authorityId = authorityId
    }

    fun getAdmin(): Int {
        return admin
    }

    fun setAdmin(admin: Int) {
        this.admin = admin
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
