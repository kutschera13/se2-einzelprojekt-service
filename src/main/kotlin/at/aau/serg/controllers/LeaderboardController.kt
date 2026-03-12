package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
    ): ResponseEntity<List<GameResult>> {
        val sorted = gameResultService.getGameResults()  //unsortiere Liste wird hintergegeben
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds })) // Liste wird hier sortiert

        if (rank == null) {
            return ResponseEntity.ok(sorted)
        }

        //Validiert den Rank Parameter
        if (rank < 1 || rank > sorted.size) {
            return ResponseEntity.badRequest().build()
        }

        val index = rank - 1  // rechnet von 1-based auf 0-based (Array) um

        val fromIndex = maxOf(0, index - 3)
        val toIndex   = minOf(sorted.size - 1, index + 3)

        val result = sorted.subList(fromIndex, toIndex+1)
        return ResponseEntity.ok(result)
    }
}