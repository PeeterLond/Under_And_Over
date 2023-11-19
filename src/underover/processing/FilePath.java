package underover.processing;

public enum FilePath {

    PLAYER_FILE_PATH("./resources/player_data.txt"),
    MATCH_FILE_PATH("./resources/match_data.txt"),
    RESULT_FILE_PATH("./src/underover/result.txt");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}