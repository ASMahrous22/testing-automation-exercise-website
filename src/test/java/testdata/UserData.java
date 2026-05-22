package testdata;

/**
 * UserData — POJO for deserializing user.json / existingUser.json test data.
 *
 * <p>Field names must match JSON keys exactly (Gson maps by name).</p>
 *
 * @author ASMahrous
 */
public class UserData
{
    // ── Identity ──────────────────────────────────────────────────────────
    public String title;           // "Mr" | "Mrs"
    public String name;            // full display name shown in navbar
    public String firstName;
    public String lastName;
    public String email;
    public String password;

    // ── Date of Birth ─────────────────────────────────────────────────────
    public String dayOfBirth;      // e.g. "15"
    public String monthOfBirth;    // e.g. "June"
    public String yearOfBirth;     // e.g. "1990"

    // ── Address ───────────────────────────────────────────────────────────
    public String company;
    public String address1;
    public String address2;
    public String country;         // e.g. "United States"
    public String state;
    public String city;
    public String zipcode;
    public String mobileNumber;
}