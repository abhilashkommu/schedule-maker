package com.akommu.tools

data class Team(val name: String, var otherDivisionAdditionalGameCount: Int = 0)
data class Division(val name: String, val teams: List<Team>, var playedOtherDivisionAdditionalGames: Boolean = false)

fun main() {
    println("\u001B[1m")
    val schedule = mutableListOf<Pair<Team, Team>>()
    val weeks = MutableList(14) { mutableListOf<Pair<Team, Team>>() }

    // Prepare teams
    val teams = prepareTeams()

    // Prepare divisions
    val divisions = prepareDivisions(teams)

    // Prepare games
    prepareGames(teams, schedule, divisions)

    // Shuffle and distribute games into 14 weeks with 6 games each week
    distributeGames(schedule, weeks)

    // Print the schedule
    printSchedule(weeks)
}

private fun prepareDivisions(teams: MutableList<Team>): List<Division> {
    val divisions = listOf(
        Division("Division 1", teams.subList(0, 3)),
        Division("Division 2", teams.subList(3, 6)),
        Division("Division 3", teams.subList(6, 9)),
        Division("Division 4", teams.subList(9, 12))
    )
    return divisions
}

fun prepareTeams(): MutableList<Team> {
    val teams = mutableListOf<Team>()
    teams.add(Team("Sampath's Nifty Team"))
    teams.add(Team("Vamsi's Victorious Team"))
    teams.add(Team("Shiva's Fabulous Team"))
    teams.add(Team("Ashwin's Agreeable Team"))
    teams.add(Team("Sai Manoj's Super Team"))
    teams.add(Team("Anirudh's Bihar Batch"))
    teams.add(Team("Son's of Pitches"))
    teams.add(Team("Varun's Tompa Bay Strikers"))
    teams.add(Team("NaatuguntalaGang"))
    teams.add(Team("Vishnu's Dazzling Team"))
    teams.add(Team("Mohan's Marvelous Team"))
    teams.add(Team("Deccan Chargers"))
    return teams
}

private fun printSchedule(weeks: MutableList<MutableList<Pair<Team, Team>>>) {
    var gamesCount = 0

    for (week in weeks.indices) {
        println("Week ${week + 1}:")
        println("--------")
        for (game in weeks[week]) {
            println("${game.first.name} VS ${game.second.name}")
            gamesCount++
        }
        println()
        println("=================================================")
    }
    println("Total games: $gamesCount")
}

fun printScheduleInTableFormat(weeks: List<List<Pair<Team, Team>>>) {
    println("Schedule:")
    println("-------------------------------------------------")
    println(String.format("%-10s | %-25s | %-25s", "Week", "Team 1", "Team 2"))
    println("-------------------------------------------------")
    weeks.forEachIndexed { weekIndex, week ->
        week.forEach { game ->
            println(String.format("%-10d | %-25s | %-25s", weekIndex + 1, game.first.name, game.second.name))
        }
    }
    println("-------------------------------------------------")
}

fun distributeGames(
    schedule: MutableList<Pair<Team, Team>>,
    weeks: MutableList<MutableList<Pair<Team, Team>>>
) {
    schedule.shuffle()
    val teamGamesPerWeek = mutableMapOf<Team, MutableSet<Int>>()
    for (week in weeks.indices) {
        teamGamesPerWeek.clear()
        var index = 0
        var tempSchedule = schedule.toMutableList()
        while (weeks[week].size < 6) {
            if (index >= tempSchedule.size) {
                teamGamesPerWeek.clear()
                weeks[week].clear()
                tempSchedule = schedule.toMutableList()
                tempSchedule.shuffle()
                index = 0
            }
            val game = tempSchedule[index]
            val (team1, team2) = game
            if (teamGamesPerWeek.getOrPut(team1) { mutableSetOf() }.contains(week) ||
                teamGamesPerWeek.getOrPut(team2) { mutableSetOf() }.contains(week)
            ) {
                index++
                continue
            } else {
                weeks[week].add(game)
                teamGamesPerWeek[team1]!!.add(week)
                teamGamesPerWeek[team2]!!.add(week)
                tempSchedule.removeAt(index)
            }
        }
        weeks[week].forEach { game ->
            schedule.remove(game)
        }
    }
}

private fun prepareGames(
    teams: List<Team>,
    schedule: MutableList<Pair<Team, Team>>,
    divisions: List<Division>
) {
    val random = java.util.Random()
    // Each team plays all other teams at least once
    for (i in teams.indices) {
        for (j in i + 1 until teams.size) {
            schedule.add(teams[i] to teams[j])
        }
    }

    // Each team plays one additional game with teams in the same division
    for (division in divisions) {
        for (i in division.teams.indices) {
            for (j in i + 1 until division.teams.size) {
                if (!schedule.contains(division.teams[i] to division.teams[j]))
                    schedule.add(division.teams[i] to division.teams[j])
                else
                    schedule.add(division.teams[j] to division.teams[i])

            }
        }
    }

    // Each team plays one additional game with a random team from another division
    for (division in divisions) {
        val otherDivision = divisions.first { it != division && !it.playedOtherDivisionAdditionalGames }
        for (team in division.teams) {
            if (team.otherDivisionAdditionalGameCount >= 1) continue
            var randomTeam = otherDivision.teams[random.nextInt(otherDivision.teams.size)]
            while (randomTeam.otherDivisionAdditionalGameCount >= 1) {
                otherDivision.teams[random.nextInt(otherDivision.teams.size)].also { randomTeam = it }
            }
            schedule.add(team to randomTeam)
            team.otherDivisionAdditionalGameCount++
            randomTeam.otherDivisionAdditionalGameCount++
        }
        otherDivision.playedOtherDivisionAdditionalGames = true
    }

    println("Total games: ${schedule.size}")
}