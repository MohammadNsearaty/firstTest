import sun.dc.pr.PRError;

public class request {
    private Object object;
    private requestType type;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public requestType getType() {
        return type;
    }

    public void setType(requestType type) {
        this.type = type;
    }
}
