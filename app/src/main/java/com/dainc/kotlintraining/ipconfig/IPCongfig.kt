package com.dainc.kotlintraining.ipconfig

interface IPConfig {
    companion object {
        val IP = "192.168.1.10:8090/training"

        val CHECK_LOGIN = "http://$IP/jwt"
        val GET_INF_USER = "http://$IP/api-user"
        val GET_LIST_ROLE = "http://$IP/api-role"
        val GET_LIST_GENDER = "http://$IP/api-gender"
        val GET_LIST_USER_BY_SEARCH = "http://$IP/api-search"
        val GET_LIST_SHUUKEI = "http://$IP/api-shuukei"
        val DELETE_USER = "http://$IP/api-user-delete"
        val EDIT_USER = "http://$IP/api-user-edit"
    }
}
