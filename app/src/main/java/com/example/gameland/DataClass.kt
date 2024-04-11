package com.example.gameland

class DataClass {
    var dataName: String? = null
        private set
    var dataCategory: String? = null
        private set
    var dataDescription: String? = null
        private set
    var dataPrice: String? = null
        private set
    var dataImage: String? = null
        private set
    var key: String? = null

    constructor(
        dataName: String?,
        dataCategory: String?,
        dataDescription: String?,
        dataPrice: String?,
        dataImage: String?
    ) {
        this.dataName = dataName
        this.dataCategory = dataCategory
        this.dataDescription = dataDescription
        this.dataPrice = dataPrice
        this.dataImage = dataImage
    }

    constructor()
}