//資管二A
//105403002
//李美慧


public class People {
	private int memberID;
	private String name;
	private String phone;
	private String email;
	private String sex;
	
	public People()
	{
	} 
	
	public People(int memberID, String name, String phone, String email, String sex)
	{
		setMemberID(memberID);
		setName(name);
		setPhone(phone);
		setEmail(email);
		setSex(sex);
	} 
	
	public void setMemberID(int memberID)
	{
		this.memberID = memberID;
	}
	public int getMemberID()
	{
		return memberID;
	}
	   
	public void setName(String name)
	{
		this.name = name;
	} 
	public String getName()
	{
		return name;
	} 
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	} 
	public String getPhone()
	{
		return phone;
	}
	   
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getEmail()
	{
		return email;
	} 

	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public String getSex()
	{
		return sex;
	}
}
