package com.github.senocak.clinked;

import com.github.senocak.clinked.domain.Article;
import com.github.senocak.clinked.domain.ArticleCriteria;
import com.github.senocak.clinked.domain.ArticleRepository;
import com.github.senocak.clinked.domain.ArticleSpecification;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.UUID;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@SpringBootApplication
@RequestMapping("/api/v1/article")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ArticleApplication {
	private final ArticleRepository articleRepository;

    public ArticleApplication(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}

	@EventListener(value = ApplicationReadyEvent.class)
	public void applicationReadyEvent() {
        for (int i = 1; i <= 10; i++) {
            final Article article = new Article();
            article.setId(UUID.randomUUID().toString());
            article.setTitle("Article Title " + i);
            article.setSlug("article-title-" + i);
            article.setContent("Article Content " + i);
            article.setAuthor("Article Author " + i);
            article.setPublish("2010-01-01T12:00:00+01:00");
            articleRepository.save(article);
        }
	}

    @GetMapping
    public Map<String, Page<Article>> getAll(
        @RequestParam(value = "next", defaultValue = "0") @Min(0) @Max(99) final int nextPage,
        @RequestParam(value = "max", defaultValue = "10") @Min(1) @Max(99) final int maxNumber,
        @RequestParam(value = "title", required = false) final String title,
        @RequestParam(value = "author", required = false) final String author,
        @RequestParam(value = "slug", required = false) final String slug,
        @RequestParam(value = "publishFrom", required = false) final String publishFrom,
        @RequestParam(value = "publishTo", required = false) final String publishTo
    ) {
        final Specification<Article> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            //Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            if (title != null && !title.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (author != null && !author.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }
            if (slug != null && !slug.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("slug"), slug));
            }
            if (publishFrom != null && !publishFrom.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("publish"), publishFrom));
            }
            if (publishTo != null && !publishTo.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("publish"), publishTo));
            }
            return predicate;
        };
        final Pageable paging = PageRequest.of(nextPage, maxNumber);
        final Page<Article> allWithLambda = articleRepository.findAll(specification, paging);
        final ArticleCriteria articleCriteria = new ArticleCriteria();
        articleCriteria.setTitle(title);
        articleCriteria.setAuthor(author);
        articleCriteria.setSlug(slug);
        articleCriteria.setPublishFrom(publishFrom);
        articleCriteria.setPublishTo(publishTo);
        final ArticleSpecification articleSpecification = new ArticleSpecification(articleCriteria);
        final Page<Article> allWithClass = articleRepository.findAll(articleSpecification, paging);
        return Map.of(
                "allWithLambda", allWithLambda,
                "allWithClass", allWithClass
        );
    }
}
