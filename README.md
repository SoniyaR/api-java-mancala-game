# Mancala Game Backend service

#### Technical stack
Java 17, MySQL database, Junit Juniper


#### Developers Guide
Steps to use this service and test in local environment:
1. Clone the repository in local
2. Set run configurations
3. Start server and access it on 8088 port
4. To view database: http://localhost:8088/h2-console (Now changed to mysql database)

#### Play locally on UI
1. Clone the respository https://github.com/SoniyaR/web-react-mancala-game
2. Open in your favourite IDE. Make sure you have npm installed.
3. run - npm start and access on http://localhost:3000/
4. Enjoy!


### Game Rules:

#### Board Setup
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit. At the start of the game, there are six stones in each
of the six round pits .
Rules
#### Game Play
The player who begins with the first move picks up all the stones in any of his
own six pits, and sows the stones on to the right, one in each of the following
pits, including his own big pit. No stones are put in the opponents' big pit. If the
player's last stone lands in his own big pit, he gets another turn. This can be
repeated several times before it's the other player's turn.
#### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little?) pit.

#### The Game Ends
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.
