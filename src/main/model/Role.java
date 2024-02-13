package model;

import java.util.ArrayList;

public interface Role {
    void addRole(Game g);
    void deleteRole(Game g);

    ArrayList<Game> getRoles();

}
