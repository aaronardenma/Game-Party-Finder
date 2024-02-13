package ui;

import model.*;


public class Main {
    public static void main(String[] args) {
        Person aaron = new Person("Aaron");
        Game leagueOfLegends = new Game("League of Legends");
        aaron.addRole(leagueOfLegends);
        GameParty league = new GameParty(5, leagueOfLegends);
        league.addMember(aaron);
    }
}
