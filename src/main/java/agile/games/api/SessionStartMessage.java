package agile.games.api;

import agile.games.tts.GameSessionCode;

public class SessionStartMessage implements Message {

    private final SessionStartInfo sessionStart;

    public SessionStartMessage(UserSessionId userSessionId) {
        sessionStart = new SessionStartInfo();
        sessionStart.userSessionId = userSessionId;
    }

    public SessionStartInfo getSessionStart() {
        return sessionStart;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return new GameSessionCode("ASFDASD");
    }

    public static class SessionStartInfo {
        private UserSessionId userSessionId;

        public String getUserSessionId() {
            return userSessionId.toString();
        }
    }
}

