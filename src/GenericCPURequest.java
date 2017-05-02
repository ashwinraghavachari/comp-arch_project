
public class GenericCPURequest extends Request {

    public GenericCPURequest(long entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
