package com.tp.programareamea.firebase

class AdminCodes constructor(inputCode: String) {
    private var userInputCode: String = inputCode

    fun setInputCode(code:String){
        this.userInputCode = code
    }

    fun checkCodeIsValid(): Boolean {
        return this.getAdminCodes().contains(this.userInputCode)
    }

    private  fun getAdminCodes(): List<String> {
        return listOf<String>("aaa","cdsf","dsa")
    }
}