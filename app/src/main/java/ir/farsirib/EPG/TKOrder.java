package ir.farsirib.EPG;

/**
 * Created by alireza on 10/07/2017.
 */
import java.lang.Object;

@DataContract()
public class TKOrder {

    @DataMember(EmitDefaultValue=false)
    public String Key;
    @DataMember(EmitDefaultValue=false)
    public String Order;
    @DataMember(EmitDefaultValue=false)
    public String JsonData;

}
