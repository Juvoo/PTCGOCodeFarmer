package kinghouser.util.youtube;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSearchContinuation;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultVideoDetails;
import com.github.kiulian.downloader.model.search.field.SortField;
import com.github.kiulian.downloader.model.search.field.TypeField;
import com.github.kiulian.downloader.model.search.field.UploadDateField;
import kinghouser.util.OCRUtils;
import kinghouser.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class YouTubeCrawler {

    private final List<String> queries;

    private final YoutubeDownloader youtubeDownloader;

    private final ArrayList<String> checkedVideoIDs;

    private final ArrayList<SearchResult> searchResults;

    private ArrayList<YouTubeVideoScanner> runningYouTubeVideoScanners = new ArrayList<>();

    public YouTubeCrawler(List<String> queries) {
        this.queries = queries;
        youtubeDownloader = new YoutubeDownloader();
        checkedVideoIDs = new ArrayList<>();
        searchResults = new ArrayList<>();
    }

    public void start() {
        for (String query : queries) {
            search(query);
        }

        ArrayList<String> videoIDs = new ArrayList<>();

        for (SearchResult searchResult : searchResults) {
            for (SearchResultVideoDetails searchResultVideoDetails : searchResult.videos()) {
                if (!videoIDs.contains(searchResultVideoDetails.videoId()) && Utils.searchResultVideoDetailsFitsCriteria(searchResultVideoDetails)) {
                    videoIDs.add(searchResultVideoDetails.videoId());
                }
            }
        }

        System.out.println("Found " + videoIDs.size() + " videos. Scanning now.");

        for (SearchResult searchResult : searchResults) {
            for (SearchResultVideoDetails searchResultVideoDetails : searchResult.videos()) {
                if (!this.checkedVideoIDs.contains(searchResultVideoDetails.videoId()) && Utils.searchResultVideoDetailsFitsCriteria(searchResultVideoDetails)) {
                    // YouTubeVideoScanner youTubeVideoScanner = new YouTubeVideoScanner(searchResultVideoDetails);
                    System.out.println(searchResultVideoDetails.badges() + " | " + searchResultVideoDetails.title() + " | " + searchResultVideoDetails.viewCount() + " | " + Utils.urlFromVideoID(searchResultVideoDetails.videoId()));
                    OCRUtils.checkVideo(YouTubeVideoDownloader.downloadYouTubeVideo(Utils.urlFromVideoID(searchResultVideoDetails.videoId())));
                    checkedVideoIDs.add(searchResultVideoDetails.videoId());
                }
            }
        }
    }

    public void search(String query) {
        RequestSearchResult request = new RequestSearchResult(query)
                .type(TypeField.VIDEO)
                .uploadedThis(UploadDateField.DAY)
                .forceExactQuery(true)
                .sortBy(SortField.VIEW_COUNT);

        SearchResult searchResult = youtubeDownloader.search(request).data();

        searchResults.add(searchResult);

        while (searchResult != null && searchResult.hasContinuation()) {
            RequestSearchContinuation nextRequest = new RequestSearchContinuation(searchResult);
            searchResult = youtubeDownloader.searchContinuation(nextRequest).data();
            if (searchResult != null) searchResults.add(searchResult);
        }
    }
}