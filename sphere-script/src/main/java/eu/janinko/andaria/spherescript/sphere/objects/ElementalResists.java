package eu.janinko.andaria.spherescript.sphere.objects;

import lombok.Getter;

@Getter
public class ElementalResists {

    private final PossibleRange resFire;
    private final PossibleRange resCold;
    private final PossibleRange resPoison;
    private final PossibleRange resEnergy;
    private final PossibleRange resNegative;

    public ElementalResists() {
        resFire = new PossibleRange();
        resCold = new PossibleRange();
        resPoison = new PossibleRange();
        resEnergy = new PossibleRange();
        resNegative = new PossibleRange();
    }

    @Override
    public String toString() {
        return "Resistances{" + "ResFire=" + resFire
                + ", resCold=" + resCold
                + ", resPoison=" + resPoison
                + ", resEnergy=" + resEnergy
                + ", resNegative=" + resNegative
                + '}';
    }
}
