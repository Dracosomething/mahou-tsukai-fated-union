package io.github.dracosomething.mtfatedunion.capability.gae_bolg;

public class GaeBolgMahou implements IGaeBolgMahou {
    private float attackDamage = 3.0F;
    private double innateCap = 10000.0;

    public GaeBolgMahou() {
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(float a) {
        this.attackDamage = a;
    }

    public void setInnateCap(double a) {
        this.innateCap = a;
    }

    public double getInnateCap() {
        return this.innateCap;
    }
}
