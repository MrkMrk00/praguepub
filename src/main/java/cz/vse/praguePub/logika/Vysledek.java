package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import lombok.*;

import java.util.function.Supplier;

/**
 * Výsledek je vrácen metodami v třídě {@link cz.vse.praguePub.logika.DatabazeImpl DatabazeImpl}. Instance této třídy jsou používány pro kontrolu zadávaných dat do databáze.
 * Např. když je nahráván do databáze podnik, který již v databázi je, tak se tato situace pomocí Výsledku může prezentovat uživateli.
 * <p>
 * Následné pokračování (v případě false-positive zachycení chybného vstupu) se zabalí do funkce {@link #pokracovat pokracovat}, která zase vrátí Vysledek.
 * @param <T> typ databázového objektu, se kterým se manipuluje
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Vysledek<T extends DBObjekt> {
    @Getter private final T dotazovany;
    @Getter private final T najity;
    @Getter private final TypVysledku typVysledku;
    @Getter private final String zprava;
    private final Supplier<Vysledek<T>> pokracovat;

    /**
     * Metoda vykoná proceduru, která byla odložena při zachycení chybného vstupu.
     * @return instanci Vysledek, <strong>nebo null</strong>
     */
    public Vysledek<T> pokracuj() {
        return (this.pokracovat != null) ? this.pokracovat.get() : null;
    }

    public Class<? extends DBObjekt> getDBObjektClass() {
        return this.dotazovany.getClass();
    }
}
