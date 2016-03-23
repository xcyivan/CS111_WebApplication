package edu.ucla.cs.cs144;

public class Entry{
	String itemID;
	String name;
	String category;
	String description;

	public Entry(){
		itemID = "";
		name = "";
		category = "";
		description = "";
	}

	public void setItemID(String itemID){
		this.itemID = itemID;
	}

	public void setName(String name){
		this.name = name;
	}

	public void appendCategory(String category){
		this.category = this.category + " " + category;
		// System.out.println("appendCategory: this.category = " + this.category);
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String toString() {
		return "name: " + name + "\ncategory: " + category + "\ndescription: " + description;
	}

	public String toContent(){
		return name + " " + category + " " + description;
	}

	public String getItemID(){
		return itemID;
	}

	public String getName(){
		return name;
	}

	public String getCategory(){
		return category;
	}

	public String getDescription(){
		return description;
	}
}