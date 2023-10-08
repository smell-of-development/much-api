package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Getter
public class PagedResult<T> {

    private List<?> elements;

    // 현재 페이지
    private Integer page;

    // 현재 페이지에 존재하는 요소의 수
    private Integer numberOfElements;

    // 전체 페이지
    @JsonInclude(NON_NULL)
    private Integer totalPages;

    // 첫번째 페이지인지
    private boolean first;

    // 마지막 페이지인지
    private boolean last;

    // 페이지 리스트가 비어있는지
    private boolean empty;


    @Builder(access = AccessLevel.PRIVATE)
    private PagedResult(List<?> elements,
                        Integer page,
                        Integer numberOfElements,
                        Integer totalPages,
                        boolean first,
                        boolean last,
                        boolean empty) {

        this.elements = elements;
        this.page = page + 1;
        this.numberOfElements = numberOfElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }


    public static PagedResult<?> ofPageWithCompletelyMapped(Page<?> page) {

        return pageBuilder(page)
                .elements(page.getContent())
                .build();
    }


    public static PagedResult<?> ofPageWithNotMapped(Page<? extends PageElement<?>> page) {

        return pageBuilder(page)
                .elements(page.getContent()
                        .stream()
                        .map(PageElement::toResponseDto)
                        .toList())
                .build();
    }


    public static PagedResult<?> ofSliceWithNotMapped(Slice<? extends PageElement<?>> slice) {

        return sliceBuilder(slice)
                .elements(slice.getContent()
                        .stream()
                        .map(PageElement::toResponseDto)
                        .toList())
                .build();
    }


    public static PagedResultBuilder<?> pageBuilder(Page<?> page) {

        return sliceBuilder(page)
                .totalPages(page.getTotalPages());
    }


    public static PagedResultBuilder<?> sliceBuilder(Slice<?> slice) {

        return PagedResult.builder()
                .page(slice.getNumber())
                .numberOfElements(slice.getNumberOfElements())
                .first(slice.isFirst())
                .last(slice.isLast())
                .empty(slice.isEmpty());
    }
}
