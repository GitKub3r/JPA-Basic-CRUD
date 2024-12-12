package mipaquete;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Random;

public class App {

    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("db-connection");
    static final EntityManager entityManager = emf.createEntityManager();
    static final EntityTransaction transaction = entityManager.getTransaction();

    public static void main(String[] args) {
        List<Jugador> players = showAllPlayers();

        for(Jugador player : players) {
            System.out.println(player.toString());
        }

        Jugador player = new Jugador();
        player.setNombre("Chiquito de la Calzada");
        player.setEstatura(1.62f);
        player.setPeso(54.31f);
        Equipo team = entityManager.find(Equipo.class, 2);
        player.setIdEquipo(team);

        addPlayer(player);
        players.clear();
        players = showAllPlayers();
        for(Jugador playerB : players) {
            System.out.println(playerB.toString());
        }

        player.setNombre("El que te cuento");
        updatePlayer(player);
        players.clear();
        players = showAllPlayers();
        for(Jugador playerB : players) {
            System.out.println(playerB.toString());
        }

        deletePlayer(player.getId());
        players.clear();
        players = showAllPlayers();
        for(Jugador playerB : players) {
            System.out.println(playerB.toString());
        }
    }

    static private List<Jugador> showAllPlayers() {
        transaction.begin();
        return entityManager.createQuery("FROM Jugador", Jugador.class).getResultList();
    }

    static private void addPlayer(Jugador player) {
        transaction.begin();
        entityManager.persist(player);
        transaction.commit();
    }

    static private void updatePlayer(Jugador player) {
        transaction.begin();
        entityManager.merge(player);
        transaction.commit();
    }

    static private void deletePlayer(int playerID) {
        transaction.begin();
        Jugador newPlayer = entityManager.find(Jugador.class, playerID);
        entityManager.remove(newPlayer);
        transaction.commit();
    }

    static private void restorePlayers() {
        transaction.begin();

        for (int i = 1; i <= 50; i++) {
            Random random = new Random();

            int teamID = random.nextInt(1, 4);
            Equipo team = entityManager.find(Equipo.class, teamID);
            float height = (float) (random.nextFloat(1, 2));
            float weight = (float) (random.nextFloat(60, 90));

            Jugador player = new Jugador();
            player.setNombre("Jugador-" + i);
            player.setEstatura(height);
            player.setPeso(weight);
            player.setIdEquipo(team);

            entityManager.persist(player);
        }

        transaction.commit();
    }

    static private List<Equipo> showAllTeams() {
        transaction.begin();
        return entityManager.createQuery("FROM Jugador", Equipo.class).getResultList();
    }

    static private void addTeam(Equipo team) {
        transaction.begin();
        Equipo newTeam = new Equipo();
        newTeam.setNombre(team.getNombre());
        entityManager.persist(newTeam);
        transaction.commit();
    }

    static private void updateTeam(Equipo team) {
        transaction.begin();
        Equipo newTeam = new Equipo();
        newTeam.setNombre(team.getNombre());
        entityManager.merge(newTeam);
        transaction.commit();
    }

    static private void deleteTeam(int teamID) {
        transaction.begin();
        Equipo newTeam = entityManager.find(Equipo.class, teamID);
        entityManager.remove(newTeam);
        transaction.commit();
    }
}
