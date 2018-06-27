package xysoft.im.entity;


public class SelectUserData
{
    private String name;
    private String cnName;
    private String fullName;
    private boolean selected;

    public SelectUserData(String fullname, boolean selected){
        this.fullName = fullname;
        this.selected = selected;
    }


    public String getName(){
        return fullName;
    }

    public String getUserName(){
    	return fullName;
        //return fullName.split("-")[0];
    }


    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
