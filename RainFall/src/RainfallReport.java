import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RainfallReport {

	//Write the required business logic as expected in the question description
	public List<AnnualRainfall> generateRainfallReport(String filePath) {
	    
		ArrayList<AnnualRainfall> Rainfall=new ArrayList<AnnualRainfall>();
		BufferedReader reader;
		double[] rainfallArr=new double[12];
		//AnnualRainfall ar=new AnnualRainfall();
		try {
			reader=new BufferedReader(new FileReader(filePath));
			String line;
			try {
				line = reader.readLine();
				
				while(line!=null) {
					String[] elements=line.split(",");
					try {
						AnnualRainfall ar=new AnnualRainfall();
						if(validate(elements[0])) {
							ar.setCityPincode(Integer.parseInt(elements[0]));
							ar.setCityName(elements[1]);
							for(int i=0;i<12;i++) {
								rainfallArr[i]=Integer.parseInt(elements[i+2]);
							}
							ar.calculateAverageAnnualRainfall(rainfallArr);	
							System.out.println(ar.getCityPincode());
							Rainfall.add(ar);
							System.out.println(Rainfall.get(0).getCityPincode());
							
						}
						line=reader.readLine();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidCityPincodeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
				
			}
		
		
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Rainfall;
	}

	
	
	
	
	public List<AnnualRainfall> findMaximumRainfallCities() {
		DBHandler dbh=new DBHandler();
		ArrayList <AnnualRainfall> rf=new ArrayList<AnnualRainfall>();
		AnnualRainfall citydet=new AnnualRainfall();
		int pincode;
		String city;
		double rainfall;
		double max=0;
		Connection conn= dbh.establishConnection();
		try {
			Statement stmt=conn.createStatement();
			String query="SELECT average_annual_rainfall FROM AnnualRainfall";
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
				//pincode=rs.getInt("city_pincode");
				//city=rs.getString("city_name");
				rainfall=rs.getDouble("average_annual_rainfall");
				if(max<rainfall) {
					max=rainfall;
				}
			}
			query="SELECT * FROM AnnualRainfall WHERE average_annual_rainfall="+max;
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				pincode=rs.getInt("city_pincode");
				city=rs.getString("city_name");
				rainfall=rs.getDouble("average_annual_rainfall");
				citydet.setCityPincode(pincode);
				citydet.setCityName(city);
				citydet.setAverageAnnualRainfall(rainfall);
				rf.add(citydet);
				citydet=null;
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rf;
		
	}
	
	
	public boolean validate(String cityPincode) throws InvalidCityPincodeException {
		boolean valid=true;
		if(cityPincode.length()!=5)
		{	valid=false;
			throw new InvalidCityPincodeException();
		}
    	return valid;
	}

}
