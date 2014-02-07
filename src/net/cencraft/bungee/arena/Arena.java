package net.cencraft.bungee.arena;


import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
*
* @Author Jake
*/
public class Arena {

//A list of all the Arena Objects
public static ArrayList<Arena> arenaObjects = new ArrayList<Arena>();

//Some fields we want each Arena object to store:
private Location startLocation, endLocation; //Some general arena locations

private String name; //Arena name
private ArrayList<String> players = new ArrayList<String>(); //And arraylist of players name

private int maxPlayers;

private boolean inGame = false; //Boolean to determine if an Arena is ingame or not, automaticly make it false


//Now for a Constructor:
public Arena (String arenaName, Location startLocation, Location endLocation, int maxPlayers) { //So basicly: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)
//Lets initalize it all:
this.name = arenaName;
this.startLocation = startLocation;
this.endLocation = endLocation;
this.maxPlayers = maxPlayers;

//Now lets add this object to the list of objects:
arenaObjects.add(this);

}

public Location getStartLocation() {
return this.startLocation;
}

public void setStartLocation(Location startLocation) {
this.startLocation = startLocation;
}

public Location getEndLocation() {
return this.endLocation;
}

public void setEndLocation(Location endLocation) {
this.endLocation = endLocation;
}

public String getName() {
return this.name;
}

public void setName(String name) {
this.name = name;
}

public int getMaxPlayers() {
return this.maxPlayers;
}

public void setMaxPlayers(int maxPlayers) {
this.maxPlayers = maxPlayers;
}

public ArrayList<String> getPlayers() {
return this.players;
}



//And finally, some booleans:
public boolean isFull() { //Returns weather the arena is full or not
if (players.size() >= maxPlayers) {
return true;
} else {
return false;
}
}


public boolean isInGame() {
return inGame;
}

public void setInGame(boolean inGame) {
this.inGame = inGame;
}

//To send each player in the arena a message
public void sendMessage(String message) {
for (String s: players) {
Bukkit.getPlayer(s).sendMessage(message);
}
}


}
