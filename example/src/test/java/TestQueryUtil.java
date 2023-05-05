import com.mark.entity.QTestEntity;
import com.mark.entity.TestEntity;
import com.mark.filter.DateTimeFilter;
import com.mark.filter.QueryFilterOperation;
import com.mark.provider.QueryFilters;
import com.mark.provider.QueryProvider;
import com.mark.util.PathFilterUtil;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


@DisplayName("PathFilterUtil 을 사용한다")
public class TestQueryUtil {

    @DisplayName("PathFilterUtil 을 사용한 결과가 동일한지 확인한다")
    @Test
    void test() {
        QTestEntity qTestEntity = QTestEntity.testEntity;
        List<Long> ids = List.of(1L, 2L, 3L);
        LocalDateTime start = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 0, 0, 0);

        List<Predicate> expectResult = List.of(
                qTestEntity.id.in(ids),
                qTestEntity.time.goe(start),
                qTestEntity.time.lt(end)
        );

        PathFilterUtil<TestEntity> util = new PathFilterUtil<>(qTestEntity);
        TestFilter testFilter = new TestFilter(
                new QueryProvider(QueryFilterOperation.IN, ids),
                new QueryProvider(
                        QueryFilterOperation.BETWEEN,
                        new DateTimeFilter(start, end)
                )
        );

        Assertions.assertEquals(util.call(testFilter.getQuery()), expectResult);
    }

    public static class TestFilter extends QueryFilters {
        private QueryProvider id;
        private QueryProvider time;

        public TestFilter(QueryProvider id, QueryProvider time) {
            this.id = id;
            this.time = time;
        }

    }
}
