package com.example.pc01movilescercado24100141delgado24100644.presentation.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BaggageCalculatorScreenTest {

    @Test
    fun `blank input returns mandatory error`() {
        val result = calculateBaggage("", FlightType.Nacional)

        assertTrue(result is BaggageCalculationState.Error)
        assertEquals("El campo es obligatorio", (result as BaggageCalculationState.Error).message)
    }

    @Test
    fun `non numeric input returns numeric error`() {
        val result = calculateBaggage("abc", FlightType.Nacional)

        assertTrue(result is BaggageCalculationState.Error)
        assertEquals("Debe ingresar un valor numérico", (result as BaggageCalculationState.Error).message)
    }

    @Test
    fun `zero or negative input returns greater than zero error`() {
        val result = calculateBaggage("0", FlightType.Internacional)

        assertTrue(result is BaggageCalculationState.Error)
        assertEquals("El peso debe ser mayor a cero", (result as BaggageCalculationState.Error).message)
    }

    @Test
    fun `national baggage within limit complies`() {
        val result = calculateBaggage("23", FlightType.Nacional)

        assertTrue(result is BaggageCalculationState.Success)
        val success = result as BaggageCalculationState.Success
        assertTrue(success.result.complies)
        assertEquals(0.0, success.result.excessKg, 0.0)
        assertEquals(23.0, success.result.limitKg, 0.0)
    }

    @Test
    fun `international baggage over limit calculates excess`() {
        val result = calculateBaggage("33.5", FlightType.Internacional)

        assertTrue(result is BaggageCalculationState.Success)
        val success = result as BaggageCalculationState.Success
        assertTrue(!success.result.complies)
        assertEquals(1.5, success.result.excessKg, 0.0)
        assertEquals(32.0, success.result.limitKg, 0.0)
    }

    @Test
    fun `comma decimal input is accepted`() {
        val result = calculateBaggage("18,5", FlightType.Nacional)

        assertTrue(result is BaggageCalculationState.Success)
        val success = result as BaggageCalculationState.Success
        assertEquals(18.5, success.result.weightKg, 0.0)
    }
}

