package Lab.AbsClass;

abstract class Creature {
    protected String name = null;
    protected boolean isAlive = false;

    public Creature(String name){
        this.name = name;
    }

    public void shoutName() {
        if (name != null && name != "") {
            System.out.println(name);
        } else {
            System.out.println("Error in name!!! Try in next time...");
        }
    }

    public abstract void bear();

    public abstract void die();
}




