package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.Mockito.`when` as whenever
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setUp() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)

    }


    @Test
    fun test_getGameResult_returnsCorrectResult() {
        val gameResult = GameResult(1.toLong(), "player1", 100, 5.0)
        // 1L = 1.toLong() --> long

        // Wenn der Service mit ID=1 aufgerufen wird, soll er das jeweilige gameResult zurückgeben
        whenever(mockedService.getGameResult(1L)).thenReturn(gameResult)

        val result = controller.getGameResult(1L)

        // Prüft ob der Service wirklich aufgerufen wurde
        verify(mockedService).getGameResult(1L)

        // Prüft ob der Controller das richtige Objekt zurückgibt
        assertEquals(gameResult, result)
    }

}