package testdata;

/**
 * ContactData — POJO for deserializing contact.json test data.
 *
 * @author ASMahrous
 */
public class ContactData
{
    public String name;
    public String email;
    public String subject;
    public String message;
    public String filePath;   // absolute or relative path to the upload file
}