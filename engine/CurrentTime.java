package engine;

import java.util.Calendar;

final class CurrentTime
{
	CurrentTime()
	{
		vCalendar = Calendar.getInstance();
		vTimeStr = getTimeString();
		vTimeLong=vCalendar.getTimeInMillis();
	}
	
	private Calendar vCalendar;
	private String vTimeStr;
	private Long vTimeLong;
	
	
	
	private String doubleNumFormat(int number)
	{	
		String s;
		if(number<10)
			s="0"+number;
		else
			s = ""+number;
		return s;
	}
	
	private String quadrupleNumFormat(int number)
	{	
		String s="";
		
		if(number<1000)
		{
			s += "0";		
			if(number<100)
			{
				s += "0";		
				if(number<10)
					s += "0";
			}
		}
		s += number;
			
		return s;
	}
	
	private String getTimeString()
	{
		String s = getDate();
		s+=" - ";
		s+=getClock();
		return s;
	}
	
	private String getDate()
	{
		String s = "";
		
		s+=doubleNumFormat(vCalendar.get(Calendar.DAY_OF_MONTH));
		s+=".";
		s+=doubleNumFormat(vCalendar.get(Calendar.MONTH)+1);
		s+=".";
		s+=quadrupleNumFormat(vCalendar.get(Calendar.YEAR));
		
		return s;
	}
	
	private String getClock()
	{
		String s = "";
		s+=doubleNumFormat(vCalendar.get(Calendar.HOUR_OF_DAY));
		s+=":";
		s+=doubleNumFormat(vCalendar.get(Calendar.MINUTE));
		return s;
	}
	
	long getTime()
	{
		return vTimeLong;
	}
	
	@Override
	public String toString()
	{
		return vTimeStr;
	}
}
