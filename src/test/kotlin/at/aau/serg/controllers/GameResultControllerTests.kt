package at.aau.serg.controllers

import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setUp() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)

    }

}