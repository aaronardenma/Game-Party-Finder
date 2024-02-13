package model;

import java.util.ArrayList;

public class Person implements Role {
    private String name;
    private int numOfGames;
    private ArrayList<Game> roles;

    public Person(String name) {
        this.name = name;
        this.numOfGames = 0;
        this.roles = new ArrayList<Game>();

    }

    @Override
    public void addRole(Game g) {
        this.roles.add(g);
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
