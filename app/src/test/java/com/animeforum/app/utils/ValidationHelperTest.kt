package com.animeforum.app.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationHelperTest {

    @Test
    fun `validateUsername should fail when blank`() {
        val result = ValidationHelper.validateUsername("")

        assertFalse(result.isValid)
        assertEquals("El nombre de usuario es requerido", result.errorMessage)
    }

    @Test
    fun `validateUsername should fail when too short`() {
        val result = ValidationHelper.validateUsername("ab")

        assertFalse(result.isValid)
        assertEquals("Mínimo 3 caracteres", result.errorMessage)
    }

    @Test
    fun `validateUsername should fail when too long`() {
        val longUsername = "a".repeat(21)

        val result = ValidationHelper.validateUsername(longUsername)

        assertFalse(result.isValid)
        assertEquals("Máximo 20 caracteres", result.errorMessage)
    }

    @Test
    fun `validateUsername should fail when contains invalid characters`() {
        val result = ValidationHelper.validateUsername("user!")

        assertFalse(result.isValid)
        assertEquals("Solo letras, números y guión bajo", result.errorMessage)
    }

    @Test
    fun `validateUsername should succeed with valid input`() {
        val result = ValidationHelper.validateUsername("user_123")

        assertTrue(result.isValid)
        assertEquals("", result.errorMessage)
    }

    @Test
    fun `validateEmail should fail when blank`() {
        val result = ValidationHelper.validateEmail("")

        assertFalse(result.isValid)
        assertEquals("El email es requerido", result.errorMessage)
    }

    @Test
    fun `validateAnimeTitle should fail when blank`() {
        val result = ValidationHelper.validateAnimeTitle("")

        assertFalse(result.isValid)
        assertEquals("El título del anime es requerido", result.errorMessage)
    }

    @Test
    fun `validateAnimeTitle should fail when too short`() {
        val result = ValidationHelper.validateAnimeTitle("A")

        assertFalse(result.isValid)
        assertEquals("Mínimo 2 caracteres", result.errorMessage)
    }

    @Test
    fun `validateAnimeTitle should fail when too long`() {
        val title = "a".repeat(101)

        val result = ValidationHelper.validateAnimeTitle(title)

        assertFalse(result.isValid)
        assertEquals("Máximo 100 caracteres", result.errorMessage)
    }

    @Test
    fun `validateAnimeTitle should succeed with valid title`() {
        val result = ValidationHelper.validateAnimeTitle("Naruto")

        assertTrue(result.isValid)
        assertEquals("", result.errorMessage)
    }

    @Test
    fun `validateMessage should fail when blank`() {
        val result = ValidationHelper.validateMessage("")

        assertFalse(result.isValid)
        assertEquals("El mensaje es requerido", result.errorMessage)
    }

    @Test
    fun `validateMessage should fail when too short`() {
        val result = ValidationHelper.validateMessage("Muy corto")

        assertFalse(result.isValid)
        assertEquals("Mínimo 10 caracteres", result.errorMessage)
    }

    @Test
    fun `validateMessage should fail when too long`() {
        val message = "a".repeat(501)

        val result = ValidationHelper.validateMessage(message)

        assertFalse(result.isValid)
        assertEquals("Máximo 500 caracteres", result.errorMessage)
    }

    @Test
    fun `validateMessage should succeed with valid message`() {
        val result = ValidationHelper.validateMessage("Este es un mensaje válido")

        assertTrue(result.isValid)
        assertEquals("", result.errorMessage)
    }

    @Test
    fun `validateCategory should fail when blank`() {
        val result = ValidationHelper.validateCategory("")

        assertFalse(result.isValid)
        assertEquals("Selecciona una categoría", result.errorMessage)
    }

    @Test
    fun `validateCategory should fail when invalid`() {
        val result = ValidationHelper.validateCategory("Invalid")

        assertFalse(result.isValid)
        assertEquals("Categoría inválida", result.errorMessage)
    }

    @Test
    fun `validateCategory should succeed with valid category`() {
        val result = ValidationHelper.validateCategory("Shonen")

        assertTrue(result.isValid)
        assertEquals("", result.errorMessage)
    }

    @Test
    fun `validateRating should fail when below minimum`() {
        val result = ValidationHelper.validateRating(0)

        assertFalse(result.isValid)
        assertEquals("La calificación mínima es 1", result.errorMessage)
    }

    @Test
    fun `validateRating should fail when above maximum`() {
        val result = ValidationHelper.validateRating(6)

        assertFalse(result.isValid)
        assertEquals("La calificación máxima es 5", result.errorMessage)
    }

    @Test
    fun `validateRating should succeed with valid rating`() {
        val result = ValidationHelper.validateRating(4)

        assertTrue(result.isValid)
        assertEquals("", result.errorMessage)
    }
}
