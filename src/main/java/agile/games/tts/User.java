package agile.games.tts;

 class User {
     private final UserId userId;
     private UserRole userRole;

     public User() {
         userId = new UserId();
         userRole = UserRole.LOBBY;
     }

     public UserRole getUserRole() {
         return userRole;
     }

     public UserId getUserId() {
         return userId;
     }

     public void setAsFacilitator() {
         userRole = UserRole.FACILITATOR;
     }

     public void setAsPlayer() {
         userRole = UserRole.PLAYER;
     }
 }
