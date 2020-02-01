package com.dainc.kotlintraining.model

import java.io.Serializable

class GenderModel(private var genderId: Int, private var genderName: String?) : Serializable {

    fun getGenderId(): Int {
        return genderId
    }

    fun setGenderId(genderId: Int) {
        this.genderId = genderId
    }

    fun getGenderName(): String? {
        return genderName
    }

    fun setGenderName(genderName: String) {
        this.genderName = genderName
    }

    override fun toString(): String {
        return this.genderName.toString()
    }

}
