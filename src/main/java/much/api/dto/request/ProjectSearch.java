package much.api.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProjectSearch {

    private static final int DEFAULT_SIZE_PER_PAGE = 5;
    private static final int MAX_SIZE_PER_PAGE = 40;

    private String search;

    private List<String> tags;

    Integer page;

    Integer size;

    boolean onlyRecruiting;

    boolean byRecent;

    @Builder
    private ProjectSearch(String search,
                          List<String> tags,
                          Integer page,
                          Integer size,
                          Boolean onlyRecruiting,
                          Boolean byRecent) {

        this.search = search;
        this.tags = (tags == null) ? new ArrayList<>() : tags;
        this.page = (page == null || page <= 0) ? 0 : page - 1;

        if (size == null || size <= 0) {
            this.size = DEFAULT_SIZE_PER_PAGE;

        } else if (size > MAX_SIZE_PER_PAGE) {
            this.size = MAX_SIZE_PER_PAGE;

        } else {
            this.size = size;
        }

        this.onlyRecruiting = !(onlyRecruiting == null) && onlyRecruiting;
        this.byRecent = (byRecent == null) || byRecent;
    }

}
