package eu.janinko.andaria.spherescript.sphere.objects;

import lombok.Getter;

public class PossibleRange {

    private Long low;

    private Long high;
    private Long value;

    public PossibleRange() {
        this.value = 0L;
    }

    public PossibleRange(Long value) {
        this.value = value;
    }

    public PossibleRange(Long low, Long high) {
        this.low = low;
        this.high = high;
    }

    public void setRange(Long low, Long high) {
        this.low = low;
        this.high = high;
    }

    public PossibleRange(Long[] range) {
        if (range.length == 2) {
            this.low = range[0];
            this.high = range[1];
            this.value = null;
        } else {
            this.value = range[0];
        }
    }

    public void setRange(Long[] range) {
        if (range.length == 2) {
            this.low = range[0];
            this.high = range[1];
            this.value = null;
        } else {
            this.value = range[0];
        }
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else {
            return "{" + "low=" + low + ", high=" + high + ", value=" + getValue() + '}';
        }
    }

    //value; low; high; avg
    public String toCSV() {
        if (value != null) {
            return String.join(";", value.toString(), "N/A", "N/A", "N/A");
        } else {
            return String.join(";", "N/A", low.toString(), high.toString(), getRange().toString());
        }
    }

    public Long getValue() {
        if (value != null)
            return value;
        else
            return (low + high) / 2;
    }

    private Long getRange() {
        if (value != null)
            return value;
        else
            return (low + high) / 2;
    }
}
