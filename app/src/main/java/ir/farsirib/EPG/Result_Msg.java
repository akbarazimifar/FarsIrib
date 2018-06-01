package ir.farsirib.EPG;

/**
 * Created by alireza on 10/07/2017.
 */

@DataContract()
public class Result_Msg {
    @DataMember(EmitDefaultValue=false)
    public boolean Result;
    @DataMember(EmitDefaultValue=false)
    public String txt_Result;
    @DataMember(EmitDefaultValue=false)
    public String JsonData;
}
