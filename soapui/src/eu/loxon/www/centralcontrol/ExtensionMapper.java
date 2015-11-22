/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package eu.loxon.www.centralcontrol;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "wsDirection".equals(typeName)) {
            return eu.loxon.www.centralcontrol.WsDirection.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "resultType".equals(typeName)) {
            return eu.loxon.www.centralcontrol.ResultType.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "objectType".equals(typeName)) {
            return eu.loxon.www.centralcontrol.ObjectType.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "wsCoordinate".equals(typeName)) {
            return eu.loxon.www.centralcontrol.WsCoordinate.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "scouting".equals(typeName)) {
            return eu.loxon.www.centralcontrol.Scouting.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "wsScore".equals(typeName)) {
            return eu.loxon.www.centralcontrol.WsScore.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "wsBuilderunit".equals(typeName)) {
            return eu.loxon.www.centralcontrol.WsBuilderunit.Factory.parse(reader);
        }

        if ("http://www.loxon.eu/CentralControl/".equals(namespaceURI) &&
                "commonResp".equals(typeName)) {
            return eu.loxon.www.centralcontrol.CommonResp.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
