package com.mark.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTestEntity is a Querydsl query type for TestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTestEntity extends EntityPathBase<TestEntity> {

    private static final long serialVersionUID = 2098073676L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTestEntity testEntity = new QTestEntity("testEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMoney money;

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public QTestEntity(String variable) {
        this(TestEntity.class, forVariable(variable), INITS);
    }

    public QTestEntity(Path<? extends TestEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTestEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTestEntity(PathMetadata metadata, PathInits inits) {
        this(TestEntity.class, metadata, inits);
    }

    public QTestEntity(Class<? extends TestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.money = inits.isInitialized("money") ? new QMoney(forProperty("money")) : null;
    }

}

