# Tasks to be done before April 10th

## Frontend
- Replace the button in MenuPanel with a .png graphic as an icon.

- Write the Dialog box that extends `JComponent`. Redirect to the Dialog box if the `New Game` Button is clicked (Check `ActionListener` of `MenuButton`). When the Dialog box is confirmed (Check the `ActionListener` of Dialog box), the CityMap page appeared <br>**_[no backend yet now, will have to implement later, after database implementation]_**. (Mohamed)

- Create a new button inside `StatisticPanel`, which is a `Back to Menu` button (for now, just a text button, later replaced with an icon) which will save the current game and go back to the Menu Page (implement `ActionListener` (`mouseClicked()`) for this button) (Mohamed)

- Create a custom panel that extends `JPanel`, which is named, `LoadGamePanel`, and implement it as you wish. (The `LoadGamePanel` page has a list with a vertical scroll bar; which will read the saved game list and show it inside the Panel. There will be 2 Buttons that operate CRUD (for this, only Read and Delete). <br> _**[Will improve the design later by overriding the paintComponent method]**_. (Dominykas)

- Add `ActionListener` (`mouseClicked()`) for Quit Button, which will quit everything. (Mohamed)

- Create a Delete Button inside the `InGameButtonPanel` of the `CityMap` Page, which will delete the elements that are clicked inside the CityMap Panel. [Later ask for confirmation with Dialog]. (Zaw Moe)

- Create a `StatisticLabel` class that extends `JLabel`. Every label inside `StatisticPanel` will be the same type `StatisticLabel` (Third MileStone)

- Use the `Timer` to render the vehicle on the road (The track of the road can be checked at `city.cells[][]`)) (Zayar)

## Backend

- Implement the `isOccupied()` method inside `City` Class, which takes `Position p` and checks if that cell is occupied or not (in other words, checking if the `CellItem` is `GENERAL` or not) (Dominykas)

- Implement `Database` class which handles the JSON files, and minimum `CRUD` has to be implemented. Design the JSON Structure. `Database` class will have to work only with `City` Class. (Dominykas)

- Implement the `constructBuilding(Position p; CellItem ct)` method, which will assign the `CellItem.BUILDING` to the `cells[][]` with `p` and it is not enough to set only 1 cell, you have to assign according to the `CellItem.BUILDING.tiles`. The given parameter p represents the top left cell. And the last thing is to create a new `Building` Object, assign respective attributes, and store it inside buildings (`ArrayList`). [Later, we will implement the satisfaction] (Dominykas)

- Implement `deleteRoad(Position p)` which will delete from the `cells[][]` (changing back to `CellItem.GENERAL`) and remove it from roads (ArrayList). (Zaw Moe)

- Implement `deleteBuilding (Position p)` which will go linear search through the `buildings` ArrayList, (use `Iterator()`) and find if the given `p` is inside that building location (inside the `Building` Class, there is already stored `List<Position>`  attributes which were assigned inside `constructBuilding()`). Once you find the respective `Building`, then first, you remove that `CellItem.BUILDING` from cells, with the help of `Building.location` (`ArrayList`) [just reassign the `CellItem.GENERAL`]. After that, you can use the `.remove()` method of `iterator` to remove that Building from the buildings list. The above logic has to be done inside `Iteration`. (Zaw Moe)

- `assignZone(Position p, CellItem ct)` methods which will assign the given `ct` into the `cells[][]` at `p`. And Create a new Zone object according to the `CellItem` and add it to the zones List. <br> **_[Later, we will implement the cost, and population inside Zone class and we will calculate it accordingly]_** (Mohamed)
 - create a time mechanism (probably as int), which we could use. To follow time, speed up the game and so forth. For example increase +1 if normal time, by 2 if doble speed, end day at 3600. (Zaw Moe)

## UI improvement

- Override the `paintComponent` of `InGameButtonPanel` and `StatisticPanel`

## For adding a new button
1. obtain the graphic `.png` file first.
2. add a new CellItem and add a (CellItem,Graphic) pair into `CityMap.graphics`
3. Create a new `InGameButton` object inside `InGameButtonPanel` and use respective graphic and image path.
4. Rendering can be done inside `paintComponent(Graphic)` method of `CityMap` class. The idea for render is that the larger element should rendered at the last step of the method.
5. Read the below docs.

## `CityMap.paintComponent()`

This method is a canvas method which will paint/draw the surface of the JPanel.
It has drawn with the coordinate point, top-left represents origin (0,0). <br>
It is just like a normal drawing on your paper. You can also draw the image on it (here we used image) <br>
It is rendered with this method <br> `gr.drawImage(img, j*tile_size, i*tile_size, tile_size, tile_size, null );` <br>
`drawImage(Image, X:int, Y:int, width:int, height:int, null)`. <br>
X and Y represent the top-left corner of the drawing. You can call this `drawImage` method as much as you want and you just need to provide what to draw, where to draw and the dimension to draw. <br>
As you see this method will render the base which is cells[][] matrix of the model. 
So, the `GENERAL`, `RESIDENTIAL`, `SERVICE_INDUSTRIAL`, `ROAD`, `TRANSMISSION_LINE` are painted first. <br>
After that, all the Buildings will be painted because buildings has larger tiles 2x2.
At the last, animation and event will be painted.
