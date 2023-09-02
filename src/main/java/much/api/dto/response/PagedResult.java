package much.api.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedResult<T> {

    private List<T> content;

    // 현재 페이지
    private int page;

    // 페이지 사이즈
    private int size;

    // 전체 페이지
    private int totalPages;

    // 전체 페이지에 존재하는 요소의 개수
    private int totalElements;

    // 첫번째 페이지인지
    private boolean first;

    // 마지막 페이지인지
    private boolean last;

    // 리스트가 비어있는지
    private boolean empty;


    public PagedResult(List<T> content,
                       int page,
                       int size,
                       int totalPages,
                       int totalElements,
                       boolean first,
                       boolean last,
                       boolean empty) {

        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }
}
