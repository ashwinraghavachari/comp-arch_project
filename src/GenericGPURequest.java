
public class GenericGPURequest extends Request {

    public GenericGPURequest(long entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public void processRequest() {
        destination.processRequest(this);
    }

}
