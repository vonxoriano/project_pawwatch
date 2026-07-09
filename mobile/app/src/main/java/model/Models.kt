package edu.cit.soriano.pawwatch.mobile.model

data class Animal(
    val animalId: Long,
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val gender: String,
    val description: String?,
    val healthStatus: String?,
    val photo: String?,
    val adoptionStatus: String
)

data class AnimalRequest(
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val gender: String,
    val description: String,
    val healthStatus: String,
    val photo: String
)

data class AdoptionApplication(
    val applicationId: Long,
    val animal: Animal,
    val user: ApplicationUser?,
    val applicationDate: String,
    val status: String,
    val remarks: String?
)

data class ApplicationUser(
    val userId: Long,
    val fullName: String,
    val email: String,
    val contactNumber: String,
    val role: String
)

data class ApplicationRequest(
    val animalId: Long
)

data class ApplicationStatusRequest(
    val status: String,
    val remarks: String
)