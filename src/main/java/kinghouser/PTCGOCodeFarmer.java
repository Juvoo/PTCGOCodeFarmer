package kinghouser;

import kinghouser.util.youtube.YouTubeCrawler;

import java.util.ArrayList;
import java.util.List;

public class PTCGOCodeFarmer {

    public static void main(String[] args) {
        List<String> queries = new ArrayList<>();
        queries.add("pokemon opening");
        queries.add("pokémon opening");

        queries.add("pokemon box opening");
        queries.add("pokémon box opening");

        queries.add("pokemon cards");
        queries.add("pokémon cards");

        queries.add("pokemon cards opening");
        queries.add("pokémon cards opening");

        queries.add("pokemon open");
        queries.add("pokémon open");

        queries.add("pokemon cards open");
        queries.add("pokémon cards open");

        queries.add("pokemon box open");
        queries.add("pokémon box open");

        YouTubeCrawler youTubeCrawler = new YouTubeCrawler(queries);
        youTubeCrawler.start();
    }
}
