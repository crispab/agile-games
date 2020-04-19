package agile.games.tts;

import java.util.Arrays;
import java.util.List;

public class AvatarAssigner {
    private final List<String> avatars;
    private int avatarIndex = 0;

    public AvatarAssigner() {
        avatars = Arrays.asList("circle.png", "heart.png", "sun.png");
    }

    public String nextAvatar() {
        avatarIndex = (avatarIndex + 1) % avatars.size();
        return avatars.get(avatarIndex);
    }
}
