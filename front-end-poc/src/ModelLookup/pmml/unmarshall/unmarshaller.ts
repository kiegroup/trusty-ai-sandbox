import * as cxml from "cxml";
import * as PMML from "../generated/www.dmg.org/PMML-4_4";

/**
 * Unmarshalls the XML using cxml, returning a typed JSON model
 * @param xml
 */
function withCXML(xml: string): Promise<PMML.document> {
  var parser = new cxml.Parser();
  var result = parser.parse(xml, PMML.document);

  return result;
}

export { withCXML };