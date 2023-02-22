package eu.janinko.uo.saves;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class Spawn {

    private final String npc;
    private final String uid;
    private final int m;
    private final int z;
    private final int y;
    private final int x;
    private final int min;
    private final int max;
    private final int dist;
    private final int amount;

    public Spawn(Item spawn) {
        if (!spawn.getDef().contains("i_worldgem_bit")) {
            throw new IllegalArgumentException("Item is not spawn: " + spawn);
        }
        try {
            this.uid = spawn.get("SERIAL");
            this.npc = spawn.get("MORE1");

            String[] p = spawn.get("P").split(",");
            this.x = Integer.parseInt(p[0]);
            this.y = Integer.parseInt(p[1]);
            this.z = p.length > 2 ? Integer.parseInt(p[2]) : 0;
            this.m = p.length > 3 ? Integer.parseInt(p[3]) : 0;

            String[] morep = spawn.get("MOREP", "0,0").split(",");
            this.min = Integer.parseInt(morep[0]);
            this.max = Integer.parseInt(morep[1]);
            this.dist = morep.length > 2 ? Integer.parseInt(morep[2]) : 80;

            this.amount = Integer.parseInt(spawn.get("AMOUNT", "1"));
        } catch (RuntimeException ex) {
            System.out.println("item: " + spawn);
            throw ex;
        }
    }

    public String getNpc() {
        return npc;
    }

    public String getUid() {
        return uid;
    }

    public int getM() {
        return m;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getDist() {
        return dist;
    }

    public int getAmount() {
        return amount;
    }

}
