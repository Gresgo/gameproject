package clientside;

import clientside.models.Box;
import clientside.models.ServerMessage;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * 
 * @author Titas Skrebe
 *
 * Helper class for (un)marshalling data
 * 
 */
public class Helper {
	
	
	/**
	 * Marshalls ServerMessage class to a string.
	 * 
	 * @param sm ServerMessage class
	 * @return an XML string
	 * @throws //JAXBException
	 */
	
	public static String marshall(ServerMessage sm) throws JAXBException {
		
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(sm, sw);
        
		return sw.toString();
	}
	
	/**
	 * Unmarshall a list of Boxes in XML to a actual object
	 * 
	 * @param data
	 * @return A list of boxes
	 * @throws //JAXBException
	 */
	
	public static List<Box> unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(WrapperList.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		WrapperList wrapList = (WrapperList) unmarshaller.unmarshal(sr);
		return wrapList.realList;
	}
	
	/**
	 * Wrapper class for marshalling/unmarshalling a list of boxes
	 */

	public static class WrapperList{
		
		List<Box> realList;
		public WrapperList(){}
		
	}

}
