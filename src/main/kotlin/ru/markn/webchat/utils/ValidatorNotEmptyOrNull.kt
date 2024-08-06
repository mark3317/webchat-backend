package ru.markn.webchat.utils

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [ValidatorNotEmptyOrNullString::class, ValidatorNotEmptyOrNullList::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotEmptyOrNull(
    val message: String = "must not be empty",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ValidatorNotEmptyOrNullString : ConstraintValidator<NotEmptyOrNull, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || value.isNotEmpty()
    }
}

class ValidatorNotEmptyOrNullList : ConstraintValidator<NotEmptyOrNull, List<*>> {
    override fun isValid(value: List<*>?, context: ConstraintValidatorContext?): Boolean {
        return value == null || value.isNotEmpty()
    }
}