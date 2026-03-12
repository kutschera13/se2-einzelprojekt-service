package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null).body.orEmpty()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeInSeconds() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null).body.orEmpty()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboad_invalidRank_returnsBadRequest() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "first", 20, 20.0)))

        assertEquals(400, controller.getLeaderboard(0).statusCode.value())
        assertEquals(400,controller.getLeaderboard(-1).statusCode.value())
        assertEquals(400,controller.getLeaderboard(2).statusCode.value())
    }

    @Test
    fun test_getLeaderboard_rankWindow_clampedCorrectly() {
        val results = (1..10).map { GameResult(it.toLong(), "player$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(results)
        assertEquals(4, controller.getLeaderboard(1).body?.size)

        whenever(mockedService.getGameResults()).thenReturn(results)
        assertEquals(7, controller.getLeaderboard(5).body?.size)

        whenever(mockedService.getGameResults()).thenReturn(results)
        assertEquals(4, controller.getLeaderboard(10).body?.size)
    }

}