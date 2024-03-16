package eu.janinko.uo.saves;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Item extends Entry {

    Long amount;
    Long cont;
    String more1;
    String more2;

    public int getAmount() {
        if (amount == null) {
            return 1;
        }
        return amount.intValue();
    }

    /**
     * SERIAL	823131 P	734193 CONT	722733 CONTGRID	584396 ATTR	447485 DISPID	421276 MORE1	290897 COLOR	271874 AMOUNT
     * 220650 MOREP	179545 QUALITY	172693 NAME	129328 MORE2	70756 LAYER	35773 TYPE	33628 LINK	26126 TIMER	21045
     * TIMESTAMP	13486 EVENTS	2815 SPEECH	686 PIN	76 REGION.FLAGS	75 ALIGN	32 ABBREV	31 MEMBER	31 HATCH	15 PLANK	15
     * WEBPAGE	14 AUTHOR	5 CHARTER0	5 CHARTER1	5 CHARTER2	3 CHARTER3	2 CHARTER5	2 BODY.27	1 BODY.26	1 BODY.29	1 BODY.28
     * 1 BODY.21	1 BODY.20	1 BODY.23	1 BODY.22	1 BODY.25	1 BODY.24	1 BODY.38	1 BODY.37	1 BODY.39	1 BODY.30	1 BODY.32	1
     * BODY.31	1 BODY.34	1 BODY.33	1 BODY.36	1 BODY.35	1 BODY.49	1 BODY.48	1 BODY.41	1 BODY.40	1 BODY.43	1 BODY.42	1
     * BODY.45	1 BODY.44	1 BODY.47	1 BODY.46	1 BODY.59	1 BODY.50	1 BODY.52	1 BODY.51	1 BODY.54	1 BODY.53	1 BODY.56	1
     * BODY.55	1 BODY.58	1 BODY.57	1 BODY.61	1 BODY.60	1 BODY.63	1 BODY.62	1 BODY.16	1 BODY.15	1 BODY.18	1 BODY.17	1
     * BODY.19	1 CHARTER4	1 BODY.5	1 BODY.4	1 BODY.7	1 BODY.6	1 BODY.9	1 BODY.8	1 BODY.10	1 BODY.12	1 BODY.1	1 BODY.11	1
     * BODY.0	1 BODY.14	1 BODY.3	1 BODY.13	1 BODY.2	1 Total: 823131
     *
     * @param def
     */
    public Item(String def) {
        super(def);
    }

}
