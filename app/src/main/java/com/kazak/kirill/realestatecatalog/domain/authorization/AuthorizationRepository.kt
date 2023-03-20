package com.kazak.kirill.realestatecatalog.domain.authorization

import com.kazak.kirill.realestatecatalog.domain.authorization.module.UserCredentialsModule

interface AuthorizationRepository {

    fun getUserCredentials(): UserCredentialsModule
    fun checkIfUserWithThisEmailExist(email: String): Boolean
    fun saveUserCredentials(userCredentials: UserCredentialsModule)
}