A straightforward, lottery game simulation built for CS 342. This project recreates a simple version of Keno using JavaFX and event-driven programming. 
The focus is on clean logic, a functional GUI, and making the game actually feel like something a real person could play.

What This App Does:
Lets a single player fill out a Keno bet card (numbers 1–80).
Player picks:1, 4, 8, or 10 spots (how many numbers they want to play).
1–4 drawings (how many rounds to run that card through).
Supports both manual number selection and auto-pick (for the “I don’t want to think” crowd).
Runs drawings one by one, shows the 20 random numbers with pauses so the user can actually see what’s happening.
After each drawing:
Shows matches
Shows winnings for that drawing
Tracks total winnings
When the run is over, player can reset and play another card or exit.

GUI Overview
Built entirely in JavaFX, zero FXML, zero CSS, zero Scene Builder. Just code and vibes.
Two main scenes:
Welcome Screen
Menu bar: Rules, Odds, Exit
“Start Game” button

Game Screen
Same menu + a New Look option that swaps to a noticeably different theme
Bet card: GridPane (8×10) of numbered, clickable nodes
Controls to pick spots and number of drawings
Button to start the drawing sequence
All interactions follow normal Keno constraints — if the user tries to do something illegal, the app stops them before everything explodes.

Logic
All drawing logic and match calculations are separate from the UI so they can be tested.
Drawings produce 20 unique random numbers.
Payouts follow the North Carolina State Lottery Keno rules for spots 1, 4, 8, and 10.

Testing
Includes 25+ JUnit 5 tests for the game logic.
No GUI tests (obviously — JavaFX refuses to cooperate in headless mode).

How to Run
The project is a standard Maven JavaFX setup using the template provided in the course.
mvn clean install
mvn javafx:run

Runs on command line with Maven 3.6.3 as required.
