public class Rektangel {
    public int height;
    public int width;


    public Rektangel(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getArea() {
        //return 60;
        return height*width;
    }

    public boolean isSquare(){
        return width==height;
    }
    public void extraMethod(){
        System.out.println("Extra Method");
    }
}
