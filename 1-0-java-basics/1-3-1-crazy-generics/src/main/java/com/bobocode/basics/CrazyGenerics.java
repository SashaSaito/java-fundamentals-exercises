package com.bobocode.basics;

import com.bobocode.basics.util.BaseEntity;
import lombok.Data;
import lombok.val;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class CrazyGenerics {
    @Data
    public static class Sourced<T> {
        private T value;
        private String source;
    }

    @Data
    public static class Limited<T extends Number> {
        private final T actual;
        private final T min;
        private final T max;
    }

    public interface Converter<T, R> {
        R convert(T obj);
    }

    public static class MaxHolder<T extends Comparable<? super T>> {
        private T max;

        public MaxHolder(T max) {
            this.max = max;
        }

        public void put(T val) {
            if (val.compareTo(max) > 0) {
                max = val;
            }
        }

        public T getMax() {
            return max;
        }
    }

    interface StrictProcessor<T extends Serializable & Comparable<? super T>> {
        void process(T obj);
    }

    interface CollectionRepository<T extends BaseEntity, C extends Collection<T>> {
        void save(T entity);

        C getEntityCollection();
    }

    interface ListRepository<T extends BaseEntity> extends CollectionRepository<T, List<T>> {
    }

    interface ComparableCollection<E> extends Collection<E>, Comparable<Collection<?>> {

        @Override
        default int compareTo(Collection<?> o) {
            return Integer.compare(this.size(), o.size());
        }
    }

    static class CollectionUtil {
        static final Comparator<BaseEntity> CREATED_ON_COMPARATOR = Comparator.comparing(BaseEntity::getCreatedOn);

        public static void print(List<?> list) {
            list.forEach(element -> System.out.println(" – " + element));
        }

        public static boolean hasNewEntities(Collection<? extends BaseEntity> entities) {
            return entities.stream()
                    .anyMatch(e -> e.getUuid() == null);
        }

        public static boolean isValidCollection(Collection<? extends BaseEntity> entities,
                                                Predicate<? super BaseEntity> validationPredicate) {
            return entities.stream()
                    .allMatch(validationPredicate);
        }

        public static <T extends BaseEntity> boolean hasDuplicates(Collection<T> entities, T targetEntity) {
            return entities.stream()
                    .filter(e -> e.getUuid().equals(targetEntity.getUuid()))
                    .count() > 1;
        }

        public static <T> Optional<T> findMax(Iterable<T> elements, Comparator<? super T> comparator) {
            var iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return Optional.empty();
            }
            var max = iterator.next();
            while (iterator.hasNext()) {
                var element = iterator.next();
                if (comparator.compare(element, max) > 0) {
                    max = element;
                }
            }
            return Optional.of(max);
        }

        public static <T extends BaseEntity> T findMostRecentlyCreatedEntity(Collection<T> entities) {
            return findMax(entities, CREATED_ON_COMPARATOR)
                    .orElseThrow();
        }

        public static void swap(List<?> elements, int i, int j) {
            Objects.checkIndex(i, elements.size());
            Objects.checkIndex(j, elements.size());
            swapHelper(elements, i, j);
        }

        private static <T> void swapHelper(List<T> elements, int i, int j) {
            T temp = elements.get(i);
            elements.set(i, elements.get(j));
            elements.set(j, temp);
        }
    }
}