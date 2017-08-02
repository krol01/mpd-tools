package io.lindstrom.mpd;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.lindstrom.mpd.data.MPD;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.function.Supplier;

public class RandomBeanTest {
    @Test
    public void randomMPD() throws IOException, SAXException {
        EnhancedRandom random = new MPDRandomBuilder()
                .constantStringField("segmentAlignment", "true")
                .constantStringField("subsegmentAlignment", "true")
                .constantStringField("lang", "sv")
                .constantStringField("contains", "1")
                .constantStringField("dependencyLevel", "1")
                .randomize(Long.class, (Supplier<Long>) () -> 5L)
                .collectionSizeRange(1, 2)
                .build();

        MPDValidator validator = new MPDValidator();
        MPDParser parser = new MPDParser();

        for (int i = 0; i < 5; i++) {
            MPD mpd = random.nextObject(MPD.class);
            String generated = parser.writeAsString(parser.parse(parser.writeAsString(mpd)));

            try {
                validator.validate(generated);
            } catch (Exception e) {
                System.out.println(generated);
                throw e;
            }
        }
    }

    public static class MPDRandomBuilder extends EnhancedRandomBuilder {
        public MPDRandomBuilder constantStringField(String fieldName, String value) {
            randomize(FieldDefinitionBuilder.field()
                    .named(fieldName).ofType(String.class).get(), (Supplier<String>) () -> value);
            return this;
        }
    }
}
