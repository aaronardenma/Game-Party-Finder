package model;

import java.util.ArrayList;

public class Person implements Role {
    private String name;
    private int numOfRoles;
    private ArrayList<Game> roles;
    private ArrayList<GameParty> gameParties;

    public Person(String name) {
        this.name = name;
        this.numOfRoles = 0;
        this.roles = new ArrayList<Game>();
        this.gameParties = new ArrayList<GameParty>();

    }

    public String getName() {
        return this.name;
    }

    public int getNumOfRoles() {
        return this.numOfRoles;
    }

    public ArrayList<GameParty> getGameParties() {
        return this.gameParties;
    }

    public void addToGameParty(GameParty gp) {
        gp.addMember(this);
        gameParties.add(gp);
    }

    @Override
    public void addRole(Game g) {
        this.roles.add(g);
        this.numOfRoles++;
    }

    @Override
    public void deleteRole(Game g) {
        this.roles.remove(g);
    }

    @Override
    public ArrayList<Game> getRoles() {
        return this.roles;
    }

}
