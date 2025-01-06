package com.project_service.backend.builder;

import com.project_service.backend.entity.Project;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import static java.util.Objects.isNull;

public class SpecificationBuilder {

    private Specification<Project> socksSpecification;

    public SpecificationBuilder andEqual(Object fieldValue, String fieldName) {
        Assert.hasText("Field name cannot be empty", fieldName);

        Specification<Project> specification = (root, query, criteriaBuilder) ->
                isNull(fieldValue) ? null : criteriaBuilder.equal(root.get(fieldName), fieldValue);

        return createOrUpdateSpecification(specification);
    }

    public SpecificationBuilder andLike(String fieldValue, String fieldName) {
        Assert.hasText("Field name cannot be empty", fieldName);

        Specification<Project> specification = (root, query, criteriaBuilder) ->
                isNull(fieldValue) ? null : criteriaBuilder.like(root.get(fieldName), "%" + fieldValue + "%");

        return createOrUpdateSpecification(specification);
    }

    private SpecificationBuilder createOrUpdateSpecification(Specification<Project> specification) {
        if (this.socksSpecification == null) this.socksSpecification = Specification.where(specification);
        else {
            this.socksSpecification = socksSpecification.and(specification);
        }

        return this;
    }

    public Specification<Project> build() {
        Specification<Project> temporarySpecification = this.socksSpecification;
        this.socksSpecification = null;

        return temporarySpecification;
    }
}
