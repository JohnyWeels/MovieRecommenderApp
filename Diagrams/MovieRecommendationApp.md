classDiagram
direction BT
class DatabaseManager {
  + DatabaseManager() 
  + getLikedMovies(String) List~Movie~
  + loginUser(String, String) boolean
  + registerUser(String, String) void
}
class Movie {
  + Movie(String, String) 
  - String title
  - String imdbID
   String imdbID
   String title
}
class MovieRecommendationApp {
  + MovieRecommendationApp() 
  - createMoviePanel(Movie, int) JPanel
  - saveLikedMovie(String, String) void
  - fetchRandomMovies() List~Movie~
  - showLoginFrame() void
  - parseOMDbResponse(String) List~Movie~
  - showLikedMovies(String) void
  - recommendMovies() void
  - initializeRecommendationApp(String) void
  + main(String[]) void
  - updatePoster(JLabel, String) void
  - updateDescriptionPane(JTextPane, String) void
  - displayRecommendations(List~Movie~) void
  - removeLikedMovie(String, String) void
   String loggedInUser
}

MovieRecommendationApp  -->  DatabaseManager 
MovieRecommendationApp  -->  Movie 
