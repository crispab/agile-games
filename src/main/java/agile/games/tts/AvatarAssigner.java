package agile.games.tts;

import java.util.Arrays;
import java.util.List;

public class AvatarAssigner {
    private final List<String> avatars;
    private int avatarIndex = 0;

    public String nextAvatar() {
        avatarIndex = (avatarIndex + 1) % avatars.size();
        return avatars.get(avatarIndex);
    }

    public AvatarAssigner() {
        avatars = Arrays.asList(
                "bear.png",
                "buffalo.png",
                "chick.png",
                "chicken.png",
                "cow.png",
                "crocodile.png",
                "dog.png",
                "duck.png",
                "elephant.png",
                "frog.png",
                "giraffe.png",
                "goat.png",
                "gorilla.png",
                "hippo.png",
                "horse.png",
                "monkey.png",
                "moose.png",
                "narwhal.png",
                "owl.png",
                "panda.png",
                "parrot.png",
                "penguin.png",
                "pig.png",
                "rabbit.png",
                "rhino.png",
                "sloth.png",
                "snake.png",
                "walrus.png",
                "whale.png",
                "zebra.png"
        );
    }
}
