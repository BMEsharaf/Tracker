package memo.paaad.com.tracker;

import android.content.res.Resources;

/**
 * Created by User on 1/27/2017.
 */

public class ObjectData {
    private String name ;
    private int  image ;
    private  boolean status ;


    ObjectData(String name ,int image , boolean status){
        this.name = name;
        this.image = image ;
        this.status = status ;
    }
    ObjectData(){

    }
    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
