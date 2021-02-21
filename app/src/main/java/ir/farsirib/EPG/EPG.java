package ir.farsirib.EPG;

/**
 * Created by alireza on 10/07/2017.
 */

public class EPG {
    @DataMember(EmitDefaultValue=false)
    public int Kind_BrodCast;
    @DataMember(EmitDefaultValue=false)
    public int SID_Network;
    @DataMember(EmitDefaultValue=false)
    public int SID_Day_Item;
    @DataMember(EmitDefaultValue=false)
    public int SID_Program;
    @DataMember(EmitDefaultValue=false)
    public String DTStart;
    @DataMember(EmitDefaultValue=false)
    public String DTEnd;
}
