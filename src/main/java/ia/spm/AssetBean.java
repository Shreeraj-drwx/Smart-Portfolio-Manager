package ia.spm;

public class AssetBean {

    int id;
    String type;
    String name;
    String broker;
    int date;
    int volume;
    double price;
    int userID;
    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getName() { return name; }
    public String getBroker() {
        return broker;
    }
    public int getDate() {
        return date;
    }
    public int getVolume() {
        return volume;
    }
    public double getPrice() {
        return price;
    }
    public int getUserID() {
        return userID;
    }
    public String getSymbol() {
        return symbol;
    }

    String symbol;
    public AssetBean(int id, String type, String name, int date, String broker, int volume, double price, int userID, String symbol){

        this.id = id;
        this.type = type;
        this.name = name;
        this.date = date;
        this.broker = broker;
        this.volume = volume;
        this.price = price;
        this.userID = userID;
        this.symbol = symbol;

    }
    public String toString()
    {
        return "AssetID: " + this.id + " Type: " + this.type + " Company Name: " + this.name + " Date: " + this.date + "    Broker: "+ this.broker + " Volume: " + this.volume + " Price: " + this.price + " Symbol: " + symbol ;

    }


}

