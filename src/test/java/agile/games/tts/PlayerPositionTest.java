package agile.games.tts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerPositionTest {

    @Test
    void distance_between_completely_different_positions() {
        PlayerPosition p1 = new PlayerPosition(10, 20);
        PlayerPosition p2 = new PlayerPosition(30, 40);

        int distance = p1.distance(p2);

        assertTrue(distance > 1);
    }

    @Test
    void distance_between_same_positions() {
        PlayerPosition p1 = new PlayerPosition(1, 1);
        PlayerPosition p2 = new PlayerPosition(1, 1);

        int distance = p1.distance(p2);

        assertEquals(0, distance);
    }

    @Test
    void distance_between_nearby_positions1() {
        PlayerPosition p1 = new PlayerPosition(1, 2);
        PlayerPosition p2 = new PlayerPosition(1, 1);

        int distance = p1.distance(p2);

        assertEquals(1, distance);
    }

    @Test
    void distance_between_nearby_positions2() {
        PlayerPosition p1 = new PlayerPosition(1, 1);
        PlayerPosition p2 = new PlayerPosition(2, 1);

        int distance = p1.distance(p2);

        assertEquals(1, distance);
    }

    @Test
    void diagonal_distance() {
        PlayerPosition p1 = new PlayerPosition(1, 1);
        PlayerPosition p2 = new PlayerPosition(2, 2);

        int distance = p1.distance(p2);

        assertEquals(2, distance);
    }
}