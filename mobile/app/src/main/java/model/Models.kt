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
    val remarks: String?,
    val housingType: String? = null,
    val hasLandlordPermission: Boolean? = null,
    val hasYard: Boolean? = null,
    val householdMembers: Int? = null,
    val hasYoungChildren: Boolean? = null,
    val hasOtherPets: Boolean? = null,
    val petExperience: String? = null,
    val hoursAwayDaily: Int? = null,
    val reasonForAdopting: String? = null,
    val agreesToReturnPolicy: Boolean? = null
)

data class ApplicationUser(
    val userId: Long,
    val fullName: String,
    val email: String,
    val contactNumber: String,
    val role: String
)

data class ApplicationRequest(
    val animalId: Long,
    val housingType: String,
    val hasLandlordPermission: Boolean?,
    val hasYard: Boolean,
    val householdMembers: Int,
    val hasYoungChildren: Boolean,
    val hasOtherPets: Boolean,
    val petExperience: String,
    val hoursAwayDaily: Int,
    val reasonForAdopting: String,
    val agreesToReturnPolicy: Boolean,
    val remarks: String? = null
)

data class ApplicationStatusRequest(
    val status: String,
    val remarks: String
)

data class Favorite(
    val favoriteId: Long,
    val animal: Animal,
    val user: ApplicationUser? = null,
    val dateAdded: String? = null
)

data class Notification(
    val notificationId: Long,
    val message: String,
    val dateSent: String,
    val status: String, // "UNREAD" or "READ"
    val applicationId: Long? = null,
    val user: ApplicationUser? = null
)