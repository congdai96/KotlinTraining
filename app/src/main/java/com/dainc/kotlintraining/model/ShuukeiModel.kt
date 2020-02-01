package com.dainc.kotlintraining.model

import java.io.Serializable

class ShuukeiModel : Serializable{

    private lateinit var roleName: String
    private var male: Int = 0
    private var female: Int = 0
    private var ageMax19: Int = 0
    private var ageMin20: Int = 0
    private var notFull: Int = 0
    private var notAge: Int = 0

    constructor (
        roleName: String,
        male: Int,
        female: Int,
        ageMax19: Int,
        ageMin20: Int,
        notFull: Int,
        notAge: Int
    ){
        this.roleName = roleName
        this.male = male
        this.female = female
        this.ageMax19 = ageMax19
        this.ageMin20 = ageMin20
        this.notFull = notFull
        this.notAge = notAge
    }

    fun getRoleName(): String {
        return roleName
    }

    fun setRoleName(roleName: String) {
        this.roleName = roleName
    }

    fun getMale(): Int {
        return male
    }

    fun setMale(male: Int) {
        this.male = male
    }

    fun getFemale(): Int {
        return female
    }

    fun setFemale(female: Int) {
        this.female = female
    }

    fun getAgeMax19(): Int {
        return ageMax19
    }

    fun setAgeMax19(ageMax19: Int) {
        this.ageMax19 = ageMax19
    }

    fun getAgeMin20(): Int {
        return ageMin20
    }

    fun setAgeMin20(ageMin20: Int) {
        this.ageMin20 = ageMin20
    }

    fun getNotFull(): Int {
        return notFull
    }

    fun setNotFull(notFull: Int) {
        this.notFull = notFull
    }

    fun getNotAge(): Int {
        return notAge
    }

    fun setNotAge(notAge: Int) {
        this.notAge = notAge
    }

}



