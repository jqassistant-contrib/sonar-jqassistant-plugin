package com.buschmais.jqassistant.scanner.test;

import com.buschmais.jqassistant.scanner.test.sets.annotation.AnnotatedType;
import com.buschmais.jqassistant.scanner.test.sets.annotation.Annotation;
import com.buschmais.jqassistant.store.api.model.ClassDescriptor;
import com.buschmais.jqassistant.store.api.model.QueryResult;
import org.junit.Test;

import java.io.IOException;

import static com.buschmais.jqassistant.scanner.test.matcher.ClassDescriptorMatcher.classDescriptor;
import static org.junit.Assert.assertThat;

public class AnnotationIT extends AbstractScannerIT {

    @Test
    public void classAnnotation() throws IOException {
        store.beginTransaction();
        scanner.scanClasses(AnnotatedType.class, Annotation.class);
        store.endTransaction();
        QueryResult result = store.executeQuery("MATCH (at:CLASS)-[:ANNOTATED_BY]->(a:CLASS) RETURN at, a");
        Iterable<QueryResult.Row> rows = result.getRows();
        QueryResult.Row row = rows.iterator().next();
        ClassDescriptor a = row.get("a");
        ClassDescriptor at = row.get("at");
        assertThat(at, classDescriptor(AnnotatedType.class));
        assertThat(a, classDescriptor(Annotation.class));
    }
}