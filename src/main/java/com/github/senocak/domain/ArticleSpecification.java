package com.github.senocak.domain;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ArticleSpecification implements Specification<Article> {
    private final ArticleCriteria articleCriteria;

    public ArticleSpecification(final ArticleCriteria articleCriteria) {
        this.articleCriteria = articleCriteria;
    }

    @Override
    public Predicate toPredicate(final Root root, final CriteriaQuery query, final CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        //Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
        if (articleCriteria.getTitle() != null && !articleCriteria.getTitle().isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + articleCriteria.getTitle().toLowerCase() + "%"));
        }
        if (articleCriteria.getAuthor() != null && !articleCriteria.getAuthor().isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + articleCriteria.getAuthor().toLowerCase() + "%"));
        }
        if (articleCriteria.getSlug() != null && !articleCriteria.getSlug().isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("slug"), articleCriteria.getSlug()));
        }
        if (articleCriteria.getPublishFrom() != null && !articleCriteria.getPublishFrom().isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("publish"), articleCriteria.getPublishFrom()));
        }
        if (articleCriteria.getPublishTo() != null && !articleCriteria.getPublishTo().isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("publish"), articleCriteria.getPublishTo()));
        }
        return predicate;
    }
}
