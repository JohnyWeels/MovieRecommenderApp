package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRecommendationApp {
    private static final String OMDB_API_KEY = "66583560";
    private static final String OMDB_API_URL = "http://www.omdbapi.com/";

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "829174633573Dj";

    private String username; // Добавлено поле для хранения имени пользователя

    private JPanel recommendationsPanel;

    public MovieRecommendationApp() {
        showLoginFrame();
    }

    private void setLoggedInUser(String username) {
        this.username = username;
    }

    private void showLoginFrame() {
        JFrame loginFrame = new JFrame("Login/Register");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (DatabaseManager.loginUser(username, password)) {
                loginFrame.dispose();
                setLoggedInUser(username); // Устанавливаем имя пользователя после входа
                initializeRecommendationApp(username);
            } else {
                JOptionPane.showMessageDialog(null, "Неверные учетные данные. Попробуйте снова.");
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            DatabaseManager.registerUser(username, password);
            JOptionPane.showMessageDialog(null, "Регистрация успешна. Теперь вы можете войти.");
        });

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginFrame.setVisible(true);
    }

    private void initializeRecommendationApp(String username) {
        JFrame frame = new JFrame("Movie Recommendation App - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        recommendationsPanel = new JPanel();
        recommendationsPanel.setLayout(new BoxLayout(recommendationsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(recommendationsPanel);

        JButton recommendButton = new JButton("Рекомендовать");
        JButton likedMoviesButton = new JButton("Понравившиеся");

        recommendButton.addActionListener(e -> recommendMovies());
        likedMoviesButton.addActionListener(e -> showLikedMovies(username));

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(recommendButton, BorderLayout.SOUTH);
        frame.getContentPane().add(likedMoviesButton, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void recommendMovies() {
        List<Movie> randomMovies = fetchRandomMovies();

        if (!randomMovies.isEmpty()) {
            displayRecommendations(randomMovies);
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось получить фильмы.");
        }
    }

    private void showLikedMovies(String username) {
        List<Movie> likedMovies = DatabaseManager.getLikedMovies(username);
        displayRecommendations(likedMovies);
    }

    private List<Movie> fetchRandomMovies() {
        List<Movie> randomMovies = new ArrayList<>();

        try {
            OkHttpClient client = new OkHttpClient();
            String url = OMDB_API_URL + "?apikey=" + OMDB_API_KEY + "&type=movie&s=";

            String[] randomWords = {"action", "comedy", "drama", "adventure", "fantasy"};
            String randomWord = randomWords[(int) (Math.random() * randomWords.length)];
            url += randomWord;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                randomMovies = parseOMDbResponse(responseData);
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка при получении фильмов. Код: " + response.code());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении фильмов.\n" + ex.getMessage());
        }

        return randomMovies;
    }

    private List<Movie> parseOMDbResponse(String responseData) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.has("Search")) {
                JSONArray results = jsonObject.getJSONArray("Search");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);
                    String title = movie.getString("Title");
                    String imdbID = movie.getString("imdbID");

                    Movie movieObj = new Movie(title, imdbID);
                    movies.add(movieObj);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при разборе ответа от OMDb.\n" + e.getMessage());
        }

        return movies;
    }

    private void displayRecommendations(List<Movie> recommendedMovies) {
        recommendationsPanel.removeAll();

        for (int i = 0; i < Math.min(recommendedMovies.size(), 3); i++) {
            Movie movie = recommendedMovies.get(i);
            JPanel moviePanel = createMoviePanel(movie, i + 1);
            recommendationsPanel.add(moviePanel);
        }

        recommendationsPanel.revalidate();
    }

    private JPanel createMoviePanel(Movie movie, int number) {
        JPanel moviePanel = new JPanel(new BorderLayout());

        Font titleFont = new Font("Arial", Font.BOLD, 20);

        JLabel titleLabel = new JLabel(number + ". " + movie.getTitle());
        titleLabel.setFont(titleFont);
        moviePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel posterLabel = new JLabel();
        updatePoster(posterLabel, movie.getImdbID());
        moviePanel.add(posterLabel, BorderLayout.WEST);

        JTextPane descriptionPane = new JTextPane();
        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        updateDescriptionPane(descriptionPane, movie.getImdbID());
        moviePanel.add(new JScrollPane(descriptionPane), BorderLayout.CENTER);

        // Добавлено: Кнопка "Понравилось"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Кнопка "Понравилось"
        JButton likeButton = new JButton("Понравилось");
        likeButton.addActionListener(e -> saveLikedMovie(movie.getTitle(), movie.getImdbID()));
        buttonPanel.add(likeButton);

        // Кнопка "Удалить из понравившихся"
        JButton unlikeButton = new JButton("Удалить из понравившихся");
        unlikeButton.addActionListener(e -> removeLikedMovie(movie.getTitle(), movie.getImdbID()));
        buttonPanel.add(unlikeButton);

        moviePanel.add(buttonPanel, BorderLayout.SOUTH);

        return moviePanel;
    }

    private void removeLikedMovie(String movieTitle, String imdbID) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "DELETE FROM liked_movies WHERE username = ? AND movie_title = ? AND imdb_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username); // Используем сохраненное имя пользователя
                statement.setString(2, movieTitle);
                statement.setString(3, imdbID);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Фильм удален из понравившихся!");
                } else {
                    JOptionPane.showMessageDialog(null, "Фильм не найден в понравившихся.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении фильма из понравившихся.\n" + ex.getMessage());
        }
    }


    private void updatePoster(JLabel posterLabel, String imdbID) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = OMDB_API_URL + "?apikey=" + OMDB_API_KEY + "&i=" + imdbID;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);

                if (jsonObject.has("Poster")) {
                    String posterUrl = jsonObject.getString("Poster");
                    ImageIcon posterIcon = new ImageIcon(new java.net.URL(posterUrl));
                    posterLabel.setIcon(posterIcon);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке постера.\n" + ex.getMessage());
        }
    }

    private void updateDescriptionPane(JTextPane descriptionPane, String imdbID) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = OMDB_API_URL + "?apikey=" + OMDB_API_KEY + "&i=" + imdbID;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);

                String plot = jsonObject.getString("Plot");
                String imdbLink = "IMDb: <a href='https://www.imdb.com/title/" + imdbID + "'>IMDb Page</a>";

                descriptionPane.setText("Plot: " + plot + "<br><br>" + imdbLink);

                descriptionPane.addHyperlinkListener(e -> {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке описания.\n" + ex.getMessage());
        }
    }

    private void saveLikedMovie(String movieTitle, String imdbID) {
        // Используем сохраненное имя пользователя
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "INSERT INTO liked_movies (username, movie_title, imdb_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username); // Используем сохраненное имя пользователя
                statement.setString(2, movieTitle);
                statement.setString(3, imdbID);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Фильм добавлен в понравившиеся!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении фильма в понравившиеся.\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieRecommendationApp::new);
    }

    private static class Movie {
        private final String title;
        private final String imdbID;

        public Movie(String title, String imdbID) {
            this.title = title;
            this.imdbID = imdbID;
        }

        public String getTitle() {
            return title;
        }

        public String getImdbID() {
            return imdbID;
        }
    }

    private static class DatabaseManager {
        static {
            // Регистрация драйвера JDBC
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public static void registerUser(String username, String password) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
                String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static boolean loginUser(String username, String password) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    ResultSet resultSet = statement.executeQuery();
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public static List<Movie> getLikedMovies(String username) {
            List<Movie> likedMovies = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
                String query = "SELECT movie_title, imdb_id FROM liked_movies WHERE username = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        String movieTitle = resultSet.getString("movie_title");
                        String imdbID = resultSet.getString("imdb_id");
                        likedMovies.add(new Movie(movieTitle, imdbID));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return likedMovies;
        }
    }
}
