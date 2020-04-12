package agile.games.tts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class AvatarAssigner {
    private static final Logger LOG = LoggerFactory.getLogger(AvatarAssigner.class);

    private String[] avatars;
    private int avatarIndex = 0;

    public AvatarAssigner() {
        try {
            URL url = getClass().getResource("/static");
            if (url != null && url.getProtocol().equals("file")) {
                Optional<File[]> fileList = Optional.ofNullable(new File(url.toURI()).listFiles());
                if (fileList.isPresent()) {
                    Optional<File> assetsDir = Arrays.stream(fileList.get())
                            .filter(f -> f.getName().equals("assets"))
                            .findAny();
                    if (assetsDir.isPresent()) {
                        Optional<File[]> assetFiles = Optional.ofNullable(assetsDir.get().listFiles());
                        if (assetFiles.isPresent()) {
                            Optional<File> avatarsDir = Arrays.stream(assetFiles.get())
                                    .filter(f -> f.getName().equals("avatars"))
                                    .findAny();
                            avatarsDir.ifPresent(f -> this.avatars = f.list());
                        }
                    }
                }
            }
        } catch (URISyntaxException e) {
            LOG.error("Can't load avatars", e);
        }
    }

    public String nextAvatar() {
        avatarIndex = (avatarIndex + 1) % avatars.length;
        return avatars[avatarIndex];
    }
}
