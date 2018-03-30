package pw.skidrevenant.fiona.detections;

public enum ChecksType
{
    COMBAT("COMBAT"), 
    MOVEMENT("MOVING"), 
    OTHER("OTHER"), 
    GOLD("GOLD");
    
    private String name;
    
    private ChecksType(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
