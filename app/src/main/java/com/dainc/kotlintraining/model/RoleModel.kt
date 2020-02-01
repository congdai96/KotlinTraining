package com.dainc.kotlintraining.model

import java.io.Serializable

class RoleModel(private var authorityId: Int, private var authorityName: String?) : Serializable {


    fun getAuthorityId(): Int {
        return authorityId
    }

    fun setAuthorityId(authorityId: Int) {
        this.authorityId = authorityId
    }

    fun getAuthorityName(): String? {
        return authorityName
    }

    fun setAuthorityName(authorityName: String) {
        this.authorityName = authorityName
    }

    override fun toString(): String {
        return this.authorityName.toString()
    }
}

