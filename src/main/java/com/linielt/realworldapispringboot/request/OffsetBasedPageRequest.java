package com.linielt.realworldapispringboot.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Mostly copied from:
 * <a href="https://blog.felix-seifert.com/limit-and-offset-spring-data-jpa-repositories/">...</a>
 * <a href="https://github.com/hafizmuhammadusman537/spring-boot-JPA-pagination-offset-and-limit/blob/main/productapi-java/src/main/java/com/productapi/services/ProductService.java">...</a>
 */

public class OffsetBasedPageRequest implements Pageable {

    private final int limit;

    private final int offset;

    // Constructor could be expanded if sorting is needed
    private final Sort sort;

    public OffsetBasedPageRequest(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public OffsetBasedPageRequest(int offset, int limit, Sort.Direction direction, Sort sort, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public OffsetBasedPageRequest(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        // Typecast possible because number of entries cannot be bigger than integer (primary key is integer)
        return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()));
    }

    public Pageable previous() {
        // The integers are positive. Subtracting does not let them become bigger than integer.
        return hasPrevious() ?
                new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize())): this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasedPageRequest( pageNumber * getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
