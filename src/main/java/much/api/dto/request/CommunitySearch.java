package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.CommunityCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommunitySearch {

    private static final int DEFAULT_SIZE_PER_PAGE = 10;
    private static final int MAX_SIZE_PER_PAGE = 40;

    @Pattern(
            regexp = "^(QNA)$|^(FREE)$|^(TECH_SHARE)$",
            message = "카테고리는 QNA, FREE, TECH_SHARE 중 하나입니다.")
    @NotNull
    private String category;

    private String search;

    private List<String> tags;

    Integer page;

    Integer size;

    boolean byRecent;

    @Builder
    private CommunitySearch(String category,
                            String search,
                            List<String> tags,
                            Integer page,
                            Integer size,
                            Boolean byRecent) {

        this.category = category;
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

        this.byRecent = byRecent == null || byRecent;
    }

    public CommunityCategory getCategory() {

        return CommunityCategory.valueOf(this.category);
    }
}
