package cz.vse.praguePub;

import cz.vse.praguePub.logika.TypVysledku;
import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VysledekTest {
    private Podnik testovaciPodnik;
    private Podnik testovaciPodnik2;
    private Pivo testovaciPivo;
    private Pivo testovaciPivo2;

    @BeforeEach
    public void priradInstance() {
        this.testovaciPivo = new Pivo(
                new ObjectId(),
                "test",
                "test",
                5.0,
                5.0,
                "test",
                "test",
                25.0,
                25.0
        );
        this.testovaciPivo2 = new Pivo(
                new ObjectId(),
                "test2",
                "test2",
                6.0,
                6.0,
                "test2",
                "test2",
                50.0,
                50.0
        );
        this.testovaciPodnik = new Podnik(
                new ObjectId(),
                "test",
                6,
                "Dejvice",
                "Dejvická",
                16000,
                "1",
                List.of(new Recenze(new ObjectId(), "test", 5.0)),
                List.of(this.testovaciPivo)
        );
        this.testovaciPodnik2 = new Podnik(
                new ObjectId(),
                "test",
                3,
                "Žižkov",
                "Dejvická",
                13000,
                "1",
                List.of(new Recenze(new ObjectId(), "test", 5.0)),
                List.of(this.testovaciPivo2)
        );
    }

    @Test
    public void getDBObjektClass() {
        Vysledek<Pivo> vysledek = new Vysledek<>(
                this.testovaciPivo,
                this.testovaciPivo,
                TypVysledku.OK,
                "",
                null
        );
        Vysledek<Podnik> vysledek2 = new Vysledek<>(
                this.testovaciPodnik,
                this.testovaciPodnik2,
                TypVysledku.OK,
                "",
                null
        );

        assertEquals(vysledek.getDBObjektClass(), Pivo.class);
        assertEquals(vysledek2.getDBObjektClass(), Podnik.class);
    }

    @Test
    public void pokracuj() {
        Vysledek<Podnik> vysl = new Vysledek<>(
                this.testovaciPodnik,
                this.testovaciPodnik2,
                TypVysledku.OK,
                "",
                null
        );

        Vysledek<Podnik> vysl2 = new Vysledek<>(
                this.testovaciPodnik,
                this.testovaciPodnik2,
                TypVysledku.STEJNA_ADRESA,
                "",
                () -> vysl
        );

        assertEquals(vysl2.pokracuj(), vysl);
    }
}
