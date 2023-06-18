package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Skill {

    JAVA("Java", "자바"),
    JAVASCRIPT("JavaScript", "자바스크립트"),
    TYPESCRIPT("TypeScript", "타입스크립트"),
    C("C", "씨언어"),
    C_PP("C++", "씨쁠쁠"),
    C_SHARP("C#", "씨샾"),
    PHP("PHP", "피에이치피"),
    PYTHON("Python", "파이썬"),
    RUBY("Ruby", "루비"),

    SPRING("Spring", "스프링"),
    SPRING_BOOT("Spring Boot", "스프링부트"),
    JPA("JPA", "제이피에이"),
    SPRING_DATA_JPA("Spring Data Jpa", "스프링데이터제이피에이"),
    QUERYDSL("Querydsl", "쿼리디에스엘"),
    REACT("React", "리액트"),
    VUE("Vue", "뷰"),
    NODE("Node", "노드"),

    FIGMA("Figma", "피그마"),
    ADOBE_XD("Adobe XD", "어도비엑스디"),
    UNITY("Unity", "유니티");

    private final String englishName;

    private final String koreanName;

}
