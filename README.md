### README

# Schedule Maker

This project is a Kotlin application that generates and prints a schedule for a set of teams divided into divisions. 
The schedule is distributed over 14 weeks, with each week containing 6 games.

## Features

- **Team and Division Management**: Define teams and divide them into divisions.
- **Game Preparation**: Generate games between teams, including additional games within divisions and between divisions.
- **Schedule Distribution**: Distribute games over 14 weeks, ensuring no team plays more than once per week.
- **Table Format Printing**: Print the schedule in a table format for better readability.

## Prerequisites

- JDK 18
- Gradle

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/yourusername/schedule-maker.git
cd schedule-maker
```

### Build the Project

```sh
./gradlew build
```

### Run the Application

```sh
./gradlew run
```

## Project Structure

- `src/main/kotlin/com/akommu/tools/Main.kt`: Main application file containing the logic for preparing teams, divisions, games, and printing the schedule.
- `.github/workflows/main.yml`: GitHub Actions workflow file to automate the build and run process.

## Usage

The main function initializes the teams and divisions, prepares the games, distributes them over the weeks, and prints the schedule in a table format.

### Example

```kotlin
fun main() {
    val schedule = mutableListOf<Pair<Team, Team>>()
    val weeks = MutableList(14) { mutableListOf<Pair<Team, Team>>() }

    val teams = prepareTeams()
    val divisions = prepareDivisions(teams)

    prepareGames(teams, schedule, divisions)
    distributeGames(schedule, weeks)
    printScheduleInTableFormat(weeks)
}
```

## GitHub Actions

The project includes a GitHub Actions workflow to automate the build and run process on every push or pull request to the `main` branch.

### Workflow File

```yaml
name: Run Schedule Maker

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v2

    - name: Set up JDK 18
      uses: actions/setup-java@v2
      with:
        distribution: 'adopt'
        java-version: '18'

    - name: Build with Gradle
      run: ./gradlew build

    - name: Run Kotlin Application
      run: ./gradlew run
```

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.